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
import org.openfact.models.DebitNoteModel;
import org.openfact.models.ModelDuplicateException;
import org.openfact.models.ModelException;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.pe.models.utils.SunatRepresentationToType;
import org.openfact.pe.representations.idm.DocumentRepresentation;
import org.openfact.services.ErrorResponse;
import org.openfact.services.managers.DebitNoteManager;
import org.openfact.ubl.SendException;

import oasis.names.specification.ubl.schema.xsd.debitnote_21.DebitNoteType;

/**
 * @author carlosthe19916@sistcoop.com
 */
@Consumes(MediaType.APPLICATION_JSON)
public class DebitNotesResource {

    protected static final Logger logger = Logger.getLogger(DebitNotesResource.class);

    private final OpenfactSession session;
    private final OrganizationModel organization;

    @Context
    protected UriInfo uriInfo;

    public DebitNotesResource(OpenfactSession session, OrganizationModel organization) {
        this.session = session;
        this.organization = organization;
    }

    @POST
    @Path("")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public Response createDebitNote(DocumentRepresentation rep) {
        DebitNoteManager debitNoteManager = new DebitNoteManager(session);

        // Double-check duplicated ID
        if (rep.getSerie() != null && rep.getNumero() != null && debitNoteManager
                .getDebitNoteByID(organization, rep.getSerie() + "-" + rep.getNumero()) != null) {
            return ErrorResponse.exists("Debit Note exists with same documentId");
        }

        try {
            DebitNoteType debitNoteType = SunatRepresentationToType.toDebitNoteType(rep);
            DebitNoteModel debitNote = debitNoteManager.addDebitNote(organization, debitNoteType, Collections.emptyMap());
            if (session.getTransactionManager().isActive()) {
                session.getTransactionManager().commit();
            }

            // Enviar a Cliente
            if (rep.isEnviarAutomaticamenteAlCliente()) {
                debitNoteManager.sendToCustomerParty(organization, debitNote);
            }

            // Enviar Sunat
            if (rep.isEnviarAutomaticamenteASunat()) {
                debitNoteManager.sendToTrirdParty(organization, debitNote);
            }

            URI location = uriInfo.getAbsolutePathBuilder().path(debitNote.getId()).build();
            return Response.created(location).build();
        } catch (ModelDuplicateException e) {
            if (session.getTransactionManager().isActive()) {
                session.getTransactionManager().setRollbackOnly();
            }
            return ErrorResponse.exists("Debit Note exists with same id or documentId");
        } catch (ModelException me) {
            if (session.getTransactionManager().isActive()) {
                session.getTransactionManager().setRollbackOnly();
            }
            return ErrorResponse.exists("Could not create debit note");
        } catch (SendException me) {
            if (session.getTransactionManager().isActive()) {
                session.getTransactionManager().setRollbackOnly();
            }
            return ErrorResponse.exists("Could not send Debit note");
        } 
    }

}