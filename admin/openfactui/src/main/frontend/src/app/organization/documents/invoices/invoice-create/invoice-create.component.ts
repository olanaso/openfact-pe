import { ActivatedRoute, Router } from '@angular/router';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';

import { AlertService } from './../../../../core/alert/alert.service';
import { DataService } from './../../../../core/data/data.service';
import { DialogService } from './../../../../core/dialog/dialog.service';
import { GenericType } from './../../../../core/model/genericType.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { OfValidators } from './../../../../shared/validators/of-validators';
import { Organization } from './../../../../core/model/organization.model';
import { Subscription } from 'rxjs/Subscription';

@Component({
  selector: 'of-invoice-create',
  templateUrl: './invoice-create.component.html',
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
export class InvoiceCreateComponent implements OnInit, OnDestroy {

  parentDataSubscription: Subscription;
  dataSubscription: Subscription;

  form: FormGroup;
  working = false;
  advanceMode = false;

  organization: Organization;
  tiposComprobantePago: GenericType[];
  tiposDocumentEntidad: GenericType[];
  tiposDeAfectacionIgv: GenericType[];

  igv: GenericType;
  monedasSoportadas = ['PEN', 'USD'];

  documentSerieNumeroMask = { allowDecimal: false, thousandsSeparatorSymbol: '' };
  numberMask = { allowDecimal: true, decimalLimit: 2 };
  quantityMask = { allowDecimal: true, decimalLimit: 3 };
  percentMask = { allowDecimal: true, decimalLimit: 2, prefix: '% ' };

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
      this.tiposComprobantePago = data['tiposComprobantePago'] || [];
      this.tiposDocumentEntidad = data['tiposDocumentEntidad'] || [];
      this.tiposDeAfectacionIgv = data['tiposDeAfectacionIgv'] || [];
      this.igv = data['igv'];
      this.loadDataForm();
    });
  }

  ngOnDestroy() {
    this.parentDataSubscription.unsubscribe();
    this.dataSubscription.unsubscribe();
  }

  buildForm() {
    this.form = this.formBuilder.group({
      serie: [null, Validators.compose([Validators.maxLength(4)])],
      numero: [null, Validators.compose([Validators.maxLength(8)])],

      tipo: [null, Validators.compose([Validators.required])],
      igv: [null, Validators.compose([Validators.required])],

      entidadTipoDeDocumento: [null, Validators.compose([Validators.required])],
      entidadNumeroDeDocumento: [null, Validators.compose([Validators.required, Validators.minLength(20), Validators.maxLength(20)])],
      entidadDenominacion: [null, Validators.compose([Validators.required, Validators.maxLength(150)])],
      entidadEmail: [null, Validators.compose([Validators.maxLength(150)])],

      moneda: [null, Validators.compose([Validators.required, Validators.maxLength(3)])],
      operacionGratuita: [false, Validators.compose([Validators.required])],

      enviarAutomaticamenteASunat: [true, Validators.compose([Validators.required])],
      enviarAutomaticamenteAlCliente: [true, Validators.compose([Validators.required])],

      observaciones: [null, Validators.compose([Validators.maxLength(150)])],

      totalGravada: [null, Validators.compose([Validators.required])],
      totalExonerada: [null, Validators.compose([Validators.required])],
      totalInafecta: [null, Validators.compose([Validators.required])],
      totalGratuita: [null, Validators.compose([Validators.required])],
      totalIgv: [null, Validators.compose([Validators.required])],

      porcentajeDescuento: [null, Validators.compose([])],
      descuentoGlobal: [null, Validators.compose([Validators.required])],

      totalOtrosCargos: [null, Validators.compose([])],
      total: [null, Validators.compose([Validators.required])],

      detalle: this.formBuilder.array([], Validators.compose([]))
    });

    // Moneda extrangera
    this.form.get('moneda').valueChanges.subscribe(value => {
      if (value && value !== 'PEN') {
        this.form.addControl('tipoDeCambio', this.formBuilder.control(null, Validators.compose([Validators.required])));
      } else {
        this.form.removeControl('tipoDeCambio');
      }
    });

    // Observer al cambiar tipo de comprobante de pago Boleta/Factura
    this.form.get('tipo').valueChanges.subscribe(value => {
      if (value) {
        let tipoDocumento;
        const tipoComprobante = this.tiposComprobantePago.find(f => f.codigo === value);
        if (tipoComprobante && tipoComprobante.abreviatura.toUpperCase() === 'BOLETA') {
          tipoDocumento = this.tiposDocumentEntidad.find(f => f.abreviatura.toUpperCase() === 'DNI');
        } else if (tipoComprobante && tipoComprobante.abreviatura.toUpperCase() === 'FACTURA') {
          tipoDocumento = this.tiposDocumentEntidad.find(f => f.abreviatura.toUpperCase() === 'RUC');
        }

        if (tipoDocumento && tipoComprobante) {
          this.form.patchValue({
            entidadTipoDeDocumento: tipoDocumento.codigo
          });
        }

        // poner envio automatico
        if (tipoComprobante) {
          if (tipoComprobante.abreviatura.toUpperCase() === 'FACTURA') {
            this.form.patchValue({
              enviarAutomaticamenteASunat: true
            });
          } else if (tipoComprobante.abreviatura.toUpperCase() === 'BOLETA') {
            this.form.patchValue({
              enviarAutomaticamenteASunat: false
            });
          }
        }
      }
    });

    // Observer para cambiar longitud del tipo de documento
    this.form.get('entidadTipoDeDocumento').valueChanges.subscribe(value => {
      if (value) {
        const tipoComprobante = this.tiposDocumentEntidad.find(f => f.codigo === value);
        if (tipoComprobante) {
          this.form.get('entidadNumeroDeDocumento').setValidators(Validators.compose([
            Validators.required,
            Validators.minLength(tipoComprobante.length),
            Validators.maxLength(tipoComprobante.length)
          ]));
        }
      }
    });

    this.form.get('operacionGratuita').valueChanges.subscribe(value => {
      this.form.patchValue({
        porcentajeDescuento: null,
        totalOtrosCargos: null
      });
      this.recalcularDatos();
    });
    this.form.get('porcentajeDescuento').valueChanges.subscribe(value => {
      this.recalcularDatos();
    });
    this.form.get('totalOtrosCargos').valueChanges.subscribe(value => {
      this.recalcularDatos();
    });
    this.detalle.valueChanges.subscribe(value => {
      this.recalcularDatos();
    });
  }

  addDetalleFormControl(): void {
    const formGroup = this.formBuilder.group({
      unitCode: [null, Validators.compose([Validators.maxLength(150)])],
      descripcion: [null, Validators.compose([Validators.required, Validators.maxLength(150)])],
      cantidad: [null, Validators.compose([Validators.required, Validators.minLength(1)])],
      tipoDeIgv: [null, Validators.compose([Validators.required, Validators.minLength(1)])],
      // valor del producto sin igv
      valorUnitario: [null, Validators.compose([Validators.required, Validators.minLength(1)])],
      // valor del producto con igv
      precioUnitario: [null, Validators.compose([Validators.required, Validators.minLength(1)])],
      subtotal: [null, Validators.compose([Validators.required, Validators.minLength(1)])],
      total: [null, Validators.compose([Validators.required, Validators.minLength(1)])],
      igv: [null, Validators.compose([Validators.required])]
    });
    this.loadDataDetalle(formGroup);
    this.detalle.push(formGroup);
  }

  removeDetalleFormControl(index) {
    this.detalle.removeAt(index);
  }

  getIgvFactor(formControl: FormControl): number {
    const tipoAfectacionIgv = this.tiposDeAfectacionIgv.find(p => p.codigo === formControl.get('tipoDeIgv').value);
    if (tipoAfectacionIgv && tipoAfectacionIgv.afectaIgv) {
      return this.getIgvAsDecimal();
    }
    return 0;
  }

  recalcularDatos() {
    let totalGratuita = 0;
    let totalGravado = 0, totalExonerado = 0, totalInafecto = 0;
    let totalIgv = 0;

    const operacionGratuita = this.form.get('operacionGratuita').value;
    let porcentajeDescuento = (this.form.get('porcentajeDescuento').value || 0) / 100;
    let totalOtrosCargos = this.form.get('totalOtrosCargos').value || 0;

    if (operacionGratuita === true) {
      porcentajeDescuento = 0;
      totalOtrosCargos = 0;
    }

    this.detalle.controls.forEach(formControl => {

      // Se debe de multiplicar nuevamente para no perder los redondeos y sumar con todos los digitos
      const subtotal = (formControl.get('cantidad').value || 0) * (formControl.get('valorUnitario').value || 0);
      const total = subtotal * this.getIgvAsInteger();
      const tipoIgv = this.tiposDeAfectacionIgv.find(p => p.codigo === formControl.get('tipoDeIgv').value);

      // Operacion gratuita
      if (operacionGratuita) {
        totalGratuita += subtotal;

        if (tipoIgv.afectaIgv) {
          totalGratuita += (subtotal * this.getIgvAsDecimal());
        }
      } else {
        if (tipoIgv.grupo.toUpperCase() === 'GRAVADO') {
          totalGravado += subtotal;
        } else if (tipoIgv.grupo.toUpperCase() === 'EXONERADO') {
          totalExonerado += subtotal;
        } else if (tipoIgv.grupo.toUpperCase() === 'INAFECTO') {
          totalInafecto += subtotal;
        } else {
          throw new Error('Invalid IGV');
        }

        if (tipoIgv.afectaIgv) {
          totalIgv += (subtotal * this.getIgvAsDecimal());
        }
      }

    });

    const totalGravadaConDescuento = totalGravado - (totalGravado * porcentajeDescuento);
    const totalExoneradaConDescuento = totalExonerado - (totalExonerado * porcentajeDescuento);
    const totalInafectaConDescuento = totalInafecto - (totalInafecto * porcentajeDescuento);
    const totalIgvConDescuento = totalIgv - (totalIgv * porcentajeDescuento);

    // Descuento global
    const descuentoGlobal =
      (totalGravado - totalGravadaConDescuento) +
      (totalExonerado - totalExoneradaConDescuento) +
      (totalInafecto - totalInafectaConDescuento);

    // Calculo del total
    const total: number = totalGravadaConDescuento +
      totalExoneradaConDescuento +
      totalInafectaConDescuento +
      totalIgvConDescuento +
      totalOtrosCargos;

    this.form.patchValue({
      totalGratuita: +totalGratuita.toFixed(2),
      totalGravada: +totalGravadaConDescuento.toFixed(2),
      totalExonerada: +totalExoneradaConDescuento.toFixed(2),
      totalInafecta: +totalInafectaConDescuento.toFixed(2),
      totalIgv: +totalIgvConDescuento.toFixed(2),
      descuentoGlobal: +descuentoGlobal.toFixed(2),
      total: +total.toFixed(2)
    });
  }

  loadDataForm() {
    const formValue: any = {};
    if (this.tiposComprobantePago && this.tiposComprobantePago.length > 0) {
      formValue.tipo = this.tiposComprobantePago[0].codigo;
    }
    if (this.igv) {
      formValue.igv = +(this.igv.valor * 100).toFixed(2);
    }
    if (this.monedasSoportadas && this.monedasSoportadas.length > 0) {
      formValue.moneda = this.monedasSoportadas[0];
    }

    this.form.patchValue(formValue);
  }

  loadDataDetalle(formGroup: FormGroup) {
    if (this.tiposDeAfectacionIgv && this.tiposDeAfectacionIgv.length > 0) {
      formGroup.patchValue({
        tipoDeIgv: this.tiposDeAfectacionIgv[0].codigo
      });
    }
  }

  round(value: number, spaces?: number): number {
    if (!spaces) {
      spaces = 2;
    }
    return Math.round(value * Math.pow(10, spaces)) / Math.pow(10, spaces);
  }

  get detalle(): FormArray {
    return this.form.get('detalle') as FormArray;
  }

  getIgvAsInteger() {
    return this.form.get('igv').value || 0;
  }

  getIgvAsDecimal(): number {
    return (this.form.get('igv').value || 0) / 100;
  }

  save(form: FormGroup): void {
    if (!form.value.detalle || form.value.detalle.length === 0) {
      this.alertService.pop('warning', 'Warning', 'Warning! Is required to add at least one line.');
      return;
    }

    this.dialogService.confirm('Confirm', 'Estas seguro de realizar esta operacion').result.then(
      (redirect) => {
        this.working = true;

        /*if (form.value.serie !== 'undefined' && form.value.serie !== null) {
          const pad = '000';
          form.value.serie = 'F' + (pad + form.value.serie).slice(-pad.length);
        }
        if (form.value.numero !== 'undefined' && form.value.numero !== null) {
          const pad = '00000000';
          form.value.numero = (pad + form.value.numero).slice(-pad.length);
        }*/

        this.dataService.organizationsSunat().createInvoice(this.organization.organization, form.value).subscribe(
          response => {
            this.working = false;
            this.alertService.pop('success', 'Success', 'Success! The invoice has been created.');
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

}
