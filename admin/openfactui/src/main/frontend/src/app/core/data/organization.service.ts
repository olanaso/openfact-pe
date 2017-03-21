import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Rx';
import { Organization } from './../model/organization.model';
import { RestangularService } from './restangular.service';
import { URLSearchParams } from '@angular/http';

const organizationIdName = 'organization';
const organizationsPath = 'organizations';

@Injectable()
export class OrganizationService {

  private restangular: RestangularService;

  constructor(restangular: RestangularService) {
    this.restangular = restangular.all('admin');
  }

  build(id: string): Organization {
    return new Organization(this.restangular.one(organizationsPath, id));
  }

  findById(id: string): Observable<Organization> {
    const organizationRestangular = this.restangular.one(organizationsPath, id);
    return organizationRestangular
      .get()
      .map(response => {
        const organization = new Organization(organizationRestangular);
        return Object.assign(organization, response.json());
      });
  }

  create(organization: Organization): Observable<Organization> {
    const organizationRestangular = this.restangular.all(organizationsPath);
    return organizationRestangular
      .post(organization)
      .map(response => {
        if (response.status === 201 || 204) {
          return undefined;
        }
        const json = response.json();
        const result = new Organization(organizationRestangular.one('', json[organizationIdName]));
        return Object.assign(result, json);
      });
  }

  getAll(queryParams?: URLSearchParams): Observable<Organization[]> {
    const organizationsRestangular = this.restangular.all(organizationsPath);
    return organizationsRestangular
      .get(queryParams)
      .map(response => {
        const json = response.json();
        const organizations = new Array<Organization>();
        json.forEach(element => {
          const organization = new Organization(organizationsRestangular.one('', element[organizationIdName]));
          organizations.push(Object.assign(organization, element));
        });
        return organizations;
      });
  }

}
