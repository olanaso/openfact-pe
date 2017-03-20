import { AfterViewInit, OnInit } from '@angular/core';
import { ChangeDetectorRef, ContentChild, Directive, Input, QueryList, ViewChild } from '@angular/core';

import { NgControl } from '@angular/forms';

@Directive({
  selector: '[ofUblLineQuantity]'
})
export class UblLineQuantityDirective {
  constructor(public control: NgControl) { }
}

// Precio sin igv
@Directive({
  selector: '[ofUblLineUnitValue]'
})
export class UblLineUnitValueDirective {
  constructor(public control: NgControl) { }
}

// Precio con igv
@Directive({
  selector: '[ofUblLineUnitPrice]'
})
export class UblLineUnitPriceDirective {
  constructor(public control: NgControl) { }
}

@Directive({
  selector: '[ofUblLineSubtotal]'
})
export class UblLineSubtotalDirective {
  constructor(public control: NgControl) { }
}

@Directive({
  selector: '[ofUblLineTotal]'
})
export class UblLineTotalDirective {
  constructor(public control: NgControl) { }
}

@Directive({
  selector: '[ofUblLineTaxAmount]'
})
export class UblLineTaxAmountDirective {
  constructor(public control: NgControl) { }
}

// al sacar subtotal y total se debe de redondear a dos digitos
// Sin embargo para el calculo de total gravado, exonerado e inafecto se
// debe debe de realizar la suma de subtotales y totales tomando en cuenta todos los decimales
export interface UblLine {
  quantity: number;
  unitValue: number;
  unitPrice: number;
  subtotal: number;
  total: number;
  taxAmount: number;
}

@Directive({
  selector: '[ofUblLine]'
})
export class UblLineDirective implements OnInit, AfterViewInit {

  // Se debe de ingresar el factor de incremento ej. 0.18
  private _tax = 1;

  @Input('ofUblLine')
  set ofUblLine(ofUblLine: number) {
    this._tax = ofUblLine + 1;
    this.currentState = null;
    this.refreshLeft();
  }

  currentState: UblLine;

  private _quantity: number;
  private _unitValue: number;
  private _unitPrice: number;
  private _subtotal: number;
  private _total: number;
  private _taxAmount: number;

  @ContentChild(UblLineQuantityDirective)
  quantityDirective: UblLineQuantityDirective;

  @ContentChild(UblLineUnitValueDirective)
  unitValueDirective: UblLineUnitValueDirective;

  @ContentChild(UblLineUnitPriceDirective)
  unitPriceDirective: UblLineUnitPriceDirective;

  @ContentChild(UblLineSubtotalDirective)
  subtotalDirective: UblLineSubtotalDirective;

  @ContentChild(UblLineTotalDirective)
  totalDirective: UblLineTotalDirective;

  @ContentChild(UblLineTaxAmountDirective)
  taxAmountDirective: UblLineTaxAmountDirective;


  constructor(private _changeDetectionRef: ChangeDetectorRef) { }

  ngOnInit() {
    this._changeDetectionRef.detach();
  }

  ngAfterViewInit() {
    if (this.quantityDirective) { this.quantity = this.quantityDirective.control.value; }
    if (this.unitValueDirective) { this.unitValue = this.unitValueDirective.control.value; }
    if (this.subtotalDirective) { this.subtotal = this.subtotalDirective.control.value; }
    if (this.totalDirective) { this.total = this.totalDirective.control.value; }
    setTimeout(() => this._changeDetectionRef.reattach());

    this.quantityDirective.control.valueChanges.subscribe(controlValue => this.quantity = controlValue);
    this.unitValueDirective.control.valueChanges.subscribe(controlValue => this.unitValue = controlValue);
    this.subtotalDirective.control.valueChanges.subscribe(controlValue => this.subtotal = controlValue);
    this.totalDirective.control.valueChanges.subscribe(controlValue => this.total = controlValue);
  }

  refreshLeft() {
    if (this._quantity && this._unitValue && this.isFireAllowed()) {
      this._subtotal = this._quantity * this._unitValue;
      this._total = this._subtotal * this._tax;

      this.currentState = this.getResult();
      this.refreshChildren();
    }
  }

  refreshSubRight() {
    if (this._unitValue && this._subtotal && this.isFireAllowed()) {
      this._quantity = this._subtotal / this._unitValue;
      this._total = this._subtotal * this._tax;

      this.currentState = this.getResult();
      this.refreshChildren();
    }
  }

  refreshRight() {
    if (this._unitValue && this._total && this.isFireAllowed()) {
      this._quantity = this._total / (this._unitValue * this._tax);
      this._subtotal = this._quantity * this._unitValue;

      this.currentState = this.getResult();
      this.refreshChildren();
    }
  }

  refreshChildren() {
    this.quantityDirective.control.control.setValue(this.currentState.quantity);
    this.unitValueDirective.control.control.setValue(this.currentState.unitValue);
    this.subtotalDirective.control.control.setValue(this.currentState.subtotal);
    this.totalDirective.control.control.setValue(this.currentState.total);
    this.unitPriceDirective.control.control.setValue(this.currentState.unitPrice);
    this.taxAmountDirective.control.control.setValue(this.currentState.taxAmount);
  }

  // redondeos para evitar errores de multiplicacion con float y decimales indeterminados
  getResult(): UblLine {
    this._quantity = +this._quantity.toFixed(3);
    this._unitPrice = +(this._unitValue * this._tax).toFixed(2);
    this._unitValue = +this._unitValue.toFixed(2);
    this._taxAmount = +(this._subtotal * (+(this._tax - 1).toFixed(2))).toFixed(2);
    this._subtotal = +this._subtotal.toFixed(2);
    this._total = +this._total.toFixed(2);

    const result: UblLine = {
      quantity: this._quantity,
      unitValue: this._unitValue,
      unitPrice: this._unitPrice,
      subtotal: this._subtotal,
      total: this._total,
      taxAmount: this._taxAmount
    };
    return result;
  }

  isFireAllowed(): boolean {
    if (this.currentState &&
      this._quantity === this.currentState.quantity &&
      this._unitValue === this.currentState.unitValue &&
      this._subtotal === this.currentState.subtotal &&
      this._total === this.currentState.total) {
      return false;
    }
    return true;
  }

  set quantity(quantity: number) {
    this._quantity = quantity;
    this.refreshLeft();
  }

  set unitValue(unitValue: number) {
    this._unitValue = unitValue;
    this.refreshLeft();
  }

  set subtotal(subtotal: number) {
    this._subtotal = subtotal;
    this.refreshSubRight();
  }

  set total(total: number) {
    this._total = total;
    this.refreshRight();
  }

}
