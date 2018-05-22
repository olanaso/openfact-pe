package org.openfact.pe.ubl.types;

import org.openfact.models.types.DocumentType;

public enum TipoDocumentoRelacionadoPercepcionRetencion {

    FACTURA("01", "FACTURA", DocumentType.INVOICE),
    BOLETA("03", "BOLETA DE VENTA", DocumentType.INVOICE),
    NOTA_CREDITO("07", "NOTA DE CREDITO", DocumentType.CREDIT_NOTE),
    NOTA_DEBITO("08", "NOTA DE DEBITO", DocumentType.DEBIT_NOTE);

    private final String codigo;
    private final String denominacion;
    private final DocumentType documentType;

    public String getCodigo() {
        return codigo;
    }

    public String getDenominacion() {
        return denominacion;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    TipoDocumentoRelacionadoPercepcionRetencion(String codigo, String denominacion, DocumentType documentType) {
        this.codigo = codigo;
        this.denominacion = denominacion;
        this.documentType = documentType;
    }

}
