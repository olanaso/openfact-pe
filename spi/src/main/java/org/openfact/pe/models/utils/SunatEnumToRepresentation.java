package org.openfact.pe.models.utils;

import org.openfact.pe.constants.*;
import org.openfact.pe.representations.idm.GenericTypeRepresentation;

/**
 * Created by lxpary on 11/01/17.
 */
public class SunatEnumToRepresentation {
    public static GenericTypeRepresentation toRepresentation(TipoAfectacionIgv type) {
        GenericTypeRepresentation rep = new GenericTypeRepresentation();
        rep.setCodigo(type.getCodigo());
        rep.setDenominacion(type.getDenominacion());
        rep.setAfectaIgv(type.getAfectaIgv());
        rep.setGrupo(type.getGrupo().getDenominacion());
        return rep;
    }

    public static GenericTypeRepresentation toRepresentation(TipoComprobante type) {
        GenericTypeRepresentation rep = new GenericTypeRepresentation();
        rep.setCodigo(type.getCodigo());
        rep.setDenominacion(type.getDenominacion());
        rep.setLength(type.getLength());
        return rep;
    }

    public static GenericTypeRepresentation toRepresentation(TipoConceptosTributarios type) {
        GenericTypeRepresentation rep = new GenericTypeRepresentation();
        rep.setCodigo(type.getCodigo());
        rep.setDenominacion(type.getDenominacion());
        return rep;
    }

    public static GenericTypeRepresentation toRepresentation(TipoDocumento type) {
        GenericTypeRepresentation rep = new GenericTypeRepresentation();
        rep.setCodigo(type.getCodigo());
        rep.setAbreviatura(type.getAbreviatura());
        rep.setDenominacion(type.getDenominacion());
        rep.setLength(type.getLength());
        return rep;
    }

    public static GenericTypeRepresentation toRepresentation(TipoDocumentoRelacionadoGuia type) {
        GenericTypeRepresentation rep = new GenericTypeRepresentation();
        rep.setCodigo(type.getCodigo());
        rep.setDenominacion(type.getDenominacion());
        return rep;
    }

    public static GenericTypeRepresentation toRepresentation(TipoDocumentoRelacionadoTributo type) {
        GenericTypeRepresentation rep = new GenericTypeRepresentation();
        rep.setCodigo(type.getCodigo());
        rep.setDenominacion(type.getDenominacion());
        return rep;
    }

    public static GenericTypeRepresentation toRepresentation(TipoElementosAdicionalesComprobante type) {
        GenericTypeRepresentation rep = new GenericTypeRepresentation();
        rep.setCodigo(type.getCodigo());
        rep.setDenominacion(type.getDenominacion());
        rep.setGrupo(type.getGrupo());
        return rep;
    }

    public static GenericTypeRepresentation toRepresentation(TipoEstadoItem type) {
        GenericTypeRepresentation rep = new GenericTypeRepresentation();
        rep.setCodigo(type.getCodigo());
        rep.setDenominacion(type.getDenominacion());
        return rep;
    }

    public static GenericTypeRepresentation toRepresentation(TipoModalidadTraslado type) {
        GenericTypeRepresentation rep = new GenericTypeRepresentation();
        rep.setCodigo(type.getCodigo());
        rep.setDenominacion(type.getDenominacion());
        return rep;
    }

    public static GenericTypeRepresentation toRepresentation(TipoMoneda type) {
        GenericTypeRepresentation rep = new GenericTypeRepresentation();
        rep.setCodigo(type.getCodigo());
        rep.setAbreviatura(type.getAbreviatura());
        rep.setDenominacion(type.getDenominacion());
        return rep;
    }

    public static GenericTypeRepresentation toRepresentation(TipoMotivoTraslado type) {
        GenericTypeRepresentation rep = new GenericTypeRepresentation();
        rep.setCodigo(type.getCodigo());
        rep.setDenominacion(type.getDenominacion());
        return rep;
    }

    public static GenericTypeRepresentation toRepresentation(TipoNotaCredito type) {
        GenericTypeRepresentation rep = new GenericTypeRepresentation();
        rep.setCodigo(type.getCodigo());
        rep.setDenominacion(type.getDenominacion());
        return rep;
    }

    public static GenericTypeRepresentation toRepresentation(TipoNotaDebito type) {
        GenericTypeRepresentation rep = new GenericTypeRepresentation();
        rep.setCodigo(type.getCodigo());
        rep.setDenominacion(type.getDenominacion());
        return rep;
    }

    public static GenericTypeRepresentation toRepresentation(TipoOperacion type) {
        GenericTypeRepresentation rep = new GenericTypeRepresentation();
        rep.setCodigo(type.getCodigo());
        rep.setDenominacion(type.getDenominacion());
        return rep;
    }

    public static GenericTypeRepresentation toRepresentation(TipoPrecioVentaUnitario type) {
        GenericTypeRepresentation rep = new GenericTypeRepresentation();
        rep.setCodigo(type.getCodigo());
        rep.setDenominacion(type.getDenominacion());
        return rep;
    }

    public static GenericTypeRepresentation toRepresentation(TipoReciboServiciosPublicos type) {
        GenericTypeRepresentation rep = new GenericTypeRepresentation();
        rep.setId(type.getId());
        rep.setCodigo(type.getCodigo());
        rep.setDenominacion(type.getDenominacion());
        return rep;
    }

    public static GenericTypeRepresentation toRepresentation(TipoRegimenPercepcion type) {
        GenericTypeRepresentation rep = new GenericTypeRepresentation();
        rep.setCodigo(type.getCodigo());
        rep.setDenominacion(type.getDenominacion());
        return rep;
    }

    public static GenericTypeRepresentation toRepresentation(TipoRegimenRetencion type) {
        GenericTypeRepresentation rep = new GenericTypeRepresentation();
        rep.setCodigo(type.getCodigo());
        rep.setDenominacion(type.getDenominacion());
        rep.setValor(type.getValor());
        return rep;
    }

    public static GenericTypeRepresentation toRepresentation(TipoSistemaCalculoISC type) {
        GenericTypeRepresentation rep = new GenericTypeRepresentation();
        rep.setCodigo(type.getCodigo());
        rep.setAbreviatura(type.getAbreviatura());
        rep.setDenominacion(type.getDenominacion());
        return rep;
    }

    public static GenericTypeRepresentation toRepresentation(TipoTributo type) {
        GenericTypeRepresentation rep = new GenericTypeRepresentation();
        rep.setId(type.getId());
        rep.setCodigo(type.getCodigo());
        rep.setAbreviatura(type.getAbreviatura());
        rep.setDenominacion(type.getDenominacion());
        return rep;
    }

    public static GenericTypeRepresentation toRepresentation(TipoValorVenta type) {
        GenericTypeRepresentation rep = new GenericTypeRepresentation();
        rep.setCodigo(type.getCodigo());
        rep.setDenominacion(type.getDenominacion());
        return rep;
    }
}
