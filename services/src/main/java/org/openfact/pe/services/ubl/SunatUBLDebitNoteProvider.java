package org.openfact.pe.services.ubl;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;

import org.openfact.common.converts.DocumentUtils;
import org.openfact.file.FileModel;
import org.openfact.file.InternetMediaType;
import org.openfact.models.*;
import org.openfact.models.enums.DestinyType;
import org.openfact.models.enums.RequiredAction;
import org.openfact.models.enums.SendResultType;
import org.openfact.pe.models.SunatSendException;
import org.openfact.pe.models.utils.SunatMarshallerUtils;
import org.openfact.pe.services.util.SunatResponseUtils;
import org.openfact.pe.services.util.SunatSenderUtils;
import org.openfact.pe.services.util.SunatTemplateUtils;
import org.openfact.services.managers.DebitNoteManager;
import org.openfact.services.managers.DebitNoteManager;
import org.openfact.ubl.UBLDebitNoteProvider;
import org.openfact.ubl.UBLIDGenerator;
import org.openfact.ubl.UBLReader;
import org.openfact.ubl.UBLSender;
import org.openfact.ubl.UBLWriter;
import org.w3c.dom.Document;

import com.helger.ubl21.UBL21Reader;
import com.helger.ubl21.UBL21Writer;
import com.helger.xml.microdom.serialize.MicroWriter;
import com.helger.xml.namespace.MapBasedNamespaceContext;
import com.helger.xml.serialize.write.XMLWriterSettings;

import oasis.names.specification.ubl.schema.xsd.debitnote_21.DebitNoteType;

public class SunatUBLDebitNoteProvider implements UBLDebitNoteProvider {

    protected OpenfactSession session;

    public SunatUBLDebitNoteProvider(OpenfactSession session) {
        this.session = session;
    }

    @Override
    public void close() {
    }

    @Override
    public UBLIDGenerator<DebitNoteType> idGenerator() {
        return new UBLIDGenerator<DebitNoteType>() {

            @Override
            public void close() {
            }

            @Override
            public String generateID(OrganizationModel organization, DebitNoteType debitNoteType) {
                return SunatUBLIDGenerator.generateDebitNoteDocumentId(session, organization, debitNoteType);
            }
        };
    }

    @Override
    public UBLReader<DebitNoteType> reader() {
        return new UBLReader<DebitNoteType>() {

            @Override
            public void close() {
            }

            @Override
            public DebitNoteType read(byte[] bytes) {
                return UBL21Reader.debitNote().read(bytes);
            }

            @Override
            public DebitNoteType read(Document document) {
                return UBL21Reader.debitNote().read(document);
            }

        };
    }

    @Override
    public UBLWriter<DebitNoteType> writer() {
        return new UBLWriter<DebitNoteType>() {

            @Override
            public void close() {
            }

            @Override
            public Document write(OrganizationModel organization, DebitNoteType t) {
                try {
                    MapBasedNamespaceContext mapBasedNamespace = SunatMarshallerUtils.getBasedNamespaceContext("urn:oasis:names:specification:ubl:schema:xsd:DebitNote-2");
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    MicroWriter.writeToStream(UBL21Writer.debitNote().getAsMicroDocument(t), out, new XMLWriterSettings().setNamespaceContext(mapBasedNamespace).setPutNamespaceContextPrefixesInRoot(true));

                    return DocumentUtils.byteToDocument(out.toByteArray());
                } catch (DatatypeConfigurationException e) {
                    throw new ModelException(e);
                } catch (Exception e) {
                    throw new ModelException(e);
                }
            }
        };
    }

    @Override
    public UBLSender<DebitNoteModel> sender() {
        return new UBLSender<DebitNoteModel>() {

            @Override
            public SendEventModel sendToCustomer(OrganizationModel organization, DebitNoteModel debitNote) throws ModelInsuficientData, SendException {
                SendEventModel sendEvent = debitNote.addSendEvent(DestinyType.CUSTOMER);
                sendToCustomer(organization, debitNote, sendEvent);
                return sendEvent;
            }

            @Override
            public void sendToCustomer(OrganizationModel organization, DebitNoteModel debitNote, SendEventModel sendEvent) throws ModelInsuficientData, SendException {
                DebitNoteManager debitNoteManager = new DebitNoteManager(session);
                debitNoteManager.sendToThirdPartyByEmail(organization, debitNote, sendEvent, debitNote.getCustomerElectronicMail());
            }

            @Override
            public SendEventModel sendToThirdParty(OrganizationModel organization, DebitNoteModel debitNote) throws ModelInsuficientData, SendException {
                SendEventModel sendEvent = debitNote.addSendEvent(DestinyType.THIRD_PARTY);
                sendToThirdParty(organization, debitNote, sendEvent);
                return sendEvent;
            }

            @Override
            public void sendToThirdParty(OrganizationModel organization, DebitNoteModel debitNote, SendEventModel sendEvent) throws ModelInsuficientData, SendException {
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
                    xmlFilename = SunatTemplateUtils.generateFileName(organization, debitNote) + ".xml";
                    zipFileName = SunatTemplateUtils.generateFileName(organization, debitNote) + ".zip";
                    zipFile = SunatTemplateUtils.generateZip(debitNote.getXmlAsFile().getFile(), xmlFilename);

                    sunatSender = new SunatSenderUtils(sunatAddress, sunatUsername, sunatPassword);
                    byte[] response = sunatSender.sendBill(zipFile, zipFileName, InternetMediaType.ZIP);

                    sendEvent.setType("SUNAT");
                    sendEvent.setDescription("Debit Note submitted successfully to SUNAT");
                    sendEvent.setResult(SendResultType.SUCCESS);

                    FileModel zipFileModel = session.files().createFile(organization, zipFileName, zipFile);
                    sendEvent.attachFile(zipFileModel);

                    FileModel responseFileModel = session.files().createFile(organization, "R" + zipFileName, response);
                    sendEvent.attachResponseFile(responseFileModel);

                    sendEvent.setSingleDestinyAttribute("address", sunatAddress);

                    for (Map.Entry<String, String> entry : SunatResponseUtils.byteResponseToMap(response).entrySet()) {
                        sendEvent.setSingleResponseAttribute(entry.getKey(), entry.getValue());
                    }

                    debitNote.removeRequiredAction(RequiredAction.SEND_TO_TRIRD_PARTY);
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
