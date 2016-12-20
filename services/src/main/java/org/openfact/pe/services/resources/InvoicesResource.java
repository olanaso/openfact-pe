package org.openfact.pe.services.resources;

import java.net.URI;
import java.util.Collections;
import java.util.List;

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
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.openfact.models.InvoiceModel;
import org.openfact.models.ModelDuplicateException;
import org.openfact.models.ModelException;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.StorageFileModel;
import org.openfact.models.utils.ModelToRepresentation;
import org.openfact.pe.models.utils.SunatRepresentationToType;
import org.openfact.pe.representations.idm.DocumentRepresentation;
import org.openfact.services.ErrorResponse;
import org.openfact.services.managers.InvoiceManager;
import org.openfact.ubl.SendEventModel;
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
		if (rep.getSerie() != null && rep.getNumero() != null
				&& invoiceManager.getInvoiceByID(organization, rep.getSerie() + "-" + rep.getNumero()) != null) {
			return ErrorResponse.exists("Invoice exists with same documentId");
		}
		try {
			InvoiceType invoiceType = SunatRepresentationToType.toInvoiceType(organization, rep);
			InvoiceModel invoice = invoiceManager.addInvoice(organization, invoiceType, Collections.emptyMap());

			// Enviar a Cliente
			if (rep.isEnviarAutomaticamenteAlCliente()) {
				try {
					invoiceManager.sendToCustomerParty(organization, invoice);
				} catch (SendException e) {
					logger.error("Error sending to Customer");
				}
			}

			// Enviar Sunat
			if (rep.isEnviarAutomaticamenteASunat()) {
				try {
					invoiceManager.sendToTrirdParty(organization, invoice);
				} catch (SendException e) {
					if (session.getTransactionManager().isActive()) {
						session.getTransactionManager().setRollbackOnly();
					}
					return ErrorResponse.error("Could not send to sunat", Response.Status.BAD_REQUEST);
				}
			}

			URI location = session.getContext().getUri().getAbsolutePathBuilder().path(invoice.getId()).build();
			return Response.created(location).entity(ModelToRepresentation.toRepresentation(invoice)).build();
		} catch (ModelDuplicateException e) {
			if (session.getTransactionManager().isActive()) {
				session.getTransactionManager().setRollbackOnly();
			}
			return ErrorResponse.exists("Invoice exists with same id or documentId");
		} catch (ModelException e) {
			if (session.getTransactionManager().isActive()) {
				session.getTransactionManager().setRollbackOnly();
			}
			return ErrorResponse.exists("Invoice could not be created");
		}
	}

	@GET
	@Path("{invoiceId}/representation/cdr")
	@NoCache
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getCdr(@QueryParam("invoiceId") final String invoiceId) {
		InvoiceManager invoiceManager = new InvoiceManager(session);
		if (invoiceId == null) {
			throw new NotFoundException("Sunat response not found");
		}
		StorageFileModel storageFile = null;
		InvoiceModel invoice = invoiceManager.getInvoiceByID(organization, invoiceId);
		List<SendEventModel> sendEvents = invoice.getSendEvents();
		for (SendEventModel model : sendEvents) {
			if (!model.getFileResponseAttatchments().isEmpty()) {
				List<StorageFileModel> fileModels = model.getFileResponseAttatchments();
				storageFile = fileModels.get(0);
			}
		}
		if (storageFile == null) {
			throw new NotFoundException("Sunat response, cdr not found");
		}
		ResponseBuilder response = Response.ok(storageFile.getFile());
		response.type(storageFile.getMimeType());
		response.header("content-disposition", "attachment; filename=\"" + storageFile.getFileName() + "\"");
		return response.build();
	}
}