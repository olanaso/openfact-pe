package org.openfact.pe.services.ubl;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPFault;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
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
import org.openfact.models.enums.RequiredAction;
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
import org.openfact.pe.services.util.SunatResponseUtils;
import org.openfact.pe.services.util.SunatSenderUtils;
import org.openfact.pe.services.util.SunatTemplateUtils;
import org.openfact.pe.services.util.SunatUtils;
import org.openfact.ubl.SendEventModel;
import org.openfact.ubl.SendEventProvider;
import org.openfact.ubl.SendException;
import org.openfact.ubl.UBLIDGenerator;
import org.openfact.ubl.UBLReader;
import org.openfact.ubl.UBLSender;
import org.openfact.ubl.UBLWriter;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

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

				int number = 0;
				if (lastSummaryDocument != null) {
					String[] splits = lastSummaryDocument.getDocumentId().split("-");
					number = Integer.parseInt(splits[2]);
				}

				int nextNumber = SunatUtils.getNextNumber(number, 99999);
				int nextSeries = SunatUtils.getDateToNumber();
				StringBuilder documentId = new StringBuilder();
				documentId.append(summaryDocumentCode.getMask().substring(0, 2));
				documentId.append("-");
				documentId.append(nextSeries);
				documentId.append("-");
				documentId.append(StringUtils.padLeft(String.valueOf(nextNumber), 3, "0"));

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
					StringWriter xmlWriter = new StringWriter();
					XMLStreamWriter xmlStream = XMLOutputFactory.newInstance().createXMLStreamWriter(xmlWriter);
					xmlStream.setNamespaceContext(SunatUtils.getBasedNamespaceContext(
							"urn:sunat:names:specification:ubl:peru:schema:xsd:SummaryDocuments-1"));
					marshallerElement.marshal(jaxbElement, xmlStream);
					Document document = DocumentUtils.getStringToDocument(xmlWriter.toString());
					return document;
				} catch (JAXBException e) {
					throw new ModelException(e);
				} catch (XMLStreamException e) {
					throw new ModelException(e);
				} catch (FactoryConfigurationError e) {
					throw new ModelException(e);
				} catch (IOException e) {
					throw new ModelException(e);
				} catch (SAXException e) {
					throw new ModelException(e);
				} catch (ParserConfigurationException e) {
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
				SendEventModel model = null;
				byte[] zip = null;
				String fileName="";
				try {
					 fileName = SunatTemplateUtils.generateXmlFileName(organization, summaryDocument);
					zip = SunatTemplateUtils.generateZip(summaryDocument.getXmlDocument(), fileName);
					// sender
					String response = new SunatSenderUtils(organization).sendSummary(zip, fileName,
							InternetMediaType.ZIP);
					// Write event to the default database
					model = session.getProvider(SunatSendEventProvider.class).addSendEvent(organization,
							SendResultType.SUCCESS, summaryDocument);
					model.setDestiny(SunatSenderUtils.getDestiny());
					model.addFileAttatchments(SunatTemplateUtils.toFileModel(InternetMediaType.ZIP, fileName, zip));
					Map<String, String> result = new HashMap<>();
					result.put("ACCEPTED BY SUNAT", "YES");
					result.put("TICKET", response);
					model.setResponse(result);
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
					model.setDestiny(SunatSenderUtils.getDestiny());
					model.setType("SUNAT");
					model.setDescription(soapFault.getFaultString());
					model.setResponse(
							SunatResponseUtils.faultToMap(soapFault.getFaultCode(), soapFault.getFaultString()));
				} catch (Exception e) {
					model = session.getProvider(SunatSendEventProvider.class).addSendEvent(organization,
							SendResultType.ERROR, summaryDocument);
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
