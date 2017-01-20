package org.openfact.pe.services.ubl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.soap.SOAPFault;
import javax.xml.transform.TransformerException;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.commons.validator.routines.EmailValidator;
import org.openfact.common.converts.DocumentUtils;
import org.openfact.email.EmailException;
import org.openfact.email.EmailTemplateProvider;
import org.openfact.file.FileModel;
import org.openfact.file.FileMymeTypeModel;
import org.openfact.file.FileProvider;
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
import org.openfact.report.ExportFormat;
import org.openfact.report.ReportException;
import org.openfact.services.managers.CreditNoteManager;
import org.openfact.services.managers.InvoiceManager;
import org.openfact.ubl.*;
import org.w3c.dom.Document;

import org.openfact.pe.models.enums.*;

import com.helger.ubl21.UBL21Reader;
import com.helger.ubl21.UBL21Writer;
import com.helger.xml.microdom.serialize.MicroWriter;
import com.helger.xml.namespace.MapBasedNamespaceContext;
import com.helger.xml.serialize.write.XMLWriterSettings;

import oasis.names.specification.ubl.schema.xsd.invoice_21.InvoiceType;

public class SunatUBLInvoiceProvider implements UBLInvoiceProvider {

    protected OpenfactSession session;

    public SunatUBLInvoiceProvider(OpenfactSession session) {
        this.session = session;
    }

    @Override
    public void close() {
    }

    @Override
    public UBLIDGenerator<InvoiceType> idGenerator() {
        return new UBLIDGenerator<InvoiceType>() {

            @Override
            public void close() {
            }

            @Override
            public String generateID(OrganizationModel organization, InvoiceType invoiceType) {
                return SunatUBLIDGenerator.generateInvoiceDocumentId(session, organization, invoiceType);
            }

        };
    }

    @Override
    public UBLReader<InvoiceType> reader() {
        return new UBLReader<InvoiceType>() {

            @Override
            public void close() {
            }

            @Override
            public InvoiceType read(byte[] bytes) {
                return UBL21Reader.invoice().read(bytes);
            }

            @Override
            public InvoiceType read(Document document) {
                return UBL21Reader.invoice().read(document);
            }

        };
    }

    @Override
    public UBLWriter<InvoiceType> writer() {
        return new UBLWriter<InvoiceType>() {

            @Override
            public void close() {
            }

            @Override
            public Document write(OrganizationModel organization, InvoiceType t) {
                try {
                    MapBasedNamespaceContext mapBasedNamespace = SunatMarshallerUtils.getBasedNamespaceContext("urn:oasis:names:specification:ubl:schema:xsd:Invoice-2");
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    MicroWriter.writeToStream(UBL21Writer.invoice().getAsMicroDocument(t), out, new XMLWriterSettings().setNamespaceContext(mapBasedNamespace).setPutNamespaceContextPrefixesInRoot(true));

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
    public UBLSender<InvoiceModel> sender() {
        return new UBLSender<InvoiceModel>() {

            @Override
            public SendEventModel sendToCustomer(OrganizationModel organization, InvoiceModel invoice) throws ModelInsuficientData, SendException {
                SendEventModel sendEvent = invoice.addSendEvent(DestinyType.CUSTOMER);
                sendToCustomer(organization, invoice, sendEvent);
                return sendEvent;
            }

            @Override
            public void sendToCustomer(OrganizationModel organization, InvoiceModel invoice, SendEventModel sendEvent) throws ModelInsuficientData, SendException {
                InvoiceManager invoiceManager = new InvoiceManager(session);
                invoiceManager.sendToThirdPartyByEmail(organization, invoice, sendEvent, invoice.getCustomerElectronicMail());
            }

            @Override
            public SendEventModel sendToThirdParty(OrganizationModel organization, InvoiceModel invoice) throws ModelInsuficientData, SendException {
                SendEventModel sendEvent = invoice.addSendEvent(DestinyType.THIRD_PARTY);
                sendToThirdParty(organization, invoice, sendEvent);
                return sendEvent;
            }

            @Override
            public void sendToThirdParty(OrganizationModel organization, InvoiceModel invoice, SendEventModel sendEvent) throws ModelInsuficientData, SendException {
                String sunatAddress = organization.getAttribute(SunatConfig.SUNAT_ADDRESS_1);
                String sunatUsername = organization.getAttribute(SunatConfig.SUNAT_USERNAME);
                String sunatPassword = organization.getAttribute(SunatConfig.SUNAT_PASSWORD);

                if (sunatAddress == null) {
                    throw new ModelInsuficientData("No se pudo encontrar una url de envio valida");
                }
                if (sunatUsername == null || sunatPassword == null) {
                    throw new ModelInsuficientData("No se pudo encontrar un usuario y/o password valido en la organizacion");
                }
                if (invoice.getInvoiceTypeCode().equals(TipoInvoice.BOLETA.getCodigo())) {
                    throw new SunatSendException("Las boletas no pueden ser enviadas individualmente. Use el Resumen diario");
                }

                String xmlFilename = "";
                String zipFileName = "";
                byte[] zipFile = null;

                SunatSenderUtils sunatSender = null;
                try {
                    xmlFilename = SunatTemplateUtils.generateFileName(organization, invoice) + ".xml";
                    zipFileName = SunatTemplateUtils.generateFileName(organization, invoice) + ".zip";
                    zipFile = SunatTemplateUtils.generateZip(invoice.getXmlAsFile().getFile(), xmlFilename);

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

                    invoice.removeRequiredAction(RequiredAction.SEND_TO_TRIRD_PARTY);
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
