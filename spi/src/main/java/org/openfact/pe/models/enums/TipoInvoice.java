package org.openfact.pe.models.enums;

public enum TipoInvoice {

    FACTURA("01", "FACTURA ELECTRONICA", "^[F]{1}\\d{1,3}[-]\\d{1,8}$"),
    BOLETA("03", "BOLETA ELECTRONICA", "^[B]{1}\\d{1,3}[-]\\d{1,8}$");

    private final String codigo;
    private final String denominacion;
    private final String documentIdPattern;

    public  String getCodigo() {
        return codigo;
    }

    public  String getDenominacion() {
        return denominacion;
    }

    public String getDocumentIdPattern() {
        return documentIdPattern;
    }

    TipoInvoice(String codigo, String denominacion, String documentIdPattern) {
        this.codigo = codigo;
        this.denominacion = denominacion;
        this.documentIdPattern = documentIdPattern;
    }
}
