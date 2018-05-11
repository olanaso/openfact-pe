package org.openfact.pe.ubl.types;

import java.util.Optional;
import java.util.stream.Stream;

public enum TipoInvoice {

    FACTURA("01", "FACTURA", "FACTURA ELECTRONICA", "^[F]{1}\\d{1,3}[-]\\d{1,8}$","F"),
    BOLETA("03", "BOLETA", "BOLETA ELECTRONICA", "^[B]{1}\\d{1,3}[-]\\d{1,8}$", "B");

    private final String codigo;
    private final String abreviatura;
    private final String denominacion;
    private final String documentIdPattern;
    private final String primeraLetra;

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

    TipoInvoice(String codigo, String abreviatura, String denominacion, String documentIdPattern, String primeraLetra) {
        this.codigo = codigo;
        this.abreviatura = abreviatura;
        this.denominacion = denominacion;
        this.documentIdPattern = documentIdPattern;
        this.primeraLetra = primeraLetra;
    }

    public static TipoInvoice getFromCode(String codigo) {
        Optional<TipoInvoice> op = Stream.of(TipoInvoice.values()).filter(p -> p.getCodigo().equals(codigo)).findFirst();
        if (op.isPresent()) {
            return op.get();
        } else {
            return null;
        }
    }

    public String getPrimeraLetra() {
        return primeraLetra;
    }
}
