package org.openfact.pe.services.ubl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
import org.openfact.models.CustomerPartyModel;
import org.openfact.models.DebitNoteModel;
import org.openfact.models.FileModel;
import org.openfact.models.DebitNoteSendEventModel;
import org.openfact.models.ModelException;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.PartyLegalEntityModel;
import org.openfact.models.ScrollModel;
import org.openfact.models.SimpleFileModel;
import org.openfact.ubl.SendEventModel;
import org.openfact.models.UserSenderModel;
import org.openfact.models.enums.InternetMediaType;
import org.openfact.models.enums.SendResultType;
import org.openfact.pe.constants.CodigoTipoDocumento;
import org.openfact.pe.models.SunatResponseModel;
import org.openfact.pe.models.SunatResponseProvider;
import org.openfact.pe.services.util.SunatSenderUtils;
import org.openfact.pe.services.util.SunatTemplateUtils;
import org.openfact.pe.services.util.SunatUtils;
import org.openfact.ubl.UBLIDGenerator;
import org.openfact.ubl.SendEventProvider;
import org.openfact.ubl.SendException;
import org.openfact.ubl.UBLDebitNoteProvider;
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
				CodigoTipoDocumento debitNoteCode = CodigoTipoDocumento.NOTA_DEBITO;

				DebitNoteModel lastDebitNote = null;
				ScrollModel<DebitNoteModel> debitNotes = session.debitNotes().getDebitNotesScroll(organization, false,
						4, 2);
				Iterator<DebitNoteModel> iterator = debitNotes.iterator();

				Pattern pattern = Pattern.compile(debitNoteCode.getMask());
				while (iterator.hasNext()) {
					DebitNoteModel debitNote = iterator.next();
					String documentId = debitNote.getDocumentId();

					Matcher matcher = pattern.matcher(documentId);
					if (matcher.find()) {
						lastDebitNote = debitNote;
						break;
					}
				}

				int series = 0;
				int number = 0;
				if (lastDebitNote != null) {
					String[] splits = lastDebitNote.getDocumentId().split("-");
					series = Integer.parseInt(splits[0].substring(1));
					number = Integer.parseInt(splits[1]);
				}

				int nextNumber = SunatUtils.getNextNumber(number, 99_999_999);
				int nextSeries = SunatUtils.getNextSerie(series, number, 999, 99_999_999);
				StringBuilder documentId = new StringBuilder();
				documentId.append(debitNoteCode.getMask().substring(0, 1));
				documentId.append(StringUtils.padLeft(String.valueOf(nextSeries), 3, "0"));
				documentId.append("-");
				documentId.append(StringUtils.padLeft(String.valueOf(nextNumber), 8, "0"));

				return documentId.toString();
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
					Map<String, String> attributes) {
				try {
					MapBasedNamespaceContext mapBasedNamespace = SunatUtils
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
			public SendEventModel sendToCustomer(OrganizationModel organization, DebitNoteModel debitNote)
					throws SendException {
				CustomerPartyModel customerParty = debitNote.getAccountingCustomerParty();
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
				file.setFileName(debitNote.getDocumentId() + ".xml");
				file.setFile(debitNote.getXmlDocument());
				file.setMimeType("application/xml");

				try {
					session.getProvider(EmailTemplateProvider.class).setOrganization(organization).setUser(user)
							.setAttachments(new ArrayList<>(Arrays.asList(file))).sendDebitNote(debitNote);

					// Write event to the database
					SendEventModel sendEvent = session.getProvider(SendEventProvider.class).addSendEvent(organization,
							SendResultType.SUCCESS, debitNote);
					sendEvent.setDescription("DebitNote Sended by Email");

					return sendEvent;
				} catch (EmailException e) {
					throw new SendException(e);
				}
			}

			@Override
			public SendEventModel sendToThridParty(OrganizationModel organization, DebitNoteModel debitNote)
					throws SendException {
				SendEventModel model = null;
				byte[] zip = null;
				try {
					String fileName = SunatTemplateUtils.generateXmlFileName(organization, debitNote);
					zip = SunatTemplateUtils.generateZip(debitNote.getXmlDocument(), fileName);
					// sender
					byte[] response = new SunatSenderUtils(organization).sendBill(zip, fileName, InternetMediaType.ZIP);
					// Write event to the default database
					model = session.getProvider(SendEventProvider.class).addSendEvent(organization,
							SendResultType.SUCCESS, debitNote);
					model.addFileAttatchments(
							SunatTemplateUtils.toFileModel(InternetMediaType.ZIP, fileName, zip));
					// Write event to the extends database
					SunatResponseModel sunatResponse = session.getProvider(SunatResponseProvider.class)
							.addSunatResponse(organization, SendResultType.SUCCESS, model);
					sunatResponse.setDocumentResponse(response);
				} catch (TransformerException e) {
					throw new SendException(e);
				} catch (IOException e) {
					throw new SendException(e);
				} catch (SOAPFaultException e) {
					SOAPFault soapFault = e.getFault();
					// Write event to the default database
					model = session.getProvider(SendEventProvider.class).addSendEvent(organization,
							SendResultType.ERROR, debitNote);
					model.setDescription(soapFault.getFaultString());
					// Write event to the extends database
					SunatResponseModel sunatResponse = session.getProvider(SunatResponseProvider.class)
							.addSunatResponse(organization, SendResultType.ERROR, model);
					sunatResponse.setErrorMessage(soapFault.getFaultString());
					sunatResponse.setResponseCode(soapFault.getFaultCode());
				}
				return model;
			}
		};
	}

}
