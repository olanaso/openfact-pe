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
import org.openfact.events.admin.OperationType;
import org.openfact.file.FileModel;
import org.openfact.file.InternetMediaType;
import org.openfact.models.*;
import org.openfact.models.enums.DestinyType;
import org.openfact.models.enums.SendResultType;
import org.openfact.models.utils.ModelToRepresentation;
import org.openfact.pe.models.SunatSendException;
import org.openfact.pe.models.utils.SunatRepresentationToType;
import org.openfact.pe.representations.idm.DocumentRepresentation;
import org.openfact.services.ErrorResponse;
import org.openfact.services.managers.DebitNoteManager;

import oasis.names.specification.ubl.schema.xsd.debitnote_21.DebitNoteType;
import org.openfact.services.managers.DebitNoteManager;
import org.openfact.services.resources.admin.AdminEventBuilder;

/**
 * @author carlosthe19916@sistcoop.com
 */
@Consumes(MediaType.APPLICATION_JSON)
public class DebitNotesResource {

	protected static final Logger logger = Logger.getLogger(DebitNotesResource.class);

	private final OpenfactSession session;
	private final OrganizationModel organization;
	private final AdminEventBuilder adminEvent;

	public DebitNotesResource(OpenfactSession session, OrganizationModel organization, AdminEventBuilder adminEvent) {
		this.session = session;
		this.organization = organization;
		this.adminEvent = adminEvent;
	}

	@POST
	@Path("")
	@NoCache
	@Produces(MediaType.APPLICATION_JSON)
	public Response createDebitNote(DocumentRepresentation rep) {
		DebitNoteManager debitNoteManager = new DebitNoteManager(session);

		if (rep.getSerie() != null || rep.getNumero() != null) {
			if (rep.getSerie() != null && rep.getSerie() != null) {
				if (debitNoteManager.getDebitNoteByDocumentId(organization, rep.getSerie() + "-" + rep.getNumero()) != null) {
					return ErrorResponse.exists("Debit Note exists with same documentId");
				}
			} else {
				return ErrorResponse.error("Numero de serie y/o numero invalido", Response.Status.BAD_REQUEST);
			}
		}

		try {
			DebitNoteType debitNoteType = SunatRepresentationToType.toDebitNoteType(organization, rep);
			DebitNoteModel debitNoteModel = debitNoteManager.addDebitNote(organization, debitNoteType, generateAttributes(rep));

			if (rep.isEnviarAutomaticamenteASunat()) {
				SendEventModel thirdPartySendEvent = debitNoteModel.addSendEvent(DestinyType.THIRD_PARTY);
				try {
					debitNoteManager.sendToTrirdParty(organization, debitNoteModel, thirdPartySendEvent);
				} catch (ModelInsuficientData e) {
					if (session.getTransactionManager().isActive()) {
						session.getTransactionManager().setRollbackOnly();
					}
					return ErrorResponse.error(e.getMessage(), Response.Status.BAD_REQUEST);
				} catch (SendException e) {
					if (session.getTransactionManager().isActive()) {
						session.getTransactionManager().setRollbackOnly();
					}
					if (e instanceof SunatSendException) {
						return ErrorResponse.error(e.getMessage(), Response.Status.BAD_REQUEST);
					} else {
						return ErrorResponse.error("Could not send to sunat", Response.Status.BAD_REQUEST);
					}
				}
			}

			if (rep.isEnviarAutomaticamenteAlCliente()) {
				SendEventModel customerSendEvent = debitNoteModel.addSendEvent(DestinyType.CUSTOMER);
				try {
					debitNoteManager.sendToCustomerParty(organization, debitNoteModel, customerSendEvent);
				} catch (ModelInsuficientData e) {
					customerSendEvent.setResult(SendResultType.ERROR);
					customerSendEvent.setDescription(e.getMessage());
				} catch (SendException e) {
					customerSendEvent.setResult(SendResultType.ERROR);
					customerSendEvent.setDescription("Internal server error");
					logger.error("Internal Server Error sending to customer", e);
				}
			}

			URI location = session.getContext().getUri().getAbsolutePathBuilder().path(debitNoteModel.getId()).build();
			adminEvent.operation(OperationType.CREATE).resourcePath(location.toString()).representation(rep).success();
			return Response.created(location).entity(ModelToRepresentation.toRepresentation(debitNoteModel)).build();
		} catch (ModelDuplicateException e) {
			if (session.getTransactionManager().isActive()) {
				session.getTransactionManager().setRollbackOnly();
			}
			return ErrorResponse.exists("Debit Note exists with same id or documentId");
		} catch (ModelException e) {
			if (session.getTransactionManager().isActive()) {
				session.getTransactionManager().setRollbackOnly();
			}
			return ErrorResponse.exists("Debit Note could not be created");
		}
	}

	private Map<String, List<String>> generateAttributes(DocumentRepresentation rep) {
		Map<String, List<String>> attributes = new HashMap<>();
		attributes.put("operacionGratuita", Arrays.asList(String.valueOf(rep.isOperacionGratuita())));

		if (rep.getTotalGravada() != null) {
			attributes.put("totalGravada", Arrays.asList(String.valueOf(rep.getTotalGravada())));
		}
		if (rep.getTotalInafecta() != null) {
			attributes.put("totalInafecta", Arrays.asList(String.valueOf(rep.getTotalInafecta())));
		}
		if (rep.getTotalExonerada() != null) {
			attributes.put("totalExonerada", Arrays.asList(String.valueOf(rep.getTotalExonerada())));
		}
		if (rep.getTotalIgv() != null) {
			attributes.put("totalIgv", Arrays.asList(String.valueOf(rep.getTotalIgv())));
		}
		if (rep.getTotalGratuita() != null) {
			attributes.put("totalGratuita", Arrays.asList(String.valueOf(rep.getTotalGratuita())));
		}
		if (rep.getIgv() != null) {
			attributes.put("igv", Arrays.asList(String.valueOf(rep.getIgv())));
		}
		if (rep.getPorcentajeDescuento() != null) {
			attributes.put("porcentajeDescuento", Arrays.asList(String.valueOf(rep.getPorcentajeDescuento())));
		}
		if (rep.getTotalOtrosCargos() != null) {
			attributes.put("totalOtrosCargos", Arrays.asList(String.valueOf(rep.getTotalOtrosCargos())));
		}
		return attributes;
	}
	
	@GET
	@Path("{debitNoteId}/cdr")
	@NoCache
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getCdr(@PathParam("debitNoteId") final String debitNoteId) {
		DebitNoteModel debitNote = session.debitNotes().getDebitNoteById(organization, debitNoteId);
		if (debitNote == null) {
			return ErrorResponse.error("Debit Note not found", Response.Status.NOT_FOUND);
		}

		Map<String, String> params = new HashMap<>();
		params.put(DebitNoteModel.SEND_EVENT_DESTINY_TYPE, DestinyType.THIRD_PARTY.toString());
		params.put(DebitNoteModel.SEND_EVENT_RESULT, SendResultType.SUCCESS.toString());

		List<SendEventModel> sendEvents = debitNote.searchForSendEvent(params, 0, 2);
		if (sendEvents.isEmpty()) {
			return ErrorResponse.error("No se encontro un evio valido a SUNAT", Response.Status.BAD_REQUEST);
		} else {
			SendEventModel sendEvent = sendEvents.get(0);
			List<FileModel> responseFiles = sendEvent.getResponseFileAttatchments();
			if (responseFiles.isEmpty()) {
				return ErrorResponse.error("Cdr no encontrado", Response.Status.NOT_FOUND);
			} else if (responseFiles.size() > 1) {
				return ErrorResponse.error("Se encontraron mas de un cdr, no se puede identificar el valido", Response.Status.CONFLICT);
			} else {
				FileModel cdrFile = responseFiles.get(0);
				ResponseBuilder response = Response.ok(cdrFile.getFile());
				String internetMediaType = InternetMediaType.getMymeTypeFromExtension(cdrFile.getExtension());
				if (internetMediaType != null && !internetMediaType.isEmpty()) response.type(internetMediaType);
				response.header("content-disposition", "attachment; filename=\"" + cdrFile.getFileName() + "\"");
				return response.build();
			}
		}
	}
}