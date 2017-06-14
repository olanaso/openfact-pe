package org.sistcoop.admin.client.resource;

import org.openfact.models.ModelException;
import org.openfact.pe.representations.idm.DocumentRepresentation;
import org.openfact.pe.representations.idm.DocumentoSunatRepresentation;
import org.openfact.pe.representations.idm.VoidedRepresentation;
import org.openfact.services.ModelErrorResponseException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Consumes(MediaType.APPLICATION_JSON)
public interface OrganizationSunatResource extends OrganizationResource {

    @POST
    @Path("/sunat/documents/invoices")
    @Produces(MediaType.APPLICATION_JSON)
    Response createInvoice(DocumentRepresentation rep) throws ModelErrorResponseException, ModelException;

    @POST
    @Path("/sunat/documents/credit-notes")
    @Produces(MediaType.APPLICATION_JSON)
    Response createCreditNote(DocumentRepresentation rep) throws ModelErrorResponseException;


    @POST
    @Path("/sunat/documents/debit-notes")
    @Produces(MediaType.APPLICATION_JSON)
    Response createDebitNote(DocumentRepresentation rep) throws ModelErrorResponseException;

    @POST
    @Path("/sunat/documents/perceptions")
    @Produces(MediaType.APPLICATION_JSON)
    Response createPerception(DocumentoSunatRepresentation rep) throws ModelErrorResponseException;

    @POST
    @Path("/sunat/documents/retentions")
    @Produces(MediaType.APPLICATION_JSON)
    Response createRetention(DocumentoSunatRepresentation rep) throws ModelErrorResponseException;

    @POST
    @Path("/sunat/documents/voided-documents")
    @Produces(MediaType.APPLICATION_JSON)
    Response createRetention(VoidedRepresentation rep) throws ModelErrorResponseException;

    @GET
    @Path("/documents/{documentId}/cdr")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    Response getCdr(@PathParam("documentId") final String documentIdPk);

    @GET
    @Path("/documents/{documentId}/check-ticket")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    Response checkTicket(@PathParam("documentId") final String documentIdPk) throws ModelErrorResponseException;
}
