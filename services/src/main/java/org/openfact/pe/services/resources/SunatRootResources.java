package org.openfact.pe.services.resources;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import org.openfact.common.ClientConnection;
import org.openfact.events.admin.ResourceType;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.security.SecurityContextProvider;
import org.openfact.services.managers.OrganizationManager;
import org.openfact.services.resources.admin.AdminAuth;
import org.openfact.services.resources.admin.AdminEventBuilder;

public class SunatRootResources {

    private final OpenfactSession session;

    public SunatRootResources(OpenfactSession session) {
        this.session = session;
    }

    private AdminEventBuilder getAdminBuilder(HttpHeaders headers) {
        OrganizationModel organization = session.getContext().getOrganization();
        ClientConnection clientConnection = session.getContext().getConnection();

        SecurityContextProvider authResult = session.getProvider(SecurityContextProvider.class);
        String organizationName = authResult.getCurrentOrganizationName(headers);

        OrganizationManager organizationManager = new OrganizationManager(session);
        OrganizationModel organizationToken = organizationManager.getOrganizationByName(organizationName);
        if (organizationToken == null) {
            throw new NotAuthorizedException("Unknown organization in token");
        }

        AdminAuth adminAuth = new AdminAuth(organization, authResult.getCurrentUser(headers));
        return new AdminEventBuilder(organization, adminAuth, session, clientConnection);
    }

    @Path("invoices")
    public InvoicesResource getInvoicesResource(@Context final HttpHeaders headers) {
        AdminEventBuilder adminEvent = getAdminBuilder(headers);
        adminEvent.organization(session.getContext().getOrganization()).resource(ResourceType.INVOICE);
        return new InvoicesResource(session, session.getContext().getOrganization(), adminEvent);
    }

    @Path("credit-notes")
    public CreditNotesResource getCreditNotesResource(@Context final HttpHeaders headers) {
        AdminEventBuilder adminEvent = getAdminBuilder(headers);
        adminEvent.organization(session.getContext().getOrganization()).resource(ResourceType.CREDIT_NOTE);
        return new CreditNotesResource(session, session.getContext().getOrganization(), adminEvent);
    }

    @Path("debit-notes")
    public DebitNotesResource getDebitNotesResource(@Context final HttpHeaders headers) {
        AdminEventBuilder adminEvent = getAdminBuilder(headers);
        adminEvent.organization(session.getContext().getOrganization()).resource(ResourceType.DEBIT_NOTE);
        return new DebitNotesResource(session, session.getContext().getOrganization(), adminEvent);
    }

    @Path("ubl-extensions/retentions")
    public RetentionsResource getRentionsResource(@Context final HttpHeaders headers) {
        AdminEventBuilder adminEvent = getAdminBuilder(headers);
        adminEvent.organization(session.getContext().getOrganization()).resource("RETENTION");
        return new RetentionsResource(session, session.getContext().getOrganization(), adminEvent);
    }

    @Path("ubl-extensions/perceptions")
    public PerceptionsResource getPerceptionsResource(@Context final HttpHeaders headers) {
        AdminEventBuilder adminEvent = getAdminBuilder(headers);
        adminEvent.organization(session.getContext().getOrganization()).resource("PERCEPTION");
        return new PerceptionsResource(session, session.getContext().getOrganization(), adminEvent);
    }

    @Path("ubl-extensions/sumary-documents")
    public SummaryDocumentsResource getSummaryDocumentsResource() {
        return new SummaryDocumentsResource(session, session.getContext().getOrganization());
    }

    @Path("ubl-extensions/voided-documents")
    public VoidedDocumentsResource getVoidedDocumentsResource() {
        return new VoidedDocumentsResource(session, session.getContext().getOrganization());
    }

    @Path("ubl-extensions/generic-types")
    public GenericTypesResource getGenericTypesResource() {
        return new GenericTypesResource(session, session.getContext().getOrganization());
    }
}