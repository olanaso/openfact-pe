package org.openfact.pe.services.ubl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.SOAPFault;
import javax.xml.transform.TransformerException;
import javax.xml.ws.soap.SOAPFaultException;

import org.openfact.common.converts.DocumentUtils;
import org.openfact.common.converts.StringUtils;
import org.openfact.models.FileModel;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.ScrollModel;
import org.openfact.ubl.SendEventModel;
import org.openfact.models.enums.InternetMediaType;
import org.openfact.models.enums.SendResultType;
import org.openfact.pe.constants.CodigoTipoDocumento;
import org.openfact.pe.model.types.RetentionType;
import org.openfact.pe.model.types.SunatFactory;
import org.openfact.pe.models.RetentionSendEventModel;
import org.openfact.pe.models.RetentionModel;
import org.openfact.pe.models.RetentionProvider;
import org.openfact.pe.models.RetentionModel;
import org.openfact.pe.models.SunatResponseModel;
import org.openfact.pe.models.SunatResponseProvider;
import org.openfact.pe.models.SunatSendEventProvider;
import org.openfact.pe.models.UBLRetentionProvider;
import org.openfact.pe.services.util.SunatSenderUtils;
import org.openfact.pe.services.util.SunatTemplateUtils;
import org.openfact.pe.services.util.SunatUtils;
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
					JAXBElement<RetentionType> jaxbRetentionType = (JAXBElement<RetentionType>) unmarshal
							.unmarshal(document);
					RetentionType retentionType = jaxbRetentionType.getValue();
					return retentionType;
				} catch (JAXBException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public RetentionType read(byte[] bytes) {
				try {
					Document document = DocumentUtils.byteToDocument(bytes);
					JAXBContext factory = JAXBContext.newInstance(SunatFactory.class);
					Unmarshaller unmarshal = factory.createUnmarshaller();
					JAXBElement<RetentionType> jaxbRetentionType = (JAXBElement<RetentionType>) unmarshal
							.unmarshal(document);
					RetentionType retentionType = jaxbRetentionType.getValue();
					return retentionType;
				} catch (JAXBException e) {
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
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
				return null;
			}

			@Override
			public Document write(OrganizationModel organization, RetentionType retentionType) {
				return null;
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
			public SendEventModel sendToCustomer(OrganizationModel organization, RetentionModel t)
					throws SendException {
				return null;
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
							.addSendEvent(organization, SendResultType.SUCCESS, retention);
					sunatResponse.setDocumentResponse(response);
					sunatResponse.setRetentionSendEvent(retentionSendEvent);
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
							.addSendEvent(organization, SendResultType.ERROR, retention);
					sunatResponse.setRetentionSendEvent(retentionSendEvent);
					sunatResponse.setErrorMessage(soapFault.getFaultString());
					sunatResponse.setResponseCode(soapFault.getFaultCode());
				}
				return retentionSendEvent;
			}
		};
	}

}
