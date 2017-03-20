import { Component, Input, OnInit } from '@angular/core';

import { Organization } from './../../../core/model/organization.model';

@Component({
  selector: 'of-settings-tabset',
  templateUrl: './settings-tabset.component.html',
  styles: []
})
export class SettingsTabsetComponent implements OnInit {

  @Input()
  organization: Organization;

  constructor() { }

  ngOnInit() {
  }

}
