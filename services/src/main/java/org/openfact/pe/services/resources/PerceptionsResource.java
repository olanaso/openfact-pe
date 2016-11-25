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
import org.openfact.pe.model.types.PerceptionType;
import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.PerceptionProvider;
import org.openfact.pe.models.utils.SunatModelToRepresentation;
import org.openfact.pe.representations.idm.DocumentRepresentation;
import org.openfact.pe.services.managers.PerceptionManager;
import org.openfact.representations.idm.search.SearchCriteriaRepresentation;
import org.openfact.representations.idm.search.SearchResultsRepresentation;
import org.openfact.services.ErrorResponse;
import org.w3c.dom.Document;

/**
 * @author carlosthe19916@sistcoop.com
 */
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
    public List<DocumentRepresentation> getPerceptions(@QueryParam("filterText") String filterText,
            @QueryParam("first") Integer firstResult, @QueryParam("max") Integer maxResults) {
        firstResult = firstResult != null ? firstResult : -1;
        maxResults = maxResults != null ? maxResults : -1;

        PerceptionProvider perceptionProvider = session.getProvider(PerceptionProvider.class);
        List<PerceptionModel> perceptions;
        if (filterText == null) {
            perceptions = perceptionProvider.getPerceptions(organization, firstResult, maxResults);
        } else {
            perceptions = perceptionProvider.searchForPerception(filterText.trim(), organization, firstResult,
                    maxResults);
        }
        return perceptions.stream().map(f -> SunatModelToRepresentation.toRepresentation(f))
                .collect(Collectors.toList());
    }

    @POST
    @Path("")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPerception(DocumentRepresentation rep) {
        PerceptionManager perceptionManager = new PerceptionManager(session);

        // Double-check duplicated ID
        if (rep.getSerie() != null && rep.getNumero() != null && perceptionManager
                .getPerceptionByDocumentId(rep.getSerie() + "-" + rep.getNumero(), organization) != null) {
            return ErrorResponse.exists("Perception exists with same documentId");
        }

        try {
            PerceptionModel perception = perceptionManager.addPerception(organization, rep);
            if (session.getTransactionManager().isActive()) {
                session.getTransactionManager().commit();
            }

            // Enviar a Cliente
            if (rep.isEnviarAutomaticamenteAlCliente()) {
                perceptionManager.sendToCustomerParty(organization, perception);
            }

            // Enviar Sunat
            if (rep.isEnviarAutomaticamenteASunat()) {
                perceptionManager.sendToTrirdParty(organization, perception);
            }

            URI location = uriInfo.getAbsolutePathBuilder().path(perception.getId()).build();
            return Response.created(location).build();
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
    public Response createInvoice(final MultipartFormDataInput input) {
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get("file");

        for (InputPart inputPart : inputParts) {
            try {
                InputStream inputStream = inputPart.getBody(InputStream.class, null);
                byte[] bytes = IOUtils.toByteArray(inputStream);

                PerceptionType perceptionType = null;
                if (perceptionType == null) {
                    throw new IOException("Invalid perception Xml");
                }

                PerceptionManager perceptionManager = new PerceptionManager(session);

                // Double-check duplicated ID
                if (perceptionType.getId() != null && perceptionManager
                        .getPerceptionByDocumentId(perceptionType.getId().getValue(), organization) != null) {
                    throw new ModelDuplicateException("Perception exists with same documentId");
                }

                PerceptionModel perception = perceptionManager.addPerception(organization, perceptionType);
                if (session.getTransactionManager().isActive()) {
                    session.getTransactionManager().commit();
                }

                // Enviar a Cliente
                perceptionManager.sendToCustomerParty(organization, perception);

                // Enviar Sunat
                perceptionManager.sendToTrirdParty(organization, perception);

                URI location = uriInfo.getAbsolutePathBuilder().path(perception.getId()).build();
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
                return ErrorResponse.exists("Perception exists with same documentId");
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
        PerceptionProvider perceptionProvider = session.getProvider(PerceptionProvider.class);

        SearchCriteriaModel criteriaModel = RepresentationToModel.toModel(criteria);
        String filterText = criteria.getFilterText();
        SearchResultsModel<PerceptionModel> results = null;
        if (filterText != null) {
            results = perceptionProvider.searchForPerception(filterText.trim(), criteriaModel, organization);
        } else {
            results = perceptionProvider.searchForPerception(criteriaModel, organization);
        }
        SearchResultsRepresentation<DocumentRepresentation> rep = new SearchResultsRepresentation<>();
        List<DocumentRepresentation> items = new ArrayList<>();
        results.getModels().forEach(f -> items.add(SunatModelToRepresentation.toRepresentation(f)));
        rep.setItems(items);
        rep.setTotalSize(results.getTotalSize());
        return rep;
    }

    @GET
    @Path("{perceptionId}")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public DocumentRepresentation getPerception(@PathParam("perceptionId") final String perceptionId) {
        PerceptionProvider perceptionProvider = session.getProvider(PerceptionProvider.class);
        PerceptionModel perception = perceptionProvider.getPerceptionByDocumentId(perceptionId, organization);
        if (perception == null) {
            throw new NotFoundException("Perception not found");
        }

        DocumentRepresentation rep = SunatModelToRepresentation.toRepresentation(perception);
        return rep;
    }

    @GET
    @Path("{perceptionId}/representation/text")
    @NoCache
    @Produces("application/text")
    public Response getPerceptionAsText(@PathParam("perceptionId") final String perceptionId) {
        PerceptionProvider perceptionProvider = session.getProvider(PerceptionProvider.class);
        PerceptionModel perception = perceptionProvider.getPerceptionByDocumentId(perceptionId, organization);
        if (perception == null) {
            throw new NotFoundException("Perception not found");
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
    @Produces("application/xml")
    public Response getPerceptionAsXml(@PathParam("perceptionId") final String perceptionId) {
        PerceptionProvider perceptionProvider = session.getProvider(PerceptionProvider.class);
        PerceptionModel perception = perceptionProvider.getPerceptionByDocumentId(perceptionId, organization);
        if (perception == null) {
            throw new NotFoundException("Perception not found");
        }

        Document document = null;
        try {
            document = DocumentUtils.byteToDocument(perception.getXmlDocument());
        } catch (Exception e) {
            return ErrorResponse.error("Invalid xml parser", Status.INTERNAL_SERVER_ERROR);
        }

        Response.ResponseBuilder response = Response.ok(document);
        return response.build();
    }

    @GET
    @Path("{perceptionId}/representation/pdf")
    @NoCache
    @Produces("application/xml")
    public Response getPerceptionAsPdf(@PathParam("perceptionId") final String perceptionId) {
        return null;
    }

    @DELETE
    @Path("{perceptionId}")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePerception(@PathParam("perceptionId") final String perceptionId) {
        PerceptionProvider perceptionProvider = session.getProvider(PerceptionProvider.class);
        PerceptionModel perception = perceptionProvider.getPerceptionByDocumentId(perceptionId, organization);
        if (perception == null) {
            throw new NotFoundException("Perception not found");
        }

        boolean removed = new PerceptionManager(session).removePerception(organization, perception);
        if (removed) {
            return Response.noContent().build();
        } else {
            return ErrorResponse.error("Perception couldn't be deleted", Response.Status.BAD_REQUEST);
        }
    }

}