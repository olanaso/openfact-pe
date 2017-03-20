import { Component, OnDestroy, OnInit } from '@angular/core';

import { ActivatedRoute } from '@angular/router';
import { AlertService } from './../../../core/alert/alert.service';
import { DataService } from './../../../core/data/data.service';
import { Document } from './../../../core/model/document.model';
import { Organization } from './../../../core/model/organization.model';
import { Subscription } from 'rxjs/Subscription';
import { URLSearchParams } from '@angular/http';

@Component({
  selector: 'of-document-send-events',
  templateUrl: './document-send-events.component.html',
  styleUrls: []
})
export class DocumentSendEventsComponent implements OnInit, OnDestroy {

  parentDataSubscription: Subscription;
  dataSubscription: Subscription;

  organization: Organization;
  document: Document;
  sendEvents: Array<any>;

  selectedDestinyType = 'CUSTOMER';
  destinyType = [
    { denomination: 'to-customer', value: 'CUSTOMER' },
    { denomination: 'to-third-party', value: 'THIRD_PARTY' },
    { denomination: 'to-custom-email', value: 'CUSTOM_EMAIL' }
  ];

  breadcrumb: string;
  documentType: string;

  constructor(
    private route: ActivatedRoute,
    private dataService: DataService,
    private alertService: AlertService) {
  }

  ngOnInit() {
    this.parentDataSubscription = this.route.parent.data.subscribe(data => {
      this.organization = data['organization'];
    });
    this.dataSubscription = this.route.data.subscribe(data => {
      this.document = data['document'];
      this.breadcrumb = this.document.documentType.toLowerCase().replace('_', '-') + 's';
      this.documentType = this.document.documentType.toLowerCase().replace('_', '-');
      if (this.breadcrumb.endsWith('ss')) {
        this.breadcrumb = this.breadcrumb.slice(0, this.breadcrumb.length - 1);
      }

      this.loadData(this.selectedDestinyType);
    });
  }

  ngOnDestroy() {
    this.parentDataSubscription.unsubscribe();
    this.dataSubscription.unsubscribe();
  }

  loadData(destinyType: string) {
    const customerQueryParams: URLSearchParams = new URLSearchParams();
    customerQueryParams.set('destinyType', destinyType);
    this.document.getSendEvents(customerQueryParams).subscribe(data => {
      this.sendEvents = data;
    });
  }

  downloadFile(file) {
    this.dataService.files().download(this.organization, file.id, file.fileName);
  }

}
