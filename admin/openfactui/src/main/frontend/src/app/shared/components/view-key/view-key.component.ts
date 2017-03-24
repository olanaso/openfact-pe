import { Component, Input, OnInit, ViewChild } from '@angular/core';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'of-view-key',
  templateUrl: './view-key.component.html',
  styles: [``]
})
export class ViewKeyComponent implements OnInit {

  @ViewChild('viewKeyModalContent')
  content: any;

  @Input()
  key: any;

  @Input()
  label: string;

  @Input()
  isSpan: boolean;

  @Input()
  isButton: boolean;

  @Input()
  isLink: boolean;

  @Input()
  disableOpen: boolean;

  constructor(private modalService: NgbModal) { }

  ngOnInit() {
  }

  open() {
    this.modalService.open(this.content).result.then((result) => {
    }, (reason) => {
    });
  }

  internalOpen() {
    if (!this.disableOpen) {
      this.modalService.open(this.content).result.then((result) => {
      }, (reason) => {
      });
    }
  }

}
