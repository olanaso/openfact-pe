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
import org.openfact.ubl.*;
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
				String codeType = null, code = "";
				if (debitNoteType.getDiscrepancyResponseCount() > 0) {
					codeType = debitNoteType.getDiscrepancyResponse().get(0).getReferenceIDValue();
				} else if (debitNoteType.getBillingReferenceCount() > 0) {
					codeType = debitNoteType.getBillingReference().get(0).getInvoiceDocumentReference().getIDValue();
				}
				if (codeType != null) {
					code = codeType.substring(0, 1);
				}
				return SunatDocumentIdProvider.generateDebitNoteDocumentId(session, organization, code);
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
			public Document write(OrganizationModel organization, DebitNoteType debitNoteType,
					Map<String, List<String>> attributes) {
				try {
					MapBasedNamespaceContext mapBasedNamespace = SunatMarshallerUtils
							.getBasedNamespaceContext("urn:oasis:names:specification:ubl:schema:xsd:DebitNote-2");
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					MicroWriter.writeToStream(UBL21Writer.debitNote().getAsMicroDocument(debitNoteType), out,
							new XMLWriterSettings().setNamespaceContext(mapBasedNamespace)
									.setPutNamespaceContextPrefixesInRoot(true));

					Document document = DocumentUtils.byteToDocument(out.toByteArray());
					return document;
				} catch (DatatypeConfigurationException e) {
					throw new ModelException(e);
				} catch (Exception e) {
					throw new ModelException(e);
				}
			}

			@Override
			public Document write(OrganizationModel organization, DebitNoteType t) {
				return write(organization, t, Collections.emptyMap());
			}
		};
	}

	@Override
	public UBLSender<DebitNoteModel> sender() {
		return new UBLSender<DebitNoteModel>() {

			@Override
			public void close() {
			}

			@Override
			public SendEventModel sendToCustomer(OrganizationModel organization, DebitNoteModel debitNote) throws SendException {
				SendEventModel sendEvent = debitNote.addSendEvent(DestinyType.CUSTOMER);
				return sendToCustomer(organization, debitNote, sendEvent);
			}

			@Override
			public SendEventModel sendToCustomer(OrganizationModel organization, DebitNoteModel debitNote, SendEventModel sendEvent) throws SendException {
				sendEvent.setType("EMAIL");

				if (debitNote.getCustomerElectronicMail() == null) {
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
						return debitNote.getCustomerRegistrationName();
					}

					@Override
					public String getEmail() {
						return debitNote.getCustomerElectronicMail();
					}
				};

				try {
					FileProvider fileProvider = session.getProvider(FileProvider.class);

					// Attatchments
					FileModel xmlFile = fileProvider.createFile(organization, debitNote.getDocumentId() + ".xml", debitNote.getXmlFile().getFile());
					FileMymeTypeModel xmlFileMymeType = new FileMymeTypeModel(xmlFile, "application/xml");

					byte[] pdfFileBytes = session.getProvider(UBLReportProvider.class).debitNote().setOrganization(organization).getReport(debitNote, ExportFormat.PDF);
					FileModel pdfFile = fileProvider.createFile(organization, debitNote.getDocumentId() + ".pdf", pdfFileBytes);
					FileMymeTypeModel pdfFileMymeType = new FileMymeTypeModel(pdfFile, "application/pdf");

					session.getProvider(EmailTemplateProvider.class)
							.setOrganization(organization).setUser(user)
							.setAttachments(Arrays.asList(xmlFileMymeType, pdfFileMymeType))
							.sendDebitNote(debitNote);

					// Write event to the database
					sendEvent.setDescription("Debit Note successfully sended");
					sendEvent.addFileAttatchments(xmlFile);
					sendEvent.addFileAttatchments(pdfFile);
					sendEvent.setResult(SendResultType.SUCCESS);

					Map<String, String> destiny = new HashMap<>();
					destiny.put("email", user.getEmail());
					sendEvent.setDestiny(destiny);

					// Remove required action
					debitNote.removeRequiredAction(RequiredAction.SEND_TO_CUSTOMER);

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
			public SendEventModel sendToThridParty(OrganizationModel organization, DebitNoteModel debitNote) throws SendException {
				SendEventModel sendEvent =  debitNote.addSendEvent(DestinyType.THIRD_PARTY);
				return sendToThridParty(organization, debitNote, sendEvent);
			}

			@Override
			public SendEventModel sendToThridParty(OrganizationModel organization, DebitNoteModel debitNote, SendEventModel sendEvent) throws SendException {
				byte[] zip = null;
				String fileName = "";
				try {
					fileName = SunatTemplateUtils.generateXmlFileName(organization, debitNote);
					zip = SunatTemplateUtils.generateZip(debitNote.getXmlDocument(), fileName);

					// sender
					byte[] response = new SunatSenderUtils(organization, EmissionType.CPE).sendBill(zip, fileName, InternetMediaType.ZIP);

					// Write event to the default database
					sendEvent.setResult(SendResultType.SUCCESS);
					sendEvent.setDestiny(SunatSenderUtils.getDestiny(EmissionType.CPE));
					//sendEvent.addFileAttatchments(SunatTemplateUtils.toFileModel(InternetMediaType.ZIP, fileName, zip));
					//sendEvent.addFileResponseAttatchments(SunatTemplateUtils.toFileModel(InternetMediaType.ZIP, "R" + fileName, response));
					sendEvent.setResponse(SunatResponseUtils.byteResponseToMap(response));
					sendEvent.setDescription("Debit Note submitted successfully to SUNAT");
					sendEvent.setType("SUNAT");
					if (sendEvent.getResult().equals(SendResultType.SUCCESS)) {
						debitNote.removeRequiredAction(RequiredAction.SEND_TO_TRIRD_PARTY);
					}
				} catch (TransformerException e) {
					throw new SendException(e);
				} catch (SOAPFaultException e) {
					SOAPFault soapFault = e.getFault();
					sendEvent.setResult(SendResultType.ERROR);
					//sendEvent.addFileAttatchments(SunatTemplateUtils.toFileModel(InternetMediaType.ZIP, fileName, zip));
					sendEvent.setDestiny(SunatSenderUtils.getDestiny(EmissionType.CPE));
					sendEvent.setType("SUNAT");
					sendEvent.setDescription(soapFault.getFaultString());
					sendEvent.setResponse(SunatResponseUtils.faultToMap(soapFault.getFaultCode(), soapFault.getFaultString()));
				} catch (Exception e) {
					sendEvent.setResult(SendResultType.ERROR);
					//sendEvent.addFileAttatchments(SunatTemplateUtils.toFileModel(InternetMediaType.ZIP, fileName, zip));
					sendEvent.setDestiny(SunatSenderUtils.getDestiny(EmissionType.CPE));
					sendEvent.setType("SUNAT");
					sendEvent.setDescription(e.getMessage());
				}
				return sendEvent;
			}
		};
	}

}
