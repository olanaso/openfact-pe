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

import org.openfact.common.converts.DocumentUtils;
import org.openfact.email.EmailException;
import org.openfact.email.EmailTemplateProvider;
import org.openfact.models.*;
import org.openfact.models.enums.RequiredAction;
import org.openfact.models.enums.SendResultType;
import org.openfact.pe.constants.EmissionType;
import org.openfact.pe.models.utils.SunatDocumentIdProvider;
import org.openfact.pe.models.utils.SunatMarshallerUtils;
import org.openfact.pe.services.util.SunatResponseUtils;
import org.openfact.pe.services.util.SunatSenderUtils;
import org.openfact.pe.services.util.SunatTemplateUtils;
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
			public SendEventModel sendToCustomer(OrganizationModel organization, InvoiceModel invoiceModel) throws SendException {
				return null;
			}

			@Override
			public SendEventModel sendToCustomer(OrganizationModel organization, InvoiceModel invoiceModel, SendEventModel sendEvent) throws SendException {
				return null;
			}

			@Override
			public SendEventModel sendToThirdParty(OrganizationModel organization, InvoiceModel invoiceModel) throws SendException {
				return null;
			}

			@Override
			public SendEventModel sendToThirdParty(OrganizationModel organization, InvoiceModel invoiceModel, SendEventModel sendEvent) throws SendException {
				return null;
			}

			@Override
			public void close() {
			}

//			@Override
//			public SendEventModel sendToThridParty(OrganizationModel organization, InvoiceModel invoice) throws SendException {
//				byte[] zip = null;
//				SendEventModel model = null;
//				String fileName = "";
//				try {
//					fileName = SunatTemplateUtils.generateXmlFileName(organization, invoice);
//					zip = SunatTemplateUtils.generateZip(invoice.getXmlDocument(), fileName);
//
//					// sender
//					byte[] response = new SunatSenderUtils(organization, EmissionType.CPE).sendBill(zip, fileName, InternetMediaType.ZIP);
//
//					// Write event to the default database
//					model = session.getProvider(SendEventProvider.class).addSendEvent(organization, SendResultType.SUCCESS, invoice);
//					model.addFileAttatchments(SunatTemplateUtils.toFileModel(InternetMediaType.ZIP, fileName, zip));
//					model.setDestiny(SunatSenderUtils.getDestiny(EmissionType.CPE));
//					model.addFileResponseAttatchments(SunatTemplateUtils.toFileModel(InternetMediaType.ZIP, "R" + fileName, response));
//					model.setResponse(SunatResponseUtils.byteResponseToMap(response));
//					model.setDescription("Invoice submitted successfully to SUNAT");
//					model.setType("SUNAT");
//					if (model.getResult()) {
//						invoice.removeRequiredAction(RequiredAction.SEND_TO_TRIRD_PARTY);
//					}
//				} catch (TransformerException e) {
//					throw new SendException(e);
//				} catch (IOException e) {
//					throw new SendException(e);
//				} catch (SOAPFaultException e) {
//					SOAPFault soapFault = e.getFault();
//					// Write event to the default database
//					model = session.getProvider(SendEventProvider.class).addSendEvent(organization,
//							SendResultType.ERROR, invoice);
//					model.addFileAttatchments(SunatTemplateUtils.toFileModel(InternetMediaType.ZIP, fileName, zip));
//					model.setDestiny(SunatSenderUtils.getDestiny(EmissionType.CPE));
//					model.setType("SUNAT");
//					model.setDescription(soapFault.getFaultString());
//					model.setResponse(
//							SunatResponseUtils.faultToMap(soapFault.getFaultCode(), soapFault.getFaultString()));
//				} catch (Exception e) {
//					model = session.getProvider(SendEventProvider.class).addSendEvent(organization,
//							SendResultType.ERROR, invoice);
//					model.addFileAttatchments(SunatTemplateUtils.toFileModel(InternetMediaType.ZIP, fileName, zip));
//					model.setDestiny(SunatSenderUtils.getDestiny(EmissionType.CPE));
//					model.setType("SUNAT");
//					model.setDescription(e.getMessage());
//				}
//				return model;
//			}
//
//			@Override
//			public SendEventModel sendToCustomer(OrganizationModel organization, InvoiceModel invoice) throws SendException {
//				if (invoice.getCustomerElectronicMail() == null) {
//					SendEventModel sendEvent =  session.getProvider(SendEventProvider.class).addSendEvent(organization, SendResultType.ERROR, invoice);
//					sendEvent.setType("EMAIL");
//					sendEvent.setDescription("Could not find a valid email for the customer.");
//					return sendEvent;
//				}
//
//				if (organization.getSmtpConfig().size() == 0) {
//					SendEventModel sendEvent =  session.getProvider(SendEventProvider.class).addSendEvent(organization, SendResultType.ERROR, invoice);
//					sendEvent.setType("EMAIL");
//					sendEvent.setDescription("Could not find a valid smtp configuration on organization.");
//					return sendEvent;
//				}
//
//				// User where the email will be send
//				UserSenderModel user = new UserSenderModel() {
//					@Override
//					public String getFullName() {
//						return invoice.getCustomerRegistrationName();
//					}
//
//					@Override
//					public String getEmail() {
//						return invoice.getCustomerElectronicMail();
//					}
//				};
//
//				try {
//					// Attatchments
//					FileModel xmlFile = new SimpleFileModel();
//					xmlFile.setFileName(invoice.getDocumentId() + ".xml");
//					xmlFile.setFile(invoice.getXmlDocument());
//					xmlFile.setMimeType("application/xml");
//
//					FileModel pdfFile = new SimpleFileModel();
//
//					pdfFile.setFileName(invoice.getDocumentId() + ".pdf");
//					pdfFile.setFile(session.getProvider(UBLReportProvider.class).invoice().setOrganization(organization).getReportAsPdf(invoice));
//					pdfFile.setMimeType("application/pdf");
//
//					session.getProvider(EmailTemplateProvider.class)
//							.setOrganization(organization).setUser(user)
//							.setAttachments(Arrays.asList(xmlFile, pdfFile))
//							.sendInvoice(invoice);
//
//					// Write event to the database
//					SendEventModel sendEvent =  session.getProvider(SendEventProvider.class).addSendEvent(organization, SendResultType.SUCCESS, invoice);
//					sendEvent.setType("EMAIL");
//
//					sendEvent.setDescription("Ivoice successfully sended");
//					sendEvent.addFileAttatchments(xmlFile);
//					sendEvent.addFileAttatchments(pdfFile);
//					sendEvent.setResult(true);
//
//					Map<String, String> destiny = new HashMap<>();
//					destiny.put("email", user.getEmail());
//					sendEvent.setDestiny(destiny);
//
//					// Remove required action
//					invoice.removeRequiredAction(RequiredAction.SEND_TO_CUSTOMER);
//
//					return sendEvent;
//				} catch (ReportException e) {
//					SendEventModel sendEvent =  session.getProvider(SendEventProvider.class).addSendEvent(organization, SendResultType.ERROR, invoice);
//					sendEvent.setType("EMAIL");
//					sendEvent.setDescription(e.getMessage());
//					throw new SendException("Could not generate pdf report", e);
//				} catch (EmailException e) {
//					SendEventModel sendEvent =  session.getProvider(SendEventProvider.class).addSendEvent(organization, SendResultType.ERROR, invoice);
//					sendEvent.setType("EMAIL");
//					sendEvent.setDescription(e.getMessage());
//					throw new SendException("Could not send email", e);
//				}
//			}
		};
	}

}
