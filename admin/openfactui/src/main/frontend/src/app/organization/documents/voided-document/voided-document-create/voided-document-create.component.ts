import { ActivatedRoute, Router } from '@angular/router';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';

import { AlertService } from './../../../../core/alert/alert.service';
import { DataService } from './../../../../core/data/data.service';
import { DialogService } from './../../../../core/dialog/dialog.service';
import { Document } from './../../../../core/model/document.model';
import { GenericType } from './../../../../core/model/genericType.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Rx';
import { Organization } from './../../../../core/model/organization.model';
import { Subscription } from 'rxjs/Subscription';
import { URLSearchParams } from '@angular/http';

@Component({
  selector: 'of-voided-document-create',
  templateUrl: './voided-document-create.component.html',
  styles: [`
    .of-display-block {
      display: block;
    }
    input[readonly] {
      background-color: #d1d1d1 !important;
      color: #363636 !important;
    }
  `]
})
export class VoidedDocumentCreateComponent implements OnInit, OnDestroy {

  parentDataSubscription: Subscription;
  dataSubscription: Subscription;
  paramsSubscription: Subscription;

  form: FormGroup;
  working = false;

  organization: Organization;
  documentosRelacionadosVoid: GenericType[];

  documentMask = [/[B|F|b|f]/, /\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/, /\d/, /\d/, /\d/, /\d/, /\d/];
  numberMask = { allowDecimal: true, decimalLimit: 2 };

  constructor(private router: Router, private route: ActivatedRoute,
    private formBuilder: FormBuilder, private modalService: NgbModal,
    private dataService: DataService, private alertService: AlertService,
    private dialogService: DialogService) { }

  ngOnInit() {
    this.buildForm();
    this.parentDataSubscription = this.route.parent.data.subscribe((data) => {
      this.organization = data['organization'];
    });
    this.dataSubscription = this.route.data.subscribe((data) => {
      this.documentosRelacionadosVoid = data['documentosRelacionadosVoid'];
    });
    this.paramsSubscription = this.route.params.subscribe(params => {
      const documentId = params['document'];
      const documentType = params['type'];

      if (documentId && documentType) {
        const formGroup = this.addDetalleFormControl();
        this.findDocument(documentId, documentType).subscribe(data => {
          if (data && data.length > 0) {
            this.applyPatchForm(formGroup, data[0]);
          } else {
            this.alertService.pop('info', 'Info', 'Could not find Document.');
          }
        });
      }
    });
  }

  ngOnDestroy() {
    this.parentDataSubscription.unsubscribe();
    this.paramsSubscription.unsubscribe();
  }

  buildForm() {
    this.form = this.formBuilder.group({
      serieDocumento: [null, Validators.compose([Validators.maxLength(11)])],
      numeroDocumento: [null, Validators.compose([Validators.maxLength(5), Validators.pattern('[0-9]{1,5}')])],

      observaciones: [null, Validators.compose([Validators.maxLength(150)])],

      enviarAutomaticamenteASunat: [true, Validators.compose([Validators.required])],
      enviarAutomaticamenteAlCliente: [true, Validators.compose([Validators.required])],

      detalle: this.formBuilder.array([], Validators.compose([]))
    });
  }

  addDetalleFormControl(): FormGroup {
    const formGroup = this.formBuilder.group({
      tipoDocumentoRelacionado: [null, Validators.compose([Validators.required])],
      numeroDocumentoRelacionado: [null, Validators.compose([Validators.required, Validators.maxLength(13)])],
      descripcionDocumentoRelacionado: [null, Validators.compose([Validators.required, Validators.maxLength(150)])],

      monedaDocumentoRelacionado: [null],
      entidadDenominacionDocumentoRelacionado: [null]
    });

    this.detalle.push(formGroup);

    return formGroup;
  }

  removeDetalleFormControl(index) {
    this.detalle.removeAt(index);
  }

  get detalle(): FormArray {
    return this.form.get('detalle') as FormArray;
  }

  save(form: FormGroup): void {
    if (!form.value.detalle || form.value.detalle.length === 0) {
      this.alertService.pop('warning', 'Warning', 'Warning! Is required to add at least one line.');
      return;
    }

    this.dialogService.confirm('Confirm', 'Estas seguro de realizar esta operacion').result.then(
      (redirect) => {
        this.working = true;

        form.value.detalle.forEach(detalle => {
          detalle.monedaDocumentoRelacionado = undefined;
          detalle.entidadDenominacionDocumentoRelacionado = undefined;
        });

        this.dataService.organizationsSunat().createVoidedDocument(this.organization.organization, form.value).subscribe(
          response => {
            this.working = false;
            this.alertService.pop('success', 'Success', 'Success! The voided document has been created.');
            if (redirect) {
              this.router.navigate(['../'], { relativeTo: this.route });
            } else {
              this.buildForm();
            }
          },
          error => {
            this.working = false;
          }
        );
      },
      (dissmiss) => { }
    );
  }

  cancel() {
    this.router.navigate(['../'], { relativeTo: this.route });
  }

  findDocument(documentId: string, documentType: string): Observable<Document[]> {
    const queryParam: URLSearchParams = new URLSearchParams();
    queryParam.set('documentId', documentId);
    queryParam.set('documentType', documentType);
    return this.dataService.documents().getAll(this.organization, queryParam);
  }

  findFormDocument(formGroup: FormGroup) {
    const numeroDocumento = formGroup.get('numeroDocumentoRelacionado').value;
    const tipoDocumentoCodigo = formGroup.get('tipoDocumentoRelacionado').value;

    if (this.documentosRelacionadosVoid && this.documentosRelacionadosVoid.length > 0) {
      const tipoDocumento = this.documentosRelacionadosVoid.find(f => f.codigo === tipoDocumentoCodigo);

      this.findDocument(numeroDocumento, tipoDocumento.grupo).subscribe(data => {
        if (data && data.length > 0) {
          this.applyPatchForm(formGroup, data[0]);
        } else {
          this.alertService.pop('info', 'Info', 'Could not find Document.');
        }
      });
    }
  }

  applyPatchForm(formGroup: FormGroup, document: Document) {
    let type: GenericType;
    if (document.documentType.toUpperCase() === 'INVOICE') {
      if (document.documentId.toUpperCase().startsWith('F')) {
        type = this.getDocumentoRelacionadoVoidByDenomination('FACTURA');
      } else if (document.documentId.toUpperCase().startsWith('B')) {
        type = this.getDocumentoRelacionadoVoidByDenomination('BOLETA');
      }
    } else if (document.documentType.toUpperCase() === 'CREDIT_NOTE') {
      type = this.getDocumentoRelacionadoVoidByDenomination('CREDITO');
    } else if (document.documentType.toUpperCase() === 'DEBIT_NOTE') {
      type = this.getDocumentoRelacionadoVoidByDenomination('DEBITO');
    }

    formGroup.patchValue({
      tipoDocumentoRelacionado: type !== null ? type.codigo : null,
      numeroDocumentoRelacionado: document['documentId'],
      monedaDocumentoRelacionado: document['documentCurrencyCode'],
      entidadDenominacionDocumentoRelacionado: document['customerRegistrationName']
    });
  }

  getDocumentoRelacionadoVoidByDenomination(denomination: string): GenericType {
    if (this.documentosRelacionadosVoid && this.documentosRelacionadosVoid.length > 0) {
      return this.documentosRelacionadosVoid.find(f => f.denominacion.toUpperCase().indexOf(denomination.toUpperCase()) !== -1);
    }
    return null;
  }

}
