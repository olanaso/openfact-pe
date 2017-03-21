import { ActivatedRoute, ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { Injectable, OnDestroy } from '@angular/core';

import { AlertService } from './../../core/alert/alert.service';
import { CurrentOrganizationService } from './../../core/services/current-organization.service';
import { Observable } from 'rxjs/Rx';
import { Subscription } from 'rxjs/Subscription';

@Injectable()
export class OrganizationConfigGuardService implements CanActivate {

  constructor(private currentOrganization: CurrentOrganizationService, private router: Router, private alertService: AlertService) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    const organization = this.currentOrganization.organization;
    if (!organization ||
      !organization.assignedIdentificationId ||
      !organization.additionalAccountId ||
      !organization.supplierName ||
      !organization.registrationName ||
      !organization.postalAddress ||
      !organization.postalAddress.cityName ||
      !organization.postalAddress.citySubdivisionName ||
      !organization.postalAddress.countryIdentificationCode ||
      !organization.postalAddress.countrySubentity ||
      !organization.postalAddress.district ||
      !organization.postalAddress.streetName) {
      this.alertService.popAsync('warning', 'warning', 'Warning! No puede crear documentos. Datos insuficientes en organizacion.');
      this.router.navigate(['organizations', organization.organization, 'settings', 'additional-information']);
    } else {
      return true;
    }
  }

}
