import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'of-dialog',
  templateUrl: './dialog.component.html',
  styles: []
})
export class DialogComponent implements OnInit {

  // which action will be activate confirm, delete
  @Input()
  action: string;

  // Title of the dialog
  @Input()
  title: string;

  // Message of the dialog
  @Input()
  message: string;

  // Used just in delete action. Indicate the object type
  @Input()
  objectType: string;

  // Used just in delete action. Indicate the object name
  @Input()
  objectName: string;

  constructor(public activeModal: NgbActiveModal) { }

  ngOnInit() {
  }

  dismiss(message: string) {
    if (this.activeModal.dismiss) {
      this.activeModal.dismiss(message);
    }
  }

}
