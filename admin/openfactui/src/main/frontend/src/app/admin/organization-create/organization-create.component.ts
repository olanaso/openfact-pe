import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl } from '@angular/forms';

import { AlertService } from './../../core/alert/alert.service';
import { DataService } from './../../core/data/data.service';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { Validators } from '@angular/forms';

@Component({
  selector: 'of-organization-create',
  templateUrl: './organization-create.component.html',
  styles: [``]
})
export class OrganizationCreateComponent implements OnInit {

  form: FormGroup;
  working = false;

  organization: any;
  importing = false;

  constructor(
    private router: Router,
    private formBuilder: FormBuilder,
    private dataService: DataService,
    private alertService: AlertService) { }

  ngOnInit() {
    this.buildForm();
  }

  buildForm() {
    this.form = this.formBuilder.group({
      organization: [null, Validators.compose([Validators.required, Validators.maxLength(60)])],
      description: [null, Validators.compose([Validators.maxLength(250)])],
    });
  }

  importFile(file) {
    this.organization = Object.assign({}, JSON.parse(file.data));
    this.form.patchValue(this.organization);
    this.importing = true;
  }

  save(form: FormControl): void {
    this.working = true;
    const organizationCopy = Object.assign(this.organization || {}, form.value);

    this.dataService.organizations().create(organizationCopy).subscribe(
      result => {
        this.alertService.pop('success', 'Success', 'Success! The organization has been created.');
        this.router.navigate(['../']);
      },
      error => {
        this.working = false;
      }
    );
  }

  reset() {
    this.organization = undefined;
    this.importing = false;
    this.buildForm();
  }

  cancel() {
    this.router.navigate(['../']);
  }

}
