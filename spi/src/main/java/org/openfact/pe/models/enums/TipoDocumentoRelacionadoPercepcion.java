package org.openfact.pe.models.enums;

import org.openfact.models.enums.DocumentType;

public enum TipoDocumentoRelacionadoPercepcion {

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

    TipoDocumentoRelacionadoPercepcion(String codigo, String denominacion, DocumentType documentType) {
        this.codigo = codigo;
        this.denominacion = denominacion;
        this.documentType = documentType;
    }

}
