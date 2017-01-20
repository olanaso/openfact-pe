package org.openfact.pe.services.ubl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.openfact.common.converts.DocumentUtils;
import org.openfact.models.*;
import org.openfact.pe.models.UBLVoidedDocumentProvider;
import org.openfact.pe.models.VoidedDocumentModel;
import org.openfact.pe.models.types.voided.VoidedDocumentsType;
import org.openfact.pe.models.utils.SunatDocumentToType;
import org.openfact.pe.models.utils.SunatTypeToDocument;
import org.openfact.ubl.UBLIDGenerator;
import org.openfact.ubl.UBLReader;
import org.openfact.ubl.UBLSender;
import org.openfact.ubl.UBLWriter;
import org.w3c.dom.Document;

public class SunatUBLVoidedDocumentProvider implements UBLVoidedDocumentProvider {

	private OpenfactSession session;

	public SunatUBLVoidedDocumentProvider(OpenfactSession session) {
		this.session = session;
	}

	@Override
	public void close() {

	}

	@Override
	public UBLIDGenerator<VoidedDocumentsType> idGenerator() {
		return new UBLIDGenerator<VoidedDocumentsType>() {

			@Override
			public void close() {
			}

			@Override
			public String generateID(OrganizationModel organization, VoidedDocumentsType voidedDocumentsType) {
				String documentId = SunatUBLIDGenerator.generateVoidedDocumentId(session, organization);
				return documentId;
			}
		};
	}

	@Override
	public UBLReader<VoidedDocumentsType> reader() {
		return new UBLReader<VoidedDocumentsType>() {

			@Override
			public void close() {
			}

			@Override
			public VoidedDocumentsType read(Document document) {
				VoidedDocumentsType type = SunatDocumentToType.toVoidedDocumentsType(document);
				return type;
			}

			@Override
			public VoidedDocumentsType read(byte[] bytes) {
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
	public UBLWriter<VoidedDocumentsType> writer() {
		return new UBLWriter<VoidedDocumentsType>() {

			@Override
			public void close() {
			}

			@Override
			public Document write(OrganizationModel organization, VoidedDocumentsType voidedDocumentsType) {
				try {
					Document document = SunatTypeToDocument.toDocument(voidedDocumentsType);
					return document;
				} catch (JAXBException e) {
					throw new ModelException(e);
				}
			}
		};
	}

	@Override
	public UBLSender<VoidedDocumentModel> sender() {
		return new UBLSender<VoidedDocumentModel>() {

			@Override
			public SendEventModel sendToCustomer(OrganizationModel organization, VoidedDocumentModel voidedDocumentModel) throws ModelInsuficientData, SendException {
				return null;
			}

			@Override
			public void sendToCustomer(OrganizationModel organization, VoidedDocumentModel voidedDocumentModel, SendEventModel sendEvent) throws ModelInsuficientData, SendException {

			}

			@Override
			public SendEventModel sendToThirdParty(OrganizationModel organization, VoidedDocumentModel voidedDocumentModel) throws ModelInsuficientData, SendException {
				return null;
			}

			@Override
			public void sendToThirdParty(OrganizationModel organization, VoidedDocumentModel voidedDocumentModel, SendEventModel sendEvent) throws ModelInsuficientData, SendException {

			}

			@Override
			public void close() {
			}

//			@Override
//			public SendEventModel sendToThridParty(OrganizationModel organization, VoidedDocumentModel voidedDocument)
//					throws SendException {
//				SendEventModel model = null;
//				byte[] zip = null;
//				String fileName = "";
//				try {
//					fileName = SunatTemplateUtils.generateXmlFileName(organization, voidedDocument);
//					zip = SunatTemplateUtils.generateZip(voidedDocument.getXmlDocument(), fileName);
//					// sender
//					String response = new SunatSenderUtils(organization, EmissionType.CPE).sendSummary(zip, fileName,
//							InternetMediaType.ZIP);
//					// Write event to the default database
//					model = session.getProvider(SunatSendEventProvider.class).addSendEvent(organization,
//							SendResultType.SUCCESS, voidedDocument);
//					model.setDestiny(SunatSenderUtils.getDestiny(EmissionType.CPE));
//					model.addFileAttatchments(SunatTemplateUtils.toFileModel(InternetMediaType.ZIP, fileName, zip));
//					Map<String, String> result = new HashMap<>();
//					result.put("ACCEPTED BY SUNAT", "YES");
//					result.put("TICKET", response);
//					model.setResponse(result);
//					model.setDescription("Voided document submitted successfully to SUNAT");
//					model.setType("SUNAT");
//					if (model.getResult()) {
//						voidedDocument.removeRequiredAction(RequiredAction.SEND_TO_TRIRD_PARTY);
//					}
//				} catch (TransformerException e) {
//					throw new SendException(e);
//				} catch (SOAPFaultException e) {
//					SOAPFault soapFault = e.getFault();
//					// Write event to the default database
//					model = session.getProvider(SunatSendEventProvider.class).addSendEvent(organization,
//							SendResultType.ERROR, voidedDocument);
//					model.addFileAttatchments(SunatTemplateUtils.toFileModel(InternetMediaType.ZIP, fileName, zip));
//					model.setDestiny(SunatSenderUtils.getDestiny(EmissionType.CPE));
//					model.setType("SUNAT");
//					model.setDescription(soapFault.getFaultString());
//					model.setResponse(
//							SunatResponseUtils.faultToMap(soapFault.getFaultCode(), soapFault.getFaultString()));
//				} catch (Exception e) {
//					model = session.getProvider(SunatSendEventProvider.class).addSendEvent(organization,
//							SendResultType.ERROR, voidedDocument);
//					model.addFileAttatchments(SunatTemplateUtils.toFileModel(InternetMediaType.ZIP, fileName, zip));
//					model.setDestiny(SunatSenderUtils.getDestiny(EmissionType.CPE));
//					model.setType("SUNAT");
//					model.setDescription(e.getMessage());
//				}
//				return model;
//			}
//
//			@Override
//			public SendEventModel sendToCustomer(OrganizationModel organization, VoidedDocumentModel perception)
//					throws SendException {
//				return null;
//			}
		};
	}

}
