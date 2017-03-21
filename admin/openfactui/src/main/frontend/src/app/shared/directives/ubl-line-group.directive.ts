import { Directive, Input, OnInit } from '@angular/core';

import { NgControl } from '@angular/forms';
import { UblLineGroupByDirective } from './ubl-line-group-by.directive';

@Directive({
  selector: '[ofUblLineGroup]'
})
export class UblLineGroupDirective {

  @Input()
  ofUblLineGroup: string;

  constructor() { }

}
