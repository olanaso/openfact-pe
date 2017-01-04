package org.openfact.pe.services.ubl;

import java.util.*;

import javax.xml.bind.JAXBException;
import javax.xml.soap.SOAPFault;
import javax.xml.transform.TransformerException;
import javax.xml.ws.soap.SOAPFaultException;

import org.openfact.common.converts.DocumentUtils;
import org.openfact.email.EmailException;
import org.openfact.email.EmailTemplateProvider;
import org.openfact.email.freemarker.beans.ProfileBean;
import org.openfact.models.FileModel;
import org.openfact.models.ModelException;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.SimpleFileModel;
import org.openfact.models.UserSenderModel;
import org.openfact.models.enums.InternetMediaType;
import org.openfact.models.enums.RequiredAction;
import org.openfact.models.enums.SendResultType;
import org.openfact.pe.models.RetentionModel;
import org.openfact.pe.models.SunatSendEventProvider;
import org.openfact.pe.models.UBLRetentionProvider;
import org.openfact.pe.models.types.retention.RetentionType;
import org.openfact.pe.models.utils.SunatDocumentIdProvider;
import org.openfact.pe.models.utils.SunatDocumentToType;
import org.openfact.pe.models.utils.SunatTypeToDocument;
import org.openfact.pe.services.constants.SunatEventType;
import org.openfact.pe.services.util.SunatResponseUtils;
import org.openfact.pe.services.util.SunatSenderUtils;
import org.openfact.pe.services.util.SunatTemplateUtils;
import org.openfact.ubl.*;
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
				String documentId = SunatDocumentIdProvider.generateRetentionDocumentId(session, organization);
				return documentId;
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
				RetentionType type = SunatDocumentToType.toRetentionType(document);
				return type;
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
					Map<String, List<String>> attributes) {
				try {
					Document document = SunatTypeToDocument.toDocument(retentionType);
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
				// User where the email will be send
				UserSenderModel user = new UserSenderModel() {
					@Override
					public String getFullName() {
						return retention.getEntityName();
					}

					@Override
					public String getEmail() {
						return retention.getEntityEmail();
					}
				};

				// Attatchments
				FileModel file = new SimpleFileModel();
				file.setFileName(retention.getDocumentId()+ InternetMediaType.XML.getExtension());
				file.setFile(retention.getXmlDocument());
				file.setMimeType("application/xml");

				SendEventModel sendEvent = null;
				try {
					Map<String, Object> attributes = new HashMap<String, Object>();
					attributes.put("user", new ProfileBean(user));
					attributes.put("organizationName", SunatTemplateUtils.getOrganizationName(organization));

					session.getProvider(EmailTemplateProvider.class).setOrganization(organization).setUser(user)
							.setAttachments(new ArrayList<>(Arrays.asList(file)))
							.send(SunatTemplateUtils.toCamelCase(SunatEventType.RETENTION) + "Subject",
									"event-" + SunatEventType.RETENTION.toString().toLowerCase() + ".ftl", attributes);

					// Write event to the database
					 sendEvent = session.getProvider(SunatSendEventProvider.class)
							.addSendEvent(organization, SendResultType.SUCCESS, retention);
					sendEvent.setDescription("Retention Sended by Email");
					Map<String, String> destiny = new HashMap<>();
					destiny.put("email", user.getEmail());
					sendEvent.setDestiny(destiny);
					if (sendEvent.getResult()) {
						retention.removeRequiredAction(RequiredAction.SEND_TO_CUSTOMER);
					}
				} catch (EmailException e) {
					sendEvent = session.getProvider(SunatSendEventProvider.class).addSendEvent(organization,
							SendResultType.ERROR, retention);
					sendEvent.addFileAttatchments(file);
					Map<String, String> destiny = new HashMap<>();
					destiny.put("email", user.getEmail());
					sendEvent.setDestiny(destiny);
					sendEvent.setType("EMAIL-CUSTOMER");
					sendEvent.setDescription(e.getMessage());
				}
				return  sendEvent;
			}

			@Override
			public SendEventModel sendToThridParty(OrganizationModel organization, RetentionModel retention)
					throws SendException {
				SendEventModel model = null;
				byte[] zip = null;
				String fileName = "";
				try {
					fileName = SunatTemplateUtils.generateXmlFileName(organization, retention);
					zip = SunatTemplateUtils.generateZip(retention.getXmlDocument(), fileName);
					// sender
					byte[] response = new SunatSenderUtils(organization).sendBill(zip, fileName, InternetMediaType.ZIP);
					// Write event to the default database
					model = session.getProvider(SunatSendEventProvider.class).addSendEvent(organization,
							SendResultType.SUCCESS, retention);
					model.setDestiny(SunatSenderUtils.getDestiny());
					model.addFileAttatchments(SunatTemplateUtils.toFileModel(InternetMediaType.ZIP, fileName, zip));
					model.addFileResponseAttatchments(
							SunatTemplateUtils.toFileModel(InternetMediaType.ZIP, "R" + fileName, response));
					model.setResponse(SunatResponseUtils.byteResponseToMap(response));
					model.setDescription("Retention submitted successfully to SUNAT");
					model.setType("SUNAT");
					if (model.getResult()) {
						retention.removeRequiredAction(RequiredAction.SEND_TO_TRIRD_PARTY);
					}
				} catch (TransformerException e) {
					throw new SendException(e);
				} catch (SOAPFaultException e) {
					SOAPFault soapFault = e.getFault();
					model = session.getProvider(SunatSendEventProvider.class).addSendEvent(organization,
							SendResultType.ERROR, retention);
					model.addFileAttatchments(SunatTemplateUtils.toFileModel(InternetMediaType.ZIP, fileName, zip));
					model.setDestiny(SunatSenderUtils.getDestiny());
					model.setType("SUNAT");
					model.setDescription(soapFault.getFaultString());
					model.setResponse(
							SunatResponseUtils.faultToMap(soapFault.getFaultCode(), soapFault.getFaultString()));
				} catch (Exception e) {
					model = session.getProvider(SunatSendEventProvider.class).addSendEvent(organization,
							SendResultType.ERROR, retention);
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
