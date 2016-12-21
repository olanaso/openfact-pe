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
import org.openfact.models.ModelDuplicateException;
import org.openfact.models.ModelException;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.StorageFileModel;
import org.openfact.models.search.SearchCriteriaModel;
import org.openfact.models.search.SearchResultsModel;
import org.openfact.models.utils.ModelToRepresentation;
import org.openfact.models.utils.RepresentationToModel;
import org.openfact.pe.model.types.RetentionType;
import org.openfact.pe.models.RetentionModel;
import org.openfact.pe.models.RetentionProvider;
import org.openfact.pe.models.utils.SunatModelToRepresentation;
import org.openfact.pe.representations.idm.DocumentoSunatRepresentation;
import org.openfact.pe.services.managers.RetentionManager;
import org.openfact.representations.idm.SendEventRepresentation;
import org.openfact.representations.idm.search.SearchCriteriaRepresentation;
import org.openfact.representations.idm.search.SearchResultsRepresentation;
import org.openfact.services.ErrorResponse;
import org.openfact.ubl.SendEventModel;
import org.openfact.ubl.SendException;
import org.w3c.dom.Document;

@Consumes(MediaType.APPLICATION_JSON)
public class RetentionsResource {

	protected static final Logger logger = Logger.getLogger(RetentionsResource.class);

	private final OpenfactSession session;
	private final OrganizationModel organization;

	@Context
	protected UriInfo uriInfo;

	public RetentionsResource(OpenfactSession session, OrganizationModel organization) {
		this.session = session;
		this.organization = organization;
	}

	@GET
	@Path("")
	@NoCache
	@Produces(MediaType.APPLICATION_JSON)
	public List<DocumentoSunatRepresentation> getRetentions(@QueryParam("filterText") String filterText,
															@QueryParam("first") Integer firstResult, @QueryParam("max") Integer maxResults) {
		firstResult = firstResult != null ? firstResult : -1;
		maxResults = maxResults != null ? maxResults : -1;

		RetentionProvider retentionProvider = session.getProvider(RetentionProvider.class);
		List<RetentionModel> retentions;
		if (filterText == null) {
			retentions = retentionProvider.getRetentions(organization, firstResult, maxResults);
		} else {
			retentions = retentionProvider.searchForRetention(organization, filterText.trim(), firstResult, maxResults);
		}
		return retentions.stream().map(f -> SunatModelToRepresentation.toRepresentation(organization, f))
				.collect(Collectors.toList());
	}

	@POST
	@Path("")
	@NoCache
	@Produces(MediaType.APPLICATION_JSON)
	public Response createRetention(DocumentoSunatRepresentation rep) {
		RetentionManager retentionManager = new RetentionManager(session);

		// Double-check duplicated ID
		if (rep.getSerieDocumento() != null && rep.getNumeroDocumento() != null && retentionManager
				.getRetentionByDocumentId(rep.getSerieDocumento() + "-" + rep.getNumeroDocumento(), organization) != null) {
			return ErrorResponse.exists("Retention exists with same documentId");
		}

		try {
			RetentionModel retention = retentionManager.addRetention(organization, rep);
			if (session.getTransactionManager().isActive()) {
				session.getTransactionManager().commit();
			}

			// Enviar a Cliente
			if (rep.isEnviarAutomaticamenteAlCliente()) {
				retentionManager.sendToCustomerParty(organization, retention);
			}

			// Enviar Sunat
			if (rep.isEnviarAutomaticamenteASunat()) {
				retentionManager.sendToTrirdParty(organization, retention);
			}

			URI location = uriInfo.getAbsolutePathBuilder().path(retention.getId()).build();
			return Response.created(location).build();
		} catch (ModelDuplicateException e) {
			if (session.getTransactionManager().isActive()) {
				session.getTransactionManager().setRollbackOnly();
			}
			return ErrorResponse.exists("Retention exists with same id or documentId");
		} catch (ModelException me) {
			if (session.getTransactionManager().isActive()) {
				session.getTransactionManager().setRollbackOnly();
			}
			return ErrorResponse.exists("Could not create retention");
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

				RetentionType retentionType = null;
				if (retentionType == null) {
					throw new IOException("Invalid invoice Xml");
				}

				RetentionManager retentionManager = new RetentionManager(session);

				// Double-check duplicated ID
				if (retentionType.getId() != null && retentionManager
						.getRetentionByDocumentId(retentionType.getId().getValue(), organization) != null) {
					throw new ModelDuplicateException("Retention exists with same documentId");
				}

				RetentionModel retention = retentionManager.addRetention(organization, retentionType);
				if (session.getTransactionManager().isActive()) {
					session.getTransactionManager().commit();
				}

				// Enviar Cliente
				retentionManager.sendToCustomerParty(organization, retention);

				// Enviar Sunat
				retentionManager.sendToTrirdParty(organization, retention);

				URI location = uriInfo.getAbsolutePathBuilder().path(retention.getId()).build();
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
				return ErrorResponse.exists("Retention exists with same documentId");
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
	public SearchResultsRepresentation<DocumentoSunatRepresentation> search(final SearchCriteriaRepresentation criteria) {
		RetentionProvider retentionProvider = session.getProvider(RetentionProvider.class);

		SearchCriteriaModel criteriaModel = RepresentationToModel.toModel(criteria);
		String filterText = criteria.getFilterText();
		SearchResultsModel<RetentionModel> results = null;
		if (filterText != null) {
			results = retentionProvider.searchForRetention(organization, criteriaModel, filterText.trim());
		} else {
			results = retentionProvider.searchForRetention(organization, criteriaModel);
		}
		SearchResultsRepresentation<DocumentoSunatRepresentation> rep = new SearchResultsRepresentation<>();
		List<DocumentoSunatRepresentation> items = new ArrayList<>();
		results.getModels().forEach(f -> items.add(SunatModelToRepresentation.toRepresentation(organization, f)));
		rep.setItems(items);
		rep.setTotalSize(results.getTotalSize());
		return rep;
	}

	@GET
	@Path("{retentionId}")
	@NoCache
	@Produces(MediaType.APPLICATION_JSON)
	public DocumentoSunatRepresentation getRetention(@PathParam("retentionId") final String retentionId) {
		RetentionProvider retentionProvider = session.getProvider(RetentionProvider.class);
		RetentionModel retention = retentionProvider.getRetentionById(organization, retentionId);
		if (retention == null) {
			throw new NotFoundException("Retention not found");
		}

		DocumentoSunatRepresentation rep = SunatModelToRepresentation.toRepresentation(organization, retention);
		return rep;
	}

	@GET
	@Path("{retentionId}/representation/text")
	@NoCache
	@Produces("application/text")
	public Response getRetentionAsText(@PathParam("retentionId") final String retentionId) {
		RetentionProvider retentionProvider = session.getProvider(RetentionProvider.class);
		RetentionModel retention = retentionProvider.getRetentionById(organization, retentionId);
		if (retention == null) {
			throw new NotFoundException("Retention not found");
		}

		String result = null;
		try {
			Document document = DocumentUtils.byteToDocument(retention.getXmlDocument());
			result = DocumentUtils.getDocumentToString(document);
		} catch (Exception e) {
			return ErrorResponse.error("Invalid xml parser", Status.INTERNAL_SERVER_ERROR);
		}

		Response.ResponseBuilder response = Response.ok(result);
		return response.build();
	}

	@GET
	@Path("{retentionId}/representation/xml")
	@NoCache
	@Produces("application/xml")
	public Response getRetentionAsXml(@PathParam("retentionId") final String retentionId) {
		RetentionProvider retentionProvider = session.getProvider(RetentionProvider.class);
		RetentionModel retention = retentionProvider.getRetentionById(organization, retentionId);
		if (retention == null) {
			throw new NotFoundException("Retention not found");
		}

		Document document = null;
		try {
			document = DocumentUtils.byteToDocument(retention.getXmlDocument());
		} catch (Exception e) {
			return ErrorResponse.error("Invalid xml parser", Status.INTERNAL_SERVER_ERROR);
		}

		Response.ResponseBuilder response = Response.ok(document);
		return response.build();
	}

	@GET
	@Path("{retentionId}/representation/pdf")
	@NoCache
	@Produces("application/xml")
	public Response getRetentionAsPdf(@PathParam("retentionId") final String retentionId) {
		return null;
	}

	@DELETE
	@Path("{retentionId}")
	@NoCache
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteRetention(@PathParam("retentionId") final String retentionId) {
		RetentionProvider retentionProvider = session.getProvider(RetentionProvider.class);
		RetentionModel retention = retentionProvider.getRetentionById(organization, retentionId);
		if (retention == null) {
			throw new NotFoundException("Retention not found");
		}

		boolean removed = new RetentionManager(session).removeRetention(organization, retention);
		if (removed) {
			return Response.noContent().build();
		} else {
			return ErrorResponse.error("Retention couldn't be deleted", Response.Status.BAD_REQUEST);
		}
	}

	@GET
	@Path("{retentionId}")
	@NoCache
	@Produces(MediaType.APPLICATION_JSON)
	public List<SendEventRepresentation> getSendEvents(@QueryParam("retentionId") final String retentionId) {
		RetentionProvider retentionProvider = session.getProvider(RetentionProvider.class);
		RetentionModel retention = retentionProvider.getRetentionById(organization, retentionId);
		if (retention == null) {
			throw new NotFoundException("Retention not found");
		}
		List<SendEventModel> sendEvents = retention.getSendEvents();
		return sendEvents.stream().map(f -> ModelToRepresentation.toRepresentation(f)).collect(Collectors.toList());
	}

	@GET
	@Path("{retentionId}/representation/cdr")
	@NoCache
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getCdr(@QueryParam("retentionId") final String retentionId) {
		RetentionProvider retentionProvider = session.getProvider(RetentionProvider.class);
		if (retentionId == null) {
			throw new NotFoundException("Sunat response not found");
		}
		StorageFileModel storageFile = null;
		RetentionModel retention = retentionProvider.getRetentionByID(organization, retentionId);
		List<SendEventModel> sendEvents = retention.getSendEvents();
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