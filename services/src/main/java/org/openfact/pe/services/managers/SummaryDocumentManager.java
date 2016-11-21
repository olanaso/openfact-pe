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
import org.openfact.email.EmailException;
import org.openfact.models.ModelException;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.enums.RequiredActionDocument;
import org.openfact.pe.models.SummaryDocumentModel;
import org.openfact.pe.models.SummaryDocumentProvider;
import org.openfact.pe.models.utils.DocumentIdProvider_PE;
import org.openfact.pe.models.utils.RepresentationToType_PE;
import org.openfact.pe.models.utils.TypeToDocument;
import org.openfact.pe.models.utils.TypeToModel_PE;
import org.openfact.pe.representations.idm.DocumentRepresentation;
import org.openfact.pe.types.SummaryDocumentsType;
import org.openfact.ubl.UblDocumentSignerProvider;
import org.w3c.dom.Document;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.IDType;

public class SummaryDocumentManager {

    protected static final Logger logger = Logger.getLogger(SummaryDocumentManager.class);

    protected OpenfactSession session;
    protected SummaryDocumentProvider model;

    public SummaryDocumentManager(OpenfactSession session) {
        this.session = session;
        this.model = session.getProvider(SummaryDocumentProvider.class);
    }

    public SummaryDocumentModel getSummaryDocumentByDocumentId(String documentId,
            OrganizationModel organization) {
        return model.getSummaryDocumentByDocumentId(documentId, organization);
    }

    public SummaryDocumentModel addSummaryDocument(OrganizationModel organization,
            DocumentRepresentation rep) {
        SummaryDocumentsType type = RepresentationToType_PE.toSummaryDocumentType(rep);
        return addSummaryDocument(organization, type);
    }

    public SummaryDocumentModel addSummaryDocument(OrganizationModel organization,
            SummaryDocumentsType type) {
        IDType documentId = type.getId();
        if (documentId == null || documentId.getValue() == null) {
            String generatedId = DocumentIdProvider_PE.generateSummaryDocumentDocumentId(session,
                    organization);
            documentId = new IDType(generatedId);
            type.setId(documentId);
        }

        SummaryDocumentModel summaryDocument = model.addSummaryDocument(organization, documentId.getValue());
        TypeToModel_PE.importSummaryDocument(session, organization, summaryDocument, type);
        RequiredActionDocument.getDefaults().stream().forEach(c -> summaryDocument.addRequiredAction(c));

        try {
            // Generate Document
            Document baseDocument = TypeToDocument.toDocument(type);

            // Sign Document
            UblDocumentSignerProvider signerProvider = session.getProvider(UblDocumentSignerProvider.class);
            Document signedDocument = signerProvider.sign(baseDocument, organization);

            byte[] bytes = DocumentUtils.getBytesFromDocument(signedDocument);
            summaryDocument.setXmlDocument(bytes);
        } catch (JAXBException e) {
            logger.error("Error on marshal model", e);
            throw new ModelException(e);
        } catch (TransformerException e) {
            logger.error("Error parsing to byte XML", e);
            throw new ModelException(e);
        }
        return summaryDocument;
    }

    public boolean removeSummaryDocument(OrganizationModel organization,
            SummaryDocumentModel summaryDocument) {
        if (model.removeSummaryDocument(summaryDocument, organization)) {
            return true;
        }
        return false;
    }

    public void enviarEmailAlCliente(OrganizationModel organization, SummaryDocumentModel summaryDocument)
            throws EmailException {

    }

    public void enviarASunat(OrganizationModel organization, SummaryDocumentModel summaryDocument)
            throws EmailException {

    }

}
