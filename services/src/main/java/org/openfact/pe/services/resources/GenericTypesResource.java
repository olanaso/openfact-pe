package org.openfact.pe.services.resources;

import org.openfact.pe.models.utils.SunatEnumToRepresentation;
import org.openfact.pe.representations.idm.GenericTypeRepresentation;
import org.openfact.pe.ubl.types.*;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Stateless
@Produces(MediaType.APPLICATION_JSON)
public class GenericTypesResource {

    @GET
    @Path("igv")
    public GenericTypeRepresentation getIgv() {
        GenericTypeRepresentation rep = new GenericTypeRepresentation();
        rep.setDenominacion("Impuesto General a las Ventas");
        rep.setAbreviatura("igv");
        rep.setValor(new BigDecimal("0.18"));
        return rep;
    }

    @GET
    @Path("tipos-afectacion-igv")
    public List<GenericTypeRepresentation> getTipoAfectacionIgv() {
        List<GenericTypeRepresentation> rep = new ArrayList<>();
        for (TipoAfectacionIgv type : TipoAfectacionIgv.values()) {
            rep.add(SunatEnumToRepresentation.toRepresentation(type));
        }
        return rep;
    }

    @GET
    @Path("tipos-comprobante-pago")
    public List<GenericTypeRepresentation> getTipoComprobante() {
        List<GenericTypeRepresentation> rep = new ArrayList<>();
        for (TipoInvoice type : TipoInvoice.values()) {
            rep.add(SunatEnumToRepresentation.toRepresentation(type));
        }
        return rep;
    }

    @GET
    @Path("conceptos-tributarios")
    public List<GenericTypeRepresentation> getTipoConceptosTributarios() {
        List<GenericTypeRepresentation> rep = new ArrayList<>();
        for (TipoConceptosTributarios type : TipoConceptosTributarios.values()) {
            rep.add(SunatEnumToRepresentation.toRepresentation(type));
        }
        return rep;
    }

    @GET
    @Path("tipos-documento-entidad")
    public List<GenericTypeRepresentation> getTipoDocumento() {
        List<GenericTypeRepresentation> rep = new ArrayList<>();
        for (TipoDocumentoEntidad type : TipoDocumentoEntidad.values()) {
            rep.add(SunatEnumToRepresentation.toRepresentation(type));
        }
        return rep;
    }

    @GET
    @Path("documento-relacionado-guia")
    public List<GenericTypeRepresentation> getTipoDocumentoRelacionadoGuia() {
        List<GenericTypeRepresentation> rep = new ArrayList<>();
        for (TipoDocumentoRelacionadoGuia type : TipoDocumentoRelacionadoGuia.values()) {
            rep.add(SunatEnumToRepresentation.toRepresentation(type));
        }
        return rep;
    }

    @GET
    @Path("documento-relacionado-tributo")
    public List<GenericTypeRepresentation> getTipoDocumentoRelacionadoTributo() {
        List<GenericTypeRepresentation> rep = new ArrayList<>();
        for (TipoDocumentoRelacionadoTributo type : TipoDocumentoRelacionadoTributo.values()) {
            rep.add(SunatEnumToRepresentation.toRepresentation(type));
        }
        return rep;
    }

    @GET
    @Path("elementos-adicionales-comprobante")
    public List<GenericTypeRepresentation> getTipoElementosAdicionalesComprobante() {
        List<GenericTypeRepresentation> rep = new ArrayList<>();
        for (TipoElementosAdicionalesComprobante type : TipoElementosAdicionalesComprobante.values()) {
            rep.add(SunatEnumToRepresentation.toRepresentation(type));
        }
        return rep;
    }

    @GET
    @Path("estado-item")
    public List<GenericTypeRepresentation> getTipoEstadoItem() {
        List<GenericTypeRepresentation> rep = new ArrayList<>();
        for (TipoEstadoItem type : TipoEstadoItem.values()) {
            rep.add(SunatEnumToRepresentation.toRepresentation(type));
        }
        return rep;
    }

    @GET
    @Path("modalidad-traslado")
    public List<GenericTypeRepresentation> getTipoModalidadTraslado() {
        List<GenericTypeRepresentation> rep = new ArrayList<>();
        for (TipoModalidadTraslado type : TipoModalidadTraslado.values()) {
            rep.add(SunatEnumToRepresentation.toRepresentation(type));
        }
        return rep;
    }

    @GET
    @Path("monedas")
    public List<GenericTypeRepresentation> getTipoMoneda() {
        List<GenericTypeRepresentation> rep = new ArrayList<>();
        for (TipoMoneda type : TipoMoneda.values()) {
            rep.add(SunatEnumToRepresentation.toRepresentation(type));
        }
        return rep;
    }

    @GET
    @Path("motivo-traslado")
    public List<GenericTypeRepresentation> getTipoMotivoTraslado() {
        List<GenericTypeRepresentation> rep = new ArrayList<>();
        for (TipoMotivoTraslado type : TipoMotivoTraslado.values()) {
            rep.add(SunatEnumToRepresentation.toRepresentation(type));
        }
        return rep;
    }

    @GET
    @Path("tipos-nota-credito")
    public List<GenericTypeRepresentation> getTipoNotaCredito() {
        List<GenericTypeRepresentation> rep = new ArrayList<>();
        for (TipoNotaCredito type : TipoNotaCredito.values()) {
            rep.add(SunatEnumToRepresentation.toRepresentation(type));
        }
        return rep;
    }

    @GET
    @Path("tipos-nota-debito")
    public List<GenericTypeRepresentation> getTipoNotaDebito() {
        List<GenericTypeRepresentation> rep = new ArrayList<>();
        for (TipoNotaDebito type : TipoNotaDebito.values()) {
            rep.add(SunatEnumToRepresentation.toRepresentation(type));
        }
        return rep;
    }

    @GET
    @Path("tipo-operacion")
    public List<GenericTypeRepresentation> getTipoOperacion() {
        List<GenericTypeRepresentation> rep = new ArrayList<>();
        for (TipoOperacion type : TipoOperacion.values()) {
            rep.add(SunatEnumToRepresentation.toRepresentation(type));
        }
        return rep;
    }

    @GET
    @Path("precio-venta-unitario")
    public List<GenericTypeRepresentation> getTipoPrecioVentaUnitario() {
        List<GenericTypeRepresentation> rep = new ArrayList<>();
        for (TipoPrecioVentaUnitario type : TipoPrecioVentaUnitario.values()) {
            rep.add(SunatEnumToRepresentation.toRepresentation(type));
        }
        return rep;
    }

    @GET
    @Path("recibo-servicios-publicos")
    public List<GenericTypeRepresentation> getTipoReciboServiciosPublicos() {
        List<GenericTypeRepresentation> rep = new ArrayList<>();
        for (TipoReciboServiciosPublicos type : TipoReciboServiciosPublicos.values()) {
            rep.add(SunatEnumToRepresentation.toRepresentation(type));
        }
        return rep;
    }

    @GET
    @Path("tipos-regimen-percepcion")
    public List<GenericTypeRepresentation> getTipoRegimenPercepcion() {
        List<GenericTypeRepresentation> rep = new ArrayList<>();
        for (TipoRegimenPercepcion type : TipoRegimenPercepcion.values()) {
            rep.add(SunatEnumToRepresentation.toRepresentation(type));
        }
        return rep;
    }

    @GET
    @Path("documentos-relacionados-percepcion")
    public List<GenericTypeRepresentation> getDocumentosRelacionadosPercepcion() {
        List<GenericTypeRepresentation> rep = new ArrayList<>();
        for (TipoDocumentoRelacionadoPercepcionRetencion type : TipoDocumentoRelacionadoPercepcionRetencion.values()) {
            rep.add(SunatEnumToRepresentation.toRepresentation(type));
        }
        return rep;
    }

    @GET
    @Path("tipos-regimen-retencion")
    public List<GenericTypeRepresentation> getTipoRegimenRetencion() {
        List<GenericTypeRepresentation> rep = new ArrayList<>();
        for (TipoRegimenRetencion type : TipoRegimenRetencion.values()) {
            rep.add(SunatEnumToRepresentation.toRepresentation(type));
        }
        return rep;
    }

    @GET
    @Path("documentos-relacionados-retencion")
    public List<GenericTypeRepresentation> getDocumentosRelacionadosRetencion() {
        List<GenericTypeRepresentation> rep = new ArrayList<>();
        for (TipoDocumentoRelacionadoPercepcionRetencion type : TipoDocumentoRelacionadoPercepcionRetencion.values()) {
            rep.add(SunatEnumToRepresentation.toRepresentation(type));
        }
        return rep;
    }

    @GET
    @Path("sistema-calculo-isc")
    public List<GenericTypeRepresentation> getTipoSistemaCalculoISC() {
        List<GenericTypeRepresentation> rep = new ArrayList<>();
        for (TipoSistemaCalculoISC type : TipoSistemaCalculoISC.values()) {
            rep.add(SunatEnumToRepresentation.toRepresentation(type));
        }
        return rep;
    }

    @GET
    @Path("tipo-tributo")
    public List<GenericTypeRepresentation> getTipoTributo() {
        List<GenericTypeRepresentation> rep = new ArrayList<>();
        for (TipoTributo type : TipoTributo.values()) {
            rep.add(SunatEnumToRepresentation.toRepresentation(type));
        }
        return rep;
    }

    @GET
    @Path("valor-venta")
    public List<GenericTypeRepresentation> getTipoValorVenta() {
        List<GenericTypeRepresentation> rep = new ArrayList<>();
        for (TipoValorVenta type : TipoValorVenta.values()) {
            rep.add(SunatEnumToRepresentation.toRepresentation(type));
        }
        return rep;
    }

}
