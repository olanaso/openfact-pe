package org.openfact.pe.services.resources;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.services.managers.OrganizationManager;

public class RootResource_PE {

    private final OpenfactSession session;

    public RootResource_PE(OpenfactSession session) {
        this.session = session;
    }

    @Path("pe/ubl-extensions/{organization}/retentions")
    public RetentionsResource getRentionsResource(@PathParam("organization") final String organizationName) {
        OrganizationModel organization = getOrganization(organizationName);

        return new RetentionsResource(session, organization);
    }

    @Path("pe/ubl-extensions/{organization}/perceptions")
    public PerceptionsResource getPerceptionsResource(
            @PathParam("organization") final String organizationName) {
        OrganizationModel organization = getOrganization(organizationName);

        return new PerceptionsResource(session, organization);
    }

    @Path("pe/ubl-extensions/{organization}/sumary-documents")
    public SummaryDocumentsResource getSummaryDocumentsResource(
            @PathParam("organization") final String organizationName) {
        OrganizationModel organization = getOrganization(organizationName);

        return new SummaryDocumentsResource(session, organization);
    }

    @Path("pe/ubl-extensions/{organization}/voided-documents")
    public VoidedDocumentsResource getVoidedDocumentsResource(
            @PathParam("organization") final String organizationName) {
        OrganizationModel organization = getOrganization(organizationName);

        return new VoidedDocumentsResource(session, organization);
    }

    private OrganizationModel getOrganization(String organizationName) {
        OrganizationManager organizationManager = new OrganizationManager(session);
        OrganizationModel organization = organizationManager.getOrganizationByName(organizationName);
        if (organization == null) {
            throw new NotFoundException("Organization not found.");
        }
        return organization;
    }

}