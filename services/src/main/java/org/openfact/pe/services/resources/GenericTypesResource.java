package org.openfact.pe.services.resources;

import org.jboss.resteasy.annotations.cache.NoCache;
import org.openfact.common.ClientConnection;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.pe.models.enums.*;
import org.openfact.pe.models.utils.SunatEnumToRepresentation;
import org.openfact.pe.representations.idm.GenericTypeRepresentation;
import org.openfact.services.ServicesLogger;

import javax.ws.rs.GET;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.List;

public class GenericTypesResource {
    private static final ServicesLogger logger = ServicesLogger.LOGGER;

    @Context
    protected UriInfo uriInfo;
    @Context
    protected OpenfactSession session;
    @Context
    protected ClientConnection clientConnection;
    @Context
    protected HttpHeaders headers;
    protected OrganizationModel organization;

    public GenericTypesResource(OpenfactSession session, OrganizationModel organization) {
        this.session = session;
        this.organization = organization;
    }

    @GET
    @Path("tipos-afectacion-igv")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public List<GenericTypeRepresentation> getTipoAfectacionIgv() {
        List<GenericTypeRepresentation> rep = new ArrayList<>();
        for (TipoAfectacionIgv type : TipoAfectacionIgv.values()) {
            rep.add(SunatEnumToRepresentation.toRepresentation(type));
        }
        return rep;
    }

    @GET
    @Path("tipos-comprobante-pago")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public List<GenericTypeRepresentation> getTipoComprobante() {
        List<GenericTypeRepresentation> rep = new ArrayList<>();
        for (TipoInvoice type : TipoInvoice.values()) {
            rep.add(SunatEnumToRepresentation.toRepresentation(type));
        }
        return rep;
    }

    @GET
    @Path("conceptos-tributarios")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public List<GenericTypeRepresentation> getTipoConceptosTributarios() {
        List<GenericTypeRepresentation> rep = new ArrayList<>();
        for (TipoConceptosTributarios type : TipoConceptosTributarios.values()) {
            rep.add(SunatEnumToRepresentation.toRepresentation(type));
        }
        return rep;
    }

    @GET
    @Path("tipos-documento-entidad")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public List<GenericTypeRepresentation> getTipoDocumento() {
        List<GenericTypeRepresentation> rep = new ArrayList<>();
        for (TipoDocumentoEntidad type : TipoDocumentoEntidad.values()) {
            rep.add(SunatEnumToRepresentation.toRepresentation(type));
        }
        return rep;
    }

    @GET
    @Path("documento-relacionado-guia")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public List<GenericTypeRepresentation> getTipoDocumentoRelacionadoGuia() {
        List<GenericTypeRepresentation> rep = new ArrayList<>();
        for (TipoDocumentoRelacionadoGuia type : TipoDocumentoRelacionadoGuia.values()) {
            rep.add(SunatEnumToRepresentation.toRepresentation(type));
        }
        return rep;
    }

    @GET
    @Path("documento-relacionado-tributo")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public List<GenericTypeRepresentation> getTipoDocumentoRelacionadoTributo() {
        List<GenericTypeRepresentation> rep = new ArrayList<>();
        for (TipoDocumentoRelacionadoTributo type : TipoDocumentoRelacionadoTributo.values()) {
            rep.add(SunatEnumToRepresentation.toRepresentation(type));
        }
        return rep;
    }

    @GET
    @Path("elementos-adicionales-comprobante")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public List<GenericTypeRepresentation> getTipoElementosAdicionalesComprobante() {
        List<GenericTypeRepresentation> rep = new ArrayList<>();
        for (TipoElementosAdicionalesComprobante type : TipoElementosAdicionalesComprobante.values()) {
            rep.add(SunatEnumToRepresentation.toRepresentation(type));
        }
        return rep;
    }

    @GET
    @Path("estado-item")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public List<GenericTypeRepresentation> getTipoEstadoItem() {
        List<GenericTypeRepresentation> rep = new ArrayList<>();
        for (TipoEstadoItem type : TipoEstadoItem.values()) {
            rep.add(SunatEnumToRepresentation.toRepresentation(type));
        }
        return rep;
    }

    @GET
    @Path("modalidad-traslado")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public List<GenericTypeRepresentation> getTipoModalidadTraslado() {
        List<GenericTypeRepresentation> rep = new ArrayList<>();
        for (TipoModalidadTraslado type : TipoModalidadTraslado.values()) {
            rep.add(SunatEnumToRepresentation.toRepresentation(type));
        }
        return rep;
    }

    @GET
    @Path("moneda")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public List<GenericTypeRepresentation> getTipoMoneda() {
        List<GenericTypeRepresentation> rep = new ArrayList<>();
        for (TipoMoneda type : TipoMoneda.values()) {
            rep.add(SunatEnumToRepresentation.toRepresentation(type));
        }
        return rep;
    }

    @GET
    @Path("motivo-traslado")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public List<GenericTypeRepresentation> getTipoMotivoTraslado() {
        List<GenericTypeRepresentation> rep = new ArrayList<>();
        for (TipoMotivoTraslado type : TipoMotivoTraslado.values()) {
            rep.add(SunatEnumToRepresentation.toRepresentation(type));
        }
        return rep;
    }

    @GET
    @Path("tipo-nota-credito")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public List<GenericTypeRepresentation> getTipoNotaCredito() {
        List<GenericTypeRepresentation> rep = new ArrayList<>();
        for (TipoNotaCredito type : TipoNotaCredito.values()) {
            rep.add(SunatEnumToRepresentation.toRepresentation(type));
        }
        return rep;
    }

    @GET
    @Path("tipo-nota-debito")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public List<GenericTypeRepresentation> getTipoNotaDebito() {
        List<GenericTypeRepresentation> rep = new ArrayList<>();
        for (TipoNotaDebito type : TipoNotaDebito.values()) {
            rep.add(SunatEnumToRepresentation.toRepresentation(type));
        }
        return rep;
    }

    @GET
    @Path("tipo-operacion")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public List<GenericTypeRepresentation> getTipoOperacion() {
        List<GenericTypeRepresentation> rep = new ArrayList<>();
        for (TipoOperacion type : TipoOperacion.values()) {
            rep.add(SunatEnumToRepresentation.toRepresentation(type));
        }
        return rep;
    }

    @GET
    @Path("precio-venta-unitario")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public List<GenericTypeRepresentation> getTipoPrecioVentaUnitario() {
        List<GenericTypeRepresentation> rep = new ArrayList<>();
        for (TipoPrecioVentaUnitario type : TipoPrecioVentaUnitario.values()) {
            rep.add(SunatEnumToRepresentation.toRepresentation(type));
        }
        return rep;
    }

    @GET
    @Path("recibo-servicios-publicos")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public List<GenericTypeRepresentation> getTipoReciboServiciosPublicos() {
        List<GenericTypeRepresentation> rep = new ArrayList<>();
        for (TipoReciboServiciosPublicos type : TipoReciboServiciosPublicos.values()) {
            rep.add(SunatEnumToRepresentation.toRepresentation(type));
        }
        return rep;
    }

    @GET
    @Path("regimen-percepcion")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public List<GenericTypeRepresentation> getTipoRegimenPercepcion() {
        List<GenericTypeRepresentation> rep = new ArrayList<>();
        for (TipoRegimenPercepcion type : TipoRegimenPercepcion.values()) {
            rep.add(SunatEnumToRepresentation.toRepresentation(type));
        }
        return rep;
    }

    @GET
    @Path("regimen-retencion")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public List<GenericTypeRepresentation> getTipoRegimenRetencion() {
        List<GenericTypeRepresentation> rep = new ArrayList<>();
        for (TipoRegimenRetencion type : TipoRegimenRetencion.values()) {
            rep.add(SunatEnumToRepresentation.toRepresentation(type));
        }
        return rep;
    }

    @GET
    @Path("sistema-calculo-isc")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public List<GenericTypeRepresentation> getTipoSistemaCalculoISC() {
        List<GenericTypeRepresentation> rep = new ArrayList<>();
        for (TipoSistemaCalculoISC type : TipoSistemaCalculoISC.values()) {
            rep.add(SunatEnumToRepresentation.toRepresentation(type));
        }
        return rep;
    }

    @GET
    @Path("tipo-tributo")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public List<GenericTypeRepresentation> getTipoTributo() {
        List<GenericTypeRepresentation> rep = new ArrayList<>();
        for (TipoTributo type : TipoTributo.values()) {
            rep.add(SunatEnumToRepresentation.toRepresentation(type));
        }
        return rep;
    }

    @GET
    @Path("valor-venta")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public List<GenericTypeRepresentation> getTipoValorVenta() {
        List<GenericTypeRepresentation> rep = new ArrayList<>();
        for (TipoValorVenta type : TipoValorVenta.values()) {
            rep.add(SunatEnumToRepresentation.toRepresentation(type));
        }
        return rep;
    }
}
