package org.openfact.pe.ubl.types;

import java.util.Optional;
import java.util.stream.Stream;

public enum TipoDocumentoRelacionadoBaja {

    FACTURA("01", "FACTURA", "INVOICE"),
    BOLETA("03", "BOLETA DE VENTA", "INVOICE"),
    NOTA_CREDITO("07", "NOTA DE CREDITO", "DEBIT_NOTE"),
    NOTA_DEBITO("08", "NOTA DE DEBITO", "CREDIT_NOTE"),

    PERCEPCION("40", "PERCEPCION", "PERCEPTION"),
    RETENCION("20", "RETENCION", "RETENTION");

    private final String codigo;
    private final String denominacion;
    private final String documentType;

    public String getCodigo() {
        return codigo;
    }

    public String getDenominacion() {
        return denominacion;
    }

    public String getDocumentType() {
        return documentType;
    }

    TipoDocumentoRelacionadoBaja(String codigo, String denominacion, String documentType) {
        this.codigo = codigo;
        this.denominacion = denominacion;
        this.documentType = documentType;
    }

    public static Optional<TipoDocumentoRelacionadoBaja> fromCodigo(String codigo) {
        return Stream.of(TipoDocumentoRelacionadoBaja.values()).filter(p -> p.codigo.equals(codigo)).findFirst();
    }

}
