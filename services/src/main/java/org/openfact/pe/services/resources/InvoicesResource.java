package org.openfact.pe.services.resources;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.openfact.models.InvoiceModel;
import org.openfact.models.ModelDuplicateException;
import org.openfact.models.ModelException;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.pe.constants.CodigoTipoDocumento;
import org.openfact.pe.models.SunatResponseModel;
import org.openfact.pe.models.SunatResponseProvider;
import org.openfact.pe.models.utils.SunatModelToRepresentation;
import org.openfact.pe.models.utils.SunatRepresentationToType;
import org.openfact.pe.representations.idm.DocumentRepresentation;
import org.openfact.pe.representations.idm.SunatResponseRepresentation;
import org.openfact.services.ErrorResponse;
import org.openfact.services.managers.InvoiceManager;
import org.openfact.ubl.SendException;

import oasis.names.specification.ubl.schema.xsd.invoice_21.InvoiceType;

/**
 * @author carlosthe19916@sistcoop.com
 */
@Consumes(MediaType.APPLICATION_JSON)
public class InvoicesResource {

    protected static final Logger logger = Logger.getLogger(InvoicesResource.class);

    private final OpenfactSession session;
    private final OrganizationModel organization;

    @Context
    protected UriInfo uriInfo;

    public InvoicesResource(OpenfactSession session, OrganizationModel organization) {
        this.session = session;
        this.organization = organization;
    }

    @POST
    @Path("")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public Response createInvoice(DocumentRepresentation rep) {
        InvoiceManager invoiceManager = new InvoiceManager(session);

        // Double-check duplicated ID
        if (rep.getSerie() != null && rep.getNumero() != null && invoiceManager.getInvoiceByID(organization, rep.getSerie() + "-" + rep.getNumero()) != null) {
            return ErrorResponse.exists("Invoice exists with same documentId");
        }

        try {
            InvoiceType invoiceType = SunatRepresentationToType.toInvoiceType(organization, rep);
            InvoiceModel invoice = invoiceManager.addInvoice(organization, invoiceType, Collections.emptyMap());
            
            // Enviar a Cliente
            if (rep.isEnviarAutomaticamenteAlCliente()) {
                invoiceManager.sendToCustomerParty(organization, invoice);
            }

            // Enviar Sunat
            if (rep.isEnviarAutomaticamenteASunat()) {
                invoiceManager.sendToTrirdParty(organization, invoice);
            }
            
            if (session.getTransactionManager().isActive()) {
                session.getTransactionManager().commit();
            }            

            URI location = uriInfo.getAbsolutePathBuilder().path(invoice.getId()).build();
            return Response.created(location).build();
        } catch (ModelDuplicateException e) {
            if (session.getTransactionManager().isActive()) {
                session.getTransactionManager().setRollbackOnly();
            }
            return ErrorResponse.exists("Invoice exists with same id or documentId");
        } catch (ModelException me) {
            if (session.getTransactionManager().isActive()) {
                session.getTransactionManager().setRollbackOnly();
            }
            return ErrorResponse.exists("Could not create invoice");
        } catch (SendException me) {
            if (session.getTransactionManager().isActive()) {
                session.getTransactionManager().setRollbackOnly();
            }
            return ErrorResponse.exists("Could not send invoice");
        } 
    }
	@GET
	@Path("{invoiceId}")
	@NoCache
	@Produces(MediaType.APPLICATION_JSON)
	public List<SunatResponseRepresentation> getSunatResponses(@QueryParam("invoiceId") final String invoiceId) {
		SunatResponseProvider responseProvider = session.getProvider(SunatResponseProvider.class);
		List<SunatResponseModel> sunatResponses;
		if (invoiceId == null) {
			throw new NotFoundException("Sunat response not found");
		}
		sunatResponses = responseProvider.getSunatResponsesByDocument(organization, CodigoTipoDocumento.FACTURA,
				invoiceId);

		return sunatResponses.stream().map(f -> SunatModelToRepresentation.toRepresentation(f))
				.collect(Collectors.toList());
	}

	@GET
	@Path("{invoiceId}/representation/cdr")
	@NoCache
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCdr(@QueryParam("invoiceId") final String invoiceId) {
		SunatResponseProvider responseProvider = session.getProvider(SunatResponseProvider.class);
		SunatResponseModel sunatResponse;
		if (invoiceId == null) {
			throw new NotFoundException("Sunat response not found");

		}
		sunatResponse = responseProvider.getSunatResponseByDocument(organization, CodigoTipoDocumento.FACTURA,
				invoiceId);
		if (sunatResponse.getDocumentResponse() == null) {
			throw new NotFoundException("Response not found");
		}
		ResponseBuilder response = Response.ok(sunatResponse.getDocumentResponse());
		response.type("application/zip");
		response.header("content-disposition", "attachment; filename=\"" + sunatResponse.getId() + ".zip\"");
		return response.build();
	}
}