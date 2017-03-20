import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'of-component-config',
  templateUrl: './component-config.component.html',
  styleUrls: []
})
export class ComponentConfigComponent implements OnInit {

  @Input()
  organization: any;

  @Input()
  config: any;

  @Input()
  properties: any;

  constructor() { }

  ngOnInit() { }

  onSwitchChange(option, $event) {
    this.config[option.name][0] = $event;
  }

  onFileChange(option, $event) {
    this.config[option.name][0] = $event.data;
  }

  onListChange(option, $event) {
    this.config[option.name][0] = $event;
  }

}
