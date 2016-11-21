package org.openfact.pe.services.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
import org.openfact.pe.models.SummaryDocumentModel;
import org.openfact.pe.models.SummaryDocumentProvider;
import org.openfact.pe.models.utils.ModelToRepresentation_PE;
import org.openfact.pe.representations.idm.DocumentRepresentation;
import org.openfact.pe.representations.idm.GeneralDocumentRepresentation;
import org.openfact.pe.services.managers.SummaryDocumentManager;
import org.openfact.representations.idm.search.SearchCriteriaRepresentation;
import org.openfact.representations.idm.search.SearchResultsRepresentation;
import org.openfact.services.ErrorResponse;

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
    public List<GeneralDocumentRepresentation> getSummaryDocuments(
            @QueryParam("filterText") String filterText, @QueryParam("first") Integer firstResult,
            @QueryParam("max") Integer maxResults) {
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
    public Response createSummaryDocument(GeneralDocumentRepresentation rep) {
        SummaryDocumentManager summaryDocumentManager = new SummaryDocumentManager(session);

        DocumentRepresentation documentRep = rep.getDocument();

        // Double-check duplicated ID
        if (documentRep.getSerie() != null && documentRep.getNumero() != null
                && summaryDocumentManager.getSummaryDocumentByDocumentId(
                        documentRep.getSerie() + "-" + documentRep.getNumero(), organization) != null) {
            return ErrorResponse.exists("SummaryDocument exists with same documentId");
        }

        try {
            SummaryDocumentModel summaryDocument = summaryDocumentManager.addSummaryDocument(organization,
                    rep);
            if (session.getTransactionManager().isActive()) {
                session.getTransactionManager().commit();
            }

            // Enviar Email
            if (rep.getDocument().isEnviarAutomaticamenteAlCliente()) {
                summaryDocumentManager.enviarEmailAlCliente(organization, summaryDocument);
            }

            // Enviar Sunat
            if (rep.getDocument().isEnviarAutomaticamenteASunat()) {
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
            logger.error("failedToSendActionsEmail");
            return ErrorResponse.error("SummaryDocument Created but, Failed to send execute actions email",
                    Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @POST
    @Path("search")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public SearchResultsRepresentation<GeneralDocumentRepresentation> search(
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
        SearchResultsRepresentation<GeneralDocumentRepresentation> rep = new SearchResultsRepresentation<>();
        List<GeneralDocumentRepresentation> items = new ArrayList<>();
        results.getModels().forEach(f -> items.add(ModelToRepresentation_PE.toRepresentation(f)));
        rep.setItems(items);
        rep.setTotalSize(results.getTotalSize());
        return rep;
    }

}