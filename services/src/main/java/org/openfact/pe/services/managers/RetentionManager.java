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

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.IDType;
import oasis.names.specification.ubl.schema.xsd.invoice_21.InvoiceType;
import org.apache.commons.lang.ArrayUtils;
import org.jboss.logging.Logger;
import org.openfact.common.converts.DocumentUtils;
import org.openfact.email.EmailException;
import org.openfact.email.EmailTemplateProvider;
import org.openfact.models.ModelException;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.UserSenderModel;
import org.openfact.models.enums.RequiredActionDocument;
import org.openfact.models.enums.UblDocumentType;
import org.openfact.models.ubl.InvoiceModel;
import org.openfact.models.ubl.common.ContactModel;
import org.openfact.models.ubl.common.CustomerPartyModel;
import org.openfact.models.ubl.common.PartyModel;
import org.openfact.models.ubl.provider.InvoiceProvider;
import org.openfact.models.utils.RepresentationToModel;
import org.openfact.models.utils.TypeToModel;
import org.openfact.pe.models.RetentionModel;
import org.openfact.pe.models.RetentionProvider;
import org.openfact.pe.models.RetentionModel;
import org.openfact.pe.models.utils.DocumentIdProvider_PE;
import org.openfact.pe.models.utils.RepresentationToType_PE;
import org.openfact.pe.models.utils.TypeToDocument;
import org.openfact.pe.models.utils.TypeToModel_PE;
import org.openfact.pe.representations.idm.GeneralDocumentRepresentation;
import org.openfact.pe.types.RetentionType;
import org.openfact.representations.idm.ubl.InvoiceRepresentation;
import org.openfact.ubl.UblDocumentProvider;
import org.openfact.ubl.UblDocumentSignerProvider;
import org.openfact.ubl.UblExtensionContentGeneratorProvider;
import org.openfact.ubl.UblIDGeneratorProvider;
import org.w3c.dom.Document;

import javax.xml.transform.TransformerException;
import java.util.Map;

public class RetentionManager {

    protected static final Logger logger = Logger.getLogger(RetentionManager.class);

    protected OpenfactSession session;
    protected RetentionProvider model;

    public RetentionManager(OpenfactSession session) {
        this.session = session;
        this.model = session.getProvider(RetentionProvider.class);
    }

    public RetentionModel getRetentionByDocumentId(String documentId, OrganizationModel organization) {
        return model.getRetentionByDocumentId(documentId, organization);
    }

    public RetentionModel addRetention(OrganizationModel organization, GeneralDocumentRepresentation rep) {
        RetentionType type = RepresentationToType_PE.toRetentionType(rep);
        return addRetention(organization, type);
    }

    public RetentionModel addRetention(OrganizationModel organization, RetentionType type) {
        IDType documentId = type.getId();
        if (documentId == null || documentId.getValue() == null) {
            String generatedId = DocumentIdProvider_PE.generateRetentionDocumentId(organization);
            documentId = new IDType(generatedId);
            type.setId(documentId);
        }
        
        RetentionModel retention = model.addRetention(organization, documentId.getValue());
        TypeToModel_PE.importRetention(session, organization, retention, type);
        RequiredActionDocument.getDefaults().stream().forEach(c -> retention.addRequiredAction(c));

        // Generate Document
        Document baseDocument = TypeToDocument.toDocument(type);

        // Sign Document
        UblDocumentSignerProvider signerProvider = session.getProvider(UblDocumentSignerProvider.class);
        Document signedDocument = signerProvider.sign(baseDocument, organization);

        try {
            byte[] bytes = DocumentUtils.getBytesFromDocument(signedDocument);
            retention.setXmlDocument(bytes);
        } catch (TransformerException e) {
            logger.error("Error parsing to byte XML", e);
            throw new ModelException(e);
        }
        return retention;
    }

    public boolean removeRetention(OrganizationModel organization, RetentionModel retention) {
        if (model.removeRetention(retention, organization)) {
            return true;
        }
        return false;
    }

    public void enviarEmailAlCliente(OrganizationModel organization, RetentionModel retention)
            throws EmailException {

    }

    public void enviarASunat(OrganizationModel organization, RetentionModel retention)
            throws EmailException {

    }

}
