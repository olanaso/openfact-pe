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
import org.openfact.models.CreditNoteModel;
import org.openfact.models.ModelDuplicateException;
import org.openfact.models.ModelException;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.StorageFileModel;
import org.openfact.models.utils.ModelToRepresentation;
import org.openfact.pe.models.utils.SunatRepresentationToType;
import org.openfact.pe.representations.idm.DocumentRepresentation;
import org.openfact.services.ErrorResponse;
import org.openfact.services.managers.CreditNoteManager;
import org.openfact.ubl.SendEventModel;
import org.openfact.ubl.SendException;

import oasis.names.specification.ubl.schema.xsd.creditnote_21.CreditNoteType;

/**
 * @author carlosthe19916@sistcoop.com
 */
@Consumes(MediaType.APPLICATION_JSON)
public class CreditNotesResource {

	protected static final Logger logger = Logger.getLogger(CreditNotesResource.class);

	private final OpenfactSession session;
	private final OrganizationModel organization;

	public CreditNotesResource(OpenfactSession session, OrganizationModel organization) {
		this.session = session;
		this.organization = organization;
	}

	@POST
	@Path("")
	@NoCache
	@Produces(MediaType.APPLICATION_JSON)
	public Response createCreditNote(DocumentRepresentation rep) {
		CreditNoteManager creditNoteManager = new CreditNoteManager(session);

		// Double-check duplicated ID
		if (rep.getSerie() != null && rep.getNumero() != null
				&& creditNoteManager.getCreditNoteByID(organization, rep.getSerie() + "-" + rep.getNumero()) != null) {
			return ErrorResponse.exists("Credit Note exists with same documentId");
		}

		try {
			CreditNoteType creditNoteType = SunatRepresentationToType.toCreditNoteType(organization, rep);
			CreditNoteModel creditNote = creditNoteManager.addCreditNote(organization, creditNoteType, Collections.emptyMap());

			// Enviar a Cliente
			if (rep.isEnviarAutomaticamenteAlCliente()) {
				try {
					creditNoteManager.sendToCustomerParty(organization, creditNote);
				} catch (SendException e) {
					logger.error("Error sending to Customer");
				}
			}

			// Enviar Sunat
			if (rep.isEnviarAutomaticamenteASunat()) {
				try {
					creditNoteManager.sendToTrirdParty(organization, creditNote);
				} catch (SendException e) {
					if (session.getTransactionManager().isActive()) {
						session.getTransactionManager().setRollbackOnly();
					}
					return ErrorResponse.error("Could not send to sunat", Response.Status.BAD_REQUEST);
				}
			}

			URI location = session.getContext().getUri().getAbsolutePathBuilder().path(creditNote.getId()).build();
			return Response.created(location).entity(ModelToRepresentation.toRepresentation(creditNote)).build();
		} catch (ModelDuplicateException e) {
			if (session.getTransactionManager().isActive()) {
				session.getTransactionManager().setRollbackOnly();
			}
			return ErrorResponse.exists("Credit Note exists with same id or documentId");
		} catch (ModelException me) {
			if (session.getTransactionManager().isActive()) {
				session.getTransactionManager().setRollbackOnly();
			}
			return ErrorResponse.exists("Could not create credit note");
		}
	}	

	@GET
	@Path("{creditNoteId}/representation/cdr")
	@NoCache
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getCdr(@QueryParam("creditNoteId") final String creditNoteId) {
		CreditNoteManager creditNoteManager = new CreditNoteManager(session);
		if (creditNoteId == null) {
			throw new NotFoundException("Sunat response not found");
		}
		StorageFileModel storageFile = null;
		CreditNoteModel creditNote = creditNoteManager.getCreditNoteByID(organization, creditNoteId);
		List<SendEventModel> sendEvents = creditNote.getSendEvents();
		for (SendEventModel model : sendEvents) {
			if (!model.getFileResponseAttatchments() .isEmpty()) {
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