import { ActivatedRoute, Router } from '@angular/router';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { AlertService } from './../../../core/alert/alert.service';
import { DataService } from './../../../core/data/data.service';
import { Subscription } from 'rxjs/Subscription';
import { findParam } from '../../../core/resolvers/find-param';

@Component({
  selector: 'of-settings-generic-keystore',
  templateUrl: './settings-generic-keystore.component.html',
  styles: [``]
})
export class SettingsGenericKeystoreComponent implements OnInit, OnDestroy {

  dataSubscription: Subscription;

  organization: any;
  serverInfo: any;

  form: FormGroup;
  create: boolean;
  working = false;

  providerFactory: any;
  instance: any;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private dataService: DataService,
    private alertService: AlertService
  ) { }

  ngOnInit() {
    this.buildForm();
    this.dataSubscription = this.route.data.subscribe(data => {
      this.organization = data['organization'];
      this.serverInfo = data['serverInfo'];
      this.instance = data['instance'] || {};
      this.refreshValues();
      this.loadData();
    });
  }

  ngOnDestroy() {
    this.dataSubscription.unsubscribe();
  }

  buildForm(): void {
    this.form = this.formBuilder.group({
      name: [null, Validators.compose([Validators.required, Validators.maxLength(60)])],
      parentId: [null, Validators.maxLength(250)],
      providerId: [null, Validators.maxLength(250)],
      providerType: [null, Validators.maxLength(250)]
    });
  }

  loadData() {
    this.form.patchValue({
      name: this.instance.name,
      parentId: this.instance.parentId,
      providerId: this.instance.providerId,
      providerType: this.instance.providerType
    });
  }

  refreshValues() {
    this.create = !this.instance.providerId;

    const providerId = findParam('provider', this.route.snapshot);

    const providers = this.serverInfo.componentTypes['org.openfact.keys.KeyProvider'];
    let providerFactory = null;
    for (let i = 0; i < providers.length; i++) {
      const p = providers[i];
      if (p.id === providerId) {
        this.providerFactory = p;
        providerFactory = p;
        break;
      }
    }

    if (this.create) {
      this.instance = {
        name: providerFactory.id,
        providerId: providerFactory.id,
        providerType: 'org.openfact.keys.KeyProvider',
        parentId: this.organization.id,
        config: {
          'priority': ['0']
        }
      };
    }

    if (providerFactory.properties) {
      for (let i = 0; i < providerFactory.properties.length; i++) {
        const configProperty = providerFactory.properties[i];
        if (!this.instance.config[configProperty.name]) {
          if (configProperty.defaultValue) {
            this.instance.config[configProperty.name] = [configProperty.defaultValue];
            if (!this.create) {
              this.instance.config[configProperty.name] = [configProperty.defaultValue];
            }
          } else {
            this.instance.config[configProperty.name] = [''];
            if (!this.create) {
              this.instance.config[configProperty.name] = [configProperty.defaultValue];
            }
          }
        }
      }
    }
  }

  save(form, config) {
    this.working = true;

    if (this.create) {
      this.organization.createComponent(Object.assign(form, { config: config })).subscribe(
        result => {
          this.alertService.pop('success', 'Success', 'Success! The organization has been created.');
          this.router.navigate(['../../'], { relativeTo: this.route });
        },
        error => {
          this.working = false;
        }
      );
    } else {
      this.organization.updateComponent(this.instance.id, Object.assign(form, { config: config })).subscribe(
        (data) => {
          this.alertService.pop('success', 'Success', 'Success! The provider has been updated.');
          this.router.navigate(['../../'], { relativeTo: this.route });
        },
        (error) => {
          this.working = false;
        }
      );
    }
  };

  reset() {
  };

  cancel() {
    this.router.navigate(['../../'], { relativeTo: this.route });
  };

}
