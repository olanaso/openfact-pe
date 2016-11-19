package org.openfact.pe.services.resources;

import org.jboss.resteasy.annotations.cache.NoCache;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.utils.ModelToRepresentation;
import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.PerceptionService;
import org.openfact.pe.models.utils.ModelToRepresentation_PE;
import org.openfact.pe.representations.idm.GeneralDocumentRepresentation;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

public class PerceptionsResource {

    private final OpenfactSession session;

    public PerceptionsResource(OpenfactSession session) {
        this.session = session;
    }

    @GET
    @Path("")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public List<GeneralDocumentRepresentation> getInvoices(
            @QueryParam("organization") String organizationName, @QueryParam("filterText") String filterText,
            @QueryParam("first") Integer firstResult, @QueryParam("max") Integer maxResults) {
        firstResult = firstResult != null ? firstResult : -1;
        maxResults = maxResults != null ? maxResults : -1;

        OrganizationModel organization = session.organizations().getOrganizationByName(organizationName);

        PerceptionService perceptionService = session.getProvider(PerceptionService.class);
        List<PerceptionModel> perceptions;
        if (filterText == null) {
            perceptions = perceptionService.getPerceptions(organization, firstResult, maxResults);
        } else {
            perceptions = perceptionService.searchForPerception(filterText.trim(), firstResult, maxResults,
                    organization);
        }
        return perceptions.stream().map(f -> ModelToRepresentation_PE.toRepresentation(f)).collect(Collectors.toList());
    }

    @POST
    @Path("")
    @NoCache
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCompany(GeneralDocumentRepresentation rep) {
        session.getProvider(ExampleService.class).addCompany(rep);
        return Response
                .created(session.getContext().getUri().getAbsolutePathBuilder().path(rep.getId()).build())
                .build();
    }

    @GET
    @NoCache
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public CompanyRepresentation getCompany(@PathParam("id") final String id) {
        return session.getProvider(ExampleService.class).findCompany(id);
    }

}