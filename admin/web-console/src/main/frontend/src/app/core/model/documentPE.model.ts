export interface DocumentPE {
    tipo: string;

    serie: string;
    numero: string;

    entidadNumeroDeDocumento: string;
    entidadTipoDeDocumento: string;
    entidadDenominacion: string;
    entidadDireccion: string;
    entidadEmail: string;

    fechaDeEmision: Date;
    fechaDeVencimiento: Date;
    fechaDeReferencia: Date;

    moneda: string;
    tipoDeCambio: number;
    operacionGratuita;

    totalGravada: number;
    totalInafecta: number;
    totalExonerada: number;
    totalIgv: number;
    totalGratuita: number;

    igv: number;
    porcentajeDescuento: number;

    descuentoGlobal: number;
    totalOtrosCargos: number;
    total: number;

    detraccion: boolean;

    observaciones: string;

    documentoQueSeModifica: string;

    tipoDeNotaDeCredito: string;
    tipoDeNotaDeDebito: string;

    enviarAutomaticamenteASunat: boolean;
    enviarAutomaticamenteAlCliente: boolean;

    cancelado: boolean;

    detalle: Array<DocumentLinePE>;
}

export interface DocumentLinePE {
    unitCode: string;
    cantidad: number;
    tipoDeIgv: string;
    valorUnitario: number;
    precioUnitario: number;
    subtotal: number;
    igv: number;
    total: number;
    descripcion: string;
}
