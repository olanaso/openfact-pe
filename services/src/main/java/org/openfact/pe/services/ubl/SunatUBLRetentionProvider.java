package org.openfact.pe.services.ubl;

import java.util.*;

import javax.xml.bind.JAXBException;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;

import org.openfact.common.converts.DocumentUtils;
import org.openfact.file.FileModel;
import org.openfact.file.InternetMediaType;
import org.openfact.models.*;
import org.openfact.models.enums.DestinyType;
import org.openfact.models.enums.RequiredAction;
import org.openfact.models.enums.SendResultType;
import org.openfact.pe.models.RetentionModel;
import org.openfact.pe.models.SunatSendException;
import org.openfact.pe.models.UBLRetentionProvider;
import org.openfact.pe.models.types.retention.RetentionType;
import org.openfact.pe.models.utils.SunatDocumentToType;
import org.openfact.pe.models.utils.SunatTypeToDocument;
import org.openfact.pe.services.managers.RetentionManager;
import org.openfact.pe.services.util.SunatResponseUtils;
import org.openfact.pe.services.util.SunatSenderUtils;
import org.openfact.pe.services.util.SunatTemplateUtils;
import org.openfact.ubl.*;
import org.w3c.dom.Document;

public class SunatUBLRetentionProvider implements UBLRetentionProvider {

    private OpenfactSession session;

    public SunatUBLRetentionProvider(OpenfactSession session) {
        this.session = session;
    }

    @Override
    public void close() {

    }

    @Override
    public UBLIDGenerator<RetentionType> idGenerator() {
        return new UBLIDGenerator<RetentionType>() {

            @Override
            public void close() {

            }

            @Override
            public String generateID(OrganizationModel organization, RetentionType retentionType) {
                String documentId = SunatUBLIDGenerator.generateRetentionDocumentId(session, organization);
                return documentId;
            }
        };
    }

    @Override
    public UBLReader<RetentionType> reader() {
        return new UBLReader<RetentionType>() {

            @Override
            public void close() {

            }

            @Override
            public RetentionType read(Document document) {
                RetentionType type = SunatDocumentToType.toRetentionType(document);
                return type;
            }

            @Override
            public RetentionType read(byte[] bytes) {
                try {
                    Document document = DocumentUtils.byteToDocument(bytes);
                    return read(document);
                } catch (Exception e) {
                    throw new ModelException(e);
                }
            }
        };
    }

    @Override
    public UBLWriter<RetentionType> writer() {
        return new UBLWriter<RetentionType>() {

            @Override
            public void close() {
            }

            @Override
            public Document write(OrganizationModel organization, RetentionType retentionType) {
                try {
                    Document document = SunatTypeToDocument.toDocument(organization, retentionType);
                    return document;
                } catch (JAXBException e) {
                    throw new ModelException(e);
                }
            }
        };
    }

    @Override
    public UBLSender<RetentionModel> sender() {
        return new UBLSender<RetentionModel>() {

            @Override
            public SendEventModel sendToCustomer(OrganizationModel organization, RetentionModel retention) throws ModelInsuficientData, SendException {
                SendEventModel sendEvent = retention.addSendEvent(DestinyType.CUSTOMER);
                sendToCustomer(organization, retention, sendEvent);
                return sendEvent;
            }

            @Override
            public void sendToCustomer(OrganizationModel organization, RetentionModel retention, SendEventModel sendEvent) throws ModelInsuficientData, SendException {
                RetentionManager manager = new RetentionManager(session);
                manager.sendToThirdPartyByEmail(organization, retention, sendEvent, retention.getEntityEmail());
            }

            @Override
            public SendEventModel sendToThirdParty(OrganizationModel organization, RetentionModel retention) throws ModelInsuficientData, SendException {
                SendEventModel sendEvent = retention.addSendEvent(DestinyType.THIRD_PARTY);
                sendToThirdParty(organization, retention, sendEvent);
                return sendEvent;
            }

            @Override
            public void sendToThirdParty(OrganizationModel organization, RetentionModel retention, SendEventModel sendEvent) throws ModelInsuficientData, SendException {
                String sunatAddress = organization.getAttribute(SunatConfig.SUNAT_ADDRESS_1);
                String sunatUsername = organization.getAttribute(SunatConfig.SUNAT_USERNAME);
                String sunatPassword = organization.getAttribute(SunatConfig.SUNAT_PASSWORD);

                if (sunatAddress == null) {
                    throw new ModelInsuficientData("No se pudo encontrar una url de envio valida");
                }
                if (sunatUsername == null || sunatPassword == null) {
                    throw new ModelInsuficientData("No se pudo encontrar un usuario y/o password valido en la organizacion");
                }

                String xmlFilename = "";
                String zipFileName = "";
                byte[] zipFile = null;

                SunatSenderUtils sunatSender = null;
                try {
                    xmlFilename = SunatTemplateUtils.generateFileName(organization, retention) + ".xml";
                    zipFileName = SunatTemplateUtils.generateFileName(organization, retention) + ".zip";
                    zipFile = SunatTemplateUtils.generateZip(retention.getXmlAsFile().getFile(), xmlFilename);

                    sunatSender = new SunatSenderUtils(sunatAddress, sunatUsername, sunatPassword);
                    byte[] response = sunatSender.sendBill(zipFile, zipFileName, InternetMediaType.ZIP);

                    sendEvent.setType("SUNAT");
                    sendEvent.setDescription("Invoice submitted successfully to SUNAT");
                    sendEvent.setResult(SendResultType.SUCCESS);

                    FileModel zipFileModel = session.files().createFile(organization, zipFileName, zipFile);
                    sendEvent.attachFile(zipFileModel);

                    FileModel responseFileModel = session.files().createFile(organization, "R" + zipFileName, response);
                    sendEvent.attachResponseFile(responseFileModel);

                    sendEvent.setSingleDestinyAttribute("address", sunatAddress);

                    for (Map.Entry<String, String> entry : SunatResponseUtils.byteResponseToMap(response).entrySet()) {
                        sendEvent.setSingleResponseAttribute(entry.getKey(), entry.getValue());
                    }

                    retention.removeRequiredAction(RequiredAction.SEND_TO_TRIRD_PARTY);
                } catch (SOAPFaultException e) {
                    SOAPFault soapFault = e.getFault();

                    sendEvent.setDescription(soapFault.getFaultString());

                    FileModel zipFileModel = session.files().createFile(organization, zipFileName, zipFile);
                    sendEvent.attachFile(zipFileModel);

                    sendEvent.setSingleDestinyAttribute("address", sunatAddress);
                    for (Map.Entry<String, String> entry : SunatResponseUtils.faultToMap(soapFault.getFaultCode(), soapFault.getFaultString()).entrySet()) {
                        sendEvent.setSingleResponseAttribute(entry.getKey(), entry.getValue());
                    }

                    throw new SunatSendException(soapFault.getFaultString(), e);
                } catch (Exception e) {
                    throw new SendException("Could not generate send to third party", e);
                } catch (Throwable e) {
                    throw new SendException("Internal Server Error", e);
                }
            }

            @Override
            public void close() {
            }
        };
    }

}
