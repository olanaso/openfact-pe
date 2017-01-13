package org.openfact.pe.services.ubl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.soap.SOAPFault;
import javax.xml.transform.TransformerException;
import javax.xml.ws.soap.SOAPFaultException;

import jodd.io.ZipBuilder;
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
import org.openfact.pe.constants.EmissionType;
import org.openfact.pe.models.utils.SunatDocumentIdProvider;
import org.openfact.pe.models.utils.SunatMarshallerUtils;
import org.openfact.pe.services.util.SunatResponseUtils;
import org.openfact.pe.services.util.SunatSenderUtils;
import org.openfact.pe.services.util.SunatTemplateUtils;
import org.openfact.report.ExportFormat;
import org.openfact.report.ReportException;
import org.openfact.ubl.UBLIDGenerator;
import org.openfact.ubl.UBLInvoiceProvider;
import org.openfact.ubl.UBLReader;
import org.openfact.ubl.UBLReportProvider;
import org.openfact.ubl.UBLSender;
import org.openfact.ubl.UBLWriter;
import org.w3c.dom.Document;

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
				String documentId = SunatDocumentIdProvider.generateInvoiceDocumentId(session, organization, invoiceType.getInvoiceTypeCodeValue());
				return documentId;
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
			public Document write(OrganizationModel organization, InvoiceType invoiceType,
					Map<String, List<String>> attributes) {
				try {
					MapBasedNamespaceContext mapBasedNamespace = SunatMarshallerUtils.getBasedNamespaceContext("urn:oasis:names:specification:ubl:schema:xsd:Invoice-2");
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					MicroWriter.writeToStream(UBL21Writer.invoice().getAsMicroDocument(invoiceType), out, new XMLWriterSettings().setNamespaceContext(mapBasedNamespace).setPutNamespaceContextPrefixesInRoot(true));

					Document document = DocumentUtils.byteToDocument(out.toByteArray());
					return document;
				} catch (DatatypeConfigurationException e) {
					throw new ModelException(e);
				} catch (Exception e) {
					throw new ModelException(e);
				}
			}

			@Override
			public Document write(OrganizationModel organization, InvoiceType t) {
				return write(organization, t, Collections.emptyMap());
			}
		};
	}

	@Override
	public UBLSender<InvoiceModel> sender() {
		return new UBLSender<InvoiceModel>() {

			@Override
			public void close() {
			}

			@Override
			public SendEventModel sendToCustomer(OrganizationModel organization, InvoiceModel invoice) throws SendException {
				SendEventModel sendEvent = invoice.addSendEvent(DestinyType.CUSTOMER);
				return sendToCustomer(organization, invoice, sendEvent);
			}

			@Override
			public SendEventModel sendToCustomer(OrganizationModel organization, InvoiceModel invoice, SendEventModel sendEvent) throws SendException {
				sendEvent.setType("EMAIL");

				if (invoice.getCustomerElectronicMail() == null) {
					sendEvent.setResult(SendResultType.ERROR);
					sendEvent.setDescription("Could not find a valid email for the customer.");
					return sendEvent;
				}

				if (organization.getSmtpConfig().size() == 0) {
					sendEvent.setResult(SendResultType.ERROR);
					sendEvent.setDescription("Could not find a valid smtp configuration on organization.");
					return sendEvent;
				}

				// User where the email will be send
				UserSenderModel user = new UserSenderModel() {
					@Override
					public String getFullName() {
						return invoice.getCustomerRegistrationName();
					}

					@Override
					public String getEmail() {
						return invoice.getCustomerElectronicMail();
					}
				};

				try {
					FileProvider fileProvider = session.getProvider(FileProvider.class);

					// Attatchments
					FileModel xmlFile = fileProvider.createFile(organization, invoice.getDocumentId() + ".xml", invoice.getXmlFile().getFile());
					FileMymeTypeModel xmlFileMymeType = new FileMymeTypeModel(xmlFile, "application/xml");

					byte[] pdfFileBytes = session.getProvider(UBLReportProvider.class).invoice().setOrganization(organization).getReport(invoice, ExportFormat.PDF);
					FileModel pdfFile = fileProvider.createFile(organization, invoice.getDocumentId() + ".pdf", pdfFileBytes);
					FileMymeTypeModel pdfFileMymeType = new FileMymeTypeModel(pdfFile, "application/pdf");

					session.getProvider(EmailTemplateProvider.class)
							.setOrganization(organization).setUser(user)
							.setAttachments(Arrays.asList(xmlFileMymeType, pdfFileMymeType))
							.sendInvoice(invoice);

					// Write event to the database
					sendEvent.setDescription("Ivoice successfully sended");
					sendEvent.addFileAttatchments(xmlFile);
					sendEvent.addFileAttatchments(pdfFile);
					sendEvent.setResult(SendResultType.SUCCESS);

					Map<String, String> destiny = new HashMap<>();
					destiny.put("email", user.getEmail());
					sendEvent.setDestiny(destiny);

					// Remove required action
					invoice.removeRequiredAction(RequiredAction.SEND_TO_CUSTOMER);

					return sendEvent;
				} catch (ReportException e) {
					sendEvent.setResult(SendResultType.ERROR);
					sendEvent.setDescription(e.getMessage());
					throw new SendException("Could not generate pdf report", e);
				} catch (EmailException e) {
					sendEvent.setResult(SendResultType.ERROR);
					sendEvent.setDescription(e.getMessage());
					throw new SendException("Could not send email", e);
				}
			}

			@Override
			public SendEventModel sendToThridParty(OrganizationModel organization, InvoiceModel invoice) throws SendException {
				SendEventModel sendEvent =  invoice.addSendEvent(DestinyType.THIRD_PARTY);
				return sendToThridParty(organization, invoice, sendEvent);
			}

			@Override
			public SendEventModel sendToThridParty(OrganizationModel organization, InvoiceModel invoice, SendEventModel sendEvent) throws SendException {
				sendEvent.setType("SUNAT");

				String fileName = SunatTemplateUtils.generateXmlFileName(organization, invoice);

				byte[] xml = invoice.getXmlFile().getFile();
				byte[] zip;
				try {
					zip = ZipBuilder.createZipInMemory().add(xml).path(fileName + ".xml").save().toBytes();
				} catch (IOException e) {
					throw new SendException("Error creating zip for send", e);
				}

				try {
					// sender
					byte[] response = new SunatSenderUtils(organization, EmissionType.CPE).sendBill(zip, fileName, InternetMediaType.ZIP);

					sendEvent.setResult(SendResultType.SUCCESS);

					// Write event
					//sendEvent.addFileAttatchments(SunatTemplateUtils.toFileModel(InternetMediaType.ZIP, fileName, zip));
					sendEvent.setDestiny(SunatSenderUtils.getDestiny(EmissionType.CPE));
					//sendEvent.addFileResponseAttatchments(SunatTemplateUtils.toFileModel(InternetMediaType.ZIP, "R" + fileName, response));
					sendEvent.setResponse(SunatResponseUtils.byteResponseToMap(response));
					sendEvent.setDescription("Invoice submitted successfully to SUNAT");
					if (sendEvent.getResult().equals(SendResultType.SUCCESS)) {
						invoice.removeRequiredAction(RequiredAction.SEND_TO_TRIRD_PARTY);
					}
				} catch (TransformerException e) {
					throw new SendException("Error transforming", e);
				} catch (SOAPFaultException e) {
					SOAPFault soapFault = e.getFault();
					// Write event to the default database
					sendEvent.setResult(SendResultType.ERROR);
					//sendEvent.addFileAttatchments(SunatTemplateUtils.toFileModel(InternetMediaType.ZIP, fileName, zip));
					sendEvent.setDestiny(SunatSenderUtils.getDestiny(EmissionType.CPE));
					sendEvent.setDescription(soapFault.getFaultString());
					sendEvent.setResponse(SunatResponseUtils.faultToMap(soapFault.getFaultCode(), soapFault.getFaultString()));
				} catch (Exception e) {
					sendEvent.setResult(SendResultType.ERROR);
					//sendEvent.addFileAttatchments(SunatTemplateUtils.toFileModel(InternetMediaType.ZIP, fileName, zip));
					sendEvent.setDestiny(SunatSenderUtils.getDestiny(EmissionType.CPE));
					sendEvent.setDescription(e.getMessage());
				}
				return sendEvent;
			}
		};
	}

}
