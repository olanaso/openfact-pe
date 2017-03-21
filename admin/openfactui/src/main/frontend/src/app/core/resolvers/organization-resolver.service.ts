import { ActivatedRouteSnapshot } from '@angular/router';
import { DataService } from './../data/data.service';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Rx';
import { Organization } from './../model/organization.model';
import { Resolve } from '@angular/router';
import { RouterStateSnapshot } from '@angular/router';
import { findParam } from './find-param';

@Injectable()
export class OrganizationResolverService implements Resolve<Organization> {

  constructor(private dataService: DataService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> | Promise<any> {
    const organizationId = findParam('organization', route);
    return this.dataService.organizations().findById(organizationId);
  }

}
