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
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.openfact.common.converts.DocumentUtils;
import org.openfact.email.EmailException;
import org.openfact.models.ModelDuplicateException;
import org.openfact.models.ModelException;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.search.SearchCriteriaModel;
import org.openfact.models.search.SearchResultsModel;
import org.openfact.models.utils.RepresentationToModel;
import org.openfact.pe.models.SummaryDocumentModel;
import org.openfact.pe.models.SummaryDocumentProvider;
import org.openfact.pe.models.utils.ModelToRepresentation_PE;
import org.openfact.pe.representations.idm.DocumentRepresentation;
import org.openfact.pe.services.managers.SummaryDocumentManager;
import org.openfact.pe.types.SummaryDocumentsType;
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
    public List<DocumentRepresentation> getSummaryDocuments(@QueryParam("filterText") String filterText,
            @QueryParam("first") Integer firstResult, @QueryParam("max") Integer maxResults) {
        firstResult = firstResult != null ? firstResult : -1;
        maxResults = maxResults != null ? maxResults : -1;

        SummaryDocumentProvider summaryDocumentProvider = session.getProvider(SummaryDocumentProvider.class);
        List<SummaryDocumentModel> summaryDocuments;
        if (filterText == null) {
            summaryDocuments = summaryDocumentProvider.getSummaryDocuments(organization, firstResult,
                    maxResults);
        } else {
            summaryDocuments = summaryDocumentProvider.searchForSummaryDocument(filterText.trim(),
                    organization, firstResult, maxResults);
        }
        return summaryDocuments.stream().map(f -> ModelToRepresentation_PE.toRepresentation(f))
                .collect(Collectors.toList());
    }

    @POST
    @Path("")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSummaryDocument(DocumentRepresentation rep) {
        SummaryDocumentManager summaryDocumentManager = new SummaryDocumentManager(session);

        // Double-check duplicated ID
        if (rep.getSerie() != null && rep.getNumero() != null
                && summaryDocumentManager.getSummaryDocumentByDocumentId(
                        rep.getSerie() + "-" + rep.getNumero(), organization) != null) {
            return ErrorResponse.exists("SummaryDocument exists with same documentId");
        }

        try {
            SummaryDocumentModel summaryDocument = summaryDocumentManager.addSummaryDocument(organization,
                    rep);
            if (session.getTransactionManager().isActive()) {
                session.getTransactionManager().commit();
            }

            // Enviar Email
            if (rep.isEnviarAutomaticamenteAlCliente()) {
                summaryDocumentManager.enviarEmailAlCliente(organization, summaryDocument);
            }

            // Enviar Sunat
            if (rep.isEnviarAutomaticamenteASunat()) {
                summaryDocumentManager.enviarASunat(organization, summaryDocument);
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
        } catch (EmailException e) {
            if (session.getTransactionManager().isActive()) {
                session.getTransactionManager().setRollbackOnly();
            }
            return ErrorResponse.error("Failed on send email", Response.Status.INTERNAL_SERVER_ERROR);
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
                if (summaryDocumentType.getId() != null
                        && summaryDocumentManager.getSummaryDocumentByDocumentId(
                                summaryDocumentType.getId().getValue(), organization) != null) {
                    throw new ModelDuplicateException("SummaryDocument exists with same documentId");
                }

                SummaryDocumentModel summaryDocument = summaryDocumentManager.addSummaryDocument(organization,
                        summaryDocumentType);
                if (session.getTransactionManager().isActive()) {
                    session.getTransactionManager().commit();
                }

                // Enviar Email
                summaryDocumentManager.enviarEmailAlCliente(organization, summaryDocument);

                // Enviar Sunat
                summaryDocumentManager.enviarASunat(organization, summaryDocument);

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
            } catch (EmailException e) {
                if (session.getTransactionManager().isActive()) {
                    session.getTransactionManager().setRollbackOnly();
                }
                return ErrorResponse.error("Failed on send email", Response.Status.INTERNAL_SERVER_ERROR);
            }
        }

        return Response.ok().build();
    }

    @POST
    @Path("search")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public SearchResultsRepresentation<DocumentRepresentation> search(
            final SearchCriteriaRepresentation criteria) {
        SummaryDocumentProvider summaryDocumentProvider = session.getProvider(SummaryDocumentProvider.class);

        SearchCriteriaModel criteriaModel = RepresentationToModel.toModel(criteria);
        String filterText = criteria.getFilterText();
        SearchResultsModel<SummaryDocumentModel> results = null;
        if (filterText != null) {
            results = summaryDocumentProvider.searchForSummaryDocument(filterText.trim(), criteriaModel,
                    organization);
        } else {
            results = summaryDocumentProvider.searchForSummaryDocument(criteriaModel, organization);
        }
        SearchResultsRepresentation<DocumentRepresentation> rep = new SearchResultsRepresentation<>();
        List<DocumentRepresentation> items = new ArrayList<>();
        results.getModels().forEach(f -> items.add(ModelToRepresentation_PE.toRepresentation(f)));
        rep.setItems(items);
        rep.setTotalSize(results.getTotalSize());
        return rep;
    }

    @GET
    @Path("{summaryDocumentId}")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public DocumentRepresentation getSummaryDocument(
            @PathParam("summaryDocumentId") final String summaryDocumentId) {
        SummaryDocumentProvider summaryDocumentProvider = session.getProvider(SummaryDocumentProvider.class);
        SummaryDocumentModel summaryDocument = summaryDocumentProvider
                .getSummaryDocumentByDocumentId(summaryDocumentId, organization);
        if (summaryDocument == null) {
            throw new NotFoundException("SummaryDocument not found");
        }

        DocumentRepresentation rep = ModelToRepresentation_PE.toRepresentation(summaryDocument);
        return rep;
    }

    @GET
    @Path("{summaryDocumentId}/representation/text")
    @NoCache
    @Produces("application/text")
    public Response getSummaryDocumentAsText(@PathParam("summaryDocumentId") final String summaryDocumentId) {
        SummaryDocumentProvider summaryDocumentProvider = session.getProvider(SummaryDocumentProvider.class);
        SummaryDocumentModel summaryDocument = summaryDocumentProvider
                .getSummaryDocumentByDocumentId(summaryDocumentId, organization);
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
        SummaryDocumentModel summaryDocument = summaryDocumentProvider
                .getSummaryDocumentByDocumentId(summaryDocumentId, organization);
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
        SummaryDocumentModel summaryDocument = summaryDocumentProvider
                .getSummaryDocumentByDocumentId(summaryDocumentId, organization);
        if (summaryDocument == null) {
            throw new NotFoundException("SummaryDocument not found");
        }

        boolean removed = new SummaryDocumentManager(session).removeSummaryDocument(organization,
                summaryDocument);
        if (removed) {
            return Response.noContent().build();
        } else {
            return ErrorResponse.error("SummaryDocument couldn't be deleted", Response.Status.BAD_REQUEST);
        }
    }

}