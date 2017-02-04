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

import org.jboss.logging.Logger;
import org.openfact.common.converts.DocumentUtils;
import org.openfact.file.FileModel;
import org.openfact.models.*;
import org.openfact.models.enums.RequiredAction;
import org.openfact.models.utils.OpenfactModelUtils;
import org.openfact.pe.models.UBLPerceptionProvider;
import org.openfact.pe.models.UBLRetentionProvider;
import org.openfact.pe.models.enums.SunatDocumentType;
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

public class SunatDocumentManager extends DocumentManager {

    protected static final Logger logger = Logger.getLogger(SunatDocumentManager.class);

    public SunatDocumentManager(OpenfactSession session) {
        super(session);
    }

    public DocumentModel getDocumentByTypeAndDocumentId(SunatDocumentToType type, String documentId, OrganizationModel organization) {
        return model.getDocumentByDocumentTypeAndId(type.toString(), documentId, organization);
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
        return documentModel;
    }

    public DocumentModel addSummaryDocument(OrganizationModel organization, SummaryDocumentsType summaryDocumentsType) {
        IDType documentId = summaryDocumentsType.getId();
        if (documentId == null || documentId.getValue() == null) {
            String generatedId = SunatUBLIDGenerator.generateSummaryDocumentDocumentId(session, organization);
            documentId = new IDType(generatedId);
            summaryDocumentsType.setId(documentId);
        }

        DocumentModel documentModel = model.addDocument(SunatDocumentType.SUMMARY.toString(), documentId.getValue(), organization);
        SunatTypeToModel.importSummaryDocument(session, organization, documentModel, summaryDocumentsType);
        RequiredAction.getDefaults().stream().forEach(c -> documentModel.addRequiredAction(c));

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
            }
        }
        if (ublProvider != null) {
            ublProvider.sender().sendToThirdParty(organization, document, sendEvent);
        } else {
            super.sendToThirdParty(organization, document, sendEvent);
        }
    }

}
