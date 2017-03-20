import { Injectable } from '@angular/core';
import { Organization } from './../model/organization.model';
import { Subject } from 'rxjs/Subject';

@Injectable()
export class CurrentOrganizationService {

  private _organization: Organization;
  private organizationSource = new Subject<Organization>();
  organization$ = this.organizationSource.asObservable();

  constructor() { }

  changeOrganizationValue(organization: Organization) {
    this._organization = organization;
    this.organizationSource.next(organization);
  }

  get organization(): Organization {
    return this._organization;
  }

}
