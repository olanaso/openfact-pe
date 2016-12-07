package org.openfact.pe.services.ubl;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.soap.SOAPFault;
import javax.xml.transform.TransformerException;
import javax.xml.ws.soap.SOAPFaultException;

import org.openfact.common.converts.DocumentUtils;
import org.openfact.common.converts.StringUtils;
import org.openfact.email.EmailException;
import org.openfact.email.EmailTemplateProvider;
import org.openfact.models.CreditNoteModel;
import org.openfact.models.CustomerPartyModel;
import org.openfact.models.FileModel;
import org.openfact.models.ModelException;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.PartyLegalEntityModel;
import org.openfact.models.ScrollModel;
import org.openfact.models.SimpleFileModel;
import org.openfact.models.UserSenderModel;
import org.openfact.models.enums.InternetMediaType;
import org.openfact.models.enums.RequiredAction;
import org.openfact.models.enums.SendResultType;
import org.openfact.pe.constants.CodigoTipoDocumento;
import org.openfact.pe.models.utils.SunatUtils;
import org.openfact.pe.services.util.SunatResponseUtils;
import org.openfact.pe.services.util.SunatSenderUtils;
import org.openfact.pe.services.util.SunatTemplateUtils;
import org.openfact.ubl.SendEventModel;
import org.openfact.ubl.SendEventProvider;
import org.openfact.ubl.SendException;
import org.openfact.ubl.UBLCreditNoteProvider;
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
				CodigoTipoDocumento creditNoteCode = CodigoTipoDocumento.NOTA_CREDITO;

				CreditNoteModel lastCreditNote = null;
				ScrollModel<CreditNoteModel> creditNotes = session.creditNotes().getCreditNotesScroll(organization,
						false, 4, 2);
				Iterator<CreditNoteModel> iterator = creditNotes.iterator();

				Pattern pattern = Pattern.compile(creditNoteCode.getMask());
				while (iterator.hasNext()) {
					CreditNoteModel creditNote = iterator.next();
					String documentId = creditNote.getDocumentId();

					Matcher matcher = pattern.matcher(documentId);
					if (matcher.find()) {
						lastCreditNote = creditNote;
						break;
					}
				}

				int series = 0;
				int number = 0;
				if (lastCreditNote != null) {
					String[] splits = lastCreditNote.getDocumentId().split("-");
					series = Integer.parseInt(splits[0].substring(1));
					number = Integer.parseInt(splits[1]);
				}

				int nextNumber = SunatUtils.getNextNumber(number, 99_999_999);
				int nextSeries = SunatUtils.getNextSerie(series, number, 999, 99_999_999);
				StringBuilder documentId = new StringBuilder();
				documentId.append(creditNoteCode.getMask().substring(0, 1));
				documentId.append(StringUtils.padLeft(String.valueOf(nextSeries), 3, "0"));
				documentId.append("-");
				documentId.append(StringUtils.padLeft(String.valueOf(nextNumber), 8, "0"));

				return documentId.toString();
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
					Map<String, String> attributes) {
				try {
					MapBasedNamespaceContext mapBasedNamespace = SunatUtils
							.getBasedNamespaceContext("urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2");
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					MicroWriter.writeToStream(UBL21Writer.creditNote().getAsMicroDocument(creditNoteType), out,
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
			public SendEventModel sendToCustomer(OrganizationModel organization, CreditNoteModel creditNote)
					throws SendException {
				CustomerPartyModel customerParty = creditNote.getAccountingCustomerParty();
				if (customerParty == null || customerParty.getParty() == null
						|| customerParty.getParty().getContact() == null
						|| customerParty.getParty().getContact().getElectronicMail() == null) {
					return null;
				}

				// User where the email will be send
				UserSenderModel user = new UserSenderModel() {
					@Override
					public String getFullName() {
						List<PartyLegalEntityModel> partyLegalEntities = customerParty.getParty().getPartyLegalEntity();
						return partyLegalEntities.stream().map(f -> f.getRegistrationName())
								.reduce((t, u) -> t + "," + u).get();
					}

					@Override
					public String getEmail() {
						return customerParty.getParty().getContact().getElectronicMail();
					}
				};

				// Attatchments
				FileModel file = new SimpleFileModel();
				file.setFileName(creditNote.getDocumentId() + ".xml");
				file.setFile(creditNote.getXmlDocument());
				file.setMimeType("application/xml");

				try {
					session.getProvider(EmailTemplateProvider.class).setOrganization(organization).setUser(user)
							.setAttachments(new ArrayList<>(Arrays.asList(file))).sendCreditNote(creditNote);

					// Write event to the database
					SendEventModel sendEvent = session.getProvider(SendEventProvider.class).addSendEvent(organization,
							SendResultType.SUCCESS, creditNote);
					sendEvent.setDescription("CreditNote Sended by Email");

					return sendEvent;
				} catch (EmailException e) {
					throw new SendException(e);
				}
			}

			@Override
			public SendEventModel sendToThridParty(OrganizationModel organization, CreditNoteModel creditNote)
					throws SendException {
				SendEventModel model = null;
				byte[] zip = null;
				String fileName="";
				try {
					 fileName = SunatTemplateUtils.generateXmlFileName(organization, creditNote);
					zip = SunatTemplateUtils.generateZip(creditNote.getXmlDocument(), fileName);
					// sender
					byte[] response = new SunatSenderUtils(organization).sendBill(zip, fileName, InternetMediaType.ZIP);
					// Write event to the default database
					model = session.getProvider(SendEventProvider.class).addSendEvent(organization,
							SendResultType.SUCCESS, creditNote);
					model.setDestiny(SunatSenderUtils.getDestiny());
					model.addFileAttatchments(SunatTemplateUtils.toFileModel(InternetMediaType.ZIP, fileName, zip));
					model.addFileResponseAttatchments(
							SunatTemplateUtils.toFileModel(InternetMediaType.ZIP, "R" + fileName, response));
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
					model = session.getProvider(SendEventProvider.class).addSendEvent(organization,
							SendResultType.ERROR, creditNote);
					model.addFileAttatchments(SunatTemplateUtils.toFileModel(InternetMediaType.ZIP, fileName, zip));
					model.setDestiny(SunatSenderUtils.getDestiny());
					model.setType("SUNAT");
					model.setDescription(soapFault.getFaultString());
					model.setResponse(
							SunatResponseUtils.faultToMap(soapFault.getFaultCode(), soapFault.getFaultString()));
				} catch (Exception e) {
					model = session.getProvider(SendEventProvider.class).addSendEvent(organization,
							SendResultType.ERROR, creditNote);
					model.addFileAttatchments(SunatTemplateUtils.toFileModel(InternetMediaType.ZIP, fileName, zip));
					model.setDestiny(SunatSenderUtils.getDestiny());
					model.setType("SUNAT");
					model.setDescription(e.getMessage());
				}
				return model;
			}
		};
	}

}
