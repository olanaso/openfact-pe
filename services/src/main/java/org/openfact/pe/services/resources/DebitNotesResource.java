package org.openfact.pe.services.resources;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
import org.openfact.services.managers.DebitNoteManager;

import oasis.names.specification.ubl.schema.xsd.debitnote_21.DebitNoteType;

/**
 * @author carlosthe19916@sistcoop.com
 */
@Consumes(MediaType.APPLICATION_JSON)
public class DebitNotesResource {

	protected static final Logger logger = Logger.getLogger(DebitNotesResource.class);

	private final OpenfactSession session;
	private final OrganizationModel organization;

	public DebitNotesResource(OpenfactSession session, OrganizationModel organization) {
		this.session = session;
		this.organization = organization;
	}

	@POST
	@Path("")
	@NoCache
	@Produces(MediaType.APPLICATION_JSON)
	public Response createDebitNote(DocumentRepresentation rep) {
		DebitNoteManager debitNoteManager = new DebitNoteManager(session);

		// Double-check duplicated ID
		if (rep.getSerie() != null && rep.getNumero() != null
				&& debitNoteManager.getDebitNoteByID(organization, rep.getSerie() + "-" + rep.getNumero()) != null) {
			return ErrorResponse.exists("Debit Note exists with same documentId");
		}

		try {
			DebitNoteType debitNoteType = SunatRepresentationToType.toDebitNoteType(organization, rep);
			DebitNoteModel debitNote = debitNoteManager.addDebitNote(organization, debitNoteType, Collections.emptyMap());

			// Enviar a Cliente
			if (rep.isEnviarAutomaticamenteAlCliente()) {
				try {
					debitNoteManager.sendToCustomerParty(organization, debitNote);
				} catch (SendException e) {
					logger.error("Error sending to Customer");
				}
			}

			// Enviar Sunat
			if (rep.isEnviarAutomaticamenteASunat()) {
				try {
					debitNoteManager.sendToTrirdParty(organization, debitNote);
				} catch (SendException e) {
					if (session.getTransactionManager().isActive()) {
						session.getTransactionManager().setRollbackOnly();
					}
					return ErrorResponse.error("Could not send to sunat", Response.Status.BAD_REQUEST);
				}
			}

			URI location = session.getContext().getUri().getAbsolutePathBuilder().path(debitNote.getId()).build();
			return Response.created(location).entity(ModelToRepresentation.toRepresentation(debitNote)).build();
		} catch (ModelDuplicateException e) {
			if (session.getTransactionManager().isActive()) {
				session.getTransactionManager().setRollbackOnly();
			}
			return ErrorResponse.exists("Debit Note exists with same id or documentId");
		} catch (ModelException me) {
			if (session.getTransactionManager().isActive()) {
				session.getTransactionManager().setRollbackOnly();
			}
			return ErrorResponse.exists("Could not create debit note");
		}
	}

	@GET
	@Path("{debitNoteId}/representation/cdr")
	@NoCache
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getCdr(@PathParam("debitNoteId") final String debitNoteId) {
		DebitNoteModel debitNote = session.debitNotes().getDebitNoteById(organization, debitNoteId);
		if (debitNote == null) {
			throw new NotFoundException("Debit Note not found");
		}

		List<SendEventModel> sendEvents = debitNote.getSendEvents();

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