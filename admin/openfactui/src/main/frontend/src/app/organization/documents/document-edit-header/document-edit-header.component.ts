import { ActivatedRoute, Router } from '@angular/router';
import { Component, Input, OnDestroy, OnInit, Output } from '@angular/core';

import { AlertService } from './../../../core/alert/alert.service';
import { DataService } from './../../../core/data/data.service';
import { DialogService } from './../../../core/dialog/dialog.service';
import { Document } from './../../../core/model/document.model';
import { NgForm } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Organization } from './../../../core/model/organization.model';
import { Subscription } from 'rxjs/Subscription';
import { URLSearchParams } from '@angular/http';

@Component({
  selector: 'of-document-edit-header',
  templateUrl: './document-edit-header.component.html',
  styles: []
})
export class DocumentEditHeaderComponent implements OnInit {

  @Input()
  organization: Organization;

  @Input()
  document: Document;

  @Input()
  breadcrumb: string;

  @Input()
  documentType: string;

  @Input()
  enableDelete = true;

  @Input()
  enableDownloadXml = true;

  @Input()
  enableDownloadCdr = true;

  @Input()
  enableDownloadPdf = true;

  @Input()
  enableAssignCreditNote = true;

  @Input()
  enableAssignDebitNote = true;

  @Input()
  enableSendToCustomer = true;

  @Input()
  enableSendToThirdParty = true;

  @Input()
  enableSendToCustomThirdParty = true;

  @Input()
  enableVoid = true;

  @Input()
  enableCheckTicket = false;

  @Input()
  documentNumberOfParents: number;

  thirdPartyByEmail: any = {};
  isAttatchedDocumentsCollapsed: boolean;
  isAttributesCollapsed: boolean;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private modalService: NgbModal,
    private dataService: DataService,
    private alertService: AlertService,
    private dialog: DialogService) {
  }

  ngOnInit() { }

  addRequiredAction(content: any) {
    this.modalService.open(content).result.then(
      (modalRequiredActions: string) => {
        const currentRequiredActions = this.document.requiredActions.slice();
        currentRequiredActions.push(modalRequiredActions.toString());
        this.document.save({
          requiredActions: currentRequiredActions
        }).subscribe(data => {
          this.document.requiredActions.push(modalRequiredActions.toString());
          this.alertService.pop('success', 'Success', 'Success! Document updated successfully.');
        });
      },
      (reason) => { });
  }

  removeRequiredAction(index: number, action: string) {
    this.dialog.confirmDelete(action, 'Required Action').result.then(
      (result) => {
        const requiredActions = this.document.requiredActions.slice().splice(index, 1);
        this.document.save({
          requiredActions: requiredActions
        }).subscribe(data => {
          this.document.requiredActions.splice(index, 1);
          this.alertService.pop('success', 'Success', 'Success! Document updated successfully.');
        });
      },
      (cancel) => { }
    );
  }

  downloadXml() {
    this.document.downloadXml();
  }

  downloadCdr() {
    this.dataService.organizationsSunat().downloadDocumentCdr(this.organization.organization, this.document.id);
  }

  checkTicket() {
    this.dataService.organizationsSunat().checkTicket(this.organization.organization, this.document.id);
  }

  downloadPdf() {
    const queryParams: URLSearchParams = new URLSearchParams();
    queryParams.set('format', 'pdf');
    this.document.downloadReport(queryParams);
  }

  sendToCustomer() {
    this.document.sendToCustomer().subscribe(data => {
      this.alertService.pop('success', 'Success', 'Success! Document sended to customer.');
    });
  }

  sendToThirdParty() {
    this.document.sendToThirdParty().subscribe(data => {
      this.alertService.pop('success', 'Success', 'Success! Document sended to third party.');
    });
  }

  sendToCustomThridPartyByEmail(content: any) {
    this.modalService.open(content).result.then(
      (form: NgForm) => {
        if (form.valid) {
          this.document.sendToThirdPartyByEmail({ email: form.value.thirdPartyByEmail.email }).subscribe(data => {
            this.alertService.pop('success', 'Success', 'Success! Document sended to third party.');
          });
        }
      },
      (reason) => { });
  }

  attachCreditNote() {
    let base = 'credit-notes';
    if (this.documentNumberOfParents) {
      for (let i = 0; i < this.documentNumberOfParents; i++) {
        base = '../' + base;
      }
    } else {
      base = '../../' + base;
    }

    this.router.navigate([base, 'create', { invoice: this.document.documentId }], { relativeTo: this.route });
  }

  attachDebitNote() {
    let base = 'debit-notes';
    if (this.documentNumberOfParents) {
      for (let i = 0; i < this.documentNumberOfParents; i++) {
        base = '../' + base;
      }
    } else {
      base = '../../' + base;
    }

    this.router.navigate([base, 'create', { invoice: this.document.documentId }], { relativeTo: this.route });
  }

  maskAsVoided() {
    let base = 'voided-documents';
    if (this.documentNumberOfParents) {
      for (let i = 0; i < this.documentNumberOfParents; i++) {
        base = '../' + base;
      }
    } else {
      base = '../../' + base;
    }
    this.router.navigate(
      [base, 'create', { document: this.document.documentId, type: this.document.documentType }],
      { relativeTo: this.route }
    );
  }

  delete() {
    this.dialog.confirmDelete(this.document.documentId, 'Document').result.then(
      (result) => {
        this.document.delete().subscribe((data) => {
          this.alertService.pop('success', 'Success', 'Success! Document deleted successfully.');

          let base = '';
          if (this.documentNumberOfParents) {
            for (let i = 0; i < this.documentNumberOfParents; i++) {
              base = '../' + base;
            }
            base = base + this.documentType + 's';
          } else {
            base = '../' + base;
          }
          this.router.navigate([base], { relativeTo: this.route });
        });
      },
      (cancel) => { }
    );
  }

}
