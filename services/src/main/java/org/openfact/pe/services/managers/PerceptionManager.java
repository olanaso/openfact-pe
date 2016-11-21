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
import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.PerceptionProvider;
import org.openfact.pe.models.utils.DocumentIdProvider_PE;
import org.openfact.pe.models.utils.RepresentationToType_PE;
import org.openfact.pe.models.utils.TypeToDocument;
import org.openfact.pe.models.utils.TypeToModel_PE;
import org.openfact.pe.representations.idm.DocumentRepresentation;
import org.openfact.pe.types.PerceptionType;
import org.openfact.ubl.UblDocumentSignerProvider;
import org.w3c.dom.Document;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.IDType;

public class PerceptionManager {

    protected static final Logger logger = Logger.getLogger(PerceptionManager.class);

    protected OpenfactSession session;
    protected PerceptionProvider model;

    public PerceptionManager(OpenfactSession session) {
        this.session = session;
        this.model = session.getProvider(PerceptionProvider.class);
    }

    public PerceptionModel getPerceptionByDocumentId(String documentId, OrganizationModel organization) {
        return model.getPerceptionByDocumentId(documentId, organization);
    }

    public PerceptionModel addPerception(OrganizationModel organization, DocumentRepresentation rep) {
        PerceptionType type = RepresentationToType_PE.toPerceptionType(rep);
        return addPerception(organization, type);
    }

    public PerceptionModel addPerception(OrganizationModel organization, PerceptionType type) {
        IDType documentId = type.getId();
        if (documentId == null || documentId.getValue() == null) {
            String generatedId = DocumentIdProvider_PE.generatePerceptionDocumentId(session, organization);
            documentId = new IDType(generatedId);
            type.setId(documentId);
        }

        PerceptionModel perception = model.addPerception(organization, documentId.getValue());
        TypeToModel_PE.importPerception(session, organization, perception, type);
        RequiredActionDocument.getDefaults().stream().forEach(c -> perception.addRequiredAction(c));

        try {
            // Generate Document
            Document baseDocument = TypeToDocument.toDocument(type);

            // Sign Document
            UblDocumentSignerProvider signerProvider = session.getProvider(UblDocumentSignerProvider.class);
            Document signedDocument = signerProvider.sign(baseDocument, organization);

            byte[] bytes = DocumentUtils.getBytesFromDocument(signedDocument);
            perception.setXmlDocument(bytes);
        } catch (JAXBException e) {
            logger.error("Error on marshal model", e);
            throw new ModelException(e);
        } catch (TransformerException e) {
            logger.error("Error parsing to byte XML", e);
            throw new ModelException(e);
        }
        return perception;
    }

    public boolean removePerception(OrganizationModel organization, PerceptionModel perception) {
        if (model.removePerception(perception, organization)) {
            return true;
        }
        return false;
    }

    public void enviarEmailAlCliente(OrganizationModel organization, PerceptionModel perception)
            throws EmailException {

    }

    public void enviarASunat(OrganizationModel organization, PerceptionModel perception)
            throws EmailException {

    }

}
