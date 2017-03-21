import { ActivatedRoute, Router } from '@angular/router';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { AlertService } from './../../../core/alert/alert.service';
import { DataService } from './../../../core/data/data.service';
import { Organization } from './../../../core/model/organization.model';
import { Subscription } from 'rxjs/Subscription';

@Component({
  selector: 'of-settings-sunat',
  templateUrl: './settings-sunat.component.html',
  styles: [`
    form {
      margin-top: 20px;
    }

    .of-no-margin {
      margin-left: 0; 
      margin-right: 0;
    }
  `]
})
export class SettingsSunatComponent implements OnInit, OnDestroy {

  dataSubscription: Subscription;

  organization: Organization;

  form: FormGroup;
  working = false;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private dataService: DataService,
    private alertService: AlertService) {
  }

  ngOnInit() {
    this.buildForm();
    this.dataSubscription = this.route.data.subscribe(data => {
      this.organization = data['organization'];
      this.loadData();
    });
  }

  ngOnDestroy() {
    this.dataSubscription.unsubscribe();
  }

  buildForm() {
    this.form = this.formBuilder.group({
      sunatUsername: [undefined, Validators.compose([Validators.required, Validators.maxLength(40)])],
      sunatPassword: [undefined, Validators.compose([Validators.required, Validators.maxLength(200)])],
      sunatAddress1: [undefined, Validators.compose([Validators.required, Validators.maxLength(200)])],
      sunatAddress2: [undefined, Validators.compose([Validators.required, Validators.maxLength(200)])]
    });
  }

  loadData() {
    this.form.patchValue(this.organization.attributes || {});
  }

  save(form: FormGroup) {
    this.working = true;

    this.organization.save({ organization: this.organization.organization, attributes: form.value }).subscribe(
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
