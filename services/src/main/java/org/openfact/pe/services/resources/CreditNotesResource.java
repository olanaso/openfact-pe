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
import org.openfact.models.CreditNoteModel;
import org.openfact.models.ModelDuplicateException;
import org.openfact.models.ModelException;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.pe.models.utils.SunatRepresentationToType;
import org.openfact.pe.representations.idm.DocumentRepresentation;
import org.openfact.services.ErrorResponse;
import org.openfact.services.managers.CreditNoteManager;

import oasis.names.specification.ubl.schema.xsd.creditnote_21.CreditNoteType;

/**
 * @author carlosthe19916@sistcoop.com
 */
@Consumes(MediaType.APPLICATION_JSON)
public class CreditNotesResource {

    protected static final Logger logger = Logger.getLogger(CreditNotesResource.class);

    private final OpenfactSession session;
    private final OrganizationModel organization;

    @Context
    protected UriInfo uriInfo;

    public CreditNotesResource(OpenfactSession session, OrganizationModel organization) {
        this.session = session;
        this.organization = organization;
    }

    @POST
    @Path("")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCreditNote(DocumentRepresentation rep) {
        CreditNoteManager creditNoteManager = new CreditNoteManager(session);

        // Double-check duplicated ID
        if (rep.getSerie() != null && rep.getNumero() != null && creditNoteManager.getCreditNoteByID(organization, rep.getSerie() + "-" + rep.getNumero()) != null) {
            return ErrorResponse.exists("Credit Note exists with same documentId");
        }

        try {
            CreditNoteType creditNoteType = SunatRepresentationToType.toCreditNoteType(rep);
            CreditNoteModel creditNote = creditNoteManager.addCreditNote(organization, creditNoteType, Collections.emptyMap());
            if (session.getTransactionManager().isActive()) {
                session.getTransactionManager().commit();
            }

            // Enviar a Cliente
            if (rep.isEnviarAutomaticamenteAlCliente()) {
                creditNoteManager.sendToCustomerParty(organization, creditNote);
            }

            // Enviar Sunat
            if (rep.isEnviarAutomaticamenteASunat()) {
                creditNoteManager.sendToTrirdParty(organization, creditNote);
            }

            URI location = uriInfo.getAbsolutePathBuilder().path(creditNote.getId()).build();
            return Response.created(location).build();
        } catch (ModelDuplicateException e) {
            if (session.getTransactionManager().isActive()) {
                session.getTransactionManager().setRollbackOnly();
            }
            return ErrorResponse.exists("Credit Note exists with same id or documentId");
        } catch (ModelException me) {
            if (session.getTransactionManager().isActive()) {
                session.getTransactionManager().setRollbackOnly();
            }
            return ErrorResponse.exists("Could not create credit note");
        }
    }

}