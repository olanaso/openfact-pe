package org.openfact.pe.models.enums;

public enum TipoInvoice {

    FACTURA("01", "FACTURA", "FACTURA ELECTRONICA", "^[F]{1}\\d{1,3}[-]\\d{1,8}$"),
    BOLETA("03", "BOLETA", "BOLETA ELECTRONICA", "^[B]{1}\\d{1,3}[-]\\d{1,8}$");

    private final String codigo;
    private final String abreviatura;
    private final String denominacion;
    private final String documentIdPattern;

    public String getCodigo() {
        return codigo;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public String getDenominacion() {
        return denominacion;
    }

    public String getDocumentIdPattern() {
        return documentIdPattern;
    }

    TipoInvoice(String codigo, String abreviatura, String denominacion, String documentIdPattern) {
        this.codigo = codigo;
        this.abreviatura = abreviatura;
        this.denominacion = denominacion;
        this.documentIdPattern = documentIdPattern;
    }
}
