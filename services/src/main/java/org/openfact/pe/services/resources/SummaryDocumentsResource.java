package org.openfact.pe.services.resources;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.openfact.common.converts.DocumentUtils;
import org.openfact.models.*;
import org.openfact.models.search.SearchCriteriaModel;
import org.openfact.models.search.SearchResultsModel;
import org.openfact.models.utils.ModelToRepresentation;
import org.openfact.models.utils.RepresentationToModel;
import org.openfact.pe.constants.EmissionType;
import org.openfact.pe.models.SummaryDocumentModel;
import org.openfact.pe.models.SummaryDocumentProvider;
import org.openfact.pe.models.types.summary.SummaryDocumentsType;
import org.openfact.pe.models.utils.SunatModelToRepresentation;
import org.openfact.pe.representations.idm.SummaryRepresentation;
import org.openfact.pe.services.managers.SummaryDocumentManager;
import org.openfact.pe.services.util.SunatResponseUtils;
import org.openfact.pe.services.util.SunatSenderUtils;
import org.openfact.pe.services.util.SunatTemplateUtils;
import org.openfact.representations.idm.SendEventRepresentation;
import org.openfact.representations.idm.search.SearchCriteriaRepresentation;
import org.openfact.representations.idm.search.SearchResultsRepresentation;
import org.openfact.services.ErrorResponse;
import org.w3c.dom.Document;

public class SummaryDocumentsResource {

	protected static final Logger logger = Logger.getLogger(SummaryDocumentsResource.class);

	private final OpenfactSession session;
	private final OrganizationModel organization;

	@Context
	protected UriInfo uriInfo;

	public SummaryDocumentsResource(OpenfactSession session, OrganizationModel organization) {
		this.session = session;
		this.organization = organization;
	}

	@GET
	@Path("")
	@NoCache
	@Produces(MediaType.APPLICATION_JSON)
	public List<SummaryRepresentation> getSummaryDocuments(@QueryParam("filterText") String filterText,
			@QueryParam("first") Integer firstResult, @QueryParam("max") Integer maxResults) {
		firstResult = firstResult != null ? firstResult : -1;
		maxResults = maxResults != null ? maxResults : -1;

		SummaryDocumentProvider summaryDocumentProvider = session.getProvider(SummaryDocumentProvider.class);
		List<SummaryDocumentModel> summaryDocuments;
		if (filterText == null) {
			summaryDocuments = summaryDocumentProvider.getSummaryDocuments(organization, firstResult, maxResults);
		} else {
			summaryDocuments = summaryDocumentProvider.searchForSummaryDocument(organization, filterText.trim(),
					firstResult, maxResults);
		}
		return summaryDocuments.stream().map(f -> SunatModelToRepresentation.toRepresentation(f))
				.collect(Collectors.toList());
	}

	@POST
	@Path("")
	@NoCache
	@Produces(MediaType.APPLICATION_JSON)
	public Response createSummaryDocument(SummaryRepresentation rep) {
		SummaryDocumentManager summaryDocumentManager = new SummaryDocumentManager(session);

		// Double-check duplicated ID
		if (rep.getSerie() != null && rep.getNumero() != null && summaryDocumentManager
				.getSummaryDocumentByDocumentId(rep.getSerie() + "-" + rep.getNumero(), organization) != null) {
			return ErrorResponse.exists("SummaryDocument exists with same documentId");
		}

		try {
			SummaryDocumentModel summaryDocument = summaryDocumentManager.addSummaryDocument(organization, rep);
			if (session.getTransactionManager().isActive()) {
				session.getTransactionManager().commit();
			}

			// Enviar a Cliente
			if (rep.isEnviarAutomaticamenteAlCliente()) {
				summaryDocumentManager.sendToCustomerParty(organization, summaryDocument);
			}

			// Enviar Sunat
			if (rep.isEnviarAutomaticamenteASunat()) {
				summaryDocumentManager.sendToTrirdParty(organization, summaryDocument);
			}

			URI location = uriInfo.getAbsolutePathBuilder().path(summaryDocument.getId()).build();
			return Response.created(location).build();
		} catch (ModelDuplicateException e) {
			if (session.getTransactionManager().isActive()) {
				session.getTransactionManager().setRollbackOnly();
			}
			return ErrorResponse.exists("SummaryDocument exists with same id or documentId");
		} catch (ModelException me) {
			if (session.getTransactionManager().isActive()) {
				session.getTransactionManager().setRollbackOnly();
			}
			return ErrorResponse.exists("Could not create summaryDocument");
		} catch (SendException e) {
			if (session.getTransactionManager().isActive()) {
				session.getTransactionManager().setRollbackOnly();
			}
			return ErrorResponse.exists("Could not send invoice");
		}
	}

	@POST
	@Path("upload")
	@Consumes("multipart/form-data")
	@Produces(MediaType.APPLICATION_JSON)
	public Response createInvoice(final MultipartFormDataInput input) {
		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		List<InputPart> inputParts = uploadForm.get("file");

		for (InputPart inputPart : inputParts) {
			try {
				InputStream inputStream = inputPart.getBody(InputStream.class, null);
				byte[] bytes = IOUtils.toByteArray(inputStream);

				SummaryDocumentsType summaryDocumentType = null;
				if (summaryDocumentType == null) {
					throw new IOException("Invalid invoice Xml");
				}

				SummaryDocumentManager summaryDocumentManager = new SummaryDocumentManager(session);

				// Double-check duplicated ID
				if (summaryDocumentType.getId() != null && summaryDocumentManager
						.getSummaryDocumentByDocumentId(summaryDocumentType.getId().getValue(), organization) != null) {
					throw new ModelDuplicateException("SummaryDocument exists with same documentId");
				}

				SummaryDocumentModel summaryDocument = summaryDocumentManager.addSummaryDocument(organization,
						summaryDocumentType);
				if (session.getTransactionManager().isActive()) {
					session.getTransactionManager().commit();
				}

				// Enviar Cliente
				summaryDocumentManager.sendToCustomerParty(organization, summaryDocument);

				// Enviar Sunat
				summaryDocumentManager.sendToTrirdParty(organization, summaryDocument);

				URI location = uriInfo.getAbsolutePathBuilder().path(summaryDocument.getId()).build();
				return Response.created(location).build();
			} catch (IOException e) {
				if (session.getTransactionManager().isActive()) {
					session.getTransactionManager().setRollbackOnly();
				}
				return ErrorResponse.error("Error Reading data", Response.Status.BAD_REQUEST);
			} catch (ModelDuplicateException e) {
				if (session.getTransactionManager().isActive()) {
					session.getTransactionManager().setRollbackOnly();
				}
				return ErrorResponse.exists("SummaryDocument exists with same documentId");
			} catch (ModelException me) {
				if (session.getTransactionManager().isActive()) {
					session.getTransactionManager().setRollbackOnly();
				}
				return ErrorResponse.exists("Could not create invoice");
			} catch (SendException e) {
				if (session.getTransactionManager().isActive()) {
					session.getTransactionManager().setRollbackOnly();
				}
				return ErrorResponse.exists("Could not send invoice");
			}
		}

		return Response.ok().build();
	}

	@POST
	@Path("search")
	@NoCache
	@Produces(MediaType.APPLICATION_JSON)
	public SearchResultsRepresentation<SummaryRepresentation> search(final SearchCriteriaRepresentation criteria) {
		SummaryDocumentProvider summaryDocumentProvider = session.getProvider(SummaryDocumentProvider.class);

		SearchCriteriaModel criteriaModel = RepresentationToModel.toModel(criteria);
		String filterText = criteria.getFilterText();
		SearchResultsModel<SummaryDocumentModel> results = null;
		if (filterText != null) {
			results = summaryDocumentProvider.searchForSummaryDocument(organization, criteriaModel, filterText.trim());
		} else {
			results = summaryDocumentProvider.searchForSummaryDocument(organization, criteriaModel);
		}
		SearchResultsRepresentation<SummaryRepresentation> rep = new SearchResultsRepresentation<>();
		List<SummaryRepresentation> items = new ArrayList<>();
		results.getModels().forEach(f -> items.add(SunatModelToRepresentation.toRepresentation(f)));
		rep.setItems(items);
		rep.setTotalSize(results.getTotalSize());
		return rep;
	}

	@GET
	@Path("{summaryDocumentId}")
	@NoCache
	@Produces(MediaType.APPLICATION_JSON)
	public SummaryRepresentation getSummaryDocument(@PathParam("summaryDocumentId") final String summaryDocumentId) {
		SummaryDocumentProvider summaryDocumentProvider = session.getProvider(SummaryDocumentProvider.class);
		SummaryDocumentModel summaryDocument = summaryDocumentProvider.getSummaryDocumentById(organization,
				summaryDocumentId);
		if (summaryDocument == null) {
			throw new NotFoundException("SummaryDocument not found");
		}

		SummaryRepresentation rep = SunatModelToRepresentation.toRepresentation(summaryDocument);
		return rep;
	}

	@GET
	@Path("{summaryDocumentId}/representation/text")
	@NoCache
	@Produces("application/text")
	public Response getSummaryDocumentAsText(@PathParam("summaryDocumentId") final String summaryDocumentId) {
		SummaryDocumentProvider summaryDocumentProvider = session.getProvider(SummaryDocumentProvider.class);
		SummaryDocumentModel summaryDocument = summaryDocumentProvider.getSummaryDocumentById(organization,
				summaryDocumentId);
		if (summaryDocument == null) {
			throw new NotFoundException("SummaryDocument not found");
		}

		String result = null;
		try {
			Document document = DocumentUtils.byteToDocument(summaryDocument.getXmlDocument());
			result = DocumentUtils.getDocumentToString(document);
		} catch (Exception e) {
			return ErrorResponse.error("Invalid xml parser", Status.INTERNAL_SERVER_ERROR);
		}

		Response.ResponseBuilder response = Response.ok(result);
		return response.build();
	}

	@GET
	@Path("{summaryDocumentId}/representation/xml")
	@NoCache
	@Produces("application/xml")
	public Response getSummaryDocumentAsXml(@PathParam("summaryDocumentId") final String summaryDocumentId) {
		SummaryDocumentProvider summaryDocumentProvider = session.getProvider(SummaryDocumentProvider.class);
		SummaryDocumentModel summaryDocument = summaryDocumentProvider.getSummaryDocumentById(organization,
				summaryDocumentId);
		if (summaryDocument == null) {
			throw new NotFoundException("SummaryDocument not found");
		}

		Document document = null;
		try {
			document = DocumentUtils.byteToDocument(summaryDocument.getXmlDocument());
		} catch (Exception e) {
			return ErrorResponse.error("Invalid xml parser", Status.INTERNAL_SERVER_ERROR);
		}

		Response.ResponseBuilder response = Response.ok(document);
		return response.build();
	}

	@GET
	@Path("{summaryDocumentId}/representation/pdf")
	@NoCache
	@Produces("application/xml")
	public Response getSummaryDocumentAsPdf(@PathParam("summaryDocumentId") final String summaryDocumentId) {
		return null;
	}

	@DELETE
	@Path("{summaryDocumentId}")
	@NoCache
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteSummaryDocument(@PathParam("summaryDocumentId") final String summaryDocumentId) {
		SummaryDocumentProvider summaryDocumentProvider = session.getProvider(SummaryDocumentProvider.class);
		SummaryDocumentModel summaryDocument = summaryDocumentProvider.getSummaryDocumentById(organization,
				summaryDocumentId);
		if (summaryDocument == null) {
			throw new NotFoundException("SummaryDocument not found");
		}

		boolean removed = new SummaryDocumentManager(session).removeSummaryDocument(organization, summaryDocument);
		if (removed) {
			return Response.noContent().build();
		} else {
			return ErrorResponse.error("SummaryDocument couldn't be deleted", Response.Status.BAD_REQUEST);
		}
	}

	@GET
	@Path("{summaryDocumentId}/send-events")
	@NoCache
	@Produces(MediaType.APPLICATION_JSON)
	public List<SendEventRepresentation> getSendEvents(@QueryParam("summaryDocumentId") final String summaryDocumentId) {
		SummaryDocumentProvider summaryDocumentProvider = session.getProvider(SummaryDocumentProvider.class);
		SummaryDocumentModel summaryDocument = summaryDocumentProvider.getSummaryDocumentById(organization,
				summaryDocumentId);
		if (summaryDocument == null) {
			throw new NotFoundException("SummaryDocument not found");
		}
		List<SendEventModel> sendEvents = summaryDocument.getSendEvents();
		return sendEvents.stream().map(f -> ModelToRepresentation.toRepresentation(f)).collect(Collectors.toList());
	}

	@GET
	@Path("{summaryDocumentId}/representation/cdr")
	@NoCache
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getCdr(@QueryParam("summaryDocumentId") final String summaryDocumentId) throws Exception {
		/*SummaryDocumentProvider summaryDocumentProvider = session.getProvider(SummaryDocumentProvider.class);
		if (summaryDocumentId == null) {
			throw new NotFoundException("Sunat response not found");
		}
		String ticket = null;
		SendEventModel sendEvent = null;
		StorageFileModel storageFile = null;
		SummaryDocumentModel summaryDocument = summaryDocumentProvider.getSummaryDocumentByID(organization,
				summaryDocumentId);
		List<SendEventModel> sendEvents = summaryDocument.getSendEvents();
		for (SendEventModel model : sendEvents) {
			if (model.getResponse().containsKey("TICKET")) {
				sendEvent = model;
			}
		}
		if (sendEvent == null) {
			throw new NotFoundException("Ticket not found");
		}
		if (sendEvent.getFileResponseAttatchments().isEmpty()) {
			byte[] result = new SunatSenderUtils(organization, EmissionType.CPE).getStatus(ticket);
			if (result == null) {
				throw new NotFoundException("Sunat response, cdr not found");
			}
			String fileName = SunatTemplateUtils.generateXmlFileName(organization, summaryDocument);
			sendEvent.addFileResponseAttatchments(
					SunatTemplateUtils.toFileModel(InternetMediaType.ZIP, "R" + fileName, result));
			sendEvent.setResponse(SunatResponseUtils.byteResponseToMap(result));
		}
		if (sendEvent.getFileResponseAttatchments().isEmpty()) {
			throw new NotFoundException("Sunat response, cdr not found");
		}
		storageFile = sendEvent.getFileResponseAttatchments().get(0);
		ResponseBuilder response = Response.ok(storageFile.getFile());
		response.type(storageFile.getMimeType());
		response.header("content-disposition", "attachment; filename=\"" + storageFile.getFileName() + "\"");
		return response.build();*/
		return null;
	}
}