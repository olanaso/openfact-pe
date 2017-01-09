package org.openfact.pe.services.ubl;

import java.io.ByteArrayOutputStream;
import java.util.*;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.soap.SOAPFault;
import javax.xml.transform.TransformerException;
import javax.xml.ws.soap.SOAPFaultException;

import org.openfact.common.converts.DocumentUtils;
import org.openfact.email.EmailException;
import org.openfact.email.EmailTemplateProvider;
import org.openfact.models.CreditNoteModel;
import org.openfact.models.FileModel;
import org.openfact.models.ModelException;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.SimpleFileModel;
import org.openfact.models.UserSenderModel;
import org.openfact.models.enums.InternetMediaType;
import org.openfact.models.enums.RequiredAction;
import org.openfact.models.enums.SendResultType;
import org.openfact.pe.constants.EmissionType;
import org.openfact.pe.models.utils.SunatDocumentIdProvider;
import org.openfact.pe.models.utils.SunatMarshallerUtils;
import org.openfact.pe.services.util.SunatResponseUtils;
import org.openfact.pe.services.util.SunatSenderUtils;
import org.openfact.pe.services.util.SunatTemplateUtils;
import org.openfact.report.ReportException;
import org.openfact.ubl.*;
import org.w3c.dom.Document;

import com.helger.ubl21.UBL21Reader;
import com.helger.ubl21.UBL21Writer;
import com.helger.xml.microdom.serialize.MicroWriter;
import com.helger.xml.namespace.MapBasedNamespaceContext;
import com.helger.xml.serialize.write.XMLWriterSettings;

import oasis.names.specification.ubl.schema.xsd.creditnote_21.CreditNoteType;

public class SunatUBLCreditNoteProvider implements UBLCreditNoteProvider {

	protected OpenfactSession session;

	public SunatUBLCreditNoteProvider(OpenfactSession session) {
		this.session = session;
	}

	@Override
	public void close() {
	}

	@Override
	public UBLIDGenerator<CreditNoteType> idGenerator() {
		return new UBLIDGenerator<CreditNoteType>() {

			@Override
			public void close() {
			}

			@Override
			public String generateID(OrganizationModel organization, CreditNoteType creditNoteType) {
				String codeType = null, code = "";
				if (creditNoteType.getDiscrepancyResponseCount() > 0) {
					codeType = creditNoteType.getDiscrepancyResponse().get(0).getReferenceIDValue();
				} else if (creditNoteType.getBillingReferenceCount() > 0) {
					codeType = creditNoteType.getBillingReference().get(0).getInvoiceDocumentReference().getIDValue();
				}
				if (codeType != null) {
					code = codeType.substring(0, 1);
				}
				return SunatDocumentIdProvider.generateCreditNoteDocumentId(session, organization, code);
			}
		};
	}

	@Override
	public UBLReader<CreditNoteType> reader() {
		return new UBLReader<CreditNoteType>() {

			@Override
			public void close() {
			}

			@Override
			public CreditNoteType read(byte[] bytes) {
				return UBL21Reader.creditNote().read(bytes);
			}

			@Override
			public CreditNoteType read(Document document) {
				return UBL21Reader.creditNote().read(document);
			}

		};
	}

	@Override
	public UBLWriter<CreditNoteType> writer() {
		return new UBLWriter<CreditNoteType>() {

			@Override
			public void close() {
			}

			@Override
			public Document write(OrganizationModel organization, CreditNoteType creditNoteType,
					Map<String, List<String>> attributes) {
				try {
					MapBasedNamespaceContext mapBasedNamespace = SunatMarshallerUtils.getBasedNamespaceContext("urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2");
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					MicroWriter.writeToStream(UBL21Writer.creditNote().getAsMicroDocument(creditNoteType), out, new XMLWriterSettings().setNamespaceContext(mapBasedNamespace).setPutNamespaceContextPrefixesInRoot(true));

					Document document = DocumentUtils.byteToDocument(out.toByteArray());
					return document;
				} catch (DatatypeConfigurationException e) {
					throw new ModelException(e);
				} catch (Exception e) {
					throw new ModelException(e);
				}
			}

			@Override
			public Document write(OrganizationModel organization, CreditNoteType t) {
				return write(organization, t, Collections.emptyMap());
			}
		};
	}

	@Override
	public UBLSender<CreditNoteModel> sender() {
		return new UBLSender<CreditNoteModel>() {

			@Override
			public void close() {
			}

			@Override
			public SendEventModel sendToCustomer(OrganizationModel organization, CreditNoteModel creditNote) throws SendException {
				if (creditNote.getCustomerElectronicMail() == null) {
					SendEventModel sendEvent =  session.getProvider(SendEventProvider.class).addSendEvent(organization, SendResultType.ERROR, creditNote);
					sendEvent.setType("EMAIL");
					sendEvent.setDescription("Could not find a valid email for the customer.");
					return sendEvent;
				}

				if (organization.getSmtpConfig().size() == 0) {
					SendEventModel sendEvent =  session.getProvider(SendEventProvider.class).addSendEvent(organization, SendResultType.ERROR, creditNote);
					sendEvent.setType("EMAIL");
					sendEvent.setDescription("Could not find a valid smtp configuration on organization.");
					return sendEvent;
				}

				// User where the email will be send
				UserSenderModel user = new UserSenderModel() {
					@Override
					public String getFullName() {
						return creditNote.getCustomerRegistrationName();
					}

					@Override
					public String getEmail() {
						return creditNote.getCustomerElectronicMail();
					}
				};

				try {
					// Attatchments
					FileModel xmlFile = new SimpleFileModel();
					xmlFile.setFileName(creditNote.getDocumentId() + ".xml");
					xmlFile.setFile(creditNote.getXmlDocument());
					xmlFile.setMimeType("application/xml");

					FileModel pdfFile = new SimpleFileModel();

					pdfFile.setFileName(creditNote.getDocumentId() + ".pdf");
					pdfFile.setFile(session.getProvider(UBLReportProvider.class).invoice().setOrganization(organization).getReportAsPdf(creditNote));
					pdfFile.setMimeType("application/pdf");

					session.getProvider(EmailTemplateProvider.class)
							.setOrganization(organization).setUser(user)
							.setAttachments(Arrays.asList(xmlFile, pdfFile))
							.sendCreditNote(creditNote);

					// Write event to the database
					SendEventModel sendEvent =  session.getProvider(SendEventProvider.class).addSendEvent(organization, SendResultType.SUCCESS, creditNote);
					sendEvent.setType("EMAIL");

					sendEvent.setDescription("Credit Note successfully sended");
					sendEvent.addFileAttatchments(xmlFile);
					sendEvent.addFileAttatchments(pdfFile);
					sendEvent.setResult(true);

					Map<String, String> destiny = new HashMap<>();
					destiny.put("email", user.getEmail());
					sendEvent.setDestiny(destiny);

					// Remove required action
					creditNote.removeRequiredAction(RequiredAction.SEND_TO_CUSTOMER);

					return sendEvent;
				} catch (ReportException e) {
					SendEventModel sendEvent =  session.getProvider(SendEventProvider.class).addSendEvent(organization, SendResultType.ERROR, creditNote);
					sendEvent.setType("EMAIL");
					sendEvent.setDescription(e.getMessage());
					throw new SendException("Could not generate pdf report", e);
				} catch (EmailException e) {
					SendEventModel sendEvent =  session.getProvider(SendEventProvider.class).addSendEvent(organization, SendResultType.ERROR, creditNote);
					sendEvent.setType("EMAIL");
					sendEvent.setDescription(e.getMessage());
					throw new SendException("Could not send email", e);
				}
			}

			@Override
			public SendEventModel sendToThridParty(OrganizationModel organization, CreditNoteModel creditNote) throws SendException {
				SendEventModel model = null;
				byte[] zip = null;
				String fileName = "";
				try {
					fileName = SunatTemplateUtils.generateXmlFileName(organization, creditNote);
					zip = SunatTemplateUtils.generateZip(creditNote.getXmlDocument(), fileName);

					// sender
					byte[] response = new SunatSenderUtils(organization, EmissionType.CPE).sendBill(zip, fileName, InternetMediaType.ZIP);

					// Write event to the default database
					model = session.getProvider(SendEventProvider.class).addSendEvent(organization, SendResultType.SUCCESS, creditNote);
					model.setDestiny(SunatSenderUtils.getDestiny(EmissionType.CPE));
					model.addFileAttatchments(SunatTemplateUtils.toFileModel(InternetMediaType.ZIP, fileName, zip));
					model.addFileResponseAttatchments(SunatTemplateUtils.toFileModel(InternetMediaType.ZIP, "R" + fileName, response));
					model.setResponse(SunatResponseUtils.byteResponseToMap(response));
					model.setDescription("Credit Note submitted successfully to SUNAT");
					model.setType("SUNAT");
					if (model.getResult()) {
						creditNote.removeRequiredAction(RequiredAction.SEND_TO_TRIRD_PARTY);
					}
				} catch (TransformerException e) {
					throw new SendException(e);
				} catch (SOAPFaultException e) {
					SOAPFault soapFault = e.getFault();
					// Write event to the default database
					model = session.getProvider(SendEventProvider.class).addSendEvent(organization, SendResultType.ERROR, creditNote);
					model.addFileAttatchments(SunatTemplateUtils.toFileModel(InternetMediaType.ZIP, fileName, zip));
					model.setDestiny(SunatSenderUtils.getDestiny(EmissionType.CPE));
					model.setType("SUNAT");
					model.setDescription(soapFault.getFaultString());
					model.setResponse(SunatResponseUtils.faultToMap(soapFault.getFaultCode(), soapFault.getFaultString()));
				} catch (Exception e) {
					model = session.getProvider(SendEventProvider.class).addSendEvent(organization, SendResultType.ERROR, creditNote);
					model.addFileAttatchments(SunatTemplateUtils.toFileModel(InternetMediaType.ZIP, fileName, zip));
					model.setDestiny(SunatSenderUtils.getDestiny(EmissionType.CPE));
					model.setType("SUNAT");
					model.setDescription(e.getMessage());
				}
				return model;
			}
		};
	}

}
