import { Component, Input, OnInit } from '@angular/core';

import { FormControl } from '@angular/forms';

@Component({
  selector: 'of-form-field-validation-messages',
  templateUrl: './form-field-validation-messages.component.html',
  styleUrls: []
})
export class FormFieldValidationMessagesComponent implements OnInit {

  @Input()
  ofFormControl: FormControl;

  constructor() { }

  ngOnInit() {
  }

}
