import { Document } from './../model/document.model';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Rx';
import { Organization } from './../model/organization.model';
import { SearchCriteria } from './../model/search-criteria.model';
import { SearchResults } from './../model/search-results.model';
import { URLSearchParams } from '@angular/http';

@Injectable()
export class DocumentService {

  constructor() { }

  findById(organization: Organization, id: string, queryParams?: URLSearchParams): Observable<Document> {
    const restangular = organization.restangular.one('documents', id);

    return restangular
      .get(queryParams)
      .map(response => {
        const data = response.json();
        return Object.assign(new Document(restangular), data);
      });
  }

  getAll(organization: Organization, queryParams?: URLSearchParams): Observable<Document[]> {
    const restangular = organization.restangular.all('documents');
    return restangular
      .get(queryParams)
      .map(response => {
        const json = response.json();
        const result = new Array<Document>();
        json.forEach(element => {
          const document = new Document(restangular.all(element['id']));
          result.push(Object.assign(document, element));
        });
        return result;
      });
  }

  search(organization: Organization, criteria: SearchCriteria): Observable<SearchResults<Document>> {
    const restangular = organization.restangular.all('documents');
    return restangular
      .all('search')
      .post(criteria)
      .map(response => {
        const json = response.json();

        const result = new SearchResults<Document>();
        const items = new Array<Document>();

        json.items.forEach(element => {
          const document = new Document(restangular.all(element['id']));
          items.push(Object.assign(document, element));
        });

        result.items = items;
        result.totalSize = json.totalSize;
        return result;
      });
  }

}
