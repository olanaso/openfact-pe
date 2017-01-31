package org.openfact.pe.services.resources;

import oasis.names.specification.ubl.schema.xsd.creditnote_21.CreditNoteType;
import oasis.names.specification.ubl.schema.xsd.debitnote_21.DebitNoteType;
import oasis.names.specification.ubl.schema.xsd.invoice_21.InvoiceType;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.openfact.events.admin.OperationType;
import org.openfact.models.*;
import org.openfact.models.enums.DestinyType;
import org.openfact.models.enums.DocumentType;
import org.openfact.models.enums.SendResultType;
import org.openfact.models.utils.ModelToRepresentation;
import org.openfact.pe.models.SunatSendException;
import org.openfact.pe.models.utils.SunatRepresentationToType;
import org.openfact.pe.representations.idm.DocumentRepresentation;
import org.openfact.pe.services.managers.SunatDocumentManager;
import org.openfact.services.ErrorResponse;
import org.openfact.services.managers.DocumentManager;
import org.openfact.services.resources.admin.AdminEventBuilder;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author carlosthe19916@sistcoop.com
 */
@Consumes(MediaType.APPLICATION_JSON)
public class DocumentExtensionsResource {

    protected static final Logger logger = Logger.getLogger(DocumentExtensionsResource.class);

    private final OpenfactSession session;
    private final OrganizationModel organization;
    private final AdminEventBuilder adminEvent;

    public DocumentExtensionsResource(OpenfactSession session, OrganizationModel organization, AdminEventBuilder adminEvent) {
        this.session = session;
        this.organization = organization;
        this.adminEvent = adminEvent;
    }

    @POST
    @Path("invoices")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public Response createInvoice(DocumentRepresentation rep) {
        SunatDocumentManager sunatDocumentManager = new SunatDocumentManager(session);

        if (rep.getSerie() != null || rep.getNumero() != null) {
            if (rep.getSerie() != null && rep.getNumero() != null) {
                if (sunatDocumentManager.getDocumentByTypeAndDocumentId(DocumentType.INVOICE, rep.getSerie() + "-" + rep.getNumero(), organization) != null) {
                    return ErrorResponse.exists("Invoice exists with same documentId");
                }
            } else {
                return ErrorResponse.error("Numero de serie y/o numero invalido", Response.Status.BAD_REQUEST);
            }
        }

        try {
            InvoiceType invoiceType = SunatRepresentationToType.toInvoiceType(organization, rep);
            DocumentModel documentModel = sunatDocumentManager.addInvoice(invoiceType, generateAttributes(rep), organization);

            if (rep.isEnviarAutomaticamenteASunat()) {
                try {
                    sunatDocumentManager.sendToThirdParty(organization, documentModel);
                } catch (ModelInsuficientData e) {
                    if (session.getTransactionManager().isActive()) {
                        session.getTransactionManager().setRollbackOnly();
                    }
                    return ErrorResponse.error(e.getMessage(), Response.Status.BAD_REQUEST);
                } catch (SendException e) {
                    if (session.getTransactionManager().isActive()) {
                        session.getTransactionManager().setRollbackOnly();
                    }
                    if (e instanceof SunatSendException) {
                        return ErrorResponse.error(e.getMessage(), Response.Status.BAD_REQUEST);
                    } else {
                        return ErrorResponse.error("Could not send to sunat", Response.Status.BAD_REQUEST);
                    }
                }
            }

            if (rep.isEnviarAutomaticamenteAlCliente()) {
                SendEventModel customerSendEvent = documentModel.addSendEvent(DestinyType.CUSTOMER);
                try {
                    sunatDocumentManager.sendToCustomerParty(organization, documentModel, customerSendEvent);
                } catch (ModelInsuficientData e) {
                    customerSendEvent.setResult(SendResultType.ERROR);
                    customerSendEvent.setDescription(e.getMessage());
                } catch (SendException e) {
                    customerSendEvent.setResult(SendResultType.ERROR);
                    customerSendEvent.setDescription("Internal server error");
                    logger.error("Internal Server Error sending to customer", e);
                }
            }

            URI location = session.getContext().getUri().getAbsolutePathBuilder().path(documentModel.getId()).build();
            adminEvent.operation(OperationType.CREATE).resourcePath(location.toString()).representation(rep).success();
            return Response.created(location).entity(ModelToRepresentation.toRepresentation(documentModel)).build();
        } catch (ModelDuplicateException e) {
            if (session.getTransactionManager().isActive()) {
                session.getTransactionManager().setRollbackOnly();
            }
            return ErrorResponse.exists("Invoice exists with same id or documentId");
        } catch (ModelException e) {
            if (session.getTransactionManager().isActive()) {
                session.getTransactionManager().setRollbackOnly();
            }
            return ErrorResponse.exists("Invoice could not be created");
        }
    }

    @POST
    @Path("credit-notes")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCreditNote(DocumentRepresentation rep) {
        SunatDocumentManager sunatDocumentManager = new SunatDocumentManager(session);

        if (rep.getSerie() != null || rep.getNumero() != null) {
            if (rep.getSerie() != null && rep.getSerie() != null) {
                if (sunatDocumentManager.getDocumentByTypeAndDocumentId(DocumentType.CREDIT_NOTE, rep.getSerie() + "-" + rep.getNumero(), organization) != null) {
                    return ErrorResponse.exists("Credit Note exists with same documentId");
                }
            } else {
                return ErrorResponse.error("Numero de serie y/o numero invalido", Response.Status.BAD_REQUEST);
            }
        }

        try {
            CreditNoteType creditNoteType = SunatRepresentationToType.toCreditNoteType(organization, rep);
            DocumentModel documentModel = sunatDocumentManager.addCreditNote(creditNoteType, generateAttributes(rep), organization);

            if (rep.isEnviarAutomaticamenteASunat()) {
                try {
                    sunatDocumentManager.sendToThirdParty(organization, documentModel);
                } catch (ModelInsuficientData e) {
                    if (session.getTransactionManager().isActive()) {
                        session.getTransactionManager().setRollbackOnly();
                    }
                    return ErrorResponse.error(e.getMessage(), Response.Status.BAD_REQUEST);
                } catch (SendException e) {
                    if (session.getTransactionManager().isActive()) {
                        session.getTransactionManager().setRollbackOnly();
                    }
                    if (e instanceof SunatSendException) {
                        return ErrorResponse.error(e.getMessage(), Response.Status.BAD_REQUEST);
                    } else {
                        return ErrorResponse.error("Could not send to sunat", Response.Status.BAD_REQUEST);
                    }
                }
            }

            if (rep.isEnviarAutomaticamenteAlCliente()) {
                SendEventModel customerSendEvent = documentModel.addSendEvent(DestinyType.CUSTOMER);
                try {
                    sunatDocumentManager.sendToCustomerParty(organization, documentModel, customerSendEvent);
                } catch (ModelInsuficientData e) {
                    customerSendEvent.setResult(SendResultType.ERROR);
                    customerSendEvent.setDescription(e.getMessage());
                } catch (SendException e) {
                    customerSendEvent.setResult(SendResultType.ERROR);
                    customerSendEvent.setDescription("Internal server error");
                    logger.error("Internal Server Error sending to customer", e);
                }
            }

            URI location = session.getContext().getUri().getAbsolutePathBuilder().path(documentModel.getId()).build();
            adminEvent.operation(OperationType.CREATE).resourcePath(location.toString()).representation(rep).success();
            return Response.created(location).entity(ModelToRepresentation.toRepresentation(documentModel)).build();
        } catch (ModelDuplicateException e) {
            if (session.getTransactionManager().isActive()) {
                session.getTransactionManager().setRollbackOnly();
            }
            return ErrorResponse.exists("Credit Note exists with same id or documentId");
        } catch (ModelException e) {
            if (session.getTransactionManager().isActive()) {
                session.getTransactionManager().setRollbackOnly();
            }
            return ErrorResponse.exists("Credit Note could not be created");
        }
    }

    @POST
    @Path("debit-notes")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public Response createDebitNote(DocumentRepresentation rep) {
        SunatDocumentManager sunatDocumentManager = new SunatDocumentManager(session);

        if (rep.getSerie() != null || rep.getNumero() != null) {
            if (rep.getSerie() != null && rep.getSerie() != null) {
                if (sunatDocumentManager.getDocumentByTypeAndDocumentId(DocumentType.DEBIT_NOTE, rep.getSerie() + "-" + rep.getNumero(), organization) != null) {
                    return ErrorResponse.exists("Debit Note exists with same documentId");
                }
            } else {
                return ErrorResponse.error("Numero de serie y/o numero invalido", Response.Status.BAD_REQUEST);
            }
        }

        try {
            DebitNoteType debitNoteType = SunatRepresentationToType.toDebitNoteType(organization, rep);
            DocumentModel documentModel = sunatDocumentManager.addDebitNote(debitNoteType, generateAttributes(rep), organization);

            if (rep.isEnviarAutomaticamenteASunat()) {
                try {
                    sunatDocumentManager.sendToThirdParty(organization, documentModel);
                } catch (ModelInsuficientData e) {
                    if (session.getTransactionManager().isActive()) {
                        session.getTransactionManager().setRollbackOnly();
                    }
                    return ErrorResponse.error(e.getMessage(), Response.Status.BAD_REQUEST);
                } catch (SendException e) {
                    if (session.getTransactionManager().isActive()) {
                        session.getTransactionManager().setRollbackOnly();
                    }
                    if (e instanceof SunatSendException) {
                        return ErrorResponse.error(e.getMessage(), Response.Status.BAD_REQUEST);
                    } else {
                        return ErrorResponse.error("Could not send to sunat", Response.Status.BAD_REQUEST);
                    }
                }
            }

            if (rep.isEnviarAutomaticamenteAlCliente()) {
                SendEventModel customerSendEvent = documentModel.addSendEvent(DestinyType.CUSTOMER);
                try {
                    sunatDocumentManager.sendToCustomerParty(organization, documentModel, customerSendEvent);
                } catch (ModelInsuficientData e) {
                    customerSendEvent.setResult(SendResultType.ERROR);
                    customerSendEvent.setDescription(e.getMessage());
                } catch (SendException e) {
                    customerSendEvent.setResult(SendResultType.ERROR);
                    customerSendEvent.setDescription("Internal server error");
                    logger.error("Internal Server Error sending to customer", e);
                }
            }

            URI location = session.getContext().getUri().getAbsolutePathBuilder().path(documentModel.getId()).build();
            adminEvent.operation(OperationType.CREATE).resourcePath(location.toString()).representation(rep).success();
            return Response.created(location).entity(ModelToRepresentation.toRepresentation(documentModel)).build();
        } catch (ModelDuplicateException e) {
            if (session.getTransactionManager().isActive()) {
                session.getTransactionManager().setRollbackOnly();
            }
            return ErrorResponse.exists("Debit Note exists with same id or documentId");
        } catch (ModelException e) {
            if (session.getTransactionManager().isActive()) {
                session.getTransactionManager().setRollbackOnly();
            }
            return ErrorResponse.exists("Debit Note could not be created");
        }
    }

    private Map<String, List<String>> generateAttributes(DocumentRepresentation rep) {
        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put("operacionGratuita", Arrays.asList(String.valueOf(rep.isOperacionGratuita())));

        if (rep.getTotalGravada() != null) {
            attributes.put("totalGravada", Arrays.asList(String.valueOf(rep.getTotalGravada())));
        }
        if (rep.getTotalInafecta() != null) {
            attributes.put("totalInafecta", Arrays.asList(String.valueOf(rep.getTotalInafecta())));
        }
        if (rep.getTotalExonerada() != null) {
            attributes.put("totalExonerada", Arrays.asList(String.valueOf(rep.getTotalExonerada())));
        }
        if (rep.getTotalIgv() != null) {
            attributes.put("totalIgv", Arrays.asList(String.valueOf(rep.getTotalIgv())));
        }
        if (rep.getTotalGratuita() != null) {
            attributes.put("totalGratuita", Arrays.asList(String.valueOf(rep.getTotalGratuita())));
        }
        if (rep.getIgv() != null) {
            attributes.put("igv", Arrays.asList(String.valueOf(rep.getIgv())));
        }
        if (rep.getPorcentajeDescuento() != null) {
            attributes.put("porcentajeDescuento", Arrays.asList(String.valueOf(rep.getPorcentajeDescuento())));
        }
        if (rep.getTotalOtrosCargos() != null) {
            attributes.put("totalOtrosCargos", Arrays.asList(String.valueOf(rep.getTotalOtrosCargos())));
        }
        return attributes;
    }

}