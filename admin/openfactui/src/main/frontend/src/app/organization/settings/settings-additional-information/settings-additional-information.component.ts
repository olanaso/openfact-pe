import { ActivatedRoute, Router } from '@angular/router';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';

import { AlertService } from './../../../core/alert/alert.service';
import { DataService } from './../../../core/data/data.service';
import { Organization } from './../../../core/model/organization.model';
import { Subscription } from 'rxjs/Subscription';

@Component({
  selector: 'of-settings-additional-information',
  templateUrl: './settings-additional-information.component.html',
  styles: [`
    form {
      margin-top: 20px;
    }
  `]
})
export class SettingsAdditionalInformationComponent implements OnInit, OnDestroy {

  dataSubscription: Subscription;

  organization: Organization;

  form: FormGroup;
  working = false;

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private formBuilder: FormBuilder,
    private dataService: DataService,
    private alertService: AlertService) {
  }

  ngOnInit() {
    this.buildForm();
    this.dataSubscription = this.activatedRoute.data.subscribe(data => {
      this.organization = data['organization'];
      this.loadData();
    });
  }

  ngOnDestroy() {
    this.dataSubscription.unsubscribe();
  }

  buildForm() {
    this.form = this.formBuilder.group({
      assignedIdentificationId: [undefined, Validators.compose([Validators.required, Validators.maxLength(20)])],
      additionalAccountId: [undefined, Validators.compose([Validators.required, Validators.maxLength(60)])],
      supplierName: [undefined, Validators.compose([Validators.required, Validators.maxLength(150)])],
      registrationName: [undefined, Validators.compose([Validators.required, Validators.maxLength(150)])],
      postalAddress: this.formBuilder.group({
        postalAddressId: [undefined, Validators.compose([Validators.required, Validators.maxLength(10)])],
        cityName: [undefined, Validators.compose([Validators.required, Validators.maxLength(150)])],
        citySubdivisionName: [undefined, Validators.compose([Validators.required, Validators.maxLength(150)])],
        countryIdentificationCode: [undefined, Validators.compose([Validators.required, Validators.maxLength(150)])],
        countrySubentity: [undefined, Validators.compose([Validators.required, Validators.maxLength(150)])],
        district: [undefined, Validators.compose([Validators.required, Validators.maxLength(150)])],
        streetName: [undefined, Validators.compose([Validators.required, Validators.maxLength(150)])],
      })
    });
  }

  loadData() {
    this.form.patchValue(this.organization);
  }

  save(form: FormControl) {
    this.working = true;

    this.organization.save(form.value).subscribe(
      result => {
        this.working = false;
        this.form.markAsPristine();
        this.alertService.pop('success', 'Success', 'Your changes have been saved to the organization.');
      },
      error => {
        this.working = false;
      }
    );
  }

}
