package org.openfact.pe.services.ubl;

import java.util.*;

import javax.xml.bind.JAXBException;

import org.openfact.common.converts.DocumentUtils;
import org.openfact.models.*;
import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.UBLPerceptionProvider;
import org.openfact.pe.models.types.perception.PerceptionType;
import org.openfact.pe.models.utils.SunatDocumentToType;
import org.openfact.pe.models.utils.SunatTypeToDocument;
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
				String documentId = SunatUBLIDGenerator.generatePerceptionDocumentId(session, organization);
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
			public Document write(OrganizationModel organization, PerceptionType perceptionType) {
				try {
					Document document = SunatTypeToDocument.toDocument(perceptionType);
					return document;
				} catch (JAXBException e) {
					throw new ModelException(e);
				}
			}
		};
	}

	@Override
	public UBLSender<PerceptionModel> sender() {
		return new UBLSender<PerceptionModel>() {

			@Override
			public SendEventModel sendToCustomer(OrganizationModel organization, PerceptionModel perceptionModel) throws ModelInsuficientData, SendException {
				return null;
			}

			@Override
			public void sendToCustomer(OrganizationModel organization, PerceptionModel perceptionModel, SendEventModel sendEvent) throws ModelInsuficientData, SendException {

			}

			@Override
			public SendEventModel sendToThirdParty(OrganizationModel organization, PerceptionModel perceptionModel) throws ModelInsuficientData, SendException {
				return null;
			}

			@Override
			public void sendToThirdParty(OrganizationModel organization, PerceptionModel perceptionModel, SendEventModel sendEvent) throws ModelInsuficientData, SendException {

			}

			@Override
			public void close() {
			}

//			@Override
//			public SendEventModel sendToCustomer(OrganizationModel organization, PerceptionModel perception)
//					throws SendException {
//
//				// User where the email will be send
//				UserSenderModel user = new UserSenderModel() {
//					@Override
//					public String getFullName() {
//						return perception.getEntityName();
//					}
//
//					@Override
//					public String getEmail() {
//						return perception.getEntityEmail();
//					}
//				};
//
//				// Attatchments
//				FileModel file = new SimpleFileModel();
//				file.setFileName(perception.getDocumentId()+ InternetMediaType.XML.getExtension());
//				file.setFile(perception.getXmlDocument());
//				file.setMimeType("application/xml");
//				SendEventModel sendEvent=null;
//				try {
//					Map<String, Object> attributes = new HashMap<String, Object>();
//					attributes.put("user", new ProfileBean(user));
//					attributes.put("organizationName", SunatTemplateUtils.getOrganizationName(organization));
//
//					session.getProvider(EmailTemplateProvider.class).setOrganization(organization).setUser(user)
//							.setAttachments(new ArrayList<>(Arrays.asList(file)))
//							.send(SunatTemplateUtils.toCamelCase(SunatEventType.PERCEPTION) + "Subject",
//									"event-" + SunatEventType.PERCEPTION.toString().toLowerCase() + ".ftl", attributes);
//
//					// Write event to the database
//					 sendEvent = session.getProvider(SunatSendEventProvider.class)
//							.addSendEvent(organization, SendResultType.SUCCESS, perception);
//					sendEvent.setDescription("Perception Sended by Email");
//					Map<String, String> destiny = new HashMap<>();
//					destiny.put("email", user.getEmail());
//					sendEvent.setDestiny(destiny);
//					if (sendEvent.getResult()) {
//						perception.removeRequiredAction(RequiredAction.SEND_TO_CUSTOMER);
//					}
//					return sendEvent;
//				} catch (EmailException e) {
//					sendEvent = session.getProvider(SunatSendEventProvider.class).addSendEvent(organization,
//							SendResultType.ERROR, perception);
//					sendEvent.addFileAttatchments(file);
//					Map<String, String> destiny = new HashMap<>();
//					destiny.put("email", user.getEmail());
//					sendEvent.setDestiny(destiny);
//					sendEvent.setType("EMAIL-CUSTOMER");
//					sendEvent.setDescription(e.getMessage());
//				}
//				return  sendEvent;
//			}
//
//			@Override
//			public SendEventModel sendToThridParty(OrganizationModel organization, PerceptionModel perception)
//					throws SendException {
//				SendEventModel model = null;
//				byte[] zip = null;
//				String fileName = "";
//				try {
//					fileName = SunatTemplateUtils.generateXmlFileName(organization, perception);
//					zip = SunatTemplateUtils.generateZip(perception.getXmlDocument(), fileName);
//					// sender
//					byte[] response = new SunatSenderUtils(organization, EmissionType.OCPE).sendBill(zip, fileName, InternetMediaType.ZIP);
//					// Write event to the default database
//					model = session.getProvider(SunatSendEventProvider.class).addSendEvent(organization,
//							SendResultType.SUCCESS, perception);
//					model.setDestiny(SunatSenderUtils.getDestiny(EmissionType.OCPE));
//					model.addFileAttatchments(SunatTemplateUtils.toFileModel(InternetMediaType.ZIP, fileName, zip));
//					model.addFileResponseAttatchments(
//							SunatTemplateUtils.toFileModel(InternetMediaType.ZIP, "R" + fileName, response));
//					model.setResponse(SunatResponseUtils.byteResponseToMap(response));
//					model.setDescription("Perception submitted successfully to SUNAT");
//					model.setType("SUNAT");
//					if (model.getResult()) {
//						perception.removeRequiredAction(RequiredAction.SEND_TO_TRIRD_PARTY);
//					}
//				} catch (TransformerException e) {
//					throw new SendException(e);
//				} catch (SOAPFaultException e) {
//					SOAPFault soapFault = e.getFault();
//					// Write event to the default database
//					model = session.getProvider(SunatSendEventProvider.class).addSendEvent(organization,
//							SendResultType.ERROR, perception);
//					model.addFileAttatchments(SunatTemplateUtils.toFileModel(InternetMediaType.ZIP, fileName, zip));
//					model.setDestiny(SunatSenderUtils.getDestiny(EmissionType.OCPE));
//					model.setType("SUNAT");
//					model.setDescription(soapFault.getFaultString());
//					model.setResponse(
//							SunatResponseUtils.faultToMap(soapFault.getFaultCode(), soapFault.getFaultString()));
//				} catch (Exception e) {
//					model = session.getProvider(SunatSendEventProvider.class).addSendEvent(organization,
//							SendResultType.ERROR, perception);
//					model.addFileAttatchments(SunatTemplateUtils.toFileModel(InternetMediaType.ZIP, fileName, zip));
//					model.setDestiny(SunatSenderUtils.getDestiny(EmissionType.OCPE));
//					model.setType("SUNAT");
//					model.setDescription(e.getMessage());
//				}
//				return model;
//			}
		};
	}

}
