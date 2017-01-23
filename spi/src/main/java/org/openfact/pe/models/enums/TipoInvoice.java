package org.openfact.pe.models.enums;

public enum TipoInvoice {

    FACTURA("01", "FACTURA ELECTRONICA", "^[F]{1}\\d{1,3}[-]\\d{1,8}$", 11),
    BOLETA("03", "BOLETA ELECTRONICA", "^[B]{1}\\d{1,3}[-]\\d{1,8}$", 8);

    private final String codigo;
    private final String denominacion;
    private final String documentIdPattern;
    private final int length;

    public  String getCodigo() {
        return codigo;
    }

    public  String getDenominacion() {
        return denominacion;
    }

    public String getDocumentIdPattern() {
        return documentIdPattern;
    }

    public int getLength() {
        return length;
    }

    TipoInvoice(String codigo, String denominacion, String documentIdPattern, int length) {
        this.codigo = codigo;
        this.denominacion = denominacion;
        this.documentIdPattern = documentIdPattern;
        this.length = length;
    }
}
