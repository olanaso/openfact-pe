package org.openfact.pe.services.resources.admin;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.IDType;
import oasis.names.specification.ubl.schema.xsd.creditnote_21.CreditNoteType;
import oasis.names.specification.ubl.schema.xsd.debitnote_21.DebitNoteType;
import oasis.names.specification.ubl.schema.xsd.invoice_21.InvoiceType;
import org.apache.commons.io.IOUtils;
import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.openfact.Config;
import org.openfact.common.ClientConnection;
import org.openfact.events.admin.OperationType;
import org.openfact.events.admin.ResourceType;
import org.openfact.models.*;
import org.openfact.models.types.DestinyType;
import org.openfact.models.types.DocumentType;
import org.openfact.models.types.InternetMediaType;
import org.openfact.models.types.SendEventStatus;
import org.openfact.models.utils.ModelToRepresentation;
import org.openfact.pe.models.SunatSendEventException;
import org.openfact.pe.models.utils.SunatRepresentationToType;
import org.openfact.pe.models.utils.SunatTypeToModel;
import org.openfact.pe.representations.idm.DocumentRepresentation;
import org.openfact.pe.representations.idm.DocumentoSunatRepresentation;
import org.openfact.pe.representations.idm.VoidedRepresentation;
import org.openfact.pe.ubl.types.SunatDocumentType;
import org.openfact.pe.ubl.types.SunatRequiredAction;
import org.openfact.pe.ubl.ubl21.perception.PerceptionType;
import org.openfact.pe.ubl.ubl21.retention.RetentionType;
import org.openfact.pe.ubl.ubl21.voided.VoidedDocumentsType;
import org.openfact.pe.ws.sunat.SunatSenderManager;
import org.openfact.services.ErrorResponse;
import org.openfact.services.ModelErrorResponseException;
import org.openfact.services.managers.DocumentManager;
import org.openfact.services.managers.EventStoreManager;
import org.openfact.services.managers.OrganizationManager;
import org.openfact.services.resource.security.OrganizationAuth;
import org.openfact.services.resource.security.Resource;
import org.openfact.services.resource.security.SecurityContextProvider;
import org.openfact.services.resources.admin.AdminEventBuilder;
import org.openfact.ubl.UBLIDGenerator;
import org.openfact.ubl.UBLReaderWriter;
import org.openfact.ubl.utils.UBLUtil;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Stateless
@Consumes(MediaType.APPLICATION_JSON)
public class SunatDocumentsAdminResource {

    private static final String UPLOAD_FILE_NAME = "file";

    private static final Logger logger = Logger.getLogger(SunatDocumentsAdminResource.class);

    @Context
    protected UriInfo uriInfo;

    @Context
    protected OpenfactSession session;

    @Context
    protected ClientConnection clientConnection;

    @Inject
    private DocumentManager documentManager;

    @Inject
    private OrganizationManager organizationManager;

    @Inject
    private EventStoreManager eventStoreManager;

    @Inject
    private SecurityContextProvider securityContext;

    @Inject
    private FileProvider fileProvider;

    @Inject
    private UBLUtil ublUtil;

    @Inject
    private SunatRepresentationToType representationToType;

    @Inject
    private ModelToRepresentation modelToRepresentation;

    @Inject
    private SunatSenderManager sunatSenderManager;

    @Inject
    private SunatResourceManager sunatResourceManager;

    private OrganizationModel getOrganizationModel() {
        String organizationName = session.getContext().getOrganization().getName();
        OrganizationModel organization = organizationManager.getOrganizationByName(organizationName);
        if (organization == null) {
            throw new NotFoundException("Organization " + organizationName + " not found.");
        }
        return organization;
    }

    private boolean validateOrganizationInfo(OrganizationModel organization) {
        if (organization.getAdditionalAccountId() == null || organization.getAdditionalAccountId().trim().isEmpty()
                || organization.getAssignedIdentificationId() == null || organization.getAssignedIdentificationId().trim().isEmpty()
                || organization.getRegistrationName() == null || organization.getRegistrationName().trim().isEmpty()
                || organization.getSupplierName() == null || organization.getSupplierName().trim().isEmpty()
                || organization.getStreetName() == null || organization.getStreetName().trim().isEmpty()
                || organization.getCitySubdivisionName() == null || organization.getCitySubdivisionName().trim().isEmpty()
                || organization.getCityName() == null || organization.getCityName().trim().isEmpty()
                || organization.getCountrySubentity() == null || organization.getCountrySubentity().trim().isEmpty()
                || organization.getDistrict() == null || organization.getDistrict().trim().isEmpty()
                || organization.getCountryIdentificationCode() == null || organization.getCountryIdentificationCode().trim().isEmpty()) {
            return false;
        }
        return true;
    }

    private DocumentModel getDocument(String documentIdPk, OrganizationModel organization) {
        DocumentModel document = documentManager.getDocumentById(documentIdPk, organization);
        if (document == null) {
            throw new NotFoundException("Document not found.");
        }
        return document;
    }

    private OrganizationAuth getAuth(OrganizationModel organization) {
        List<OrganizationModel> permittedOrganizations = securityContext.getPermittedOrganizations(session);
        if (!securityContext.getClientUser(session).hasAppRole(AdminRoles.ADMIN) && !permittedOrganizations.contains(organization)) {
            throw new ForbiddenException();
        }
        return securityContext.getClientUser(session).organizationAuth(Resource.DOCUMENT);
    }

    private AdminEventBuilder getAdminEvent(OrganizationModel organization) {
        return new AdminEventBuilder(organization, securityContext.getClientUser(session), session, clientConnection).resource(ResourceType.DOCUMENT);
    }

    private void checkSerieAndNumber(String serie, String numero, String documentType, OrganizationModel organization) throws ModelErrorResponseException {
        if (serie != null || numero != null) {
            if (serie != null && numero != null) {
                // Double-check duplicated documentId
                if (documentManager.getDocumentByTypeAndDocumentId(documentType, serie + "-" + numero, organization) != null) {
                    throw new ModelErrorResponseException("Document exists with same documentId", Response.Status.CONFLICT);
                }
            } else {
                throw new ModelErrorResponseException("Invalid number and/or serie", Response.Status.BAD_REQUEST);
            }
        }
    }

    private List<InputPart> getInputs(MultipartFormDataInput multipartFormDataInput) {
        Map<String, List<InputPart>> uploadForm = multipartFormDataInput.getFormDataMap();
        return uploadForm.get(UPLOAD_FILE_NAME);
    }

    private <T> T readFile(InputPart inputPart, String documentType, Class<T> tClass) throws IOException {
        InputStream inputStream = inputPart.getBody(InputStream.class, null);
        byte[] bytes = IOUtils.toByteArray(inputStream);

        String provider = Config.scope(documentType.toLowerCase()).get("provider");
        UBLReaderWriter<T> readerWriter = (UBLReaderWriter<T>) ublUtil.getReaderWriter(provider, documentType).reader();
        T t = readerWriter.reader().read(bytes);
        if (t == null) {
            throw new ModelRuntimeException("Could not read file");
        }
        return t;
    }

    private void setPerceptionIDType(OrganizationModel organization, final PerceptionType perceptionType) {
        IDType documentId = perceptionType.getId();
        if (documentId == null || documentId.getValue() == null) {
            UBLIDGenerator ublIDGenerator = ublUtil.getIDGenerator(SunatDocumentType.PERCEPTION.toString());
            String newDocumentId = ublIDGenerator.generateID(organization, perceptionType);
            documentId = new IDType(newDocumentId);
            perceptionType.setId(documentId);
        }
    }

    private void setRetentionIDType(OrganizationModel organization, final RetentionType retentionType) {
        IDType documentId = retentionType.getId();
        if (documentId == null || documentId.getValue() == null) {
            UBLIDGenerator ublIDGenerator = ublUtil.getIDGenerator(SunatDocumentType.RETENTION.toString());
            String newDocumentId = ublIDGenerator.generateID(organization, retentionType);
            documentId = new IDType(newDocumentId);
            retentionType.setId(documentId);
        }
    }

    @POST
    @Path("/documents/invoices")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createInvoice(@QueryParam("async") @DefaultValue("false") Boolean async, @Valid final DocumentRepresentation rep) throws ModelErrorResponseException, ModelException {
        OrganizationModel organization = getOrganizationModel();
        OrganizationAuth auth = getAuth(organization);

        auth.requireManage();
        checkSerieAndNumber(rep.getSerie(), rep.getNumero(), DocumentType.INVOICE.toString(), organization);

        if (!validateOrganizationInfo(organization)) {
            throw new ModelErrorResponseException("Datos de organizacion insuficientes", Response.Status.BAD_REQUEST);
        }

        try {
            DocumentModel document = sunatResourceManager.createInvoice(organization, rep);

            try {
                if (rep.isEnviarAutomaticamenteASunat()) {
                    Future<Boolean> future = sunatResourceManager.sendDocumentToThirdParty(organization.getId(), document.getId());
                    if (!async) {
                        future.get();
                    }
                }
                if (rep.isEnviarAutomaticamenteAlCliente()) {
                    Future<Boolean> future = sunatResourceManager.sendDocumentToCustomer(organization.getId(), document.getId());
                    if (!async) {
                        future.get();
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                logger.error(e);
            }

            URI location = uriInfo.getAbsolutePathBuilder().path(document.getId()).build();
            eventStoreManager.send(organization, getAdminEvent(organization)
                    .operation(OperationType.CREATE)
                    .resourcePath(uriInfo, document.getId())
                    .representation(rep)
                    .getEvent());
            return Response.created(location).entity(modelToRepresentation.toRepresentation(document, false)).build();
        } catch (ModelDuplicateException e) {
            throw new ModelErrorResponseException("Invoice exists with same documentId", Response.Status.CONFLICT);
        } catch (ModelException e) {
            throw new ModelErrorResponseException("Invoice could not be created", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @POST
    @Path("/documents/credit-notes")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCreditNote(@QueryParam("async") @DefaultValue("false") Boolean async, @Valid final DocumentRepresentation rep) throws ModelErrorResponseException {
        OrganizationModel organization = getOrganizationModel();
        OrganizationAuth auth = getAuth(organization);

        auth.requireManage();
        checkSerieAndNumber(rep.getSerie(), rep.getNumero(), DocumentType.CREDIT_NOTE.toString(), organization);

        if (!validateOrganizationInfo(organization)) {
            throw new ModelErrorResponseException("Datos de organizacion insuficientes", Response.Status.BAD_REQUEST);
        }

        try {
            DocumentModel document = sunatResourceManager.createCreditNote(organization, rep);

            try {
                if (rep.isEnviarAutomaticamenteASunat()) {
                    Future<Boolean> future = sunatResourceManager.sendDocumentToThirdParty(organization.getId(), document.getId());
                    if (!async) {
                        future.get();
                    }
                }
                if (rep.isEnviarAutomaticamenteAlCliente()) {
                    Future<Boolean> future = sunatResourceManager.sendDocumentToCustomer(organization.getId(), document.getId());
                    if (!async) {
                        future.get();
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                logger.error(e);
            }

            URI location = uriInfo.getAbsolutePathBuilder().path(document.getId()).build();
            eventStoreManager.send(organization, getAdminEvent(organization)
                    .operation(OperationType.CREATE)
                    .resourcePath(uriInfo, document.getId())
                    .representation(rep)
                    .getEvent());
            return Response.created(location).entity(modelToRepresentation.toRepresentation(document, false)).build();
        } catch (ModelDuplicateException e) {
            throw new ModelErrorResponseException("Credit note exists with same documentId", Response.Status.CONFLICT);
        } catch (ModelException e) {
            throw new ModelErrorResponseException("Credit note could not be created", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @POST
    @Path("/documents/debit-notes")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createDebitNote(@QueryParam("async") @DefaultValue("false") Boolean async, @Valid final DocumentRepresentation rep) throws ModelErrorResponseException {
        OrganizationModel organization = getOrganizationModel();
        OrganizationAuth auth = getAuth(organization);

        auth.requireManage();
        checkSerieAndNumber(rep.getSerie(), rep.getNumero(), DocumentType.DEBIT_NOTE.toString(), organization);

        if (!validateOrganizationInfo(organization)) {
            throw new ModelErrorResponseException("Datos de organizacion insuficientes", Response.Status.BAD_REQUEST);
        }

        try {
            DocumentModel document = sunatResourceManager.createDebitNote(organization, rep);

            try {
                if (rep.isEnviarAutomaticamenteASunat()) {
                    Future<Boolean> future = sunatResourceManager.sendDocumentToThirdParty(organization.getId(), document.getId());
                    if (!async) {
                        future.get();
                    }
                }
                if (rep.isEnviarAutomaticamenteAlCliente()) {
                    Future<Boolean> future = sunatResourceManager.sendDocumentToCustomer(organization.getId(), document.getId());
                    if (!async) {
                        future.get();
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                logger.error(e);
            }

            URI location = uriInfo.getAbsolutePathBuilder().path(document.getId()).build();
            eventStoreManager.send(organization, getAdminEvent(organization)
                    .operation(OperationType.CREATE)
                    .resourcePath(uriInfo, document.getId())
                    .representation(rep)
                    .getEvent());
            return Response.created(location).entity(modelToRepresentation.toRepresentation(document, false)).build();
        } catch (ModelDuplicateException e) {
            throw new ModelErrorResponseException("Debit note exists with same documentId", Response.Status.CONFLICT);
        } catch (ModelException e) {
            throw new ModelErrorResponseException("Debit note could not be created", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @POST
    @Path("/documents/extensions/upload/perceptions")
    @Consumes("multipart/form-data")
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadPerception(final MultipartFormDataInput input) throws ModelErrorResponseException {
        OrganizationModel organization = getOrganizationModel();
        OrganizationAuth auth = getAuth(organization);

        auth.requireManage();

        List<InputPart> inputParts = getInputs(input);

        for (InputPart inputPart : inputParts) {
            try {
                PerceptionType perceptionType = readFile(inputPart, SunatDocumentType.PERCEPTION.toString(), PerceptionType.class);

                // Double-check duplicated documentId
                if (perceptionType.getId() != null && perceptionType.getId().getValue() != null && documentManager.getDocumentByTypeAndDocumentId(SunatDocumentType.PERCEPTION.toString(), perceptionType.getId().getValue(), organization) != null) {
                    throw new ModelDuplicateException("Perception exists with same documentId[" + perceptionType.getId().getValue() + "]");
                }

                setPerceptionIDType(organization, perceptionType);

                DocumentModel document = documentManager.addDocument(organization, perceptionType.getId().getValue(), SunatDocumentType.PERCEPTION.toString(), perceptionType);
                eventStoreManager.send(organization, getAdminEvent(organization)
                        .operation(OperationType.CREATE)
                        .resourcePath(uriInfo, document.getId())
                        .representation(perceptionType)
                        .getEvent());
            } catch (IOException e) {
                logger.error("Error reading input data", e);
                throw new ModelErrorResponseException("Error Reading data", Response.Status.BAD_REQUEST);
            } catch (ModelDuplicateException e) {
                throw new ModelErrorResponseException("Perception exists with same documentId");
            } catch (ModelException e) {
                throw new ModelErrorResponseException("Could not create document");
            }
        }

        return Response.ok().build();
    }

    @POST
    @Path("/documents/extensions/upload/retentions")
    @Consumes("multipart/form-data")
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadRetention(final MultipartFormDataInput input) throws ModelErrorResponseException {
        OrganizationModel organization = getOrganizationModel();
        OrganizationAuth auth = getAuth(organization);

        auth.requireManage();

        List<InputPart> inputParts = getInputs(input);

        for (InputPart inputPart : inputParts) {
            try {
                RetentionType retentionType = readFile(inputPart, SunatDocumentType.RETENTION.toString(), RetentionType.class);

                // Double-check duplicated documentId
                if (retentionType.getId() != null && retentionType.getId().getValue() != null && documentManager.getDocumentByTypeAndDocumentId(SunatDocumentType.RETENTION.toString(), retentionType.getId().getValue(), organization) != null) {
                    throw new ModelDuplicateException("Perception exists with same documentId[" + retentionType.getId().getValue() + "]");
                }

                setRetentionIDType(organization, retentionType);

                DocumentModel document = documentManager.addDocument(organization, retentionType.getId().getValue(), SunatDocumentType.RETENTION.toString(), retentionType);
                eventStoreManager.send(organization, getAdminEvent(organization)
                        .operation(OperationType.CREATE)
                        .resourcePath(uriInfo, document.getId())
                        .representation(retentionType)
                        .getEvent());
            } catch (IOException e) {
                logger.error("Error reading input data", e);
                throw new ModelErrorResponseException("Error Reading data", Response.Status.BAD_REQUEST);
            } catch (ModelDuplicateException e) {
                throw new ModelErrorResponseException("Retention exists with same documentId");
            } catch (ModelException e) {
                throw new ModelErrorResponseException("Could not create document");
            }
        }

        return Response.ok().build();
    }

//    @POST
//    @Path("/documents/extensions/upload/voided-documents")
//    @Consumes("multipart/form-data")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response uploadVoidedDocument(final MultipartFormDataInput input) throws ModelErrorResponseException {
//        OrganizationModel organization = getOrganizationModel();
//        OrganizationAuth auth = getAuth(organization);
//
//        auth.requireManage();
//
//        List<InputPart> inputParts = getInputs(input);
//
//        for (InputPart inputPart : inputParts) {
//            try {
//                VoidedDocumentsType voidedDocumentsType = readFile(inputPart, SunatDocumentType.VOIDED_DOCUMENTS.toString(), VoidedDocumentsType.class);
//
//                // Double-check duplicated documentId
//                if (voidedDocumentsType.getID() != null && voidedDocumentsType.getID().getValue() != null && documentManager.getDocumentByTypeAndDocumentId(SunatDocumentType.RETENTION.toString(), voidedDocumentsType.getID().getValue(), organization) != null) {
//                    throw new ModelDuplicateException("Voided document exists with same documentId[" + voidedDocumentsType.getID().getValue() + "]");
//                }
//
//                setVoidedDocumentIDType(organization, voidedDocumentsType);
//
//                DocumentModel document = documentManager.addDocument(organization, voidedDocumentsType.getID().getValue(), SunatDocumentType.VOIDED_DOCUMENTS.toString(), voidedDocumentsType);
//                eventStoreManager.send(organization, getAdminEvent(organization)
//                        .operation(OperationType.CREATE)
//                        .resourcePath(uriInfo, document.getId())
//                        .representation(voidedDocumentsType)
//                        .getEvent());
//            } catch (IOException e) {
//                logger.error("Error reading input data", e);
//                throw new ModelErrorResponseException("Error Reading data", Response.Status.BAD_REQUEST);
//            } catch (ModelDuplicateException e) {
//                throw new ModelErrorResponseException("Retention exists with same documentId");
//            } catch (ModelException e) {
//                throw new ModelErrorResponseException("Could not create document");
//            }
//        }
//
//        return Response.ok().build();
//    }

    @POST
    @Path("/documents/perceptions")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPerception(DocumentoSunatRepresentation rep) throws ModelErrorResponseException {
        OrganizationModel organization = getOrganizationModel();
        OrganizationAuth auth = getAuth(organization);

        auth.requireManage();
        checkSerieAndNumber(rep.getSerieDocumento(), rep.getNumeroDocumento(), SunatDocumentType.PERCEPTION.toString(), organization);

        if (!validateOrganizationInfo(organization)) {
            throw new ModelErrorResponseException("Datos de organizacion insuficientes", Response.Status.BAD_REQUEST);
        }

        try {
            PerceptionType perceptionType = representationToType.toPerceptionType(organization, rep);
            setPerceptionIDType(organization, perceptionType);

            DocumentModel document = documentManager.addDocument(organization, perceptionType.getId().getValue(), SunatDocumentType.PERCEPTION.toString(), perceptionType);

            if (rep.isEnviarAutomaticamenteASunat()) {
                sunatResourceManager.sendDocumentToThirdParty(organization.getId(), document.getId());
            }
            if (rep.isEnviarAutomaticamenteAlCliente()) {
                sunatResourceManager.sendDocumentToCustomer(organization.getId(), document.getId());
            }

            URI location = uriInfo.getAbsolutePathBuilder().path(document.getId()).build();
            eventStoreManager.send(organization, getAdminEvent(organization)
                    .operation(OperationType.CREATE)
                    .resourcePath(uriInfo, document.getId())
                    .representation(rep)
                    .getEvent());
            return Response.created(location).entity(modelToRepresentation.toRepresentation(document, false)).build();
        } catch (ModelDuplicateException e) {
            throw new ModelErrorResponseException("Perception exists with same documentId", Response.Status.CONFLICT);
        } catch (ModelException e) {
            throw new ModelErrorResponseException("Perception could not be created", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @POST
    @Path("/documents/retentions")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRetention(DocumentoSunatRepresentation rep) throws ModelErrorResponseException {
        OrganizationModel organization = getOrganizationModel();
        OrganizationAuth auth = getAuth(organization);

        auth.requireManage();
        checkSerieAndNumber(rep.getSerieDocumento(), rep.getNumeroDocumento(), SunatDocumentType.RETENTION.toString(), organization);

        if (!validateOrganizationInfo(organization)) {
            throw new ModelErrorResponseException("Datos de organizacion insuficientes", Response.Status.BAD_REQUEST);
        }

        try {
            RetentionType retentionType = representationToType.toRetentionType(organization, rep);
            setRetentionIDType(organization, retentionType);

            DocumentModel document = documentManager.addDocument(organization, retentionType.getId().getValue(), SunatDocumentType.RETENTION.toString(), retentionType);

            if (rep.isEnviarAutomaticamenteASunat()) {
                sunatResourceManager.sendDocumentToThirdParty(organization.getId(), document.getId());
            }
            if (rep.isEnviarAutomaticamenteAlCliente()) {
                sunatResourceManager.sendDocumentToCustomer(organization.getId(), document.getId());
            }

            URI location = uriInfo.getAbsolutePathBuilder().path(document.getId()).build();
            eventStoreManager.send(organization, getAdminEvent(organization)
                    .operation(OperationType.CREATE)
                    .resourcePath(uriInfo, document.getId())
                    .representation(rep)
                    .getEvent());
            return Response.created(location).entity(modelToRepresentation.toRepresentation(document, false)).build();
        } catch (ModelDuplicateException e) {
            throw new ModelErrorResponseException("Perception exists with same documentId", Response.Status.CONFLICT);
        } catch (ModelException e) {
            throw new ModelErrorResponseException("Perception could not be created", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @POST
    @Path("/documents/voided-documents")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRetention(@QueryParam("async") @DefaultValue("false") Boolean async, VoidedRepresentation rep) throws ModelErrorResponseException {
        OrganizationModel organization = getOrganizationModel();
        OrganizationAuth auth = getAuth(organization);

        auth.requireManage();
        checkSerieAndNumber(rep.getSerieDocumento(), rep.getNumeroDocumento(), SunatDocumentType.VOIDED_DOCUMENTS.toString(), organization);

        if (!validateOrganizationInfo(organization)) {
            throw new ModelErrorResponseException("Datos de organizacion insuficientes", Response.Status.BAD_REQUEST);
        }

        try {
            DocumentModel document = sunatResourceManager.createVoidedDocuments(organization, rep);

            try {
                if (rep.isEnviarAutomaticamenteASunat()) {
                    Future<Boolean> future = sunatResourceManager.sendDocumentToThirdParty(organization.getId(), document.getId());
                    if (!async) {
                        future.get();
                    }
                }
                if (rep.isEnviarAutomaticamenteAlCliente()) {
                    Future<Boolean> future = sunatResourceManager.sendDocumentToCustomer(organization.getId(), document.getId());
                    if (!async) {
                        future.get();
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                logger.error(e);
            }

            URI location = uriInfo.getAbsolutePathBuilder().path(document.getId()).build();
            eventStoreManager.send(organization, getAdminEvent(organization)
                    .operation(OperationType.CREATE)
                    .resourcePath(uriInfo, document.getId())
                    .representation(rep)
                    .getEvent());
            return Response.created(location).entity(modelToRepresentation.toRepresentation(document, false)).build();
        } catch (ModelDuplicateException e) {
            throw new ModelErrorResponseException("Perception exists with same documentId", Response.Status.CONFLICT);
        } catch (ModelException e) {
            throw new ModelErrorResponseException("Perception could not be created", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @GET
    @Path("/documents/{documentId}/cdr")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getCdr(@PathParam("documentId") final String documentIdPk) {
        OrganizationModel organization = getOrganizationModel();
        OrganizationAuth auth = getAuth(organization);

        auth.requireView();
        DocumentModel document = getDocument(documentIdPk, organization);

        Map<String, String> params = new HashMap<>();
        params.put(DocumentModel.SEND_EVENT_DESTINY, DestinyType.THIRD_PARTY.toString());
        params.put(DocumentModel.SEND_EVENT_STATUS, SendEventStatus.SUCCESS.toString());

        List<SendEventModel> sendEvents = document.searchForSendEvent(params, 0, 2);
        if (sendEvents.isEmpty()) {
            return ErrorResponse.error("No se encontro un envio valido a SUNAT", Response.Status.BAD_REQUEST);
        } else {
            SendEventModel sendEvent = sendEvents.get(0);
            List<String> fileIds = sendEvent.getAttachedFileIds();
            if (fileIds.isEmpty()) {
                return ErrorResponse.error("Cdr no encontrado", Response.Status.NOT_FOUND);
            } else if (fileIds.size() > 1) {
                return ErrorResponse.error("Se encontraron mas de un cdr, no se puede identificar el valido", Response.Status.CONFLICT);
            } else {
                FileModel cdrFile = fileProvider.getFileById(organization, fileIds.get(0));
                Response.ResponseBuilder response = Response.ok(cdrFile.getFile());
                String internetMediaType = InternetMediaType.getMymeTypeFromExtension(cdrFile.getExtension());
                if (internetMediaType != null && !internetMediaType.isEmpty()) response.type(internetMediaType);
                response.header("content-disposition", "attachment; filename=\"" + cdrFile.getFileName() + "\"");
                return response.build();
            }
        }
    }

    @GET
    @Path("/documents/{documentId}/check-ticket")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response checkTicket(@PathParam("documentId") final String documentIdPk) throws ModelErrorResponseException {
        OrganizationModel organization = getOrganizationModel();
        OrganizationAuth auth = getAuth(organization);

        auth.requireView();
        DocumentModel document = getDocument(documentIdPk, organization);

        Map<String, String> params = new HashMap<>();
        params.put(DocumentModel.SEND_EVENT_DESTINY, DestinyType.THIRD_PARTY.toString());
        params.put(DocumentModel.SEND_EVENT_STATUS, SendEventStatus.SUCCESS.toString());

        List<SendEventModel> sendEvents = document.searchForSendEvent(params, 0, 2);
        if (sendEvents.isEmpty()) {
            return ErrorResponse.error("No se encontro un envio valido a SUNAT", Response.Status.BAD_REQUEST);
        } else {
            if (sendEvents.size() > 1) {
                return ErrorResponse.error("Se encontro mas de un envio valido, debe existir solo un envio", Response.Status.BAD_REQUEST);
            } else {
                SendEventModel sendEvent = sendEvents.get(0);

                if (document.getRequiredActions().contains(SunatRequiredAction.CONSULTAR_TICKET.toString())) {
                    try {
                        String ticket = sendEvent.getAttribute(SunatTypeToModel.NUMERO_TICKET);
                        if (ticket == null) {
                            throw new ModelErrorResponseException("No se encontro un numero de ticket valido", Response.Status.BAD_REQUEST);
                        }

                        byte[] result = sunatSenderManager.checkTicket(organization, ticket);
                        FileModel file = fileProvider.createFile(organization, "R" + ticket + ".zip", result);
                        sendEvent.attachFile(file);

                        // Remove required action
                        document.removeRequiredAction(SunatRequiredAction.CONSULTAR_TICKET.toString());

                        Response.ResponseBuilder response = Response.ok(file.getFile());
                        String internetMediaType = InternetMediaType.getMymeTypeFromExtension(file.getExtension());
                        if (internetMediaType != null && !internetMediaType.isEmpty()) response.type(internetMediaType);
                        response.header("content-disposition", "attachment; filename=\"" + file.getFileName() + "\"");
                        return response.build();
                    } catch (SunatSendEventException e) {
                        throw new ModelErrorResponseException("Ocurrio un error al consultar a la sunat", Response.Status.INTERNAL_SERVER_ERROR);
                    } catch (ModelException e) {
                        throw new ModelErrorResponseException("No se pudo realizar la consulta a la sunat", Response.Status.INTERNAL_SERVER_ERROR);
                    }
                } else {
                    List<String> fileIds = sendEvent.getAttachedFileIds();
                    if (fileIds.isEmpty()) {
                        return ErrorResponse.error("Cdr no encontrado", Response.Status.NOT_FOUND);
                    } else if (fileIds.size() > 1) {
                        return ErrorResponse.error("Se encontraron mas de un cdr, no se puede identificar el valido", Response.Status.CONFLICT);
                    } else {
                        FileModel cdrFile = fileProvider.getFileById(organization, fileIds.get(0));
                        Response.ResponseBuilder response = Response.ok(cdrFile.getFile());
                        String internetMediaType = InternetMediaType.getMymeTypeFromExtension(cdrFile.getExtension());
                        if (internetMediaType != null && !internetMediaType.isEmpty()) response.type(internetMediaType);
                        response.header("content-disposition", "attachment; filename=\"" + cdrFile.getFileName() + "\"");
                        return response.build();
                    }
                }

            }
        }
    }

}