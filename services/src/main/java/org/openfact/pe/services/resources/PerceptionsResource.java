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
import org.json.JSONObject;
import org.openfact.common.converts.DocumentUtils;
import org.openfact.events.admin.OperationType;
import org.openfact.events.admin.ResourceType;
import org.openfact.file.FileModel;
import org.openfact.file.InternetMediaType;
import org.openfact.models.*;
import org.openfact.models.enums.DestinyType;
import org.openfact.models.enums.SendResultType;
import org.openfact.models.search.SearchCriteriaModel;
import org.openfact.models.search.SearchResultsModel;
import org.openfact.models.utils.ModelToRepresentation;
import org.openfact.models.utils.OpenfactModelUtils;
import org.openfact.models.utils.RepresentationToModel;
import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.PerceptionProvider;
import org.openfact.pe.models.PerceptionUBLModel;
import org.openfact.pe.models.SunatSendException;
import org.openfact.pe.models.types.perception.PerceptionType;
import org.openfact.pe.models.utils.SunatDocumentToType;
import org.openfact.pe.models.utils.SunatModelToRepresentation;
import org.openfact.pe.models.utils.SunatRepresentationToType;
import org.openfact.pe.representations.idm.DocumentoSunatRepresentation;
import org.openfact.pe.services.managers.PerceptionManager;
import org.openfact.report.ExportFormat;
import org.openfact.report.ReportException;
import org.openfact.representations.idm.SendEventRepresentation;
import org.openfact.representations.idm.ThirdPartyEmailRepresentation;
import org.openfact.representations.idm.search.SearchCriteriaRepresentation;
import org.openfact.representations.idm.search.SearchResultsRepresentation;
import org.openfact.services.ErrorResponse;
import org.openfact.services.managers.InvoiceManager;
import org.openfact.services.resources.admin.AdminEventBuilder;
import org.openfact.services.scheduled.ScheduledTaskRunner;
import org.openfact.timer.ScheduledTask;
import org.openfact.ubl.UBLReportProvider;
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
    private final AdminEventBuilder adminEvent;

    public PerceptionsResource(OpenfactSession session, OrganizationModel organization, AdminEventBuilder adminEvent) {
        this.session = session;
        this.organization = organization;
        this.adminEvent = adminEvent;
    }

    @GET
    @Path("")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public List<DocumentoSunatRepresentation> getPerceptions(
            @QueryParam("filterText") String filterText,
            @QueryParam("documentId") String documentId,
            @QueryParam("first") Integer firstResult,
            @QueryParam("max") Integer maxResults) {
        firstResult = firstResult != null ? firstResult : -1;
        maxResults = maxResults != null ? maxResults : Constants.DEFAULT_MAX_RESULTS;

        List<PerceptionModel> perceptionModels;
        if (filterText != null) {
            perceptionModels = session.getProvider(PerceptionProvider.class).searchForPerception(organization, filterText.trim(), firstResult, maxResults);
        } else if (documentId != null) {
            Map<String, String> attributes = new HashMap<>();
            if (documentId != null) {
                attributes.put(InvoiceModel.DOCUMENT_ID, documentId);
            }
            perceptionModels = session.getProvider(PerceptionProvider.class).searchForPerception(attributes, organization, firstResult, maxResults);
        } else {
            perceptionModels = session.getProvider(PerceptionProvider.class).getPerceptions(organization, firstResult, maxResults);
        }

        return perceptionModels.stream()
                .map(f -> SunatModelToRepresentation.toRepresentation(f))
                .collect(Collectors.toList());
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

                PerceptionType perceptionType = SunatDocumentToType.toPerceptionType(DocumentUtils.byteToDocument(bytes));

                if (perceptionType == null) {
                    throw new IOException("Invalid perception Xml");
                }

                PerceptionManager perceptionManager = new PerceptionManager(session);

                // Double-check duplicated documentId
                if (perceptionType.getId() != null && perceptionManager.getPerceptionByDocumentId(organization, perceptionType.getId().getValue()) != null) {
                    throw new ModelDuplicateException("Perception exists with same documentId[" + perceptionType.getId().getValue() + "]");
                }

                PerceptionModel perception = perceptionManager.addPerception(organization, perceptionType);

                URI location = session.getContext().getUri().getAbsolutePathBuilder().path(perception.getId()).build();
                adminEvent.operation(OperationType.CREATE).resourcePath(location.toString(), perception.getId()).representation(perceptionType).success();
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
            } catch (Exception e) {
                if (session.getTransactionManager().isActive()) {
                    session.getTransactionManager().setRollbackOnly();
                }
                return ErrorResponse.exists("Could not create perception");
            }
        }

        return Response.ok().build();
    }

    @POST
    @Path("")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPerception(DocumentoSunatRepresentation rep) {
        PerceptionManager perceptionManager = new PerceptionManager(session);

        if (rep.getSerieDocumento() != null || rep.getNumeroDocumento() != null) {
            if (rep.getSerieDocumento() != null && rep.getNumeroDocumento() != null) {
                if (perceptionManager.getPerceptionByDocumentId(organization, rep.getSerieDocumento() + "-" + rep.getNumeroDocumento()) != null) {
                    return ErrorResponse.exists("Invoice exists with same documentId");
                }
            } else {
                return ErrorResponse.error("Numero de serie y/o numero invalido", Response.Status.BAD_REQUEST);
            }
        }

        try {
            PerceptionType perceptionType = SunatRepresentationToType.toPerceptionType(session, organization, rep);
            PerceptionModel perceptionModel = perceptionManager.addPerception(organization, perceptionType);

            if (rep.isEnviarAutomaticamenteASunat()) {
                SendEventModel thirdPartySendEvent = perceptionModel.addSendEvent(DestinyType.THIRD_PARTY);
                try {
                    perceptionManager.sendToTrirdParty(organization, perceptionModel, thirdPartySendEvent);
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
                SendEventModel customerSendEvent = perceptionModel.addSendEvent(DestinyType.CUSTOMER);
                try {
                    perceptionManager.sendToCustomerParty(organization, perceptionModel, customerSendEvent);
                } catch (ModelInsuficientData e) {
                    customerSendEvent.setResult(SendResultType.ERROR);
                    customerSendEvent.setDescription(e.getMessage());
                } catch (SendException e) {
                    customerSendEvent.setResult(SendResultType.ERROR);
                    customerSendEvent.setDescription("Internal server error");
                    logger.error("Internal Server Error sending to customer", e);
                }
            }

            URI location = session.getContext().getUri().getAbsolutePathBuilder().path(perceptionModel.getId()).build();
            adminEvent.operation(OperationType.CREATE).resourcePath(location.toString()).representation(rep).success();
            return Response.created(location).entity(SunatModelToRepresentation.toRepresentation(perceptionModel)).build();
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
        results.getModels().forEach(f -> items.add(SunatModelToRepresentation.toRepresentation(f)));
        rep.setItems(items);
        rep.setTotalSize(results.getTotalSize());
        return rep;
    }


    @GET
    @Path("{perceptionId}")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public DocumentoSunatRepresentation getRepresentation(@PathParam("perceptionId") final String perceptionId) {
        PerceptionProvider perceptionProvider = session.getProvider(PerceptionProvider.class);
        PerceptionModel perception = perceptionProvider.getPerceptionById(organization, perceptionId);
        if (perception == null) {
            throw new NotFoundException("Perception not found");
        }

        return SunatModelToRepresentation.toRepresentation(perception);
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

        JSONObject jsonObject = perception.getXmlAsJSONObject();
        if (jsonObject != null) {
            return Response.ok(jsonObject.toString()).build();
        } else {
            return ErrorResponse.exists("No json attached to current perception");
        }
    }

    @GET
    @Path("{perceptionId}/representation/xml")
    @NoCache
    @Produces("application/xml")
    public Response getPerceptionAsXml(@PathParam("perceptionId") final String perceptionId) {
        PerceptionProvider perceptionProvider = session.getProvider(PerceptionProvider.class);
        PerceptionModel perception = perceptionProvider.getPerceptionById(organization, perceptionId);
        if (perception == null) {
            return ErrorResponse.exists("Perception not found");
        }

        Document document = perception.getXmlAsDocument();
        if (document != null) {
            return Response.ok(document).build();
        } else {
            return ErrorResponse.exists("No xml document attached to current invoice");
        }
    }

    @GET
    @Path("{perceptionId}/report")
    @NoCache
    @Produces("application/pdf")
    public Response getPerceptionAsPdf(
            @PathParam("perceptionId") final String perceptionId,
            @QueryParam("theme") String theme,
            @QueryParam("format") @DefaultValue("pdf") String format) {
        PerceptionProvider perceptionProvider = session.getProvider(PerceptionProvider.class);
        PerceptionModel perception = perceptionProvider.getPerceptionById(organization, perceptionId);
        if (perception == null) {
            return ErrorResponse.exists("Perception not found");
        }

        ExportFormat exportFormat = ExportFormat.valueOf(format.toUpperCase());

        try {
            byte[] reportBytes = session.getProvider(UBLReportProvider.class)
                    .ublModel()
                    .setOrganization(organization)
                    .setThemeName(theme)
                    .getReport(new PerceptionUBLModel(perception), exportFormat);

            ResponseBuilder response = Response.ok(reportBytes);
            switch (exportFormat) {
                case PDF:
                    response.type("application/pdf");
                    response.header("content-disposition", "attachment; filename=\"" + perception.getDocumentId() + ".pdf\"");
                    break;
                case HTML:
                    response.type("application/html");
                    break;
            }

            return response.build();
        } catch (ReportException e) {
            return ErrorResponse.error("Error generating report", Response.Status.INTERNAL_SERVER_ERROR);
        }
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
            URI location = session.getContext().getUri().getAbsolutePathBuilder().path(perception.getId()).build();
            adminEvent.operation(OperationType.DELETE).resourcePath(location.toString()).success();
            return Response.noContent().build();
        } else {
            return ErrorResponse.error("Perception couldn't be deleted", Response.Status.BAD_REQUEST);
        }
    }

    @POST
    @Path("{perceptionId}/send-to-customer")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public SendEventRepresentation sendToCustomer(@PathParam("perceptionId") final String perceptionId) {
        PerceptionProvider perceptionProvider = session.getProvider(PerceptionProvider.class);
        PerceptionModel perception = perceptionProvider.getPerceptionById(organization, perceptionId);
        if (perception == null) {
            throw new NotFoundException("Perception not found");
        }

        SendEventModel sendEvent = perception.addSendEvent(DestinyType.CUSTOMER);

        OpenfactModelUtils.runThreadInTransaction(session.getOpenfactSessionFactory(), sessionThread -> {
            PerceptionManager manager = new PerceptionManager(sessionThread);

            OrganizationModel organizationThread = sessionThread.organizations().getOrganization(organization.getId());
            PerceptionModel perceptionThread = session.getProvider(PerceptionProvider.class).getPerceptionById(organizationThread, perception.getId());
            SendEventModel sendEventThread = perceptionThread.getSendEventById(sendEvent.getId());
            try {
                manager.sendToCustomerParty(organizationThread, perceptionThread, sendEventThread);
            } catch (ModelInsuficientData e) {
                sendEventThread.setResult(SendResultType.ERROR);
                sendEventThread.setDescription(e.getMessage());
            } catch (SendException e) {
                sendEventThread.setResult(SendResultType.ERROR);
                if (e.getMessage() != null) {
                    sendEventThread.setDescription(e.getMessage().length() < 200 ? e.getMessage() : e.getMessage().substring(0, 197).concat("..."));
                } else {
                    sendEventThread.setDescription("Internal Server Error");
                }
                logger.error("Internal Server Error sending to customer", e);
            }
        });

        return ModelToRepresentation.toRepresentation(sendEvent);
    }

    @POST
    @Path("{perceptionId}/send-to-third-party")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public SendEventRepresentation sendToThridParty(@PathParam("perceptionId") final String perceptionId) {
        PerceptionProvider perceptionProvider = session.getProvider(PerceptionProvider.class);
        PerceptionModel perception = perceptionProvider.getPerceptionById(organization, perceptionId);
        if (perception == null) {
            throw new NotFoundException("Perception not found");
        }

        SendEventModel sendEvent = perception.addSendEvent(DestinyType.THIRD_PARTY);

        OpenfactModelUtils.runThreadInTransaction(session.getOpenfactSessionFactory(), sessionThread -> {
            PerceptionManager manager = new PerceptionManager(sessionThread);

            OrganizationModel organizationThread = sessionThread.organizations().getOrganization(organization.getId());
            PerceptionModel perceptionThread = session.getProvider(PerceptionProvider.class).getPerceptionById(organizationThread, perception.getId());
            SendEventModel sendEventThread = perceptionThread.getSendEventById(sendEvent.getId());
            try {
                manager.sendToTrirdParty(organizationThread, perceptionThread, sendEventThread);
            } catch (ModelInsuficientData e) {
                sendEventThread.setResult(SendResultType.ERROR);
                sendEventThread.setDescription(e.getMessage());
            } catch (SendException e) {
                sendEventThread.setResult(SendResultType.ERROR);
                if (e.getMessage() != null) {
                    sendEventThread.setDescription(e.getMessage().length() < 200 ? e.getMessage() : e.getMessage().substring(0, 197).concat("..."));
                } else {
                    sendEventThread.setDescription("Internal Server Error");
                }
                logger.error("Internal Server Error sending to third party", e);
            }
        });

        return ModelToRepresentation.toRepresentation(sendEvent);
    }

    @POST
    @Path("{perceptionId}/send-to-third-party-by-email")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public SendEventRepresentation sendToThridParty(
            @PathParam("perceptionId") final String perceptionId,
            ThirdPartyEmailRepresentation thirdParty) {
        PerceptionProvider perceptionProvider = session.getProvider(PerceptionProvider.class);
        PerceptionModel perception = perceptionProvider.getPerceptionById(organization, perceptionId);
        if (perception == null) {
            throw new NotFoundException("Perception not found");
        }

        if (thirdParty == null || thirdParty.getEmail() == null) {
            throw new BadRequestException("Invalid email sended");
        }

        SendEventModel sendEvent = perception.addSendEvent(DestinyType.THIRD_PARTY_BY_EMAIL);

        OpenfactModelUtils.runThreadInTransaction(session.getOpenfactSessionFactory(), sessionThread -> {
            PerceptionManager manager = new PerceptionManager(sessionThread);

            OrganizationModel organizationThread = sessionThread.organizations().getOrganization(organization.getId());
            PerceptionModel perceptionThread = session.getProvider(PerceptionProvider.class).getPerceptionById(organizationThread, perception.getId());
            SendEventModel sendEventThread = perceptionThread.getSendEventById(sendEvent.getId());
            try {
                manager.sendToThirdPartyByEmail(organizationThread, perceptionThread, thirdParty.getEmail());
            } catch (ModelInsuficientData e) {
                sendEventThread.setResult(SendResultType.ERROR);
                sendEventThread.setDescription(e.getMessage());
            } catch (SendException e) {
                sendEventThread.setResult(SendResultType.ERROR);
                if (e.getMessage() != null) {
                    sendEventThread.setDescription(e.getMessage().length() < 200 ? e.getMessage() : e.getMessage().substring(0, 197).concat("..."));
                } else {
                    sendEventThread.setDescription("Internal Server Error");
                }
                logger.error("Internal Server Error sending to customer", e);
            }
        });

        return ModelToRepresentation.toRepresentation(sendEvent);
    }

    @GET
    @Path("{perceptionId}/send-events")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public List<SendEventRepresentation> getSendEvents(
            @PathParam("perceptionId") final String perceptionId,
            @QueryParam("destinyType") String destinyType,
            @QueryParam("type") String type,
            @QueryParam("result") String result,
            @QueryParam("first") Integer firstResult,
            @QueryParam("max") Integer maxResults) {
        PerceptionProvider perceptionProvider = session.getProvider(PerceptionProvider.class);
        PerceptionModel perception = perceptionProvider.getPerceptionById(organization, perceptionId);
        if (perception == null) {
            throw new NotFoundException("Perception not found");
        }

        firstResult = firstResult != null ? firstResult : -1;
        maxResults = maxResults != null ? maxResults : Constants.DEFAULT_MAX_RESULTS;

        List<SendEventModel> sendEventModels;
        if (destinyType != null || type != null || result != null) {
            Map<String, String> attributes = new HashMap<>();
            if (destinyType != null) {
                attributes.put(PerceptionModel.SEND_EVENT_DESTINY_TYPE, destinyType);
            }
            if (type != null) {
                attributes.put(PerceptionModel.SEND_EVENT_TYPE, type);
            }
            if (result != null) {
                attributes.put(PerceptionModel.SEND_EVENT_RESULT, result);
            }
            sendEventModels = perception.searchForSendEvent(attributes, firstResult, maxResults);
        } else {
            sendEventModels = perception.getSendEvents(firstResult, maxResults);
        }

        return sendEventModels.stream()
                .map(f -> ModelToRepresentation.toRepresentation(f))
                .collect(Collectors.toList());
    }

    @GET
    @Path("{perceptionId}/representation/cdr")
    @NoCache
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getCdr(@PathParam("perceptionId") final String perceptionId) {
        PerceptionProvider perceptionProvider = session.getProvider(PerceptionProvider.class);
        PerceptionModel perception = perceptionProvider.getPerceptionById(organization, perceptionId);
        if (perception == null) {
            throw new NotFoundException("Perception not found");
        }

        Map<String, String> params = new HashMap<>();
        params.put(PerceptionModel.SEND_EVENT_DESTINY_TYPE, DestinyType.THIRD_PARTY.toString());
        params.put(PerceptionModel.SEND_EVENT_RESULT, SendResultType.SUCCESS.toString());

        List<SendEventModel> sendEvents = perception.searchForSendEvent(params, 0, 2);
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