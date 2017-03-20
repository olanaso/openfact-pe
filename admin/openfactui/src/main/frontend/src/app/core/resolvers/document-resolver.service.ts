import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot } from '@angular/router';

import { DataService } from './../data/data.service';
import { Document } from './../model/document.model';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Rx';
import { findParam } from './find-param';

@Injectable()
export class DocumentResolverService implements Resolve<Document> {

  constructor(private dataService: DataService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Document> | Promise<Document> {
    const organizationId = findParam('organization', route);
    const documentId = findParam('document', route);
    const organization = this.dataService.organizations().build(organizationId);
    return this.dataService.documents().findById(organization, documentId);
  }

}
