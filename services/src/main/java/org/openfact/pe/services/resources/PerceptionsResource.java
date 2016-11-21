package org.openfact.pe.services.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.openfact.email.EmailException;
import org.openfact.models.ModelDuplicateException;
import org.openfact.models.ModelException;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.search.SearchCriteriaModel;
import org.openfact.models.search.SearchResultsModel;
import org.openfact.models.utils.RepresentationToModel;
import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.PerceptionProvider;
import org.openfact.pe.models.utils.ModelToRepresentation_PE;
import org.openfact.pe.representations.idm.DocumentRepresentation;
import org.openfact.pe.representations.idm.GeneralDocumentRepresentation;
import org.openfact.pe.services.managers.PerceptionManager;
import org.openfact.representations.idm.search.SearchCriteriaRepresentation;
import org.openfact.representations.idm.search.SearchResultsRepresentation;
import org.openfact.services.ErrorResponse;

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
    public List<GeneralDocumentRepresentation> getPerceptions(@QueryParam("filterText") String filterText,
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
        return perceptions.stream().map(f -> ModelToRepresentation_PE.toRepresentation(f))
                .collect(Collectors.toList());
    }

    @POST
    @Path("")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPerception(GeneralDocumentRepresentation rep) {
        PerceptionManager perceptionManager = new PerceptionManager(session);

        DocumentRepresentation documentRep = rep.getDocument();

        // Double-check duplicated ID
        if (documentRep.getSerie() != null && documentRep.getNumero() != null
                && perceptionManager.getPerceptionByDocumentId(
                        documentRep.getSerie() + "-" + documentRep.getNumero(), organization) != null) {
            return ErrorResponse.exists("Perception exists with same documentId");
        }

        try {
            PerceptionModel perception = perceptionManager.addPerception(organization, rep);
            if (session.getTransactionManager().isActive()) {
                session.getTransactionManager().commit();
            }

            // Enviar Email
            if (rep.getDocument().isEnviarAutomaticamenteAlCliente()) {
                perceptionManager.enviarEmailAlCliente(organization, perception);
            }

            // Enviar Sunat
            if (rep.getDocument().isEnviarAutomaticamenteASunat()) {
                perceptionManager.enviarASunat(organization, perception);
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
        } catch (EmailException e) {
            logger.error("failedToSendActionsEmail");
            return ErrorResponse.error("Perception Created but, Failed to send execute actions email",
                    Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @POST
    @Path("search")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public SearchResultsRepresentation<GeneralDocumentRepresentation> search(
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
        SearchResultsRepresentation<GeneralDocumentRepresentation> rep = new SearchResultsRepresentation<>();
        List<GeneralDocumentRepresentation> items = new ArrayList<>();
        results.getModels().forEach(f -> items.add(ModelToRepresentation_PE.toRepresentation(f)));
        rep.setItems(items);
        rep.setTotalSize(results.getTotalSize());
        return rep;
    }

}