package org.openfact.pe.services.ubl;

import javax.ws.rs.Path;
import javax.xml.bind.JAXBException;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;

import org.openfact.common.converts.DocumentUtils;
import org.openfact.file.FileModel;
import org.openfact.file.InternetMediaType;
import org.openfact.models.*;
import org.openfact.models.enums.DestinyType;
import org.openfact.models.enums.RequiredAction;
import org.openfact.models.enums.SendEventStatus;
import org.openfact.pe.models.SunatSendException;
import org.openfact.pe.models.UBLSummaryDocumentProvider;
import org.openfact.pe.models.types.summary.SummaryDocumentsType;
import org.openfact.pe.models.utils.SunatDocumentToType;
import org.openfact.pe.models.utils.SunatTypeToDocument;
import org.openfact.pe.models.utils.SunatTypeToModel;
import org.openfact.pe.services.managers.SunatDocumentManager;
import org.openfact.pe.services.util.SunatResponseUtils;
import org.openfact.pe.services.util.SunatSenderUtils;
import org.openfact.pe.services.util.SunatTemplateUtils;
import org.openfact.ubl.UBLIDGenerator;
import org.openfact.ubl.UBLReader;
import org.openfact.ubl.UBLSender;
import org.openfact.ubl.UBLWriter;
import org.w3c.dom.Document;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.util.Map;

public class SunatUBLSummaryDocumentProvider implements UBLSummaryDocumentProvider {
    private OpenfactSession session;

    public SunatUBLSummaryDocumentProvider(OpenfactSession session) {
        this.session = session;
    }

    @Override
    public void close() {
    }

    @Override
    public UBLIDGenerator<SummaryDocumentsType> idGenerator() {
        return new UBLIDGenerator<SummaryDocumentsType>() {
            @Override
            public void close() {
            }

            @Override
            public String generateID(OrganizationModel organization, SummaryDocumentsType summaryDocumentsType) {
                String documentId = SunatUBLIDGenerator.generateSummaryDocumentDocumentId(session, organization);
                return documentId;
            }
        };
    }

    @Override
    public UBLReader<SummaryDocumentsType> reader() {
        return new UBLReader<SummaryDocumentsType>() {
            @Override
            public void close() {
            }

            @Override
            public SummaryDocumentsType read(Document document) {
                SummaryDocumentsType type = SunatDocumentToType.toSummaryDocumentsType(document);
                return type;
            }

            @Override
            public SummaryDocumentsType read(byte[] bytes) {
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
    public UBLWriter<SummaryDocumentsType> writer() {
        return new UBLWriter<SummaryDocumentsType>() {
            @Override
            public void close() {
            }

            @Override
            public Document write(OrganizationModel organization, SummaryDocumentsType summaryDocumentsType) {
                try {
                    Document document = SunatTypeToDocument.toDocument(organization, summaryDocumentsType);
                    return document;
                } catch (JAXBException e) {
                    throw new ModelException(e);
                }
            }
        };
    }

    @Override
    public UBLSender<DocumentModel> sender() {
        return new UBLSender<DocumentModel>() {

            @Override
            public void close() {
            }

            @Override
            public SendEventModel sendToCustomer(OrganizationModel organization, DocumentModel document) throws ModelInsuficientData, SendException {
                SendEventModel sendEvent = document.addSendEvent(DestinyType.CUSTOMER);
                sendToCustomer(organization, document, sendEvent);
                return sendEvent;
            }

            @Override
            public void sendToCustomer(OrganizationModel organization, DocumentModel document, SendEventModel sendEvent) throws ModelInsuficientData, SendException {
                SunatDocumentManager manager = new SunatDocumentManager(session);
                manager.sendToThirdPartyByEmail(organization, document, sendEvent, document.getCustomerElectronicMail());
                document.removeRequiredAction(RequiredAction.SEND_TO_CUSTOMER);
            }

            @Override
            public SendEventModel sendToThirdParty(OrganizationModel organization, DocumentModel document) throws ModelInsuficientData, SendException {
                SendEventModel sendEvent = document.addSendEvent(DestinyType.THIRD_PARTY);
                sendToThirdParty(organization, document, sendEvent);
                return sendEvent;
            }

            @Override
            public void sendToThirdParty(OrganizationModel organization, DocumentModel document, SendEventModel sendEvent) throws ModelInsuficientData, SendException {
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
                    xmlFilename = SunatTemplateUtils.generateSummaryFileName(organization, document) + ".xml";
                    zipFileName = SunatTemplateUtils.generateSummaryFileName(organization, document) + ".zip";
                    zipFile = SunatTemplateUtils.generateZip(document.getXmlAsFile().getFile(), xmlFilename);



                    sunatSender = new SunatSenderUtils(sunatAddress, sunatUsername, sunatPassword);
                    String response = sunatSender.sendSummary(zipFile, zipFileName, InternetMediaType.ZIP);

                    sendEvent.setDescription("Invoice submitted successfully to SUNAT");
                    sendEvent.setResult(SendEventStatus.SUCCESS);

                    sendEvent.setAttribute("address", sunatAddress);
                    sendEvent.setAttribute("ticket", response);

                    document.removeRequiredAction(RequiredAction.SEND_TO_THIRD_PARTY);

                    document.setSingleAttribute(SunatTypeToModel.NUMERO_TICKET, response);
                } catch (SOAPFaultException e) {
                    SOAPFault soapFault = e.getFault();

                    sendEvent.setDescription(soapFault.getFaultString());

                    FileModel zipFileModel = session.files().createFile(organization, zipFileName, zipFile);
                    sendEvent.attachFile(zipFileModel);

                    sendEvent.setAttribute("address", sunatAddress);
                    for (Map.Entry<String, String> entry : SunatResponseUtils.faultToMap(soapFault.getFaultCode(), soapFault.getFaultString()).entrySet()) {
                        sendEvent.setAttribute(entry.getKey(), entry.getValue());
                    }

                    throw new SunatSendException(soapFault.getFaultString(), e);
                } catch (Exception e) {
                    throw new SendException("Could not generate send to third party", e);
                } catch (Throwable e) {
                    throw new SendException("Internal Server Error", e);
                }
            }

        };
    }

}
