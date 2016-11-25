package org.openfact.pe.services.resources;

import java.net.URI;
import java.util.Collections;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.openfact.models.InvoiceModel;
import org.openfact.models.ModelDuplicateException;
import org.openfact.models.ModelException;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.pe.models.utils.SunatRepresentationToType;
import org.openfact.pe.representations.idm.DocumentRepresentation;
import org.openfact.services.ErrorResponse;
import org.openfact.services.managers.InvoiceManager;

import oasis.names.specification.ubl.schema.xsd.invoice_21.InvoiceType;

/**
 * @author carlosthe19916@sistcoop.com
 */
@Consumes(MediaType.APPLICATION_JSON)
public class InvoicesResource {

    protected static final Logger logger = Logger.getLogger(InvoicesResource.class);

    private final OpenfactSession session;
    private final OrganizationModel organization;

    @Context
    protected UriInfo uriInfo;

    public InvoicesResource(OpenfactSession session, OrganizationModel organization) {
        this.session = session;
        this.organization = organization;
    }

    @POST
    @Path("")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public Response createInvoice(DocumentRepresentation rep) {
        InvoiceManager invoiceManager = new InvoiceManager(session);

        // Double-check duplicated ID
        if (rep.getSerie() != null && rep.getNumero() != null && invoiceManager.getInvoiceByID(organization, rep.getSerie() + "-" + rep.getNumero()) != null) {
            return ErrorResponse.exists("Invoice exists with same documentId");
        }

        try {
            InvoiceType invoiceType = SunatRepresentationToType.toInvoiceType(organization, rep);
            InvoiceModel invoice = invoiceManager.addInvoice(organization, invoiceType, Collections.emptyMap());
            if (session.getTransactionManager().isActive()) {
                session.getTransactionManager().commit();
            }

            // Enviar a Cliente
            if (rep.isEnviarAutomaticamenteAlCliente()) {
                invoiceManager.sendToCustomerParty(organization, invoice);
            }

            // Enviar Sunat
            if (rep.isEnviarAutomaticamenteASunat()) {
                invoiceManager.sendToTrirdParty(organization, invoice);
            }

            URI location = uriInfo.getAbsolutePathBuilder().path(invoice.getId()).build();
            return Response.created(location).build();
        } catch (ModelDuplicateException e) {
            if (session.getTransactionManager().isActive()) {
                session.getTransactionManager().setRollbackOnly();
            }
            return ErrorResponse.exists("Invoice exists with same id or documentId");
        } catch (ModelException me) {
            if (session.getTransactionManager().isActive()) {
                session.getTransactionManager().setRollbackOnly();
            }
            return ErrorResponse.exists("Could not create invoice");
        }
    }

}