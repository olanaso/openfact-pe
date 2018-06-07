package org.openfact.pe.ubl.types;

public enum TipoDocumentoEntidad {

    DOC_TRIB_NO_DOM_SIN_RUC("0", "NO DOMICILIADO", "DOC. TRIBUTARIO NO DOMICILIADO SIN RUC", 1, 15),
    DNI("1", "DNI", "DOC.NACIONAL DE IDENTIDAD", 8, 8),
    EXTRANJERIA("4", "C.EXTRANJERIA", "CARNET DE EXTRANJERIA", 1, 12),
    RUC("6", "RUC", "REGISTRO UNICO DE CONTRIBUYENTE", 11, 11),
    PASAPORTE("7", "PASS.", "PASAPORT", 1, 12),
    DEC_DIPLOMATICA("A", "CED.DIPLOMATICA", "CEDULA DIPLOMATICA DE IDENTIDAD", 15, 15);

    private final String codigo;
    private final String abreviatura;
    private final String denominacion;
    private final int minlength;
    private final int maxlength;

    public String getCodigo() {
        return codigo;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public String getDenominacion() {
        return denominacion;
    }

    public int getMinlength() {
        return minlength;
    }

    public int getMaxlength() {
        return maxlength;
    }


    private TipoDocumentoEntidad(String codigo, String abreviatura, String denominacion, int minlength, int maxlength) {
        this.codigo = codigo;
        this.abreviatura = abreviatura;
        this.denominacion = denominacion;
        this.minlength = minlength;
        this.maxlength = maxlength;
    }


}
