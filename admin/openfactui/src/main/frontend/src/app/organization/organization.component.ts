import { Component, OnDestroy, OnInit } from '@angular/core';

import { ActivatedRoute } from '@angular/router';
import { AlertService } from './../core/alert/alert.service';
import { CurrentOrganizationService } from './../core/services/current-organization.service';
import { DataService } from './../core/data/data.service';
import { Organization } from './../core/model/organization.model';
import { Subscription } from 'rxjs/Subscription';

@Component({
  selector: 'of-organization',
  templateUrl: './organization.component.html',
  styles: [`
    .container-pf-nav-pf-vertical {      
      top: 60px;
      position: relative;
      margin-left: 200px;
    }
  `]
})
export class OrganizationComponent implements OnInit, OnDestroy {

  dataSubscription: Subscription;

  organization: Organization;
  organizations: Array<Organization>;

  constructor(
    private route: ActivatedRoute,
    private dataService: DataService,
    private alertService: AlertService,
    private currentOrganization: CurrentOrganizationService) { }

  ngOnInit() {
    this.dataSubscription = this.route.data.subscribe(
      (data) => {
        this.organization = data['organization'];
        this.currentOrganization.changeOrganizationValue(this.organization);
      }
    );
    this.loadAllowedOrganizations();
  }

  ngOnDestroy() {
    this.dataSubscription.unsubscribe();
  }

  loadAllowedOrganizations() {
    this.dataService.organizations().getAll().subscribe(
      (data) => {
        this.organizations = data;
      }
    );
  }

}
