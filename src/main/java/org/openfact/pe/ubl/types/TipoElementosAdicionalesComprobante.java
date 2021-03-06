package org.openfact.pe.ubl.types;

public enum TipoElementosAdicionalesComprobante {

    MONTO_EN_LETRAS("1000", "", "MONTO EN LETRAS"),
    LEYENDA_TRANSFERENCIA_GRATUITA("1002", "LEYENDA", "TRANSFERENCIA GRATUITA DE UN BIEN Y/O SERVICIO PRESTADO GRATUITAMENTE"),
    LEYENDA_COMPROBANTE_DE_PERCEPCION("2000", "LEYENDA", "COMPROBANTE DE PERCEPCIÓN"),
    LEYENDA_BIENES_TRANSFERIDOS_EN_LA_AMAZONIA("2001", "LEYENDA", "BIENES TRANSFERIDOS EN LA AMAZONÍA REGIÓN SELVAPARA SER CONSUMIDOS EN LA MISMA"),
    LEYENDA_SERVICIOS_PRESTADOS_EN_LA_AMAZONIA("2002", "LEYENDA", "SERVICIOS PRESTADOS EN LA AMAZONÍA REGIÓN SELVA PARA SER CONSUMIDOS EN LA MISMA"),
    LEYENDA_CONTRATOS_DE_CONSTRUCCION_EJECUTADOS_EN_LA_AMAZONIA("2003", "LEYENDA", "CONTRATOS DE CONSTRUCCIÓN EJECUTADOS EN LA AMAZONÍA REGIÓN SELVA"),
    LEYENDA_AGENCIA_DE_VIAJES("2004", "LEYENDA", "AGENCIA DE VIAJE - PAQUETE TURISTICO"),
    LEYENDA_VENTA_REALIZADA_POR_EMISOR_ITINERANTE("2005", "LEYENDA", "VENTA REALIZADA POR EMISOR ITINERANTE"),
    LEYENDA_OPERACION_SUJETA_A_DETRACCION("2006", "LEYENDA", "OPERACION SUJETA A DETRACCION"),
    LEYENDA_OPERACION_SUJETA_IVAP("2007", "LEYENDA", "OPERACION SUJETA A IVAP"),
    DETRACCIONES_CODIGO_DE_BB_SS("3000", "DETRACCIONES", "CODIGO DE BB Y SS SUJETOS A DETRACCION"),
    DETRACCIONES_NUMERO_CTA_BN("3001", "DETRACCIONES", "NUMERO DE CUENTA EN EL BN"),
    DETRACCIONES_RECURSOS_HIDROBIOLOGICOS_NOMBRE_MATRICULA_EMBARCACION("3002", "DETRACCIONES", "RECURSOS HIDROBIOLOGICOS-NOMBRE Y MATRICULA DE LA EMBARCACION"),
    DETRACCIONES_RECURSOS_HIDROBIOLOGICOS_TIPO_CANTIDAD_ESPECIE_VENDIDA("3003", "DETRACCIONES", "RECURSOS HIDROBIOLOGICOS-TIPO Y CANTIDAD DE ESPECIE VENDIDA"),
    DETRACCIONES_RECURSOS_HIDROBIOLOGICOS_LUGAR_DESCARGA("3004", "DETRACCIONES", "RECURSOS HIDROBIOLOGICOS-LUGAR DE DESCARGA"),
    DETRACCIONES_RECURSOS_HIDROBIOLOGICOS_FECHA_DESCARGA("3005", "DETRACCIONES", "RECURSOS HIDROBIOLOGICOS-FECHA DE DESCARGA"),
    DETRACCIONES_TRANSPORTE_VIENES_VIA_TERRESTRE_REGISTRO_MTC("3006", "DETRACCIONES", "TRANSPORTE DE BIENES VIA TERRESTRE-NUMERO REGISTRO MTC"),
    DETRACCIONES_TRANSPORTE_VIENES_VIA_TERRESTRE_CONF_VEHICULAR("3007", "DETRACCIONES", "TRANSPORTE DE BIENES VIA TERRESTRE-CONFIGURACION VEHICULAR"),
    DETRACCIONES_TRANSPORTE_VIENES_VIA_TERRESTRE_PUNTO_ORIGEN("3008", "DETRACCIONES", "TRANSPORTE DE BIENES VIA TERRESTRE-PUNTO DE ORIGEN"),
    DETRACCIONES_TRANSPORTE_VIENES_VIA_TERRESTRE_PUNTO_DESTINO("3009", "DETRACCIONES", "TRANSPORTE DE BIENES VIA TERRESTRE-PUNTO DESTINO"),
    DETRACCIONES_TRANSPORTE_VIENES_VIA_TERRESTRE_VALOR_REFERENCIA_PRELIMINAR("3010", "DETRACCIONES", "TRANSPORTE DE BIENES VIA TERRESTRE-VALOR REFERENCIAL PRELIMINAR"),
    BENEFICIO_HOSPEDAJE_COD_PAIS_EMISION_PASAPORTE("4000", "BENEFICIO HOSPEDAJE", "CODIGO PAIS DE EMISION DEL PASAPORTE"),
    BENEFICIO_HOSPEDAJE_COD_PAIS_RESIDENCIA_SUJETO_NO_DOMICILIADO("4001", "BENEFICIO HOSPEDAJE", "CODIGO PAIS DE RESIDENCIA DEL SUJETO NO DOMICILIADO"),
    BENEFICIO_HOSPEDAJE_FECHA_INGRESO_PAIS("4002", "BENEFICIO HOSPEDAJE", "FECHA DE ENGRESO AL PAIS"),
    BENEFICIO_HOSPEDAJE_FECHA_INGRESO_ESTABLECIMIENTO("4003", "BENEFICIO HOSPEDAJE", "FECHA DEL INGRESO AL ESTABLECIMIENTO"),
    BENEFICIO_HOSPEDAJE_FECHA_SALIDA_ESTABLECIMIENTO("4004", "BENEFICIO HOSPEDAJE", "FECHA DE SALIDA DEL ESTABLECIMIENTO"),
    BENEFICIO_HOSPEDAJE_NUMERO_DIAS_PERMANENCIA("4005", "BENEFICIO HOSPEDAJE", "NUMERO DE DIAS DE PERMANENCIA"),
    BENEFICIO_HOSPEDAJE_FECHA_CONSUMO("4006", "BENEFICIO HOSPEDAJE", "FECHA DE CONSUMO"),
    BENEFICIO_HOSPEDAJE_PAQUETE_TURISTICO_NOMBRE_APELLIDO_HUESPED("4007", "BENEFICIO HOSPEDAJE", "PAQUETE TURISTICO-NOMBRES Y APELLIDOS DEL HUESPED"),
    BENEFICIO_HOSPEDAJE_PAQUETE_TURISTICO_TIPO_DOCUMENTO_IDENTIDAD("4008", "BENEFICIO HOSPEDAJE", "PAQUETE TURISTICO-TIPO DOCUMENTO IDENTIDAD DEL HUESPED"),
    BENEFICIO_HOSPEDAJE_PAQUETE_TURISTICO_NUMERO_DOCUMENTO_IDENTIDAD("4009", "BENEFICIO HOSPEDAJE", "PAQUETE TURISTICO-NUMERO DE DOCUMENTO IDENTIDAD DE HUESPED"),
    PROVEEDORES_ESTADO_NUMERO_EXPEDIENTE("5000", "PROVEEDORES ESTADO", "NUMERO DE EXPEDIENTE"),
    PROVEEDORES_ESTADO_COD_UNID_EJECUTORA("5001", "PROVEEDORES ESTADO", "CODIGO DE UNIDAD EJECUTORA"),
    PROVEEDORES_ESTADO_NRO_PROCESO_SELECCION("5002", "PROVEEDORES ESTADO", "NUMERO DE PROCESO DE SELECCION"),
    PROVEEDORES_ESTADO_NRO_CONTRATO("5003", "PROVEEDORES ESTADO", "NUMERO DE CONTRATO"),
    COMERCIALIZACION_ORO_COD_UNICO_CONCESION_MINERA("6000", "COMERCIALIZACION DE ORO", "CODIGO UNICO CONCESION MINERA"),
    COMERCIALIZACION_ORO_NRO_DEC_COMPROMISO("6001", "COMERCIALIZACION DE ORO", "NUMERO DECLARACION COMPROMISO"),
    COMERCIALIZACION_ORO_NRO_REG_ESPECIAL_COMERC_ORO("6002", "COMERCIALIZACION DE ORO", "NUMERO REGISTRO ESPECIAL COMERCIALIZACION DE ORO"),
    COMERCIALIZACION_ORO_NRO_RESOLUCION_AUTORIZA_PLANTA_BENEFICIO("6003", "COMERCIALIZACION DE ORO", "NUMERO RESOLUCION QUE AUTORIZA PLANTA DE BENEFICIO"),
    COMERCIALIZACION_ORO_LEY_MINERAL("6004", "COMERCIALIZACION DE ORO", "LEY MINERAL (% CONCENTRACION ORO)");
    private final String codigo;
    private final String grupo;
    private final String denominacion;

    public String getCodigo() {
        return codigo;
    }

    public String getGrupo() {
        return grupo;
    }

    public String getDenominacion() {
        return denominacion;
    }

    private TipoElementosAdicionalesComprobante(String codigo, String grupo, String denominacion) {
        this.codigo = codigo;
        this.grupo = grupo;
        this.denominacion = denominacion;

    }
}
