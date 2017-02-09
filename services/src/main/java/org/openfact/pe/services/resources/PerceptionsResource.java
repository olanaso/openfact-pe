package org.openfact.pe.services.resources;

import org.apache.commons.io.IOUtils;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.openfact.common.converts.DocumentUtils;
import org.openfact.events.admin.OperationType;
import org.openfact.models.*;
import org.openfact.models.enums.DestinyType;
import org.openfact.models.enums.SendEventStatus;
import org.openfact.models.utils.ModelToRepresentation;
import org.openfact.pe.models.SunatSendException;
import org.openfact.pe.models.enums.SunatDocumentType;
import org.openfact.pe.models.types.perception.PerceptionType;
import org.openfact.pe.models.utils.SunatDocumentToType;
import org.openfact.pe.models.utils.SunatRepresentationToType;
import org.openfact.pe.representations.idm.DocumentoSunatRepresentation;
import org.openfact.pe.services.managers.SunatDocumentManager;
import org.openfact.services.ErrorResponse;
import org.openfact.services.resources.admin.AdminEventBuilder;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;

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

                SunatDocumentManager sunatDocumentManager = new SunatDocumentManager(session);

                // Double-check duplicated documentId
                if (perceptionType.getId() != null && sunatDocumentManager.getDocumentByTypeAndDocumentId(SunatDocumentType.PERCEPTION.toString(), perceptionType.getId().getValue(), organization) != null) {
                    throw new ModelDuplicateException("Perception exists with same documentId[" + perceptionType.getId().getValue() + "]");
                }

                DocumentModel documentModel = sunatDocumentManager.addPerception(organization, perceptionType);

                URI location = session.getContext().getUri().getAbsolutePathBuilder().path(documentModel.getId()).build();
                adminEvent.operation(OperationType.CREATE).resourcePath(location.toString(), documentModel.getId()).representation(perceptionType).success();
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
        SunatDocumentManager sunatDocumentManager = new SunatDocumentManager(session);

        if (rep.getSerieDocumento() != null || rep.getNumeroDocumento() != null) {
            if (rep.getSerieDocumento() != null && rep.getNumeroDocumento() != null) {
                if (sunatDocumentManager.getDocumentByTypeAndDocumentId(SunatDocumentType.PERCEPTION.toString(), rep.getSerieDocumento() + "-" + rep.getNumeroDocumento(), organization) != null) {
                    throw new ModelDuplicateException("Perception exists with same documentId[" + rep.getSerieDocumento() + "-" + rep.getNumeroDocumento() + "]");
                }
            } else {
                return ErrorResponse.error("Numero de serie y/o numero invalido", Response.Status.BAD_REQUEST);
            }
        }

        try {
            PerceptionType perceptionType = SunatRepresentationToType.toPerceptionType(session, organization, rep);
            DocumentModel documentModel = sunatDocumentManager.addPerception(organization, perceptionType);

            if (rep.isEnviarAutomaticamenteASunat()) {
                try {
                    sunatDocumentManager.sendToThirdParty(organization, documentModel);
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
                SendEventModel customerSendEvent = documentModel.addSendEvent(DestinyType.CUSTOMER);
                try {
                    sunatDocumentManager.sendToCustomerParty(organization, documentModel, customerSendEvent);
                } catch (ModelInsuficientData e) {
                    customerSendEvent.setResult(SendEventStatus.ERROR);
                    customerSendEvent.setDescription(e.getMessage());
                } catch (SendException e) {
                    customerSendEvent.setResult(SendEventStatus.ERROR);
                    customerSendEvent.setDescription("Internal server error");
                    logger.error("Internal Server Error sending to customer", e);
                }
            }

            URI location = session.getContext().getUri().getAbsolutePathBuilder().path(documentModel.getId()).build();
            adminEvent.operation(OperationType.CREATE).resourcePath(location.toString()).representation(rep).success();
            return Response.created(location).entity(ModelToRepresentation.toRepresentation(documentModel)).build();
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

}