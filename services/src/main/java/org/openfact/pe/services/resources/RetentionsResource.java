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
import org.openfact.pe.models.*;
import org.openfact.pe.models.types.retention.RetentionType;
import org.openfact.pe.models.types.retention.RetentionType;
import org.openfact.pe.models.utils.SunatDocumentToType;
import org.openfact.pe.models.utils.SunatModelToRepresentation;
import org.openfact.pe.models.utils.SunatRepresentationToType;
import org.openfact.pe.representations.idm.DocumentoSunatRepresentation;
import org.openfact.pe.services.managers.RetentionManager;
import org.openfact.pe.services.managers.RetentionManager;
import org.openfact.report.ExportFormat;
import org.openfact.report.ReportException;
import org.openfact.representations.idm.SendEventRepresentation;
import org.openfact.representations.idm.ThirdPartyEmailRepresentation;
import org.openfact.representations.idm.search.SearchCriteriaRepresentation;
import org.openfact.representations.idm.search.SearchResultsRepresentation;
import org.openfact.services.ErrorResponse;
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
public class RetentionsResource {

    protected static final Logger logger = Logger.getLogger(RetentionsResource.class);

    private final OpenfactSession session;
    private final OrganizationModel organization;
    private final AdminEventBuilder adminEvent;

    @Context
    protected UriInfo uriInfo;

    public RetentionsResource(OpenfactSession session, OrganizationModel organization, AdminEventBuilder adminEvent) {
        this.session = session;
        this.organization = organization;
        this.adminEvent = adminEvent;
    }

    @GET
    @Path("")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public List<DocumentoSunatRepresentation> getRetentions(
            @QueryParam("filterText") String filterText,
            @QueryParam("documentId") String documentId,
            @QueryParam("first") Integer firstResult,
            @QueryParam("max") Integer maxResults) {
        firstResult = firstResult != null ? firstResult : -1;
        maxResults = maxResults != null ? maxResults : Constants.DEFAULT_MAX_RESULTS;

        List<RetentionModel> retentionModels;
        if (filterText != null) {
            retentionModels = session.getProvider(RetentionProvider.class).searchForRetention(organization, filterText.trim(), firstResult, maxResults);
        } else if (documentId != null) {
            Map<String, String> attributes = new HashMap<>();
            if (documentId != null) {
                attributes.put(InvoiceModel.DOCUMENT_ID, documentId);
            }
            retentionModels = session.getProvider(RetentionProvider.class).searchForRetention(attributes, organization, firstResult, maxResults);
        } else {
            retentionModels = session.getProvider(RetentionProvider.class).getRetentions(organization, firstResult, maxResults);
        }

        return retentionModels.stream()
                .map(f -> SunatModelToRepresentation.toRepresentation(f))
                .collect(Collectors.toList());
    }

    @POST
    @Path("upload")
    @Consumes("multipart/form-data")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRetention(final MultipartFormDataInput input) {

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get("file");

        for (InputPart inputPart : inputParts) {
            try {
                InputStream inputStream = inputPart.getBody(InputStream.class, null);
                byte[] bytes = IOUtils.toByteArray(inputStream);

                RetentionType retentionType = SunatDocumentToType.toRetentionType(DocumentUtils.byteToDocument(bytes));

                if (retentionType == null) {
                    throw new IOException("Invalid retention Xml");
                }

                RetentionManager retentionManager = new RetentionManager(session);

                // Double-check duplicated documentId
                if (retentionType.getId() != null && retentionManager.getRetentionByDocumentId(organization, retentionType.getId().getValue()) != null) {
                    throw new ModelDuplicateException("Retention exists with same documentId[" + retentionType.getId().getValue() + "]");
                }

                RetentionModel retention = retentionManager.addRetention(organization, retentionType);

                URI location = session.getContext().getUri().getAbsolutePathBuilder().path(retention.getId()).build();
                adminEvent.operation(OperationType.CREATE).resourcePath(location.toString(), retention.getId()).representation(retentionType).success();
                return Response.created(location).entity(SunatModelToRepresentation.toRepresentation(retention)).build();
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
                return ErrorResponse.exists("Could not create retention");
            } catch (Exception e) {
                if (session.getTransactionManager().isActive()) {
                    session.getTransactionManager().setRollbackOnly();
                }
                return ErrorResponse.exists("Could not create retention");
            }
        }

        return Response.ok().build();
    }

    @POST
    @Path("")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRetention(DocumentoSunatRepresentation rep) {
        RetentionManager retentionManager = new RetentionManager(session);

        if (rep.getSerieDocumento() != null || rep.getNumeroDocumento() != null) {
            if (rep.getSerieDocumento() != null && rep.getNumeroDocumento() != null) {
                if (retentionManager.getRetentionByDocumentId(organization, rep.getSerieDocumento() + "-" + rep.getNumeroDocumento()) != null) {
                    return ErrorResponse.exists("Invoice exists with same documentId");
                }
            } else {
                return ErrorResponse.error("Numero de serie y/o numero invalido", Response.Status.BAD_REQUEST);
            }
        }

        try {
            RetentionType retentionType = SunatRepresentationToType.toRetentionType(session, organization, rep);
            RetentionModel retentionModel = retentionManager.addRetention(organization, retentionType);

            if (rep.isEnviarAutomaticamenteASunat()) {
                SendEventModel thirdPartySendEvent = retentionModel.addSendEvent(DestinyType.THIRD_PARTY);
                try {
                    retentionManager.sendToTrirdParty(organization, retentionModel, thirdPartySendEvent);
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
                SendEventModel customerSendEvent = retentionModel.addSendEvent(DestinyType.CUSTOMER);
                try {
                    retentionManager.sendToCustomerParty(organization, retentionModel, customerSendEvent);
                } catch (ModelInsuficientData e) {
                    customerSendEvent.setResult(SendResultType.ERROR);
                    customerSendEvent.setDescription(e.getMessage());
                } catch (SendException e) {
                    customerSendEvent.setResult(SendResultType.ERROR);
                    customerSendEvent.setDescription("Internal server error");
                    logger.error("Internal Server Error sending to customer", e);
                }
            }

            URI location = session.getContext().getUri().getAbsolutePathBuilder().path(retentionModel.getId()).build();
            adminEvent.operation(OperationType.CREATE).resourcePath(location.toString()).representation(rep).success();
            return Response.created(location).entity(SunatModelToRepresentation.toRepresentation(retentionModel)).build();
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
        }
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
        results.getModels().forEach(f -> items.add(SunatModelToRepresentation.toRepresentation(f)));
        rep.setItems(items);
        rep.setTotalSize(results.getTotalSize());
        return rep;
    }


    @GET
    @Path("{retentionId}")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public DocumentoSunatRepresentation getRepresentation(@PathParam("retentionId") final String retentionId) {
        RetentionProvider retentionProvider = session.getProvider(RetentionProvider.class);
        RetentionModel retention = retentionProvider.getRetentionById(organization, retentionId);
        if (retention == null) {
            throw new NotFoundException("Retention not found");
        }

        return SunatModelToRepresentation.toRepresentation(retention);
    }

    @GET
    @Path("{retentionId}/representation/json")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRetentionAsJson(@PathParam("retentionId") final String retentionId) {
        RetentionProvider retentionProvider = session.getProvider(RetentionProvider.class);
        RetentionModel retention = retentionProvider.getRetentionById(organization, retentionId);
        if (retention == null) {
            return ErrorResponse.exists("Retention not found");
        }

        JSONObject jsonObject = retention.getXmlAsJSONObject();
        if (jsonObject != null) {
            return Response.ok(jsonObject.toString()).build();
        } else {
            return ErrorResponse.exists("No json attached to current retention");
        }
    }

    @GET
    @Path("{retentionId}/representation/xml")
    @NoCache
    @Produces("application/xml")
    public Response getRetentionAsXml(@PathParam("retentionId") final String retentionId) {
        RetentionProvider retentionProvider = session.getProvider(RetentionProvider.class);
        RetentionModel retention = retentionProvider.getRetentionById(organization, retentionId);
        if (retention == null) {
            return ErrorResponse.exists("Retention not found");
        }

        Document document = retention.getXmlAsDocument();
        if (document != null) {
            return Response.ok(document).build();
        } else {
            return ErrorResponse.exists("No xml document attached to current invoice");
        }
    }

    @GET
    @Path("{retentionId}/report")
    @NoCache
    @Produces("application/pdf")
    public Response getRetentionAsPdf(
            @PathParam("retentionId") final String retentionId,
            @QueryParam("theme") String theme,
            @QueryParam("format") @DefaultValue("pdf") String format) {
        RetentionProvider retentionProvider = session.getProvider(RetentionProvider.class);
        RetentionModel retention = retentionProvider.getRetentionById(organization, retentionId);
        if (retention == null) {
            return ErrorResponse.exists("Retention not found");
        }

        ExportFormat exportFormat = ExportFormat.valueOf(format.toUpperCase());

        try {
            byte[] reportBytes = session.getProvider(UBLReportProvider.class)
                    .ublModel()
                    .setOrganization(organization)
                    .setThemeName(theme)
                    .getReport(new RetentionUBLModel(retention), exportFormat);

            ResponseBuilder response = Response.ok(reportBytes);
            switch (exportFormat) {
                case PDF:
                    response.type("application/pdf");
                    response.header("content-disposition", "attachment; filename=\"" + retention.getDocumentId() + ".pdf\"");
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
    @Path("{retentionId}")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRetention(@PathParam("retentionId") final String retentionId) {
        RetentionProvider retentionProvider = session.getProvider(RetentionProvider.class);
        RetentionModel retention = retentionProvider.getRetentionById(organization, retentionId);
        if (retention == null) {
            return ErrorResponse.exists("Retention not found");
        }

        boolean removed = new RetentionManager(session).removeRetention(organization, retention);
        if (removed) {
            URI location = session.getContext().getUri().getAbsolutePathBuilder().path(retention.getId()).build();
            adminEvent.operation(OperationType.DELETE).resourcePath(location.toString()).success();
            return Response.noContent().build();
        } else {
            return ErrorResponse.error("Retention couldn't be deleted", Response.Status.BAD_REQUEST);
        }
    }

    @POST
    @Path("{retentionId}/send-to-customer")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public SendEventRepresentation sendToCustomer(@PathParam("retentionId") final String retentionId) {
        RetentionProvider retentionProvider = session.getProvider(RetentionProvider.class);
        RetentionModel retention = retentionProvider.getRetentionById(organization, retentionId);
        if (retention == null) {
            throw new NotFoundException("Retention not found");
        }

        SendEventModel sendEvent = retention.addSendEvent(DestinyType.CUSTOMER);

        OpenfactModelUtils.runThreadInTransaction(session.getOpenfactSessionFactory(), sessionThread -> {
            RetentionManager manager = new RetentionManager(sessionThread);

            OrganizationModel organizationThread = sessionThread.organizations().getOrganization(organization.getId());
            RetentionModel retentionThread = session.getProvider(RetentionProvider.class).getRetentionById(organizationThread, retention.getId());
            SendEventModel sendEventThread = retentionThread.getSendEventById(sendEvent.getId());
            try {
                manager.sendToCustomerParty(organizationThread, retentionThread, sendEventThread);
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
    @Path("{retentionId}/send-to-third-party")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public SendEventRepresentation sendToThridParty(@PathParam("retentionId") final String retentionId) {
        RetentionProvider retentionProvider = session.getProvider(RetentionProvider.class);
        RetentionModel retention = retentionProvider.getRetentionById(organization, retentionId);
        if (retention == null) {
            throw new NotFoundException("Retention not found");
        }

        SendEventModel sendEvent = retention.addSendEvent(DestinyType.THIRD_PARTY);

        OpenfactModelUtils.runThreadInTransaction(session.getOpenfactSessionFactory(), sessionThread -> {
            RetentionManager manager = new RetentionManager(sessionThread);

            OrganizationModel organizationThread = sessionThread.organizations().getOrganization(organization.getId());
            RetentionModel retentionThread = session.getProvider(RetentionProvider.class).getRetentionById(organizationThread, retention.getId());
            SendEventModel sendEventThread = retentionThread.getSendEventById(sendEvent.getId());
            try {
                manager.sendToTrirdParty(organizationThread, retentionThread, sendEventThread);
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
    @Path("{retentionId}/send-to-third-party-by-email")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public SendEventRepresentation sendToThridParty(
            @PathParam("retentionId") final String retentionId,
            ThirdPartyEmailRepresentation thirdParty) {
        RetentionProvider retentionProvider = session.getProvider(RetentionProvider.class);
        RetentionModel retention = retentionProvider.getRetentionById(organization, retentionId);
        if (retention == null) {
            throw new NotFoundException("Retention not found");
        }

        if (thirdParty == null || thirdParty.getEmail() == null) {
            throw new BadRequestException("Invalid email sended");
        }

        SendEventModel sendEvent = retention.addSendEvent(DestinyType.THIRD_PARTY_BY_EMAIL);

        OpenfactModelUtils.runThreadInTransaction(session.getOpenfactSessionFactory(), sessionThread -> {
            RetentionManager manager = new RetentionManager(sessionThread);

            OrganizationModel organizationThread = sessionThread.organizations().getOrganization(organization.getId());
            RetentionModel retentionThread = session.getProvider(RetentionProvider.class).getRetentionById(organizationThread, retention.getId());
            SendEventModel sendEventThread = retentionThread.getSendEventById(sendEvent.getId());
            try {
                manager.sendToThirdPartyByEmail(organizationThread, retentionThread, thirdParty.getEmail());
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
    @Path("{retentionId}/send-events")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public List<SendEventRepresentation> getSendEvents(
            @PathParam("retentionId") final String retentionId,
            @QueryParam("destinyType") String destinyType,
            @QueryParam("type") String type,
            @QueryParam("result") String result,
            @QueryParam("first") Integer firstResult,
            @QueryParam("max") Integer maxResults) {
        RetentionProvider retentionProvider = session.getProvider(RetentionProvider.class);
        RetentionModel retention = retentionProvider.getRetentionById(organization, retentionId);
        if (retention == null) {
            throw new NotFoundException("Retention not found");
        }

        firstResult = firstResult != null ? firstResult : -1;
        maxResults = maxResults != null ? maxResults : Constants.DEFAULT_MAX_RESULTS;

        List<SendEventModel> sendEventModels;
        if (destinyType != null || type != null || result != null) {
            Map<String, String> attributes = new HashMap<>();
            if (destinyType != null) {
                attributes.put(RetentionModel.SEND_EVENT_DESTINY_TYPE, destinyType);
            }
            if (type != null) {
                attributes.put(RetentionModel.SEND_EVENT_TYPE, type);
            }
            if (result != null) {
                attributes.put(RetentionModel.SEND_EVENT_RESULT, result);
            }
            sendEventModels = retention.searchForSendEvent(attributes, firstResult, maxResults);
        } else {
            sendEventModels = retention.getSendEvents(firstResult, maxResults);
        }

        return sendEventModels.stream()
                .map(f -> ModelToRepresentation.toRepresentation(f))
                .collect(Collectors.toList());
    }

    @GET
    @Path("{retentionId}/representation/cdr")
    @NoCache
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getCdr(@PathParam("retentionId") final String retentionId) {
        RetentionProvider retentionProvider = session.getProvider(RetentionProvider.class);
        RetentionModel retention = retentionProvider.getRetentionById(organization, retentionId);
        if (retention == null) {
            throw new NotFoundException("Retention not found");
        }

        Map<String, String> params = new HashMap<>();
        params.put(RetentionModel.SEND_EVENT_DESTINY_TYPE, DestinyType.THIRD_PARTY.toString());
        params.put(RetentionModel.SEND_EVENT_RESULT, SendResultType.SUCCESS.toString());

        List<SendEventModel> sendEvents = retention.searchForSendEvent(params, 0, 2);
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