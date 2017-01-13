package org.openfact.pe.services.resources;

import java.net.URI;
import java.util.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.openfact.file.FileModel;
import org.openfact.file.InternetMediaType;
import org.openfact.models.*;
import org.openfact.models.enums.DestinyType;
import org.openfact.models.enums.SendResultType;
import org.openfact.models.utils.ModelToRepresentation;
import org.openfact.pe.models.utils.SunatRepresentationToType;
import org.openfact.pe.representations.idm.DocumentRepresentation;
import org.openfact.services.ErrorResponse;
import org.openfact.services.managers.InvoiceManager;

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
		if (rep.getSerie() != null && rep.getNumero() != null && invoiceManager.getInvoiceByID(organization, rep.getSerie() + "-" + rep.getNumero()) != null) {
			return ErrorResponse.exists("Invoice exists with same documentId");
		}
		try {
			InvoiceType invoiceType = SunatRepresentationToType.toInvoiceType(organization, rep);
			InvoiceModel invoice = invoiceManager.addInvoice(organization, invoiceType, generateAttributes(rep));

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

	private Map<String, List<String>> generateAttributes(DocumentRepresentation rep) {
		Map<String, List<String>> attributes = new HashMap<>();
		attributes.put("operacionGratuita", Arrays.asList(String.valueOf(rep.isOperacionGratuita())));

		if(rep.getTotalGravada() != null) {
			attributes.put("totalGravada", Arrays.asList(String.valueOf(rep.getTotalGravada())));
		}
		if(rep.getTotalInafecta() != null) {
			attributes.put("totalInafecta", Arrays.asList(String.valueOf(rep.getTotalInafecta())));
		}
		if(rep.getTotalExonerada() != null) {
			attributes.put("totalExonerada", Arrays.asList(String.valueOf(rep.getTotalExonerada())));
		}
		if(rep.getTotalIgv() != null) {
			attributes.put("totalIgv", Arrays.asList(String.valueOf(rep.getTotalIgv())));
		}
		if(rep.getTotalGratuita() != null) {
			attributes.put("totalGratuita", Arrays.asList(String.valueOf(rep.getTotalGratuita())));
		}
		if(rep.getIgv() != null) {
			attributes.put("igv", Arrays.asList(String.valueOf(rep.getIgv())));
		}
		if(rep.getPorcentajeDescuento() != null) {
			attributes.put("porcentajeDescuento", Arrays.asList(String.valueOf(rep.getPorcentajeDescuento())));
		}
		if(rep.getTotalOtrosCargos() != null) {
			attributes.put("totalOtrosCargos", Arrays.asList(String.valueOf(rep.getTotalOtrosCargos())));
		}
		return  attributes;
	}

	@GET
	@Path("{invoiceId}/cdr")
	@NoCache
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getCdr(@PathParam("invoiceId") final String invoiceId) {
		InvoiceModel invoice = session.invoices().getInvoiceById(organization, invoiceId);
		if (invoice == null) {
			throw new NotFoundException("Invoice not found");
		}

		List<SendEventModel> sendEvents = invoice.getSendEvents();

		Optional<SendEventModel> op = sendEvents.stream()
				.filter(p -> p.getDestityType().equals(DestinyType.THIRD_PARTY))
				.filter(p -> p.getResult().equals(SendResultType.SUCCESS)).findFirst();

		if (op.isPresent()) {
			SendEventModel sendEvent = op.get();
			FileModel file = sendEvent.getFileResponseAttatchments().get(0);

			ResponseBuilder response = Response.ok(file.getFile());
			response.type(InternetMediaType.getMymeTypeFromExtension(file.getExtension()));
			response.header("content-disposition", "attachment; filename=\"" + file.getFileName() + "\"");
			return response.build();
		} else {
			throw new NotFoundException("Cdr not found or was not generated.");
		}
	}
}