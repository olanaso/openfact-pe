import { ActivatedRoute, Router } from '@angular/router';
import { Component, OnDestroy, OnInit } from '@angular/core';

import { AlertService } from './../../../../core/alert/alert.service';
import { DataService } from './../../../../core/data/data.service';
import { DialogService } from './../../../../core/dialog/dialog.service';
import { Document } from './../../../../core/model/document.model';
import { NgForm } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Rx';
import { Organization } from './../../../../core/model/organization.model';
import { Subscription } from 'rxjs/Subscription';
import { URLSearchParams } from '@angular/http';

@Component({
  selector: 'of-credit-note-edit',
  templateUrl: './credit-note-edit.component.html',
  styles: [``]
})
export class CreditNoteEditComponent implements OnInit, OnDestroy {

  parentDataSubscription: Subscription;
  dataSubscription: Subscription;

  organization: Organization;
  document: Document;
  documentJson: Observable<any>;

  constructor(private activatedRoute: ActivatedRoute) { }

  ngOnInit() {
    this.parentDataSubscription = this.activatedRoute.parent.data.subscribe(data => {
      this.organization = data['organization'];
    });
    this.dataSubscription = this.activatedRoute.data.subscribe(data1 => {
      this.document = data1['document'];
      this.document.getJsonRepresentation().subscribe(data2 => {
        this.documentJson = data2;
      });
    });
  }

  ngOnDestroy() {
    this.parentDataSubscription.unsubscribe();
    this.dataSubscription.unsubscribe();
  }

}
