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
import org.openfact.models.*;
import org.openfact.models.enums.RequiredAction;
import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.PerceptionProvider;
import org.openfact.pe.models.UBLPerceptionProvider;
import org.openfact.pe.models.types.perception.PerceptionType;
import org.openfact.pe.models.utils.*;
import org.openfact.pe.representations.idm.DocumentoSunatRepresentation;
import org.openfact.ubl.SignerProvider;
import org.w3c.dom.Document;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.IDType;

public class PerceptionManager {

	protected static final Logger logger = Logger.getLogger(PerceptionManager.class);

	protected OpenfactSession session;
	protected PerceptionProvider model;
	protected UBLPerceptionProvider ubl;

	public PerceptionManager(OpenfactSession session) {
		this.session = session;
		this.model = session.getProvider(PerceptionProvider.class);
		this.ubl = session.getProvider(UBLPerceptionProvider.class);
	}

	public PerceptionModel getPerceptionByDocumentId(String documentId, OrganizationModel organization) {
		return model.getPerceptionById(organization, documentId);
	}

	public PerceptionModel addPerception(OrganizationModel organization, DocumentoSunatRepresentation rep) {
		PerceptionType type = SunatRepresentationToType.toPerceptionType(session,organization, rep);
		return addPerception(organization, type);
	}

	public PerceptionModel addPerception(OrganizationModel organization, PerceptionType type) {
		IDType documentId = type.getId();
		if (documentId == null || documentId.getValue() == null) {
			String generatedId = SunatDocumentIdProvider.generatePerceptionDocumentId(session, organization);
			documentId = new IDType(generatedId);
			type.setId(documentId);
		}

		PerceptionModel perception = model.addPerception(organization, documentId.getValue());
		SunatTypeToModel.importPerception(session, organization, perception, type);
		RequiredAction.getDefaults().stream().forEach(c -> perception.addRequiredAction(c));

		try {
			// Generate Document
			Document baseDocument = SunatTypeToDocument.toDocument(type);
			SunatSignerUtils.removeSignature(baseDocument);
			// Sign Document
			SignerProvider signerProvider = session.getProvider(SignerProvider.class);
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
		if (model.removePerception(organization, perception)) {
			return true;
		}
		return false;
	}

	public SendEventModel sendToCustomerParty(OrganizationModel organization, PerceptionModel perception)
			throws SendException {
		return ubl.sender().sendToCustomer(organization, perception);
	}

	public SendEventModel sendToTrirdParty(OrganizationModel organization, PerceptionModel perception)
			throws SendException {
		return ubl.sender().sendToThridParty(organization, perception);
	}

}
