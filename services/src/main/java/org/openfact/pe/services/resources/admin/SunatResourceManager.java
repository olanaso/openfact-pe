package org.openfact.pe.services.resources.admin;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.IDType;
import oasis.names.specification.ubl.schema.xsd.creditnote_21.CreditNoteType;
import oasis.names.specification.ubl.schema.xsd.debitnote_21.DebitNoteType;
import oasis.names.specification.ubl.schema.xsd.invoice_21.InvoiceType;
import org.jboss.logging.Logger;
import org.openfact.events.admin.OperationType;
import org.openfact.models.*;
import org.openfact.models.jpa.entities.SerieNumeroController;
import org.openfact.models.jpa.entities.SerieNumeroEntity;
import org.openfact.models.types.DestinyType;
import org.openfact.models.types.DocumentType;
import org.openfact.models.types.SendEventStatus;
import org.openfact.pe.models.utils.SunatRepresentationToType;
import org.openfact.pe.models.utils.SunatTypeToModel;
import org.openfact.pe.representations.idm.DocumentRepresentation;
import org.openfact.pe.representations.idm.VoidedRepresentation;
import org.openfact.pe.ubl.types.SunatDocumentType;
import org.openfact.pe.ubl.ubl21.voided.VoidedDocumentsType;
import org.openfact.services.ModelErrorResponseException;
import org.openfact.services.managers.DocumentManager;
import org.openfact.services.resource.security.OrganizationAuth;
import org.openfact.ubl.UBLIDGenerator;
import org.openfact.ubl.utils.UBLUtil;

import javax.ejb.*;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Stateless
public class SunatResourceManager {

    private static final Logger logger = Logger.getLogger(SunatResourceManager.class);

    @Inject
    private DocumentProvider documentProvider;

    @Inject
    private DocumentManager documentManager;

    @Inject
    private OrganizationProvider organizationProvider;

    @Inject
    private SunatRepresentationToType representationToType;

    @Inject
    private UBLUtil ublUtil;

    @Inject
    private SerieNumeroController serieNumeroController;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public DocumentModel createInvoice(OrganizationModel organization, DocumentRepresentation rep) throws ModelException {
        InvoiceType invoiceType = representationToType.toInvoiceType(organization, rep);

        IDType documentId = invoiceType.getID();
        if (documentId == null) {
            UBLIDGenerator<InvoiceType> ublIDGenerator = ublUtil.getIDGenerator(DocumentType.INVOICE);
            String newDocumentId = ublIDGenerator.generateID(organization, invoiceType);
            documentId = new IDType(newDocumentId);
            invoiceType.setID(documentId);
        } else {
            String[] assignedID = documentId.getValue().split("-");
            String serieString = assignedID[0];
            String numeroString = assignedID[1];

            Pattern pattern = Pattern.compile("F[0-9]{3}");
            Matcher matcher = pattern.matcher(serieString);
            if (matcher.matches()) {
                int customSerie = Integer.parseInt(serieString.substring(1));
                int customNumero = Integer.parseInt(numeroString);
                String firstLetterSerie = serieString.substring(0, 1);

                Optional<SerieNumeroEntity> serieNumeroDB = serieNumeroController.getSerieNumero(organization, DocumentType.INVOICE.toString(), firstLetterSerie);
                if (serieNumeroDB.isPresent()) {
                    SerieNumeroEntity serieNumeroEntity = serieNumeroDB.get();
                    if (customSerie > serieNumeroEntity.getSerie()) {
                        // poner nueva serie y numero
                        serieNumeroEntity.setSerie(customSerie);
                        serieNumeroEntity.setNumero(customNumero);
                        serieNumeroController.updateSerieNumero(serieNumeroEntity);
                    } else if (customSerie == serieNumeroEntity.getSerie()){
                        if (customNumero > serieNumeroEntity.getNumero()) {
                            // poner serie igual pero incrementar el numero
                            serieNumeroEntity.setNumero(customNumero);
                            serieNumeroController.updateSerieNumero(serieNumeroEntity);
                        }
                    }
                } else {
                    // poner serie y numero de rep
                    SerieNumeroEntity serieNumeroEntity = new SerieNumeroEntity();
                    serieNumeroEntity.setDocumentType(DocumentType.INVOICE.toString());
                    serieNumeroEntity.setOrganizationId(organization.getId());
                    serieNumeroEntity.setSerie(customSerie);
                    serieNumeroEntity.setNumero(customNumero);
                    serieNumeroEntity.setFirstLetter(firstLetterSerie);

                    serieNumeroController.createSerieNumero(serieNumeroEntity);
                }
            }
        }

        DocumentModel document = documentManager.addDocument(organization, invoiceType.getIDValue(), DocumentType.INVOICE, invoiceType);
        generateAttributes(rep, document);
        return document;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public DocumentModel createCreditNote(OrganizationModel organization, DocumentRepresentation rep) throws ModelException {
        CreditNoteType creditNoteType = representationToType.toCreditNoteType(organization, rep);

        IDType documentId = creditNoteType.getID();
        if (documentId == null) {
            UBLIDGenerator ublIDGenerator = ublUtil.getIDGenerator(DocumentType.CREDIT_NOTE);
            String newDocumentId = ublIDGenerator.generateID(organization, creditNoteType);
            documentId = new IDType(newDocumentId);
            creditNoteType.setID(documentId);
        } else {
            String[] assignedID = documentId.getValue().split("-");
            String serieString = assignedID[0];
            String numeroString = assignedID[1];

            Pattern pattern = Pattern.compile("[F|B][0-9]{3}");
            Matcher matcher = pattern.matcher(serieString);
            if (matcher.matches()) {
                int customSerie = Integer.parseInt(serieString.substring(1));
                int customNumero = Integer.parseInt(numeroString);
                String firstLetterSerie = serieString.substring(0, 1);

                Optional<SerieNumeroEntity> serieNumeroDB = serieNumeroController.getSerieNumero(organization, DocumentType.CREDIT_NOTE.toString(), firstLetterSerie);
                if (serieNumeroDB.isPresent()) {
                    SerieNumeroEntity serieNumeroEntity = serieNumeroDB.get();
                    if (customSerie > serieNumeroEntity.getSerie()) {
                        // poner nueva serie y numero
                        serieNumeroEntity.setSerie(customSerie);
                        serieNumeroEntity.setNumero(customNumero);
                        serieNumeroController.updateSerieNumero(serieNumeroEntity);
                    } else if (customSerie == serieNumeroEntity.getSerie()){
                        if (customNumero > serieNumeroEntity.getNumero()) {
                            // poner serie igual pero incrementar el numero
                            serieNumeroEntity.setNumero(customNumero);
                            serieNumeroController.updateSerieNumero(serieNumeroEntity);
                        }
                    }
                } else {
                    // poner serie y numero de rep
                    SerieNumeroEntity serieNumeroEntity = new SerieNumeroEntity();
                    serieNumeroEntity.setDocumentType(DocumentType.CREDIT_NOTE.toString());
                    serieNumeroEntity.setOrganizationId(organization.getId());
                    serieNumeroEntity.setSerie(customSerie);
                    serieNumeroEntity.setNumero(customNumero);
                    serieNumeroEntity.setFirstLetter(firstLetterSerie);

                    serieNumeroController.createSerieNumero(serieNumeroEntity);
                }
            }
        }

        DocumentModel document = documentManager.addDocument(organization, creditNoteType.getIDValue(), DocumentType.CREDIT_NOTE, creditNoteType);
        generateAttributes(rep, document);
        return document;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public DocumentModel createDebitNote(OrganizationModel organization, DocumentRepresentation rep) throws ModelException {
        DebitNoteType debitNoteType = representationToType.toDebitNoteType(organization, rep);

        IDType documentId = debitNoteType.getID();
        if (documentId == null) {
            UBLIDGenerator ublIDGenerator = ublUtil.getIDGenerator(DocumentType.DEBIT_NOTE);
            String newDocumentId = ublIDGenerator.generateID(organization, debitNoteType);
            documentId = new IDType(newDocumentId);
            debitNoteType.setID(documentId);
        } else {
            String[] assignedID = documentId.getValue().split("-");
            String serieString = assignedID[0];
            String numeroString = assignedID[1];

            Pattern pattern = Pattern.compile("[F|B][0-9]{3}");
            Matcher matcher = pattern.matcher(serieString);
            if (matcher.matches()) {
                int customSerie = Integer.parseInt(serieString.substring(1));
                int customNumero = Integer.parseInt(numeroString);
                String firstLetterSerie = serieString.substring(0, 1);

                Optional<SerieNumeroEntity> serieNumeroDB = serieNumeroController.getSerieNumero(organization, DocumentType.DEBIT_NOTE.toString(), firstLetterSerie);
                if (serieNumeroDB.isPresent()) {
                    SerieNumeroEntity serieNumeroEntity = serieNumeroDB.get();
                    if (customSerie > serieNumeroEntity.getSerie()) {
                        // poner nueva serie y numero
                        serieNumeroEntity.setSerie(customSerie);
                        serieNumeroEntity.setNumero(customNumero);
                        serieNumeroController.updateSerieNumero(serieNumeroEntity);
                    } else if (customSerie == serieNumeroEntity.getSerie()){
                        if (customNumero > serieNumeroEntity.getNumero()) {
                            // poner serie igual pero incrementar el numero
                            serieNumeroEntity.setNumero(customNumero);
                            serieNumeroController.updateSerieNumero(serieNumeroEntity);
                        }
                    }
                } else {
                    // poner serie y numero de rep
                    SerieNumeroEntity serieNumeroEntity = new SerieNumeroEntity();
                    serieNumeroEntity.setDocumentType(DocumentType.DEBIT_NOTE.toString());
                    serieNumeroEntity.setOrganizationId(organization.getId());
                    serieNumeroEntity.setSerie(customSerie);
                    serieNumeroEntity.setNumero(customNumero);
                    serieNumeroEntity.setFirstLetter(firstLetterSerie);

                    serieNumeroController.createSerieNumero(serieNumeroEntity);
                }
            }
        }

        DocumentModel document = documentManager.addDocument(organization, debitNoteType.getIDValue(), DocumentType.DEBIT_NOTE, debitNoteType);
        generateAttributes(rep, document);
        return document;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public DocumentModel createVoidedDocuments(OrganizationModel organization, VoidedRepresentation rep) throws ModelException {
        VoidedDocumentsType voidedDocumentsType = representationToType.toVoidedDocumentType(organization, rep);
        setVoidedDocumentIDType(organization, voidedDocumentsType);

        DocumentModel document = documentManager.addDocument(organization, voidedDocumentsType.getID().getValue(), SunatDocumentType.VOIDED_DOCUMENTS.toString(), voidedDocumentsType);
        return document;
    }

    private void setVoidedDocumentIDType(OrganizationModel organization, final VoidedDocumentsType voidedDocumentsType) {
        IDType documentId = voidedDocumentsType.getID();
        if (documentId == null || documentId.getValue() == null) {
            UBLIDGenerator ublIDGenerator = ublUtil.getIDGenerator(SunatDocumentType.VOIDED_DOCUMENTS.toString());
            String newDocumentId = ublIDGenerator.generateID(organization, voidedDocumentsType);
            documentId = new IDType(newDocumentId);
            voidedDocumentsType.setID(documentId);
        }
    }

    private void generateAttributes(DocumentRepresentation representation, DocumentModel document) {
        document.setSingleAttribute(SunatTypeToModel.ES_OPERACION_GRATUITA, String.valueOf(representation.isOperacionGratuita()));

        if (representation.getTotalGravada() != null) {
            document.setSingleAttribute(SunatTypeToModel.TOTAL_OPERACIONES_GRAVADAS, representation.getTotalGravada().toString());
        }
        if (representation.getTotalInafecta() != null) {
            document.setSingleAttribute(SunatTypeToModel.TOTAL_OPERACIONES_INAFECTAS, representation.getTotalInafecta().toString());
        }
        if (representation.getTotalExonerada() != null) {
            document.setSingleAttribute(SunatTypeToModel.TOTAL_OPERACIONES_EXONERADAS, representation.getTotalExonerada().toString());
        }
        if (representation.getTotalIgv() != null) {
            document.setSingleAttribute(SunatTypeToModel.TOTAL_IGV, representation.getTotalIgv().toString());
        }
        if (representation.getTotalGratuita() != null) {
            document.setSingleAttribute(SunatTypeToModel.TOTAL_OPERACIONES_GRATUITAS, representation.getTotalGratuita().toString());
        }
        if (representation.getIgv() != null) {
            document.setSingleAttribute(SunatTypeToModel.VALOR_IGV, representation.getIgv().toString());
        }
        if (representation.getPorcentajeDescuento() != null) {
            document.setSingleAttribute(SunatTypeToModel.PORCENTAJE_DESCUENTO_GLOBAL_APLICADO, representation.getPorcentajeDescuento().toString());
        }
        if (representation.getTotalOtrosCargos() != null) {
            document.setSingleAttribute(SunatTypeToModel.TOTAL_OTROS_CARGOS_APLICADO, representation.getTotalOtrosCargos().toString());
        }
    }

    @Asynchronous
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Future<Boolean> sendDocumentToThirdParty(String organizationId, String documentId) throws ModelErrorResponseException {
        OrganizationModel organization = organizationProvider.getOrganization(organizationId);
        DocumentModel document = documentProvider.getDocumentById(documentId, organization);
        try {
            documentManager.sendToThirdParty(organization, document);
            return new AsyncResult<>(true);
        } catch (ModelInsuficientData | SendEventException e) {
            logger.error(e);
            return new AsyncResult<>(false);
        }
    }

    @Asynchronous
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Future<Boolean> sendDocumentToCustomer(String organizationId, String documentId) throws ModelErrorResponseException {
        OrganizationModel organization = organizationProvider.getOrganization(organizationId);
        DocumentModel document = documentProvider.getDocumentById(documentId, organization);
        try {
            documentManager.sendToCustomerParty(organization, document);
            return new AsyncResult<>(true);
        } catch (ModelInsuficientData | SendEventException e) {
            logger.error(e.getMessage(), e);
            SendEventModel sendEventModel = document.addSendEvent(DestinyType.CUSTOMER);
            sendEventModel.setResult(SendEventStatus.ERROR);
            sendEventModel.setDescription("Error sending email");
            return new AsyncResult<>(false);
        }
    }


}
