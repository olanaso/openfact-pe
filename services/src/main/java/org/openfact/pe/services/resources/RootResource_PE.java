package org.openfact.pe.services.resources;

import javax.ws.rs.Path;

import org.openfact.models.OpenfactSession;

public class RootResource_PE {

    private final OpenfactSession session;

    public RootResource_PE(OpenfactSession session) {
        this.session = session;
    }

    @Path("retentions")
    public RetentionsResource getRentionsResource() {
        return new RetentionsResource(session);
    }

    @Path("perceptions")
    public PerceptionsResource getPerceptionsResource() {
        return new PerceptionsResource(session);
    }

    @Path("sumary-documents")
    public SummaryDocumentsResource getSummaryDocumentsResource() {
        return new SummaryDocumentsResource(session);
    }

    @Path("voided-documents")
    public VoidedDocumentsResource getVoidedDocumentsResource() {
        return new VoidedDocumentsResource(session);
    }

}