import { ActivatedRoute, Router } from '@angular/router';
import { Component, OnDestroy, OnInit } from '@angular/core';

import { Document } from './../../../core/model/document.model';
import { Organization } from './../../../core/model/organization.model';
import { Subscription } from 'rxjs/Subscription';

@Component({
  selector: 'of-document-attached-documents',
  templateUrl: './document-attached-documents.component.html',
  styles: [``]
})
export class DocumentAttachedDocumentsComponent implements OnInit, OnDestroy {

  dataSubscription: Subscription;
  parentDataSubscription: Subscription;

  organization: Organization;
  document: Document;

  breadcrumb: string;
  documentType: string;

  constructor(
    private route: ActivatedRoute,
    private router: Router) {
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
    });
  }

  ngOnDestroy() {
    this.parentDataSubscription.unsubscribe();
    this.dataSubscription.unsubscribe();
  }

  viewAttachedDocument(document: Document) {
    if (document.documentType.toUpperCase() === 'INVOICE') {
      this.router.navigate(['../../../invoices', document.id], { relativeTo: this.route });
    } else if (document.documentType.toUpperCase() === 'CREDIT_NOTE') {
      this.router.navigate(['../../../credit-notes', document.id], { relativeTo: this.route });
    } else if (document.documentType.toUpperCase() === 'DEBIT_NOTE') {
      this.router.navigate(['../../../debit-notes', document.id], { relativeTo: this.route });
    } else if (document.documentType.toUpperCase() === 'PERCEPTION') {
      this.router.navigate(['../../../perceptions', document.id], { relativeTo: this.route });
    } else if (document.documentType.toUpperCase() === 'RETENTIONS') {
      this.router.navigate(['../../../retentions', document.id], { relativeTo: this.route });
    } else if (document.documentType.toUpperCase() === 'VOIDED_DOCUMENTS') {
      this.router.navigate(['../../../voided-documents', document.id], { relativeTo: this.route });
    } else if (document.documentType.toUpperCase() === 'SUMMARY_DOCUMENTS') {
      this.router.navigate(['../../../summary-documents', document.id], { relativeTo: this.route });
    }
  }

}
