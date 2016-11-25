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
import org.openfact.models.ModelDuplicateException;
import org.openfact.models.ModelException;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.search.SearchCriteriaModel;
import org.openfact.models.search.SearchResultsModel;
import org.openfact.models.utils.RepresentationToModel;
import org.openfact.pe.model.types.VoidedDocumentsType;
import org.openfact.pe.models.VoidedDocumentModel;
import org.openfact.pe.models.VoidedDocumentProvider;
import org.openfact.pe.models.utils.SunatModelToRepresentation;
import org.openfact.pe.representations.idm.DocumentRepresentation;
import org.openfact.pe.services.managers.VoidedDocumentManager;
import org.openfact.representations.idm.search.SearchCriteriaRepresentation;
import org.openfact.representations.idm.search.SearchResultsRepresentation;
import org.openfact.services.ErrorResponse;
import org.w3c.dom.Document;

public class VoidedDocumentsResource {

    protected static final Logger logger = Logger.getLogger(VoidedDocumentsResource.class);

    private final OpenfactSession session;
    private final OrganizationModel organization;

    @Context
    protected UriInfo uriInfo;

    public VoidedDocumentsResource(OpenfactSession session, OrganizationModel organization) {
        this.session = session;
        this.organization = organization;
    }

    @GET
    @Path("")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public List<DocumentRepresentation> getVoidedDocuments(@QueryParam("filterText") String filterText,
            @QueryParam("first") Integer firstResult, @QueryParam("max") Integer maxResults) {
        firstResult = firstResult != null ? firstResult : -1;
        maxResults = maxResults != null ? maxResults : -1;

        VoidedDocumentProvider voidedDocumentProvider = session.getProvider(VoidedDocumentProvider.class);
        List<VoidedDocumentModel> voidedDocuments;
        if (filterText == null) {
            voidedDocuments = voidedDocumentProvider.getVoidedDocuments(organization, firstResult,
                    maxResults);
        } else {
            voidedDocuments = voidedDocumentProvider.searchForVoidedDocument(filterText.trim(), organization,
                    firstResult, maxResults);
        }
        return voidedDocuments.stream().map(f -> SunatModelToRepresentation.toRepresentation(f))
                .collect(Collectors.toList());
    }

    @POST
    @Path("")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public Response createVoidedDocument(DocumentRepresentation rep) {
        VoidedDocumentManager voidedDocumentManager = new VoidedDocumentManager(session);

        // Double-check duplicated ID
        if (rep.getSerie() != null && rep.getNumero() != null
                && voidedDocumentManager.getVoidedDocumentByDocumentId(rep.getSerie() + "-" + rep.getNumero(),
                        organization) != null) {
            return ErrorResponse.exists("VoidedDocument exists with same documentId");
        }

        try {
            VoidedDocumentModel voidedDocument = voidedDocumentManager.addVoidedDocument(organization, rep);
            if (session.getTransactionManager().isActive()) {
                session.getTransactionManager().commit();
            }

            // Enviar a Cliente
            if (rep.isEnviarAutomaticamenteAlCliente()) {
                voidedDocumentManager.sendToCustomerParty(organization, voidedDocument);
            }

            // Enviar Sunat
            if (rep.isEnviarAutomaticamenteASunat()) {
                voidedDocumentManager.sendToTrirdParty(organization, voidedDocument);
            }

            URI location = uriInfo.getAbsolutePathBuilder().path(voidedDocument.getId()).build();
            return Response.created(location).build();
        } catch (ModelDuplicateException e) {
            if (session.getTransactionManager().isActive()) {
                session.getTransactionManager().setRollbackOnly();
            }
            return ErrorResponse.exists("VoidedDocument exists with same id or documentId");
        } catch (ModelException me) {
            if (session.getTransactionManager().isActive()) {
                session.getTransactionManager().setRollbackOnly();
            }
            return ErrorResponse.exists("Could not create voidedDocument");
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

                VoidedDocumentsType voidedDocumentType = null;
                if (voidedDocumentType == null) {
                    throw new IOException("Invalid invoice Xml");
                }

                VoidedDocumentManager voidedDocumentManager = new VoidedDocumentManager(session);

                // Double-check duplicated ID
                if (voidedDocumentType.getID() != null && voidedDocumentManager.getVoidedDocumentByDocumentId(
                        voidedDocumentType.getID().getValue(), organization) != null) {
                    throw new ModelDuplicateException("VoidedDocument exists with same documentId");
                }

                VoidedDocumentModel voidedDocument = voidedDocumentManager.addVoidedDocument(organization,
                        voidedDocumentType);
                if (session.getTransactionManager().isActive()) {
                    session.getTransactionManager().commit();
                }

                // Enviar Cliente
                voidedDocumentManager.sendToCustomerParty(organization, voidedDocument);

                // Enviar Sunat
                voidedDocumentManager.sendToTrirdParty(organization, voidedDocument);

                URI location = uriInfo.getAbsolutePathBuilder().path(voidedDocument.getId()).build();
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
                return ErrorResponse.exists("VoidedDocument exists with same documentId");
            } catch (ModelException me) {
                if (session.getTransactionManager().isActive()) {
                    session.getTransactionManager().setRollbackOnly();
                }
                return ErrorResponse.exists("Could not create invoice");
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
        VoidedDocumentProvider voidedDocumentProvider = session.getProvider(VoidedDocumentProvider.class);

        SearchCriteriaModel criteriaModel = RepresentationToModel.toModel(criteria);
        String filterText = criteria.getFilterText();
        SearchResultsModel<VoidedDocumentModel> results = null;
        if (filterText != null) {
            results = voidedDocumentProvider.searchForVoidedDocument(filterText.trim(), criteriaModel,
                    organization);
        } else {
            results = voidedDocumentProvider.searchForVoidedDocument(criteriaModel, organization);
        }
        SearchResultsRepresentation<DocumentRepresentation> rep = new SearchResultsRepresentation<>();
        List<DocumentRepresentation> items = new ArrayList<>();
        results.getModels().forEach(f -> items.add(SunatModelToRepresentation.toRepresentation(f)));
        rep.setItems(items);
        rep.setTotalSize(results.getTotalSize());
        return rep;
    }

    @GET
    @Path("{voidedDocumentId}")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public DocumentRepresentation getVoidedDocument(
            @PathParam("voidedDocumentId") final String voidedDocumentId) {
        VoidedDocumentProvider voidedDocumentProvider = session.getProvider(VoidedDocumentProvider.class);
        VoidedDocumentModel voidedDocument = voidedDocumentProvider
                .getVoidedDocumentByDocumentId(voidedDocumentId, organization);
        if (voidedDocument == null) {
            throw new NotFoundException("VoidedDocument not found");
        }

        DocumentRepresentation rep = SunatModelToRepresentation.toRepresentation(voidedDocument);
        return rep;
    }

    @GET
    @Path("{voidedDocumentId}/representation/text")
    @NoCache
    @Produces("application/text")
    public Response getVoidedDocumentAsText(@PathParam("voidedDocumentId") final String voidedDocumentId) {
        VoidedDocumentProvider voidedDocumentProvider = session.getProvider(VoidedDocumentProvider.class);
        VoidedDocumentModel voidedDocument = voidedDocumentProvider
                .getVoidedDocumentByDocumentId(voidedDocumentId, organization);
        if (voidedDocument == null) {
            throw new NotFoundException("VoidedDocument not found");
        }

        String result = null;
        try {
            Document document = DocumentUtils.byteToDocument(voidedDocument.getXmlDocument());
            result = DocumentUtils.getDocumentToString(document);
        } catch (Exception e) {
            return ErrorResponse.error("Invalid xml parser", Status.INTERNAL_SERVER_ERROR);
        }

        Response.ResponseBuilder response = Response.ok(result);
        return response.build();
    }

    @GET
    @Path("{voidedDocumentId}/representation/xml")
    @NoCache
    @Produces("application/xml")
    public Response getVoidedDocumentAsXml(@PathParam("voidedDocumentId") final String voidedDocumentId) {
        VoidedDocumentProvider voidedDocumentProvider = session.getProvider(VoidedDocumentProvider.class);
        VoidedDocumentModel voidedDocument = voidedDocumentProvider
                .getVoidedDocumentByDocumentId(voidedDocumentId, organization);
        if (voidedDocument == null) {
            throw new NotFoundException("VoidedDocument not found");
        }

        Document document = null;
        try {
            document = DocumentUtils.byteToDocument(voidedDocument.getXmlDocument());
        } catch (Exception e) {
            return ErrorResponse.error("Invalid xml parser", Status.INTERNAL_SERVER_ERROR);
        }

        Response.ResponseBuilder response = Response.ok(document);
        return response.build();
    }

    @GET
    @Path("{voidedDocumentId}/representation/pdf")
    @NoCache
    @Produces("application/xml")
    public Response getVoidedDocumentAsPdf(@PathParam("voidedDocumentId") final String voidedDocumentId) {
        return null;
    }

    @DELETE
    @Path("{voidedDocumentId}")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteVoidedDocument(@PathParam("voidedDocumentId") final String voidedDocumentId) {
        VoidedDocumentProvider voidedDocumentProvider = session.getProvider(VoidedDocumentProvider.class);
        VoidedDocumentModel voidedDocument = voidedDocumentProvider
                .getVoidedDocumentByDocumentId(voidedDocumentId, organization);
        if (voidedDocument == null) {
            throw new NotFoundException("VoidedDocument not found");
        }

        boolean removed = new VoidedDocumentManager(session).removeVoidedDocument(organization,
                voidedDocument);
        if (removed) {
            return Response.noContent().build();
        } else {
            return ErrorResponse.error("VoidedDocument couldn't be deleted", Response.Status.BAD_REQUEST);
        }
    }

}