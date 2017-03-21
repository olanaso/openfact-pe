import { ActivatedRoute, Router } from '@angular/router';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';

import { AlertService } from './../../../core/alert/alert.service';
import { DataService } from './../../../core/data/data.service';
import { Organization } from './../../../core/model/organization.model';
import { Subscription } from 'rxjs/Subscription';

@Component({
  selector: 'of-settings-general-information',
  templateUrl: './settings-general-information.component.html',
  styles: [`
    form {
      margin-top: 20px;
    }
  `]
})
export class SettingsGeneralInformationComponent implements OnInit, OnDestroy {

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
      organization: [{ value: null, disabled: true }, Validators.compose([Validators.required, Validators.maxLength(60)])],
      description: [null, Validators.compose([Validators.maxLength(250)])],
      enabled: [null, Validators.compose([Validators.required])],
    });
  }

  loadData(): void {
    this.form.patchValue(this.organization);
  }

  save(form: FormControl) {
    this.working = true;

    this.organization.save(form.value).subscribe(
      (data) => {
        this.working = false;
        this.form.markAsPristine();
        this.alertService.pop('success', 'Success', 'Your changes have been saved to the organization.');
      },
      (error) => {
        this.working = false;
      }
    );
  }

}
