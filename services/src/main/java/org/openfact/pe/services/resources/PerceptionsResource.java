package org.openfact.pe.services.resources;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import javax.ws.rs.*;
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
import org.openfact.file.FileModel;
import org.openfact.file.InternetMediaType;
import org.openfact.models.*;
import org.openfact.models.enums.DestinyType;
import org.openfact.models.enums.SendResultType;
import org.openfact.models.search.SearchCriteriaModel;
import org.openfact.models.search.SearchResultsModel;
import org.openfact.models.utils.ModelToRepresentation;
import org.openfact.models.utils.RepresentationToModel;
import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.PerceptionProvider;
import org.openfact.pe.models.types.perception.PerceptionType;
import org.openfact.pe.models.utils.SunatDocumentToType;
import org.openfact.pe.models.utils.SunatModelToRepresentation;
import org.openfact.pe.representations.idm.DocumentoSunatRepresentation;
import org.openfact.pe.services.managers.PerceptionManager;
import org.openfact.representations.idm.SendEventRepresentation;
import org.openfact.representations.idm.search.SearchCriteriaRepresentation;
import org.openfact.representations.idm.search.SearchResultsRepresentation;
import org.openfact.services.ErrorResponse;
import org.openfact.services.scheduled.ScheduledTaskRunner;
import org.openfact.timer.ScheduledTask;
import org.w3c.dom.Document;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Consumes(MediaType.APPLICATION_JSON)
public class PerceptionsResource {

	protected static final Logger logger = Logger.getLogger(PerceptionsResource.class);

	private final OpenfactSession session;
	private final OrganizationModel organization;

	@Context
	protected UriInfo uriInfo;

	public PerceptionsResource(OpenfactSession session, OrganizationModel organization) {
		this.session = session;
		this.organization = organization;
	}

	@GET
	@Path("")
	@NoCache
	@Produces(MediaType.APPLICATION_JSON)
	public List<DocumentoSunatRepresentation> getPerceptions(@QueryParam("filterText") String filterText,
															@QueryParam("first") Integer firstResult, @QueryParam("max") Integer maxResults) {
		firstResult = firstResult != null ? firstResult : -1;
		maxResults = maxResults != null ? maxResults : -1;

		PerceptionProvider perceptionProvider = session.getProvider(PerceptionProvider.class);
		List<PerceptionModel> perceptions;
		if (filterText == null) {
			perceptions = perceptionProvider.getPerceptions(organization, firstResult, maxResults);
		} else {
			perceptions = perceptionProvider.searchForPerception(organization, filterText.trim(), firstResult, maxResults);
		}
		return perceptions.stream().map(f -> SunatModelToRepresentation.toRepresentation(f))
				.collect(Collectors.toList());
	}

	@POST
	@Path("")
	@NoCache
	@Produces(MediaType.APPLICATION_JSON)
	public Response createPerception(DocumentoSunatRepresentation rep) {
		PerceptionManager perceptionManager = new PerceptionManager(session);

		// Double-check duplicated ID
		if (rep.getSerieDocumento() != null && rep.getNumeroDocumento() != null && perceptionManager
				.getPerceptionByDocumentId(rep.getSerieDocumento() + "-" + rep.getNumeroDocumento(), organization) != null) {
			return ErrorResponse.exists("Perception exists with same documentId");
		}

		try {
			PerceptionModel perception = perceptionManager.addPerception(organization, rep);
			// Enviar a Cliente
			if (rep.isEnviarAutomaticamenteAlCliente()) {
				try {
					perceptionManager.sendToCustomerParty(organization, perception);
				} catch (SendException e) {
					logger.error("Error sending to Customer");
				}
			}

			// Enviar Sunat
			if (rep.isEnviarAutomaticamenteASunat()) {
				try {
					perceptionManager.sendToTrirdParty(organization, perception);
				} catch (SendException e) {
					logger.error("Error sending to Sunat");
				}
			}

			URI location = session.getContext().getUri().getAbsolutePathBuilder().path(perception.getId()).build();
			return Response.created(location).entity(SunatModelToRepresentation.toRepresentation(perception)).build();
		} catch (ModelDuplicateException e) {
			if (session.getTransactionManager().isActive()) {
				session.getTransactionManager().setRollbackOnly();
			}
			return ErrorResponse.exists("Perception exists with same id or documentId");
		} catch (ModelException me) {
			if (session.getTransactionManager().isActive()) {
				session.getTransactionManager().setRollbackOnly();
			}
			return ErrorResponse.exists("Could not create perception");
		}
	}

	@POST
	@Path("upload")
	@Consumes("multipart/form-data")
	@Produces(MediaType.APPLICATION_JSON)
	public Response createPerception(final MultipartFormDataInput input) {
		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		List<InputPart> inputParts = uploadForm.get("file");

		for (InputPart inputPart : inputParts) {
			try {
				InputStream inputStream = inputPart.getBody(InputStream.class, null);
				byte[] bytes = IOUtils.toByteArray(inputStream);
				Document document = DocumentUtils.byteToDocument(bytes);
				PerceptionType perceptionType = SunatDocumentToType.toPerceptionType(document);;
				if (perceptionType == null) {
					throw new IOException("Invalid invoice Xml");
				}

				PerceptionManager perceptionManager = new PerceptionManager(session);

				// Double-check duplicated ID
				if (perceptionType.getId() != null && perceptionManager
						.getPerceptionByDocumentId(perceptionType.getId().getValue(), organization) != null) {
					throw new ModelDuplicateException("Perception exists with same documentId");
				}

				PerceptionModel perception = perceptionManager.addPerception(organization, perceptionType);
				// Enviar Cliente
				perceptionManager.sendToCustomerParty(organization, perception);
				// Enviar Sunat
				perceptionManager.sendToTrirdParty(organization, perception);

				URI location = session.getContext().getUri().getAbsolutePathBuilder().path(perception.getId()).build();
				return Response.created(location).entity(SunatModelToRepresentation.toRepresentation(perception)).build();
			} catch (IOException e) {
				if (session.getTransactionManager().isActive()) {
					session.getTransactionManager().setRollbackOnly();
				}
				return ErrorResponse.error("Error Reading data", Response.Status.BAD_REQUEST);
			} catch (ModelDuplicateException e) {
				if (session.getTransactionManager().isActive()) {
					session.getTransactionManager().setRollbackOnly();
				}
				return ErrorResponse.exists("Perception exists with same documentId");
			} catch (ModelException me) {
				if (session.getTransactionManager().isActive()) {
					session.getTransactionManager().setRollbackOnly();
				}
				return ErrorResponse.exists("Could not create perception");
			} catch (SendException e) {
				if (session.getTransactionManager().isActive()) {
					session.getTransactionManager().setRollbackOnly();
				}
				return ErrorResponse.exists("Could not send perception");
			} catch (Exception e) {
				if (session.getTransactionManager().isActive()) {
					session.getTransactionManager().setRollbackOnly();
				}
				return ErrorResponse.exists("Could not send perception");			}
		}

		return Response.ok().build();
	}

	@POST
	@Path("search")
	@NoCache
	@Produces(MediaType.APPLICATION_JSON)
	public SearchResultsRepresentation<DocumentoSunatRepresentation> search(final SearchCriteriaRepresentation criteria) {
		PerceptionProvider perceptionProvider = session.getProvider(PerceptionProvider.class);

		SearchCriteriaModel criteriaModel = RepresentationToModel.toModel(criteria);
		String filterText = criteria.getFilterText();
		SearchResultsModel<PerceptionModel> results = null;
		if (filterText != null) {
			results = perceptionProvider.searchForPerception(organization, criteriaModel, filterText.trim());
		} else {
			results = perceptionProvider.searchForPerception(organization, criteriaModel);
		}
		SearchResultsRepresentation<DocumentoSunatRepresentation> rep = new SearchResultsRepresentation<>();
		List<DocumentoSunatRepresentation> items = new ArrayList<>();
		results.getModels().forEach(f -> items.add(SunatModelToRepresentation.toRepresentation( f)));
		rep.setItems(items);
		rep.setTotalSize(results.getTotalSize());
		return rep;
	}


	@GET
	@Path("{perceptionId}")
	@NoCache
	@Produces(MediaType.APPLICATION_JSON)
	public DocumentoSunatRepresentation getPerception(@PathParam("perceptionId") final String perceptionId) {
		PerceptionProvider perceptionProvider = session.getProvider(PerceptionProvider.class);
		PerceptionModel perception = perceptionProvider.getPerceptionById(organization, perceptionId);
		if (perception == null) {
			throw new NotFoundException("Perception not found");
		}

		DocumentoSunatRepresentation rep = SunatModelToRepresentation.toRepresentation(perception);
		return rep;
	}

	@GET
	@Path("{perceptionId}/representation/json")
	@NoCache
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPerceptionAsJson(@PathParam("perceptionId") final String perceptionId) {
		PerceptionProvider perceptionProvider = session.getProvider(PerceptionProvider.class);
		PerceptionModel perception = perceptionProvider.getPerceptionById(organization, perceptionId);
		if (perception == null) {
			return ErrorResponse.exists("Perception not found");
		}

		Document document ;
		try {
			document = DocumentUtils.byteToDocument(perception.getXmlDocument());
		} catch (Exception e) {
			return ErrorResponse.error("Invalid xml parser", Status.INTERNAL_SERVER_ERROR);
		}
		PerceptionType type= SunatDocumentToType.toPerceptionType(document);
		Response.ResponseBuilder response = Response.ok(type);
		return response.build();

	}
	@GET
	@Path("{perceptionId}/representation/text")
	@NoCache
	@Produces("application/text")
	public Response getPerceptionAsText(@PathParam("perceptionId") final String perceptionId) {
		PerceptionProvider perceptionProvider = session.getProvider(PerceptionProvider.class);
		PerceptionModel perception = perceptionProvider.getPerceptionById(organization, perceptionId);
		if (perception == null) {
			return ErrorResponse.exists("Perception not found");
		}

		String result = null;
		try {
			Document document = DocumentUtils.byteToDocument(perception.getXmlDocument());
			result = DocumentUtils.getDocumentToString(document);
		} catch (Exception e) {
			return ErrorResponse.error("Invalid xml parser", Status.INTERNAL_SERVER_ERROR);
		}

		Response.ResponseBuilder response = Response.ok(result);
		return response.build();
	}

	@GET
	@Path("{perceptionId}/representation/xml")
	@NoCache
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getPerceptionAsXml(@PathParam("perceptionId") final String perceptionId) {
		PerceptionProvider perceptionProvider = session.getProvider(PerceptionProvider.class);
		PerceptionModel perception = perceptionProvider.getPerceptionById(organization, perceptionId);
		if (perception == null) {
			return ErrorResponse.exists("Perception not found");
		}

		Document document = null;
		try {
			document = DocumentUtils.byteToDocument(perception.getXmlDocument());
		} catch (Exception e) {
			return ErrorResponse.error("Invalid xml parser", Status.INTERNAL_SERVER_ERROR);
		}
		ResponseBuilder response = Response.ok(document);
		response.type("application/xml");
		response.header("content-disposition", "attachment; filename=\"" + perception.getDocumentId() + "\".xml");
		return response.build();
	}

	@GET
	@Path("{perceptionId}/representation/pdf")
	@NoCache
	@Produces("application/pdf")
	public Response getPerceptionAsPdf(@PathParam("perceptionId") final String perceptionId) {
		return null;
	}

	@DELETE
	@Path("{perceptionId}")
	@NoCache
	@Produces(MediaType.APPLICATION_JSON)
	public Response deletePerception(@PathParam("perceptionId") final String perceptionId) {
		PerceptionProvider perceptionProvider = session.getProvider(PerceptionProvider.class);
		PerceptionModel perception = perceptionProvider.getPerceptionById(organization, perceptionId);
		if (perception == null) {
			return ErrorResponse.exists("Perception not found");
		}

		boolean removed = new PerceptionManager(session).removePerception(organization, perception);
		if (removed) {
			return Response.noContent().build();
		} else {
			return ErrorResponse.error("Perception couldn't be deleted", Response.Status.BAD_REQUEST);
		}
	}

	@GET
	@Path("{perceptionId}/send-events")
	@NoCache
	@Produces(MediaType.APPLICATION_JSON)
	public List<SendEventRepresentation> getSendEvents(@PathParam("perceptionId") final String perceptionId) {
		PerceptionProvider perceptionProvider = session.getProvider(PerceptionProvider.class);
		PerceptionModel perception = perceptionProvider.getPerceptionById(organization, perceptionId);
		if (perception == null) {
			throw new NotFoundException("Perception not found");
		}
		List<SendEventModel> sendEvents = perception.getSendEvents();
		return sendEvents.stream().map(f -> ModelToRepresentation.toRepresentation(f)).collect(Collectors.toList());
	}

	@GET
	@Path("{perceptionId}/representation/cdr")
	@NoCache
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getCdr(@PathParam("perceptionId") final String perceptionId) {
		PerceptionModel perception = session.getProvider(PerceptionProvider.class).getPerceptionByID(organization, perceptionId);
		if (perception == null) {
			throw new NotFoundException("Perception not found");
		}

		List<SendEventModel> sendEvents = perception.getSendEvents();

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

	@POST
	@Path("{perceptionId}/send-to-customer")
	@NoCache
	@Produces(MediaType.APPLICATION_JSON)
	public void sendToCustomer(@PathParam("perceptionId") final String perceptionId) {
		PerceptionManager perceptionManager = new PerceptionManager(session);
		PerceptionProvider perceptionProvider = session.getProvider(PerceptionProvider.class);
		if (perceptionId == null) {
			throw new NotFoundException("Perception Id not found");
		}
		try {
			PerceptionModel perception = perceptionProvider.getPerceptionById(organization, perceptionId);
			OrganizationModel organizationThread = session.organizations().getOrganization(organization.getId());
			perceptionManager.sendToCustomerParty(organizationThread, perception);
		} catch (SendException e) {
			throw new NotFoundException("Error sending email");
		}

	/*	ExecutorService executorService = null;
		try {
			executorService = Executors.newCachedThreadPool();

			ScheduledTaskRunner scheduledTaskRunner = new ScheduledTaskRunner(session.getOpenfactSessionFactory(), new ScheduledTask() {
				@Override
				public void run(OpenfactSession session) {
					PerceptionManager perceptionManager = new PerceptionManager(session);
					try {
						OrganizationModel organizationThread = session.organizations().getOrganization(organization.getId());
						PerceptionModel perception = perceptionProvider.getPerceptionById(organization, perceptionId);
						perceptionManager.sendToCustomerParty(organizationThread, perception);
					} catch (SendException e) {
						throw new InternalServerErrorException(e);
					}
				}
			});
			executorService.execute(scheduledTaskRunner);
		} finally {
			if(executorService != null) {
				executorService.shutdown();
			}
		}*/
	}

	@POST
	@Path("{perceptionId}/send-to-third-party")
	@NoCache
	@Produces(MediaType.APPLICATION_JSON)
	public void sendToThridParty(@PathParam("perceptionId") final String perceptionId) {
		PerceptionProvider perceptionProvider = session.getProvider(PerceptionProvider.class);
		if (perceptionId == null) {
			throw new NotFoundException("Perception Id not found");
		}
		try {
			PerceptionManager perceptionManager = new PerceptionManager(session);
			OrganizationModel organizationThread = session.organizations().getOrganization(organization.getId());
			PerceptionModel perception = perceptionProvider.getPerceptionById(organization, perceptionId);
			perceptionManager.sendToTrirdParty(organizationThread, perception);
		} catch (SendException e) {
			throw new NotFoundException("Error sending sunat");
		}
		/*ExecutorService executorService = null;
		try {
			executorService = Executors.newCachedThreadPool();
			ScheduledTaskRunner scheduledTaskRunner = new ScheduledTaskRunner(session.getOpenfactSessionFactory(), new ScheduledTask() {
				@Override
				public void run(OpenfactSession session) {
					PerceptionManager perceptionManager = new PerceptionManager(session);
					try {
						OrganizationModel organizationThread = session.organizations().getOrganization(organization.getId());
						PerceptionModel perception = perceptionProvider.getPerceptionById(organization, perceptionId);
						perceptionManager.sendToTrirdParty(organizationThread, perception);
					} catch (SendException e) {
						throw new InternalServerErrorException(e);
					}
				}
			});
			executorService.execute(scheduledTaskRunner);
		} finally {
			if(executorService != null) {
				executorService.shutdown();
			}
		}*/
	}
}