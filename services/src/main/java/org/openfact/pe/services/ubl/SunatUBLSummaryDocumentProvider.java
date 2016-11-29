package org.openfact.pe.services.ubl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
import org.openfact.models.FileModel;
import org.openfact.models.ModelException;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.ScrollModel;
import org.openfact.models.enums.InternetMediaType;
import org.openfact.models.enums.SendResultType;
import org.openfact.pe.constants.CodigoTipoDocumento;
import org.openfact.pe.model.types.SummaryDocumentsType;
import org.openfact.pe.model.types.SunatFactory;
import org.openfact.pe.models.SummaryDocumentModel;
import org.openfact.pe.models.SummaryDocumentProvider;
import org.openfact.pe.models.SummaryDocumentsSendEventModel;
import org.openfact.pe.models.SunatResponseModel;
import org.openfact.pe.models.SunatResponseProvider;
import org.openfact.pe.models.SunatSendEventProvider;
import org.openfact.pe.models.UBLSummaryDocumentProvider;
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
				CodigoTipoDocumento summaryDocumentCode = CodigoTipoDocumento.RESUMEN_DIARIO;
				SummaryDocumentModel lastSummaryDocument = null;
				ScrollModel<SummaryDocumentModel> summaryDocuments = session.getProvider(SummaryDocumentProvider.class)
						.getSummaryDocumentsScroll(organization, false, 4, 2);
				Iterator<SummaryDocumentModel> iterator = summaryDocuments.iterator();

				Pattern pattern = Pattern.compile(summaryDocumentCode.getMask());
				while (iterator.hasNext()) {
					SummaryDocumentModel summaryDocument = iterator.next();
					String documentId = summaryDocument.getDocumentId();

					Matcher matcher = pattern.matcher(documentId);
					if (matcher.find()) {
						lastSummaryDocument = summaryDocument;
						break;
					}
				}

				int series = 0;
				int number = 0;
				if (lastSummaryDocument != null) {
					String[] splits = lastSummaryDocument.getDocumentId().split("-");
					series = Integer.parseInt(splits[0].substring(1));
					number = Integer.parseInt(splits[1]);
				}

				int nextNumber = SunatUtils.getNextNumber(number, 99_999_999);
				int nextSeries = SunatUtils.getNextSerie(series, number, 999, 99_999_999);
				StringBuilder documentId = new StringBuilder();
				documentId.append(summaryDocumentCode.getMask().substring(0, 1));
				documentId.append(StringUtils.padLeft(String.valueOf(nextSeries), 3, "0"));
				documentId.append("-");
				documentId.append(StringUtils.padLeft(String.valueOf(nextNumber), 8, "0"));

				return documentId.toString();
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
				try {
					JAXBContext factory = JAXBContext.newInstance(SunatFactory.class);
					Unmarshaller unmarshal = factory.createUnmarshaller();
					@SuppressWarnings("unchecked")
					JAXBElement<SummaryDocumentsType> jaxbSummaryDocumentsType = (JAXBElement<SummaryDocumentsType>) unmarshal
							.unmarshal(document);
					SummaryDocumentsType summaryDocumentsType = jaxbSummaryDocumentsType.getValue();
					return summaryDocumentsType;
				} catch (JAXBException e) {
					throw new ModelException(e);
				}
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
					Map<String, String> attributes) {
				SunatFactory factory = new SunatFactory();
				JAXBContext context;
				try {
					context = JAXBContext.newInstance(SunatFactory.class);
					Marshaller marshallerElement = context.createMarshaller();
					JAXBElement<SummaryDocumentsType> jaxbElement = factory
							.createSummaryDocuments(summaryDocumentsType);
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
			public SendEventModel sendToThridParty(OrganizationModel organization, SummaryDocumentModel summaryDocument)
					throws SendException {
				SummaryDocumentsSendEventModel summaryDocumentsSendEvent = null;
				byte[] zip = null;
				try {
					List<FileModel> files = new ArrayList<>();
					String fileName = SunatTemplateUtils.generateXmlFileName(organization, summaryDocument);
					zip = SunatTemplateUtils.generateZip(summaryDocument.getXmlDocument(), fileName);
					files.add(SunatTemplateUtils.toFileModel(InternetMediaType.ZIP.getMimeType(), fileName, zip));
					// sender
					String response = new SunatSenderUtils(organization).sendSummary(zip, fileName,
							InternetMediaType.ZIP);
					// Write event to the default database
					summaryDocumentsSendEvent = (SummaryDocumentsSendEventModel) session
							.getProvider(SunatSendEventProvider.class)
							.addSendEvent(organization, SendResultType.SUCCESS, summaryDocument);
					// summaryDocumentsSendEvent.setAttachments(files);
					summaryDocumentsSendEvent.setSummaryDocument(summaryDocument);
					// Write event to the extends database
					SunatResponseModel sunatResponse = session.getProvider(SunatResponseProvider.class)
							.addSunatResponse(organization, SendResultType.SUCCESS, summaryDocumentsSendEvent);
					sunatResponse.setTicket(response);
				} catch (TransformerException e) {
					throw new SendException(e);
				} catch (IOException e) {
					throw new SendException(e);
				} catch (SOAPFaultException e) {
					SOAPFault soapFault = e.getFault();
					// Write event to the default database
					summaryDocumentsSendEvent = (SummaryDocumentsSendEventModel) session
							.getProvider(SunatSendEventProvider.class)
							.addSendEvent(organization, SendResultType.ERROR, summaryDocument);
					summaryDocumentsSendEvent.setSummaryDocument(summaryDocument);
					summaryDocumentsSendEvent.setDescription(soapFault.getFaultString());
					// Write event to the extends database
					SunatResponseModel sunatResponse = session.getProvider(SunatResponseProvider.class)
							.addSunatResponse(organization, SendResultType.ERROR, summaryDocumentsSendEvent);
					sunatResponse.setErrorMessage(soapFault.getFaultString());
					sunatResponse.setResponseCode(soapFault.getFaultCode());
				}
				return summaryDocumentsSendEvent;
			}
		};
	}

}
