package org.openfact.pe.services.ubl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.SOAPFault;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.ws.soap.SOAPFaultException;

import org.openfact.common.converts.DocumentUtils;
import org.openfact.common.converts.StringUtils;
import org.openfact.email.EmailException;
import org.openfact.email.EmailTemplateProvider;
import org.openfact.email.freemarker.beans.ProfileBean;
import org.openfact.models.FileModel;
import org.openfact.models.ModelException;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.PartyLegalEntityModel;
import org.openfact.models.PartyModel;
import org.openfact.models.ScrollModel;
import org.openfact.models.UserSenderModel;
import org.openfact.models.enums.InternetMediaType;
import org.openfact.models.enums.RequiredAction;
import org.openfact.models.enums.SendResultType;
import org.openfact.pe.constants.CodigoTipoDocumento;
import org.openfact.pe.model.types.RetentionType;
import org.openfact.pe.model.types.SunatFactory;
import org.openfact.pe.models.RetentionModel;
import org.openfact.pe.models.RetentionProvider;
import org.openfact.pe.models.RetentionSendEventModel;
import org.openfact.pe.models.SunatResponseModel;
import org.openfact.pe.models.SunatResponseProvider;
import org.openfact.pe.models.SunatSendEventProvider;
import org.openfact.pe.models.UBLRetentionProvider;
import org.openfact.pe.services.constants.SunatEventType;
import org.openfact.pe.services.util.SunatSenderUtils;
import org.openfact.pe.services.util.SunatTemplateUtils;
import org.openfact.pe.services.util.SunatUtils;
import org.openfact.ubl.SendEventModel;
import org.openfact.ubl.SendException;
import org.openfact.ubl.UBLIDGenerator;
import org.openfact.ubl.UBLReader;
import org.openfact.ubl.UBLSender;
import org.openfact.ubl.UBLWriter;
import org.w3c.dom.Document;

public class SunatUBLRetentionProvider implements UBLRetentionProvider {

	private OpenfactSession session;

	public SunatUBLRetentionProvider(OpenfactSession session) {
		this.session = session;
	}

	@Override
	public void close() {

	}

	@Override
	public UBLIDGenerator<RetentionType> idGenerator() {
		return new UBLIDGenerator<RetentionType>() {

			@Override
			public void close() {

			}

			@Override
			public String generateID(OrganizationModel organization, RetentionType retentionType) {
				CodigoTipoDocumento retentionCode = CodigoTipoDocumento.RETENCION;
				RetentionModel lastRetention = null;
				ScrollModel<RetentionModel> retentions = session.getProvider(RetentionProvider.class)
						.getRetentionsScroll(organization, false, 4, 2);
				Iterator<RetentionModel> iterator = retentions.iterator();

				Pattern pattern = Pattern.compile(retentionCode.getMask());
				while (iterator.hasNext()) {
					RetentionModel retention = iterator.next();
					String documentId = retention.getDocumentId();

					Matcher matcher = pattern.matcher(documentId);
					if (matcher.find()) {
						lastRetention = retention;
						break;
					}
				}

				int series = 0;
				int number = 0;
				if (lastRetention != null) {
					String[] splits = lastRetention.getDocumentId().split("-");
					series = Integer.parseInt(splits[0].substring(1));
					number = Integer.parseInt(splits[1]);
				}

				int nextNumber = SunatUtils.getNextNumber(number, 99_999_999);
				int nextSeries = SunatUtils.getNextSerie(series, number, 999, 99_999_999);
				StringBuilder documentId = new StringBuilder();
				documentId.append(retentionCode.getMask().substring(0, 1));
				documentId.append(StringUtils.padLeft(String.valueOf(nextSeries), 3, "0"));
				documentId.append("-");
				documentId.append(StringUtils.padLeft(String.valueOf(nextNumber), 8, "0"));

				return documentId.toString();
			}
		};
	}

	@Override
	public UBLReader<RetentionType> reader() {
		return new UBLReader<RetentionType>() {

			@Override
			public void close() {

			}

			@Override
			public RetentionType read(Document document) {
				try {
					JAXBContext factory = JAXBContext.newInstance(SunatFactory.class);
					Unmarshaller unmarshal = factory.createUnmarshaller();
					@SuppressWarnings("unchecked")
					JAXBElement<RetentionType> jaxbRetentionType = (JAXBElement<RetentionType>) unmarshal
							.unmarshal(document);
					RetentionType retentionType = jaxbRetentionType.getValue();
					return retentionType;
				} catch (JAXBException e) {
					throw new ModelException(e);
				}
			}

			@Override
			public RetentionType read(byte[] bytes) {
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
	public UBLWriter<RetentionType> writer() {
		return new UBLWriter<RetentionType>() {

			@Override
			public void close() {
			}

			@Override
			public Document write(OrganizationModel organization, RetentionType retentionType,
					Map<String, String> attributes) {
				SunatFactory factory = new SunatFactory();
				JAXBContext context;
				try {
					context = JAXBContext.newInstance(SunatFactory.class);
					Marshaller marshallerElement = context.createMarshaller();
					JAXBElement<RetentionType> jaxbElement = factory.createRetention(retentionType);
					DOMResult res = new DOMResult();
					marshallerElement.marshal(jaxbElement, res);
					// Element element = ((Document)
					// res.getNode()).getDocumentElement();
					Document document = ((Document) res.getNode()).getOwnerDocument();
					return document;
				} catch (JAXBException e) {
					throw new ModelException(e);
				}
			}

			@Override
			public Document write(OrganizationModel organization, RetentionType retentionType) {
				return write(organization, retentionType, Collections.emptyMap());
			}
		};
	}

	@Override
	public UBLSender<RetentionModel> sender() {
		return new UBLSender<RetentionModel>() {

			@Override
			public void close() {
			}

			@Override
			public SendEventModel sendToCustomer(OrganizationModel organization, RetentionModel retention)
					throws SendException {
				PartyModel retentionParty = retention.getReceiverParty();
				if (retentionParty == null || retentionParty.getContact() == null
						|| retentionParty.getContact().getElectronicMail() == null) {
					return null;
				}

				// User where the email will be send
				UserSenderModel user = new UserSenderModel() {
					@Override
					public String getFullName() {
						List<PartyLegalEntityModel> partyLegalEntities = retentionParty.getPartyLegalEntity();
						return partyLegalEntities.stream().map(f -> f.getRegistrationName())
								.reduce((t, u) -> t + "," + u).get();
					}

					@Override
					public String getEmail() {
						return retentionParty.getContact().getElectronicMail();
					}
				};

				// Attatchments
				FileModel file = new FileModel();
				file.setFileName(retention.getDocumentId());
				file.setFile(retention.getXmlDocument());
				file.setMimeType("application/xml");

				try {
					Map<String, Object> attributes = new HashMap<String, Object>();
					attributes.put("user", new ProfileBean(user));
					attributes.put("organizationName", SunatTemplateUtils.getOrganizationName(organization));

					session.getProvider(EmailTemplateProvider.class).setOrganization(organization).setUser(user)
							.setAttachments(new ArrayList<>(Arrays.asList(file)))
							.send(SunatTemplateUtils.toCamelCase(SunatEventType.RETENTION) + "Subject",
									"event-" + SunatEventType.RETENTION.toString().toLowerCase() + ".ftl", attributes);

					// Write event to the database
					SendEventModel sendEvent = session.getProvider(SunatSendEventProvider.class)
							.addSendEvent(organization, SendResultType.SUCCESS, retention);
					sendEvent.setDescription("Retention Sended by Email");

					if (sendEvent.getResult()) {
						retention.removeRequiredAction(RequiredAction.SEND_TO_CUSTOMER);
					}
					return sendEvent;
				} catch (EmailException e) {
					throw new SendException(e);
				}
			}

			@Override
			public SendEventModel sendToThridParty(OrganizationModel organization, RetentionModel retention)
					throws SendException {
				RetentionSendEventModel retentionSendEvent = null;
				byte[] zip = null;
				try {
					List<FileModel> files = new ArrayList<>();
					String fileName = SunatTemplateUtils.generateXmlFileName(organization, retention);
					zip = SunatTemplateUtils.generateZip(retention.getXmlDocument(), fileName);
					files.add(SunatTemplateUtils.toFileModel(InternetMediaType.ZIP.getMimeType(), fileName, zip));
					// sender
					byte[] response = new SunatSenderUtils(organization).sendBill(zip, fileName, InternetMediaType.ZIP);
					// Write event to the default database
					retentionSendEvent = (RetentionSendEventModel) session.getProvider(SunatSendEventProvider.class)
							.addSendEvent(organization, SendResultType.SUCCESS, retention);
					// retentionSendEvent.setAttachments(files);
					retentionSendEvent.setRetention(retention);
					// Write event to the extends database
					SunatResponseModel sunatResponse = session.getProvider(SunatResponseProvider.class)
							.addSunatResponse(organization, SendResultType.SUCCESS, retentionSendEvent);
					sunatResponse.setDocumentResponse(response);
				} catch (TransformerException e) {
					throw new SendException(e);
				} catch (IOException e) {
					throw new SendException(e);
				} catch (SOAPFaultException e) {
					SOAPFault soapFault = e.getFault();
					// Write event to the default database
					retentionSendEvent = (RetentionSendEventModel) session.getProvider(SunatSendEventProvider.class)
							.addSendEvent(organization, SendResultType.ERROR, retention);
					retentionSendEvent.setRetention(retention);
					retentionSendEvent.setDescription(soapFault.getFaultString());
					// Write event to the extends database
					SunatResponseModel sunatResponse = session.getProvider(SunatResponseProvider.class)
							.addSunatResponse(organization, SendResultType.ERROR, retentionSendEvent);
					sunatResponse.setErrorMessage(soapFault.getFaultString());
					sunatResponse.setResponseCode(soapFault.getFaultCode());
				}
				return retentionSendEvent;
			}
		};
	}

}
