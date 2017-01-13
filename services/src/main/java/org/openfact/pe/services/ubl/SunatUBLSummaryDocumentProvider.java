package org.openfact.pe.services.ubl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.openfact.common.converts.DocumentUtils;
import org.openfact.models.*;
import org.openfact.pe.models.SummaryDocumentModel;
import org.openfact.pe.models.UBLSummaryDocumentProvider;
import org.openfact.pe.models.types.summary.SummaryDocumentsType;
import org.openfact.pe.models.utils.SunatDocumentIdProvider;
import org.openfact.pe.models.utils.SunatDocumentToType;
import org.openfact.pe.models.utils.SunatTypeToDocument;
import org.openfact.ubl.UBLIDGenerator;
import org.openfact.ubl.UBLReader;
import org.openfact.ubl.UBLSender;
import org.openfact.ubl.UBLWriter;
import org.w3c.dom.Document;

public class SunatUBLSummaryDocumentProvider implements UBLSummaryDocumentProvider {
	private OpenfactSession session;

	public SunatUBLSummaryDocumentProvider(OpenfactSession session) {
		this.session = session;
	}

	@Override
	public void close() {
	}

	@Override
	public UBLIDGenerator<SummaryDocumentsType> idGenerator() {
		return new UBLIDGenerator<SummaryDocumentsType>() {

			@Override
			public void close() {
			}

			@Override
			public String generateID(OrganizationModel organization, SummaryDocumentsType summaryDocumentsType) {
				String documentId = SunatDocumentIdProvider.generateSummaryDocumentDocumentId(session, organization);
				return documentId;
			}
		};
	}

	@Override
	public UBLReader<SummaryDocumentsType> reader() {
		return new UBLReader<SummaryDocumentsType>() {

			@Override
			public void close() {
			}

			@Override
			public SummaryDocumentsType read(Document document) {
				SummaryDocumentsType type = SunatDocumentToType.toSummaryDocumentsType(document);
				return type;				
			}

			@Override
			public SummaryDocumentsType read(byte[] bytes) {
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
	public UBLWriter<SummaryDocumentsType> writer() {
		return new UBLWriter<SummaryDocumentsType>() {

			@Override
			public void close() {
			}

			@Override
			public Document write(OrganizationModel organization, SummaryDocumentsType summaryDocumentsType,
					Map<String, List<String>> attributes) {
				try {
					Document document = SunatTypeToDocument.toDocument(summaryDocumentsType);
					return document;
				} catch (JAXBException e) {
					throw new ModelException(e);
				}
			}

			@Override
			public Document write(OrganizationModel organization, SummaryDocumentsType summaryDocumentsType) {
				return write(organization, summaryDocumentsType, Collections.emptyMap());
			}
		};
	}

	@Override
	public UBLSender<SummaryDocumentModel> sender() {
		return new UBLSender<SummaryDocumentModel>() {

			@Override
			public void close() {
			}

			@Override
			public SendEventModel sendToCustomer(OrganizationModel organization, SummaryDocumentModel summaryDocument)
					throws SendException {
				return null;
			}

			@Override
			public SendEventModel sendToCustomer(OrganizationModel organization, SummaryDocumentModel summaryDocumentModel, SendEventModel sendEvent) throws SendException {
				return null;
			}

			@Override
			public SendEventModel sendToThridParty(OrganizationModel organization, SummaryDocumentModel summaryDocument)
					throws SendException {
				/*SendEventModel model = null;
				byte[] zip = null;
				String fileName = "";
				try {
					fileName = SunatTemplateUtils.generateXmlFileName(organization, summaryDocument);
					zip = SunatTemplateUtils.generateZip(summaryDocument.getXmlDocument(), fileName);
					// sender
					String response = new SunatSenderUtils(organization, EmissionType.CPE).sendSummary(zip, fileName,
							InternetMediaType.ZIP);
					// Write event to the default database
					model = session.getProvider(SunatSendEventProvider.class).addSendEvent(organization,
							SendResultType.SUCCESS, summaryDocument);
					model.setDestiny(SunatSenderUtils.getDestiny(EmissionType.CPE));
					model.addFileAttatchments(SunatTemplateUtils.toFileModel(InternetMediaType.ZIP, fileName, zip));
					Map<String, String> result = new HashMap<>();
					result.put("ACCEPTED BY SUNAT", "YES");
					result.put("TICKET", response);
					model.setResponse(result);
					model.setDescription("Summary document submitted successfully to SUNAT");
					model.setType("SUNAT");
					if (model.getResult()) {
						summaryDocument.removeRequiredAction(RequiredAction.SEND_TO_TRIRD_PARTY);
					}
				} catch (TransformerException e) {
					throw new SendException(e);
				} catch (SOAPFaultException e) {
					SOAPFault soapFault = e.getFault();
					// Write event to the default database
					model = session.getProvider(SunatSendEventProvider.class).addSendEvent(organization,
							SendResultType.ERROR, summaryDocument);
					model.addFileAttatchments(SunatTemplateUtils.toFileModel(InternetMediaType.ZIP, fileName, zip));
					model.setDestiny(SunatSenderUtils.getDestiny(EmissionType.CPE));
					model.setType("SUNAT");
					model.setDescription(soapFault.getFaultString());
					model.setResponse(
							SunatResponseUtils.faultToMap(soapFault.getFaultCode(), soapFault.getFaultString()));
				} catch (Exception e) {
					model = session.getProvider(SunatSendEventProvider.class).addSendEvent(organization,
							SendResultType.ERROR, summaryDocument);
					model.addFileAttatchments(SunatTemplateUtils.toFileModel(InternetMediaType.ZIP, fileName, zip));
					model.setDestiny(SunatSenderUtils.getDestiny(EmissionType.CPE));
					model.setType("SUNAT");
					model.setDescription(e.getMessage());
				}
				return model;*/
				return null;
			}

			@Override
			public SendEventModel sendToThridParty(OrganizationModel organization, SummaryDocumentModel summaryDocumentModel, SendEventModel sendEvent) throws SendException {
				return null;
			}
		};
	}

}
