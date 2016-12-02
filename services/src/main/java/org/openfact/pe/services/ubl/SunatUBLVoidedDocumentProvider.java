package org.openfact.pe.services.ubl;

import java.io.IOException;
import java.io.StringWriter;
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
import org.openfact.models.enums.SendResultType;
import org.openfact.pe.constants.CodigoTipoDocumento;
import org.openfact.pe.model.types.SunatFactory;
import org.openfact.pe.model.types.VoidedDocumentsType;
import org.openfact.pe.models.SunatResponseModel;
import org.openfact.pe.models.SunatResponseProvider;
import org.openfact.pe.models.SunatSendEventProvider;
import org.openfact.pe.models.UBLVoidedDocumentProvider;
import org.openfact.pe.models.VoidedDocumentModel;
import org.openfact.pe.models.VoidedDocumentProvider;
import org.openfact.pe.models.VoidedDocumentsSendEventModel;
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
import org.xml.sax.SAXException;

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
				CodigoTipoDocumento voidedDocumentCode = CodigoTipoDocumento.BAJA;
				VoidedDocumentModel lastVoidedDocument = null;
				ScrollModel<VoidedDocumentModel> voidedDocuments = session.getProvider(VoidedDocumentProvider.class)
						.getVoidedDocumentsScroll(organization, false, 4, 2);
				Iterator<VoidedDocumentModel> iterator = voidedDocuments.iterator();

				Pattern pattern = Pattern.compile(voidedDocumentCode.getMask());
				while (iterator.hasNext()) {
					VoidedDocumentModel voidedDocument = iterator.next();
					String documentId = voidedDocument.getDocumentId();

					Matcher matcher = pattern.matcher(documentId);
					if (matcher.find()) {
						lastVoidedDocument = voidedDocument;
						break;
					}
				}

				int number = 0;
				if (lastVoidedDocument != null) {
					String[] splits = lastVoidedDocument.getDocumentId().split("-");
					number = Integer.parseInt(splits[2]);
				}

				int nextNumber = SunatUtils.getNextNumber(number, 999);
				int nextSeries = SunatUtils.getDateToNumber();
				StringBuilder documentId = new StringBuilder();
				documentId.append(voidedDocumentCode.getMask().substring(0, 2));
				documentId.append("-");
				documentId.append(nextSeries);
				documentId.append("-");
				documentId.append(StringUtils.padLeft(String.valueOf(nextNumber), 3, "0"));

				return documentId.toString();
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
				try {
					JAXBContext factory = JAXBContext.newInstance(SunatFactory.class);
					Unmarshaller unmarshal = factory.createUnmarshaller();
					@SuppressWarnings("unchecked")
					JAXBElement<VoidedDocumentsType> jaxbVoidedDocumentsType = (JAXBElement<VoidedDocumentsType>) unmarshal
							.unmarshal(document);
					VoidedDocumentsType voidedDocumentsType = jaxbVoidedDocumentsType.getValue();
					return voidedDocumentsType;
				} catch (JAXBException e) {
					throw new ModelException(e);
				}
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
			public Document write(OrganizationModel organization, VoidedDocumentsType voidedDocumentsType,
					Map<String, String> attributes) {
				SunatFactory factory = new SunatFactory();
				JAXBContext context;
				try {
					context = JAXBContext.newInstance(SunatFactory.class);
					Marshaller marshallerElement = context.createMarshaller();
					JAXBElement<VoidedDocumentsType> jaxbElement = factory.createVoidedDocuments(voidedDocumentsType);
					StringWriter xmlWriter = new StringWriter();
					XMLStreamWriter xmlStream = XMLOutputFactory.newInstance().createXMLStreamWriter(xmlWriter);
					xmlStream.setNamespaceContext(SunatUtils.getBasedNamespaceContext(
							"urn:sunat:names:specification:ubl:peru:schema:xsd:VoidedDocuments-1"));
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
			public Document write(OrganizationModel organization, VoidedDocumentsType voidedDocumentsType) {
				return write(organization, voidedDocumentsType, Collections.emptyMap());
			}
		};
	}

	@Override
	public UBLSender<VoidedDocumentModel> sender() {
		return new UBLSender<VoidedDocumentModel>() {

			@Override
			public void close() {
			}

			@Override
			public SendEventModel sendToThridParty(OrganizationModel organization, VoidedDocumentModel voidedDocument)
					throws SendException {
				VoidedDocumentsSendEventModel summaryDocumentsSendEvent = null;
				byte[] zip = null;
				try {
					List<FileModel> files = new ArrayList<>();
					String fileName = SunatTemplateUtils.generateXmlFileName(organization, voidedDocument);
					zip = SunatTemplateUtils.generateZip(voidedDocument.getXmlDocument(), fileName);
					files.add(SunatTemplateUtils.toFileModel(InternetMediaType.ZIP.getMimeType(), fileName, zip));
					// sender
					String response = new SunatSenderUtils(organization).sendSummary(zip, fileName,
							InternetMediaType.ZIP);
					// Write event to the default database
					summaryDocumentsSendEvent = (VoidedDocumentsSendEventModel) session
							.getProvider(SunatSendEventProvider.class)
							.addSendEvent(organization, SendResultType.SUCCESS, voidedDocument);
					// summaryDocumentsSendEvent.setAttachments(files);
					summaryDocumentsSendEvent.setVoidedDocument(voidedDocument);
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
					summaryDocumentsSendEvent = (VoidedDocumentsSendEventModel) session
							.getProvider(SunatSendEventProvider.class)
							.addSendEvent(organization, SendResultType.ERROR, voidedDocument);
					summaryDocumentsSendEvent.setVoidedDocument(voidedDocument);
					summaryDocumentsSendEvent.setDescription(soapFault.getFaultString());
					// Write event to the extends database
					SunatResponseModel sunatResponse = session.getProvider(SunatResponseProvider.class)
							.addSunatResponse(organization, SendResultType.ERROR, summaryDocumentsSendEvent);
					sunatResponse.setErrorMessage(soapFault.getFaultString());
					sunatResponse.setResponseCode(soapFault.getFaultCode());
				}
				return summaryDocumentsSendEvent;
			}

			@Override
			public SendEventModel sendToCustomer(OrganizationModel organization, VoidedDocumentModel perception)
					throws SendException {
				return null;
			}
		};
	}

}
