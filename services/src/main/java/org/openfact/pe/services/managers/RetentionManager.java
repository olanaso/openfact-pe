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

import org.apache.commons.validator.routines.EmailValidator;
import org.jboss.logging.Logger;
import org.openfact.common.converts.DocumentUtils;
import org.openfact.email.EmailException;
import org.openfact.email.EmailTemplateProvider;
import org.openfact.file.FileModel;
import org.openfact.file.FileMymeTypeModel;
import org.openfact.file.FileProvider;
import org.openfact.models.*;
import org.openfact.models.enums.DestinyType;
import org.openfact.models.enums.RequiredAction;
import org.openfact.models.enums.SendResultType;
import org.openfact.models.utils.OpenfactModelUtils;
import org.openfact.pe.models.*;
import org.openfact.pe.models.types.retention.RetentionType;
import org.openfact.pe.models.utils.*;
import org.openfact.pe.representations.idm.DocumentoSunatRepresentation;
import org.openfact.pe.services.ubl.SunatUBLIDGenerator;
import org.openfact.pe.services.util.SunatTemplateUtils;
import org.openfact.report.ExportFormat;
import org.openfact.report.ReportException;
import org.openfact.ubl.SignerProvider;
import org.openfact.ubl.UBLReportProvider;
import org.w3c.dom.Document;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.IDType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RetentionManager {

	protected static final Logger logger = Logger.getLogger(RetentionManager.class);

	protected OpenfactSession session;
	protected RetentionProvider model;
	protected UBLRetentionProvider ublProvider;

	public RetentionManager(OpenfactSession session) {
		this.session = session;
		this.model = session.getProvider(RetentionProvider.class);
		this.ublProvider = session.getProvider(UBLRetentionProvider.class);
	}

	public RetentionModel getRetentionByDocumentId(OrganizationModel organization, String documentId) {
		return model.getRetentionById(organization, documentId);
	}

	public RetentionModel addRetention(OrganizationModel organization, RetentionType retentionType) {
		IDType documentId = retentionType.getId();
		if (documentId == null || documentId.getValue() == null) {
			String generatedId = SunatUBLIDGenerator.generateRetentionDocumentId(session, organization);
			documentId = new IDType(generatedId);
			retentionType.setId(documentId);
		}
		RetentionModel retentionModel = model.addRetention(organization, documentId.getValue());
		SunatTypeToModel.importRetention(session, organization, retentionModel, retentionType);
		RequiredAction.getDefaults().stream().forEach(c -> retentionModel.addRequiredAction(c));

		try {
			// Generate Document
			Document baseDocument = SunatTypeToDocument.toDocument(organization, retentionType);
			SunatSignerUtils.removeSignature(baseDocument);
			Document signedDocument = session.getProvider(SignerProvider.class).sign(baseDocument, organization);
			byte[] signedDocumentBytes = DocumentUtils.getBytesFromDocument(signedDocument);

			// File
			FileModel xmlFile = session.files().createFile(organization, OpenfactModelUtils.generateId() + ".xml", signedDocumentBytes);
			retentionModel.attachXmlFile(xmlFile);
		} catch (JAXBException e) {
			logger.error("Error on marshal model", e);
			throw new ModelException(e);
		} catch (TransformerException e) {
			logger.error("Error parsing to byte XML", e);
			throw new ModelException(e);
		}

		fireRetentionPostCreate(retentionModel);
		return retentionModel;
	}

	public boolean removeRetention(OrganizationModel organization, RetentionModel retention) {
		if (model.removeRetention(organization, retention)) {
			return true;
		}
		return false;
	}

	public boolean removeRetention(OrganizationModel organization, RetentionModel retention, RetentionProvider retentionProvider) {
		if (model.removeRetention(organization, retention)) {
			session.getOpenfactSessionFactory().publish(new RetentionModel.RetentionRemovedEvent() {
				@Override
				public OrganizationModel getOrganization() {
					return organization;
				}

				@Override
				public RetentionModel getRetention() {
					return retention;
				}

				@Override
				public OpenfactSession getOpenfactSession() {
					return session;
				}
			});
			return true;
		}
		return false;
	}

	private void fireRetentionPostCreate(RetentionModel retention) {
		session.getOpenfactSessionFactory().publish(new RetentionModel.RetentionPostCreateEvent() {
			@Override
			public RetentionModel getCreatedRetention() {
				return retention;
			}

			@Override
			public OpenfactSession getOpenfactSession() {
				return session;
			}
		});
	}

	public SendEventModel sendToCustomerParty(OrganizationModel organization, RetentionModel retention) throws ModelInsuficientData, SendException {
		SendEventModel sendEvent = retention.addSendEvent(DestinyType.CUSTOMER);
		sendToCustomerParty(organization, retention, sendEvent);
		return sendEvent;
	}

	public void sendToCustomerParty(OrganizationModel organization, RetentionModel retention, SendEventModel sendEvent) throws ModelInsuficientData, SendException {
		ublProvider.sender().sendToCustomer(organization, retention, sendEvent);
	}

	public SendEventModel sendToTrirdParty(OrganizationModel organization, RetentionModel retention) throws ModelInsuficientData, SendException {
		SendEventModel sendEvent = retention.addSendEvent(DestinyType.THIRD_PARTY);
		sendToTrirdParty(organization, retention, sendEvent);
		return sendEvent;
	}

	public void sendToTrirdParty(OrganizationModel organization, RetentionModel retention, SendEventModel sendEvent) throws ModelInsuficientData, SendException {
		ublProvider.sender().sendToThirdParty(organization, retention, sendEvent);
	}

	public SendEventModel sendToThirdPartyByEmail(OrganizationModel organization, RetentionModel retention, String email) throws ModelInsuficientData, SendException {
		SendEventModel sendEvent = retention.addSendEvent(DestinyType.THIRD_PARTY_BY_EMAIL);
		sendToThirdPartyByEmail(organization, retention, sendEvent, email);
		return sendEvent;
	}

	public void sendToThirdPartyByEmail(OrganizationModel organization, RetentionModel retention, SendEventModel sendEvent, String email) throws ModelInsuficientData, SendException {
		if (email == null || !EmailValidator.getInstance().isValid(email)) {
			throw new ModelInsuficientData("Invalid Email");
		}
		if (organization.getSmtpConfig().size() == 0) {
			throw new ModelInsuficientData("Could not find a valid smtp configuration on organization");
		}

		// User where the email will be send
		UserSenderModel user = new UserSenderModel(email);

		try {
			FileProvider fileProvider = session.getProvider(FileProvider.class);

			// Attatchments
			FileModel xmlFile = fileProvider.createFile(organization, retention.getDocumentId() + ".xml", retention.getXmlAsFile().getFile());
			FileMymeTypeModel xmlFileMymeType = new FileMymeTypeModel(xmlFile, "application/xml");

			byte[] pdfFileBytes = session.getProvider(UBLReportProvider.class).ublModel().setOrganization(organization).getReport(new RetentionUBLModel(retention), ExportFormat.PDF);
			FileModel pdfFile = fileProvider.createFile(organization, retention.getDocumentId() + ".pdf", pdfFileBytes);
			FileMymeTypeModel pdfFileMymeType = new FileMymeTypeModel(pdfFile, "application/pdf");

			Map<String, Object> attributes = new HashMap<String, Object>();
			attributes.put("user", user.getFullName());
			attributes.put("organizationName", SunatTemplateUtils.getOrganizationName(organization));

			StringBuilder subject = new StringBuilder();
			if (organization.getDisplayName() != null) {
				subject.append(organization.getDisplayName());
			} else {
				subject.append(organization.getName());
			}
			subject.append("/").append("retention").append(" ").append(retention.getDocumentId());

			session.getProvider(EmailTemplateProvider.class)
					.setOrganization(organization)
					.setUser(user)
					.setAttachments(Arrays.asList(xmlFileMymeType, pdfFileMymeType))
					.send(subject.toString(), "retention.ftl", attributes);

			// Write event to the database
			sendEvent.setType("EMAIL");
			sendEvent.setDescription("Retention successfully sended");
			sendEvent.attachFile(xmlFile);
			sendEvent.attachFile(pdfFile);
			sendEvent.setResult(SendResultType.SUCCESS);

			sendEvent.setSingleDestinyAttribute("email", user.getEmail());
		} catch (ReportException e) {
			throw new SendException("Could not generate pdf report to attach file", e);
		} catch (EmailException e) {
			throw new SendException("Could not send email", e);
		} catch (Throwable e) {
			throw new SendException("Internal Server Error", e);
		}
	}
	
}
