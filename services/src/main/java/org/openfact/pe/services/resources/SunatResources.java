package org.openfact.pe.services.resources;

import javax.ws.rs.Path;

import org.openfact.models.OpenfactSession;

public class SunatResources {

    private final OpenfactSession session;

    public SunatResources(OpenfactSession session) {
        this.session = session;
    }

    @Path("invoices")
    public InvoicesResource getInvoicesResource() {
        return new InvoicesResource(session, session.getContext().getOrganization());
    }

    @Path("credit-notes")
    public CreditNotesResource getCreditNotesResource() {
        return new CreditNotesResource(session, session.getContext().getOrganization());
    }

    @Path("debit-notes")
    public DebitNotesResource getDebitNotesResource() {
        return new DebitNotesResource(session, session.getContext().getOrganization());
    }

    @Path("ubl-extensions/retentions")
    public RetentionsResource getRentionsResource() {
        return new RetentionsResource(session, session.getContext().getOrganization());
    }

    @Path("ubl-extensions/perceptions")
    public PerceptionsResource getPerceptionsResource() {
        return new PerceptionsResource(session, session.getContext().getOrganization());
    }

    @Path("ubl-extensions/sumary-documents")
    public SummaryDocumentsResource getSummaryDocumentsResource() {
        return new SummaryDocumentsResource(session, session.getContext().getOrganization());
    }

    @Path("ubl-extensions/voided-documents")
    public VoidedDocumentsResource getVoidedDocumentsResource() {
        return new VoidedDocumentsResource(session, session.getContext().getOrganization());
    }
    @Path("ubl-extensions/storage-files")
    public SunatStorageFilesAdminResource getSunatStorageFilesAdminResource() {
        return new SunatStorageFilesAdminResource(session, session.getContext().getOrganization());
    }
}