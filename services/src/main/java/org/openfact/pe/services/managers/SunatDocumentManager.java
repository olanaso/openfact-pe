/*******************************************************************************
 * Copyright 2016 Sistcoop, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.openfact.pe.services.managers;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.InvoiceLineType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.TaxSubtotalType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.TaxTotalType;
import oasis.names.specification.ubl.schema.xsd.invoice_21.InvoiceType;
import org.jboss.logging.Logger;
import org.openfact.common.converts.DocumentUtils;
import org.openfact.file.FileModel;
import org.openfact.file.FileProvider;
import org.openfact.models.*;
import org.openfact.models.enums.DocumentType;
import org.openfact.models.enums.RequiredAction;
import org.openfact.models.utils.OpenfactModelUtils;
import org.openfact.models.utils.TypeToModel;
import org.openfact.pe.models.UBLPerceptionProvider;
import org.openfact.pe.models.UBLRetentionProvider;
import org.openfact.pe.models.UBLSummaryDocumentProvider;
import org.openfact.pe.models.UBLVoidedDocumentProvider;
import org.openfact.pe.models.enums.SunatDocumentType;
import org.openfact.pe.models.enums.SunatRequiredAction;
import org.openfact.pe.models.enums.TipoDocumentoRelacionadoPercepcionRetencion;
import org.openfact.pe.models.types.perception.PerceptionType;
import org.openfact.pe.models.types.retention.RetentionType;
import org.openfact.pe.models.types.summary.SummaryDocumentsType;
import org.openfact.pe.models.types.voided.VoidedDocumentsType;
import org.openfact.pe.models.utils.*;
import org.openfact.pe.services.ubl.SunatUBLIDGenerator;
import org.openfact.services.managers.DocumentManager;
import org.openfact.ubl.*;
import org.w3c.dom.Document;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.IDType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SunatDocumentManager extends DocumentManager {

    protected static final Logger logger = Logger.getLogger(SunatDocumentManager.class);

    public SunatDocumentManager(OpenfactSession session) {
        super(session);
    }

    public DocumentModel getDocumentByTypeAndDocumentId(SunatDocumentToType type, String documentId, OrganizationModel organization) {
        return model.getDocumentByTypeAndDocumentId(type.toString(), documentId, organization);
    }

    @Override
    public DocumentModel addInvoice(InvoiceType invoiceType, Map<String, List<String>> attributes, OrganizationModel organization) {
        DocumentModel invoice = super.addInvoice(invoiceType, attributes, organization);

        if (invoiceType.getInvoiceLine() != null && !invoiceType.getInvoiceLine().isEmpty()) {
            Consumer<InvoiceLineType> consumer = invoiceLineType -> {
                DocumentLineModel documentLine = invoice.addDocumentLine();
                if (invoiceLineType.getItem() != null && invoiceLineType.getItem().getDescription() != null && !invoiceLineType.getItem().getDescription().isEmpty()) {
                    documentLine.setAttribute("itemDescription", invoiceLineType.getItem().getDescription().stream().map(f -> f.getValue()).collect(Collectors.joining(",")));
                }
                if (invoiceLineType.getInvoicedQuantity() != null) {
                    documentLine.setAttribute("quantity", invoiceLineType.getInvoicedQuantityValue());
                }
                if (invoiceLineType.getPrice() != null && invoiceLineType.getPrice().getPriceAmount() != null) {
                    documentLine.setAttribute("priceAmount", invoiceLineType.getPrice().getPriceAmountValue());
                }
                if (invoiceLineType.getTaxTotal() != null && !invoiceLineType.getTaxTotal().isEmpty()) {
                    // IGV
                    TaxTotalType taxTotalType = invoiceLineType.getTaxTotal().get(0);//.getTaxSubtotal().get(0).getTaxCategory().getTaxExemptionReasonCode().getValue();
                    if (taxTotalType.getTaxSubtotal() != null && !taxTotalType.getTaxSubtotal().isEmpty()) {
                        TaxSubtotalType taxSubtotalType = taxTotalType.getTaxSubtotal().get(0);
                        if (taxSubtotalType.getTaxCategory() != null && taxSubtotalType.getTaxCategory().getTaxExemptionReasonCode() != null) {
                            documentLine.setAttribute("taxExemptionReasonCodeIGV", taxSubtotalType.getTaxCategory().getTaxExemptionReasonCodeValue());
                        }
                    }
                }
            };

            invoiceType.getInvoiceLine().forEach(consumer);
        }

        return invoice;
    }

    public DocumentModel addPerception(OrganizationModel organization, PerceptionType perceptionType) {
        IDType documentId = perceptionType.getId();
        if (documentId == null || documentId.getValue() == null) {
            String generatedId = SunatUBLIDGenerator.generatePerceptionDocumentId(session, organization);
            documentId = new IDType(generatedId);
            perceptionType.setId(documentId);
        }
        DocumentModel documentModel = model.addDocument(SunatDocumentType.PERCEPTION.toString(), documentId.getValue(), organization);

        SunatTypeToModel.importPerception(session, organization, documentModel, perceptionType);
        RequiredAction.getDefaults().stream().forEach(c -> documentModel.addRequiredAction(c));

        try {
            // Generate Document
            Document baseDocument = SunatTypeToDocument.toDocument(organization, perceptionType);
            SunatSignerUtils.removeSignature(baseDocument);
            Document signedDocument = session.getProvider(SignerProvider.class).sign(baseDocument, organization);
            byte[] signedDocumentBytes = DocumentUtils.getBytesFromDocument(signedDocument);

            // File
            FileModel xmlFile = session.files().createFile(organization, documentModel.getDocumentId() + ".xml", signedDocumentBytes);
            documentModel.attachXmlFile(xmlFile);
        } catch (JAXBException e) {
            logger.error("Error on marshal model", e);
            throw new ModelException(e);
        } catch (TransformerException e) {
            logger.error("Error parsing to byte XML", e);
            throw new ModelException(e);
        }

        if (perceptionType.getSunatPerceptionDocumentReference() != null && !perceptionType.getSunatPerceptionDocumentReference().isEmpty()) {
            perceptionType.getSunatPerceptionDocumentReference().stream().forEach(c -> {
                String attachedDocumentId = c.getId().getValue();
                String attachedDocumentCodeType = c.getId().getSchemeID();
                Optional<TipoDocumentoRelacionadoPercepcionRetencion> tipoDocumentoRelacionadoPercepcion = Arrays
                        .stream(TipoDocumentoRelacionadoPercepcionRetencion.values())
                        .filter(p -> p.getCodigo().equals(attachedDocumentCodeType))
                        .findAny();
                if (tipoDocumentoRelacionadoPercepcion.isPresent()) {
                    DocumentModel attachedDocument = session.documents().getDocumentByTypeAndDocumentId(tipoDocumentoRelacionadoPercepcion.get().getDocumentType(), attachedDocumentId, organization);
                    if (attachedDocument != null) {
                        documentModel.addAttachedDocument(attachedDocument);
                    }
                }
            });
        }

        fireDocumentPostCreate(documentModel);
        return documentModel;
    }

    public DocumentModel addRetention(OrganizationModel organization, RetentionType retentionType) {
        IDType documentId = retentionType.getId();
        if (documentId == null || documentId.getValue() == null) {
            String generatedId = SunatUBLIDGenerator.generateRetentionDocumentId(session, organization);
            documentId = new IDType(generatedId);
            retentionType.setId(documentId);
        }
        DocumentModel documentModel = model.addDocument(SunatDocumentType.RETENTION.toString(), documentId.getValue(), organization);
        SunatTypeToModel.importRetention(session, organization, documentModel, retentionType);
        RequiredAction.getDefaults().stream().forEach(c -> documentModel.addRequiredAction(c));

        try {
            // Generate Document
            Document baseDocument = SunatTypeToDocument.toDocument(organization, retentionType);
            SunatSignerUtils.removeSignature(baseDocument);
            Document signedDocument = session.getProvider(SignerProvider.class).sign(baseDocument, organization);
            byte[] signedDocumentBytes = DocumentUtils.getBytesFromDocument(signedDocument);

            // File
            FileModel xmlFile = session.files().createFile(organization, OpenfactModelUtils.generateId() + ".xml", signedDocumentBytes);
            documentModel.attachXmlFile(xmlFile);
        } catch (JAXBException e) {
            logger.error("Error on marshal model", e);
            throw new ModelException(e);
        } catch (TransformerException e) {
            logger.error("Error parsing to byte XML", e);
            throw new ModelException(e);
        }

        if (retentionType.getSunatRetentionDocumentReference() != null && !retentionType.getSunatRetentionDocumentReference().isEmpty()) {
            retentionType.getSunatRetentionDocumentReference().stream().forEach(c -> {
                String attachedDocumentId = c.getID().getValue();
                String attachedDocumentCodeType = c.getID().getSchemeID();
                Optional<TipoDocumentoRelacionadoPercepcionRetencion> tipoDocumentoRelacionadoPercepcion = Arrays
                        .stream(TipoDocumentoRelacionadoPercepcionRetencion.values())
                        .filter(p -> p.getCodigo().equals(attachedDocumentCodeType))
                        .findAny();
                if (tipoDocumentoRelacionadoPercepcion.isPresent()) {
                    DocumentModel attachedDocument = session.documents().getDocumentByTypeAndDocumentId(tipoDocumentoRelacionadoPercepcion.get().getDocumentType(), attachedDocumentId, organization);
                    if (attachedDocument != null) {
                        documentModel.addAttachedDocument(attachedDocument);
                    }
                }
            });
        }

        fireDocumentPostCreate(documentModel);
        return documentModel;
    }

    public DocumentModel addVoidedDocument(OrganizationModel organization, VoidedDocumentsType voidedDocumentsType) {
        IDType documentId = voidedDocumentsType.getID();
        if (documentId == null || documentId.getValue() == null) {
            String generatedId = SunatUBLIDGenerator.generateVoidedDocumentId(session, organization);
            documentId = new IDType(generatedId);
            voidedDocumentsType.setID(documentId);
        }

        DocumentModel documentModel = model.addDocument(SunatDocumentType.VOIDED_DOCUMENTS.toString(), documentId.getValue(), organization);
        SunatTypeToModel.importVoidedDocument(session, organization, documentModel, voidedDocumentsType);
        RequiredAction.getDefaults().stream().forEach(c -> documentModel.addRequiredAction(c));

        // SUNAT REQUIRED ACTION
        documentModel.addRequiredAction(SunatRequiredAction.CONSULTAR_TICKET.toString());

        try {
            // Generate Document
            Document baseDocument = SunatTypeToDocument.toDocument(organization, voidedDocumentsType);
            SunatSignerUtils.removeSignature(baseDocument);
            Document signedDocument = session.getProvider(SignerProvider.class).sign(baseDocument, organization);
            byte[] signedDocumentBytes = DocumentUtils.getBytesFromDocument(signedDocument);

            // File
            FileModel xmlFile = session.files().createFile(organization, OpenfactModelUtils.generateId() + ".xml", signedDocumentBytes);
            documentModel.attachXmlFile(xmlFile);
        } catch (JAXBException e) {
            logger.error("Error on marshal model", e);
            throw new ModelException(e);
        } catch (TransformerException e) {
            logger.error("Error parsing to byte XML", e);
            throw new ModelException(e);
        }

        if (voidedDocumentsType.getVoidedDocumentsLine() != null && !voidedDocumentsType.getVoidedDocumentsLine().isEmpty()) {
            voidedDocumentsType.getVoidedDocumentsLine().stream().forEach(c -> {
                String attachedDocumentId = c.getDocumentSerialID().getValue() + "-" + c.getDocumentNumberID().getValue();
                String attachedDocumentCodeType = c.getDocumentTypeCode().getValue();
                Optional<TipoDocumentoRelacionadoPercepcionRetencion> tipoDocumentoRelacionadoPercepcion = Arrays
                        .stream(TipoDocumentoRelacionadoPercepcionRetencion.values())
                        .filter(p -> p.getCodigo().equals(attachedDocumentCodeType))
                        .findAny();
                if (tipoDocumentoRelacionadoPercepcion.isPresent()) {
                    DocumentModel attachedDocument = session.documents().getDocumentByTypeAndDocumentId(tipoDocumentoRelacionadoPercepcion.get().getDocumentType(), attachedDocumentId, organization);
                    if (attachedDocument != null) {
                        documentModel.addAttachedDocument(attachedDocument);
                    }
                }
            });
        }

        return documentModel;
    }

    public DocumentModel addSummaryDocument(OrganizationModel organization, SummaryDocumentsType summaryDocumentsType) {
        IDType documentId = summaryDocumentsType.getId();
        if (documentId == null || documentId.getValue() == null) {
            String generatedId = SunatUBLIDGenerator.generateSummaryDocumentDocumentId(session, organization);
            documentId = new IDType(generatedId);
            summaryDocumentsType.setId(documentId);
        }

        DocumentModel documentModel = model.addDocument(SunatDocumentType.SUMMARY_DOCUMENTS.toString(), documentId.getValue(), organization);
        SunatTypeToModel.importSummaryDocument(session, organization, documentModel, summaryDocumentsType);
        RequiredAction.getDefaults().stream().forEach(c -> documentModel.addRequiredAction(c));

        // SUNAT REQUIRED ACTION
        documentModel.addRequiredAction(SunatRequiredAction.CONSULTAR_TICKET.toString());

        try {
            // Generate Document
            Document baseDocument = SunatTypeToDocument.toDocument(organization, summaryDocumentsType);
            SunatSignerUtils.removeSignature(baseDocument);
            Document signedDocument = session.getProvider(SignerProvider.class).sign(baseDocument, organization);
            byte[] signedDocumentBytes = DocumentUtils.getBytesFromDocument(signedDocument);

            // File
            FileModel xmlFile = session.files().createFile(organization, OpenfactModelUtils.generateId() + ".xml", signedDocumentBytes);
            documentModel.attachXmlFile(xmlFile);
        } catch (JAXBException e) {
            logger.error("Error on marshal model", e);
            throw new ModelException(e);
        } catch (TransformerException e) {
            logger.error("Error parsing to byte XML", e);
            throw new ModelException(e);
        }
        return documentModel;
    }

    @Override
    public void sendToCustomerParty(OrganizationModel organization, DocumentModel document, SendEventModel sendEvent) throws ModelInsuficientData, SendException {
        SunatDocumentType documentType = SunatDocumentType.getFromString(document.getDocumentType());
        UBLProvider ublProvider = null;
        if (documentType != null) {
            switch (documentType) {
                case PERCEPTION:
                    ublProvider = session.getProvider(UBLPerceptionProvider.class);
                    break;
                case RETENTION:
                    ublProvider = session.getProvider(UBLRetentionProvider.class);
                    break;
            }
        }
        if (ublProvider != null) {
            ublProvider.sender().sendToCustomer(organization, document, sendEvent);
        } else {
            super.sendToCustomerParty(organization, document, sendEvent);
        }
    }

    @Override
    public void sendToThirdParty(OrganizationModel organization, DocumentModel document, SendEventModel sendEvent) throws ModelInsuficientData, SendException {
        SunatDocumentType documentType = SunatDocumentType.getFromString(document.getDocumentType());
        UBLProvider ublProvider = null;
        if (documentType != null) {
            switch (documentType) {
                case PERCEPTION:
                    ublProvider = session.getProvider(UBLPerceptionProvider.class);
                    break;
                case RETENTION:
                    ublProvider = session.getProvider(UBLRetentionProvider.class);
                    break;
                case VOIDED_DOCUMENTS:
                    ublProvider = session.getProvider(UBLVoidedDocumentProvider.class);
                    break;
                case SUMMARY_DOCUMENTS:
                    ublProvider = session.getProvider(UBLSummaryDocumentProvider.class);
                    break;
            }
        }
        if (ublProvider != null) {
            ublProvider.sender().sendToThirdParty(organization, document, sendEvent);
        } else {
            super.sendToThirdParty(organization, document, sendEvent);
        }
    }

}
