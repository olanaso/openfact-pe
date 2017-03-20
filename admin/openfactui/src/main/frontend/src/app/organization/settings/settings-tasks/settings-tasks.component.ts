import { ActivatedRoute, Router } from '@angular/router';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';

import { AlertService } from './../../../core/alert/alert.service';
import { DataService } from './../../../core/data/data.service';
import { Organization } from './../../../core/model/organization.model';
import { Subscription } from 'rxjs/Subscription';

@Component({
  selector: 'of-settings-tasks',
  templateUrl: './settings-tasks.component.html',
  styles: [`
    form {
      margin-top: 20px;
    }
  `]
})
export class SettingsTasksComponent implements OnInit, OnDestroy {

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
      tasksEnabled: [false, Validators.compose([Validators.required])],
      taskDelay: [undefined, Validators.compose([Validators.maxLength(150)])],
      taskFirstDate: [undefined, Validators.compose([Validators.maxLength(150)])],
      taskFirstTime: [undefined, Validators.compose([Validators.maxLength(150)])]
    });

    const formControl = this.form.get('tasksEnabled') as FormControl;
    if (formControl) {
      formControl.valueChanges.subscribe(taskEnabled => {
        if (taskEnabled === true) {
          this.form.addControl('taskDelay', this.formBuilder.control(undefined, Validators.compose([Validators.required])));
          this.form.addControl('taskFirstDate', this.formBuilder.control(undefined, Validators.compose([Validators.required])));
          this.form.addControl('taskFirstTime', this.formBuilder.control(undefined, Validators.compose([Validators.required])));

          const organizationTaskFirstTime = new Date(this.organization.taskFirstTime);

          this.form.patchValue({
            taskDelay: this.organization.taskDelay,
            taskFirstDate: {
              year: organizationTaskFirstTime.getFullYear(),
              month: organizationTaskFirstTime.getMonth() + 1,
              day: organizationTaskFirstTime.getDate()
            },
            taskFirstTime: {
              hour: organizationTaskFirstTime.getHours(),
              minute: organizationTaskFirstTime.getMinutes(),
              second: organizationTaskFirstTime.getSeconds()
            }
          });

        } else {
          /*this.form.removeControl('taskDelay');
          this.form.removeControl('taskFirstDate');
          this.form.removeControl('taskFirstTime');*/
        }
      });
    }
  }

  loadData() {
    this.form.patchValue(this.organization);
  }

  save(form: FormGroup) {
    this.working = true;

    const taskConfig = {
      tasksEnabled: form.value.tasksEnabled,
      taskDelay: form.value.taskDelay,
      taskFirstTime: undefined
    };

    if (form.value.tasksEnabled) {
      const taskDatetime = new Date();
      taskDatetime.setFullYear(form.value.taskFirstDate.year);
      taskDatetime.setMonth(form.value.taskFirstDate.month - 1);
      taskDatetime.setDate(form.value.taskFirstDate.day);

      taskDatetime.setHours(form.value.taskFirstTime.hour);
      taskDatetime.setMinutes(form.value.taskFirstTime.minute);
      taskDatetime.setSeconds(0);

      taskConfig.taskFirstTime = taskDatetime;
    }

    this.organization.save(taskConfig).subscribe(
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
