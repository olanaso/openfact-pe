package org.openfact.pe.services.ubl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.openfact.models.PartyLegalEntityModel;
import org.openfact.models.PartyModel;
import org.openfact.models.SimpleFileModel;
import org.openfact.models.UserSenderModel;
import org.openfact.models.enums.InternetMediaType;
import org.openfact.models.enums.RequiredAction;
import org.openfact.models.enums.SendResultType;
import org.openfact.pe.model.types.PerceptionType;
import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.SunatSendEventProvider;
import org.openfact.pe.models.UBLPerceptionProvider;
import org.openfact.pe.models.utils.SunatDocumentIdProvider;
import org.openfact.pe.models.utils.SunatDocumentToType;
import org.openfact.pe.models.utils.SunatTypeToDocument;
import org.openfact.pe.services.constants.SunatEventType;
import org.openfact.pe.services.util.SunatResponseUtils;
import org.openfact.pe.services.util.SunatSenderUtils;
import org.openfact.pe.services.util.SunatTemplateUtils;
import org.openfact.ubl.SendEventModel;
import org.openfact.ubl.SendException;
import org.openfact.ubl.UBLIDGenerator;
import org.openfact.ubl.UBLReader;
import org.openfact.ubl.UBLSender;
import org.openfact.ubl.UBLWriter;
import org.w3c.dom.Document;

public class SunatUBLPerceptionProvider implements UBLPerceptionProvider {
	private OpenfactSession session;

	public SunatUBLPerceptionProvider(OpenfactSession session) {
		this.session = session;
	}

	@Override
	public void close() {

	}

	@Override
	public UBLIDGenerator<PerceptionType> idGenerator() {
		return new UBLIDGenerator<PerceptionType>() {

			@Override
			public void close() {
			}

			@Override
			public String generateID(OrganizationModel organization, PerceptionType perceptionType) {
				String documentId = SunatDocumentIdProvider.generatePerceptionDocumentId(session, organization);
				return documentId;
			}
		};
	}

	@Override
	public UBLReader<PerceptionType> reader() {
		return new UBLReader<PerceptionType>() {

			@Override
			public void close() {
			}

			@Override
			public PerceptionType read(Document document) {
				PerceptionType type = SunatDocumentToType.toPerceptionType(document);
				return type;					
			}

			@Override
			public PerceptionType read(byte[] bytes) {
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
	public UBLWriter<PerceptionType> writer() {
		return new UBLWriter<PerceptionType>() {

			@Override
			public void close() {
			}

			@Override
			public Document write(OrganizationModel organization, PerceptionType perceptionType,
					Map<String, String> attributes) {
				try {
					Document document = SunatTypeToDocument.toDocument(perceptionType);
					return document;
				} catch (JAXBException e) {
					throw new ModelException(e);
				}
			}

			@Override
			public Document write(OrganizationModel organization, PerceptionType perceptionType) {
				return write(organization, perceptionType, Collections.emptyMap());
			}
		};
	}

	@Override
	public UBLSender<PerceptionModel> sender() {
		return new UBLSender<PerceptionModel>() {

			@Override
			public void close() {
			}

			@Override
			public SendEventModel sendToCustomer(OrganizationModel organization, PerceptionModel perception)
					throws SendException {

				// User where the email will be send
				UserSenderModel user = new UserSenderModel() {
					@Override
					public String getFullName() {
						return perception.getEntityName();
					}

					@Override
					public String getEmail() {
						return perception.getEntityEmail();
					}
				};

				// Attatchments
				FileModel file = new SimpleFileModel();
				file.setFileName(perception.getPerceptionDocumentNumber());
				file.setFile(perception.getXmlDocument());
				file.setMimeType("application/xml");

				try {
					Map<String, Object> attributes = new HashMap<String, Object>();
					attributes.put("user", new ProfileBean(user));
					attributes.put("organizationName", SunatTemplateUtils.getOrganizationName(organization));

					session.getProvider(EmailTemplateProvider.class).setOrganization(organization).setUser(user)
							.setAttachments(new ArrayList<>(Arrays.asList(file)))
							.send(SunatTemplateUtils.toCamelCase(SunatEventType.PERCEPTION) + "Subject",
									"event-" + SunatEventType.PERCEPTION.toString().toLowerCase() + ".ftl", attributes);

					// Write event to the database
					SendEventModel sendEvent = session.getProvider(SunatSendEventProvider.class)
							.addSendEvent(organization, SendResultType.SUCCESS, perception);
					sendEvent.setDescription("Perception Sended by Email");

					if (sendEvent.getResult()) {
						perception.removeRequiredAction(RequiredAction.SEND_TO_CUSTOMER);
					}
					return sendEvent;
				} catch (EmailException e) {
					throw new SendException(e);
				}
			}

			@Override
			public SendEventModel sendToThridParty(OrganizationModel organization, PerceptionModel perception)
					throws SendException {
				SendEventModel model = null;
				byte[] zip = null;
				String fileName = "";
				try {
					fileName = SunatTemplateUtils.generateXmlFileName(organization, perception);
					zip = SunatTemplateUtils.generateZip(perception.getXmlDocument(), fileName);
					// sender
					byte[] response = new SunatSenderUtils(organization).sendBill(zip, fileName, InternetMediaType.ZIP);
					// Write event to the default database
					model = session.getProvider(SunatSendEventProvider.class).addSendEvent(organization,
							SendResultType.SUCCESS, perception);
					model.setDestiny(SunatSenderUtils.getDestiny());
					model.addFileAttatchments(SunatTemplateUtils.toFileModel(InternetMediaType.ZIP, fileName, zip));
					model.addFileResponseAttatchments(
							SunatTemplateUtils.toFileModel(InternetMediaType.ZIP, "R" + fileName, response));
					model.setResponse(SunatResponseUtils.byteResponseToMap(response));
					model.setDescription("Perception submitted successfully to SUNAT");
					model.setType("SUNAT");
					if (model.getResult()) {
						perception.removeRequiredAction(RequiredAction.SEND_TO_TRIRD_PARTY);
					}
				} catch (TransformerException e) {
					throw new SendException(e);
				} catch (SOAPFaultException e) {
					SOAPFault soapFault = e.getFault();
					// Write event to the default database
					model = session.getProvider(SunatSendEventProvider.class).addSendEvent(organization,
							SendResultType.ERROR, perception);
					model.addFileAttatchments(SunatTemplateUtils.toFileModel(InternetMediaType.ZIP, fileName, zip));
					model.setDestiny(SunatSenderUtils.getDestiny());
					model.setType("SUNAT");
					model.setDescription(soapFault.getFaultString());
					model.setResponse(
							SunatResponseUtils.faultToMap(soapFault.getFaultCode(), soapFault.getFaultString()));
				} catch (Exception e) {
					model = session.getProvider(SunatSendEventProvider.class).addSendEvent(organization,
							SendResultType.ERROR, perception);
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
