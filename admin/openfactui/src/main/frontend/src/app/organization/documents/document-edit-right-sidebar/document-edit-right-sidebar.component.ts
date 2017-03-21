import { Component, Input, OnInit } from '@angular/core';

import { Document } from './../../../core/model/document.model';

@Component({
  selector: 'of-document-edit-right-sidebar',
  templateUrl: './document-edit-right-sidebar.component.html',
  styles: []
})
export class DocumentEditRightSidebarComponent implements OnInit {

  @Input()
  document: Document;

  isAttributesCollapsed = false;

  constructor() { }

  ngOnInit() {
  }

}
