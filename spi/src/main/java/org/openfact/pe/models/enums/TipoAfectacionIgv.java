package org.openfact.pe.models.enums;

public enum TipoAfectacionIgv {

    GRAVADO_OPERACION_ONEROSA("10", "Gravado - Operación Onerosa", true, TipoValorVenta.GRAVADO),
    GRAVADO_RETIRO_POR_PREMIO("11", "Gravado - Retiro por premio", true, TipoValorVenta.GRAVADO),
    GRAVADO_RETIRO_POR_DONACION("12", "Gravado - Retiro por donación", true, TipoValorVenta.GRAVADO),
    GRAVADO_RETIRO("13", "Gravado - Retiro", true, TipoValorVenta.GRAVADO),
    GRAVADO_RETIRO_POR_PUBLICIDAD("14", "Gravado - Retiro por publicidad", true, TipoValorVenta.GRAVADO),
    GRAVADO_BONIFICACIONES("15", "Gravado - Bonificaciones", true, TipoValorVenta.GRAVADO),
    GRAVADO_RETIRO_POR_ENTREGA_A_TRABAJADORES("16", "Gravado – Retiro por entrega a trabajadores", true, TipoValorVenta.GRAVADO),
    GRAVADO_IVAP("17", "Gravado – IVAP", false, TipoValorVenta.GRAVADO),
    EXONERADO_OPERACION_ONEROSA("20", "Exonerado - Operación Onerosa", false, TipoValorVenta.EXONERADO),
    EXONERADO_TRANSFERENCIA_GRATUITA("21", "Exonerado – Transferencia Gratuita", false, TipoValorVenta.EXONERADO),
    INAFECTO_OPERACION_ONEROSA("30", "Inafecto - Operación Onerosa", false, TipoValorVenta.INAFECTO),
    INAFECTO_RETIRO_POR_BONIFICACION("31", "Inafecto - Retiro por Bonificación", false, TipoValorVenta.INAFECTO),
    INAFECTO_RETIRO("32", "Inafecto - Retiro", false, TipoValorVenta.INAFECTO),
    INAFECTO_RETIRO_POR_MUESTRAS_MEDICAS("33", "Inafecto - Retiro por Muestras Médicas", false, TipoValorVenta.INAFECTO),
    INAFECTO_RETIRO_POR_CONVENIO_COLECTIVO("34", "Inafecto - Retiro por Convenio Colectivo", false, TipoValorVenta.INAFECTO),
    INAFECTO_RETIRO_POR_PREMIO("35", "Inafecto - Retiro por premio", false, TipoValorVenta.INAFECTO),
    INAFECTO_RETIRO_POR_PUBLICIDAD("36", "Inafecto - Retiro por publicidad", false, TipoValorVenta.INAFECTO),
    EXPORTACION("40", "Exportacion", false, TipoValorVenta.EXPORTACION);

    private final String codigo;
    private final String denominacion;
    private final boolean afectaIgv;
    private final TipoValorVenta grupo;

    public String getCodigo() {
        return codigo;
    }

    public String getDenominacion() {
        return denominacion;
    }

    public boolean getAfectaIgv() {
        return afectaIgv;
    }


    public TipoValorVenta getGrupo() {
        return grupo;
    }


    private TipoAfectacionIgv(String codigo, String denominacion, boolean afectaIgv, TipoValorVenta grupo) {
        this.codigo = codigo;
        this.denominacion = denominacion;
        this.afectaIgv = afectaIgv;
        this.grupo = grupo;
    }
}
