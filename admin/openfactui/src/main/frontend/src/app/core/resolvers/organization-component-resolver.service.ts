import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot } from '@angular/router';

import { DataService } from './../data/data.service';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Rx';
import { findParam } from './find-param';

@Injectable()
export class OrganizationComponentResolverService implements Resolve<any> {

  constructor(private dataService: DataService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> | Promise<any> {
    const organizationId = findParam('organization', route);
    const componentId = findParam('component', route);
    const organization = this.dataService.organizations().build(organizationId);
    return organization.getComponent(componentId);
  }

}
