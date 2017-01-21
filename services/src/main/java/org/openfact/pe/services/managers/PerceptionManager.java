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
import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.PerceptionProvider;
import org.openfact.pe.models.PerceptionUBLModel;
import org.openfact.pe.models.UBLPerceptionProvider;
import org.openfact.pe.models.types.perception.PerceptionType;
import org.openfact.pe.models.utils.*;
import org.openfact.pe.services.ubl.SunatUBLIDGenerator;
import org.openfact.report.ExportFormat;
import org.openfact.report.ReportException;
import org.openfact.ubl.SignerProvider;
import org.openfact.ubl.UBLReportProvider;
import org.w3c.dom.Document;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.IDType;

import java.util.Arrays;

public class PerceptionManager {

    protected static final Logger logger = Logger.getLogger(PerceptionManager.class);

    protected OpenfactSession session;
    protected PerceptionProvider model;
    protected UBLPerceptionProvider ublProvider;

    public PerceptionManager(OpenfactSession session) {
        this.session = session;
        this.model = session.getProvider(PerceptionProvider.class);
        this.ublProvider = session.getProvider(UBLPerceptionProvider.class);
    }

    public PerceptionModel getPerceptionByDocumentId(OrganizationModel organization, String documentId) {
        return model.getPerceptionById(organization, documentId);
    }

    public PerceptionModel addPerception(OrganizationModel organization, PerceptionType perceptionType) {
        IDType documentId = perceptionType.getId();
        if (documentId == null || documentId.getValue() == null) {
            String generatedId = SunatUBLIDGenerator.generatePerceptionDocumentId(session, organization);
            documentId = new IDType(generatedId);
            perceptionType.setId(documentId);
        }
        PerceptionModel perceptionModel = model.addPerception(organization, documentId.getValue());
        SunatTypeToModel.importPerception(session, organization, perceptionModel, perceptionType);
        RequiredAction.getDefaults().stream().forEach(c -> perceptionModel.addRequiredAction(c));

        try {
            // Generate Document
            Document baseDocument = SunatTypeToDocument.toDocument(organization, perceptionType);
            SunatSignerUtils.removeSignature(baseDocument);
            Document signedDocument = session.getProvider(SignerProvider.class).sign(baseDocument, organization);
            byte[] signedDocumentBytes = DocumentUtils.getBytesFromDocument(signedDocument);

            // File
            FileModel xmlFile = session.files().createFile(organization, OpenfactModelUtils.generateId() + ".xml", signedDocumentBytes);
            perceptionModel.attachXmlFile(xmlFile);
        } catch (JAXBException e) {
            logger.error("Error on marshal model", e);
            throw new ModelException(e);
        } catch (TransformerException e) {
            logger.error("Error parsing to byte XML", e);
            throw new ModelException(e);
        }

        firePerceptionPostCreate(perceptionModel);
        return perceptionModel;
    }

    public boolean removePerception(OrganizationModel organization, PerceptionModel perception) {
        if (model.removePerception(organization, perception)) {
            return true;
        }
        return false;
    }

    public boolean removePerception(OrganizationModel organization, PerceptionModel perception, PerceptionProvider perceptionProvider) {
        if (model.removePerception(organization, perception)) {
            session.getOpenfactSessionFactory().publish(new PerceptionModel.PerceptionRemovedEvent() {
                @Override
                public OrganizationModel getOrganization() {
                    return organization;
                }

                @Override
                public PerceptionModel getPerception() {
                    return perception;
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

    private void firePerceptionPostCreate(PerceptionModel perception) {
        session.getOpenfactSessionFactory().publish(new PerceptionModel.PerceptionPostCreateEvent() {
            @Override
            public PerceptionModel getCreatedPerception() {
                return perception;
            }

            @Override
            public OpenfactSession getOpenfactSession() {
                return session;
            }
        });
    }

    public SendEventModel sendToCustomerParty(OrganizationModel organization, PerceptionModel perception) throws ModelInsuficientData, SendException {
        SendEventModel sendEvent = perception.addSendEvent(DestinyType.CUSTOMER);
        sendToCustomerParty(organization, perception, sendEvent);
        return sendEvent;
    }

    public void sendToCustomerParty(OrganizationModel organization, PerceptionModel perception, SendEventModel sendEvent) throws ModelInsuficientData, SendException {
        ublProvider.sender().sendToCustomer(organization, perception, sendEvent);
    }

    public SendEventModel sendToTrirdParty(OrganizationModel organization, PerceptionModel perception) throws ModelInsuficientData, SendException {
        SendEventModel sendEvent = perception.addSendEvent(DestinyType.THIRD_PARTY);
        sendToTrirdParty(organization, perception, sendEvent);
        return sendEvent;
    }

    public void sendToTrirdParty(OrganizationModel organization, PerceptionModel perception, SendEventModel sendEvent) throws ModelInsuficientData, SendException {
        ublProvider.sender().sendToThirdParty(organization, perception, sendEvent);
    }

    public SendEventModel sendToThirdPartyByEmail(OrganizationModel organization, PerceptionModel perception, String email) throws ModelInsuficientData, SendException {
        SendEventModel sendEvent = perception.addSendEvent(DestinyType.THIRD_PARTY_BY_EMAIL);
        sendToThirdPartyByEmail(organization, perception, sendEvent, email);
        return sendEvent;
    }

    public void sendToThirdPartyByEmail(OrganizationModel organization, PerceptionModel perception, SendEventModel sendEvent, String email) throws ModelInsuficientData, SendException {
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
            FileModel xmlFile = fileProvider.createFile(organization, perception.getDocumentId() + ".xml", perception.getXmlAsFile().getFile());
            FileMymeTypeModel xmlFileMymeType = new FileMymeTypeModel(xmlFile, "application/xml");

            byte[] pdfFileBytes = session.getProvider(UBLReportProvider.class).ublModel().setOrganization(organization).getReport(new PerceptionUBLModel(perception), ExportFormat.PDF);
            FileModel pdfFile = fileProvider.createFile(organization, perception.getDocumentId() + ".pdf", pdfFileBytes);
            FileMymeTypeModel pdfFileMymeType = new FileMymeTypeModel(pdfFile, "application/pdf");

            session.getProvider(EmailTemplateProvider.class)
                    .setOrganization(organization).setUser(user)
                    .setAttachments(Arrays.asList(xmlFileMymeType, pdfFileMymeType))
                    .sendPerception(perception);

            // Write event to the database
            sendEvent.setType("EMAIL");
            sendEvent.setDescription("Ivoice successfully sended");
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
