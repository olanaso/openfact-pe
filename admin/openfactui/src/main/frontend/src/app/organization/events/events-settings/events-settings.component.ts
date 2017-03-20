import { ActivatedRoute, Router } from '@angular/router';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { AlertService } from './../../../core/alert/alert.service';
import { DataService } from './../../../core/data/data.service';
import { DialogService } from './../../../core/dialog/dialog.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Organization } from './../../../core/model/organization.model';
import { Subscription } from 'rxjs/Subscription';

@Component({
  selector: 'of-events-settings',
  templateUrl: './events-settings.component.html',
  styles: []
})
export class EventsSettingsComponent implements OnInit, OnDestroy {

  dataSubscription: Subscription;
  parentDataSubscription: Subscription;

  form: FormGroup;
  working = false;

  organization: Organization;
  serverInfo: any;
  eventsConfig: any;

  eventListeners: any;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private dataService: DataService,
    private alertService: AlertService,
    private dialogService: DialogService) {
  }

  ngOnInit() {
    this.buildForm();
    this.parentDataSubscription = this.route.data.subscribe(data => {
      this.organization = data['organization'];
    });
    this.dataSubscription = this.route.data.subscribe(data => {
      this.serverInfo = data['serverInfo'];
      this.eventsConfig = data['eventsConfig'];
      //this.eventListeners = Object.keys(this.serverInfo.providers.eventsListener.providers);

      this.loadData();
    });
  }

  ngOnDestroy() {
    this.dataSubscription.unsubscribe();
  }

  buildForm(): void {
    this.form = this.formBuilder.group({
      adminEventsEnabled: [null, Validators.compose([Validators.required])],
      adminEventsDetailsEnabled: [null, Validators.compose([Validators.required])]
    });
  }

  loadData() {
    this.form.patchValue({
      adminEventsEnabled: this.eventsConfig.adminEventsEnabled,
      adminEventsDetailsEnabled: this.eventsConfig.adminEventsEnabled
    });
  }

  clearAdminEvents(content) {
    this.dialogService.confirmDelete('', 'Events').result.then((data1) => {
      this.organization.clearAdminEvents().subscribe(data2 => {
        this.alertService.pop('success', 'Success', 'The admin events has been cleared.');
      });
    }, (reason) => {
    });
  }

  save(form) {
    this.working = true;

    this.organization.updateEventsConfig(Object.assign(this.eventsConfig || {}, form)).subscribe(
        result => {
          this.working = false;
          this.alertService.pop('success', 'Success', 'Your changes have been saved to the organization.');
        }
    );
  }

  cancel() {
    this.router.navigate(['../'], { relativeTo: this.route });
  }

}
