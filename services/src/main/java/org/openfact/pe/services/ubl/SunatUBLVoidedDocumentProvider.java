package org.openfact.pe.services.ubl;

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
import org.openfact.pe.models.UBLVoidedDocumentProvider;
import org.openfact.pe.models.enums.TipoDocumentoRelacionadoPercepcionRetencion;
import org.openfact.pe.models.types.voided.VoidedDocumentsType;
import org.openfact.pe.models.utils.SunatDocumentToType;
import org.openfact.pe.models.utils.SunatTypeToDocument;
import org.openfact.pe.models.utils.SunatTypeToModel;
import org.openfact.pe.services.managers.SunatDocumentManager;
import org.openfact.pe.services.send.ServiceConfigurationException;
import org.openfact.pe.services.util.SunatResponseUtils;
import org.openfact.pe.services.util.SunatSenderUtils;
import org.openfact.pe.services.util.SunatTemplateUtils;
import org.openfact.ubl.*;
import org.w3c.dom.Document;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

public class SunatUBLVoidedDocumentProvider implements UBLVoidedDocumentProvider {

    private OpenfactSession session;

    public SunatUBLVoidedDocumentProvider(OpenfactSession session) {
        this.session = session;
    }

    @Override
    public void close() {
    }

    @Override
    public UBLIDGenerator<VoidedDocumentsType> idGenerator() {
        return new UBLIDGenerator<VoidedDocumentsType>() {
            @Override
            public void close() {
            }

            @Override
            public String generateID(OrganizationModel organization, VoidedDocumentsType voidedDocumentsType) {
                String documentId = SunatUBLIDGenerator.generateVoidedDocumentId(session, organization);
                return documentId;
            }
        };
    }

    @Override
    public UBLReader<VoidedDocumentsType> reader() {
        return new UBLReader<VoidedDocumentsType>() {
            @Override
            public void close() {
            }

            @Override
            public VoidedDocumentsType read(Document document) {
                VoidedDocumentsType type = SunatDocumentToType.toVoidedDocumentsType(document);
                return type;
            }

            @Override
            public VoidedDocumentsType read(byte[] bytes) {
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
    public UBLWriter<VoidedDocumentsType> writer() {
        return new UBLWriter<VoidedDocumentsType>() {
            @Override
            public void close() {
            }

            @Override
            public Document write(OrganizationModel organization, VoidedDocumentsType voidedDocumentsType) {
                try {
                    Document document = SunatTypeToDocument.toDocument(organization, voidedDocumentsType);
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
                    xmlFilename = SunatTemplateUtils.generateVoidedDocumentFileName(organization, document) + ".xml";
                    zipFileName = SunatTemplateUtils.generateVoidedDocumentFileName(organization, document) + ".zip";
                    zipFile = SunatTemplateUtils.generateZip(document.getXmlAsFile().getFile(), xmlFilename);

                    sunatSender = new SunatSenderUtils(sunatAddress, sunatUsername, sunatPassword);
                    String numeroTicket = sunatSender.sendSummary(zipFile, zipFileName, InternetMediaType.ZIP);

                    sendEvent.setDescription("Invoice submitted successfully to SUNAT");
                    sendEvent.setResult(SendEventStatus.SUCCESS);

                    sendEvent.setAttribute("address", sunatAddress);
                    sendEvent.setAttribute("ticket", numeroTicket);
                    document.removeRequiredAction(RequiredAction.SEND_TO_THIRD_PARTY);

                    document.setSingleAttribute(SunatTypeToModel.NUMERO_TICKET, numeroTicket);

                    // Disable all related documents
                    UBLProvider ublProvider = session.getProvider(UBLVoidedDocumentProvider.class);
                    VoidedDocumentsType voidedDocumentsType = (VoidedDocumentsType) ublProvider.reader().read(document.getXmlAsFile().getFile());
                    voidedDocumentsType.getVoidedDocumentsLine().stream().forEach(c -> {
                        String attachedDocumentId = c.getDocumentSerialID().getValue() + "-" + c.getDocumentNumberID().getValue();
                        String attachedDocumentCodeType = c.getDocumentTypeCode().getValue();
                        Optional<TipoDocumentoRelacionadoPercepcionRetencion> tipoDocumentoRelacionadoPercepcion = Arrays
                                .stream(TipoDocumentoRelacionadoPercepcionRetencion.values())
                                .filter(p -> p.getCodigo().equals(attachedDocumentCodeType))
                                .findAny();
                        if (tipoDocumentoRelacionadoPercepcion.isPresent()) {
                            DocumentModel attachedDocument = session.documents().getDocumentByTypeAndDocumentId(tipoDocumentoRelacionadoPercepcion.get().getDocumentType(), attachedDocumentId, organization);
                            attachedDocument.disable();
                        }
                    });

                    document.getXmlAsFile();
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

    public static FileModel checkTicket(OpenfactSession session, OrganizationModel organization, SendEventModel sendEvent) throws SendException, ModelInsuficientData {
        String ticket = sendEvent.getAttribute(SunatTypeToModel.NUMERO_TICKET);
        if (ticket == null) {
            throw new ModelInsuficientData("ticket not found on send event");
        }

        String sunatAddress = organization.getAttribute(SunatConfig.SUNAT_ADDRESS_1);
        String sunatUsername = organization.getAttribute(SunatConfig.SUNAT_USERNAME);
        String sunatPassword = organization.getAttribute(SunatConfig.SUNAT_PASSWORD);

        try {
            byte[] result = new SunatSenderUtils(sunatAddress, sunatUsername, sunatPassword).getStatus(ticket);

            FileModel responseFileModel = session.files().createFile(organization, "R" + ticket + ".zip", result);
            sendEvent.attachFile(responseFileModel);
            return responseFileModel;
        } catch (ServiceConfigurationException e) {
            throw new SendException("Could not generate send to third party", e);
        }
    }

}
