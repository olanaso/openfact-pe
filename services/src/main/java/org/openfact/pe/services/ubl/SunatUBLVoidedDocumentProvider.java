package org.openfact.pe.services.ubl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.soap.SOAPFault;
import javax.xml.transform.TransformerException;
import javax.xml.ws.soap.SOAPFaultException;

import org.openfact.common.converts.StringUtils;
import org.openfact.models.FileModel;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.ScrollModel;
import org.openfact.models.enums.InternetMediaType;
import org.openfact.models.enums.SendResultType;
import org.openfact.pe.constants.CodigoTipoDocumento;
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

				int series = 0;
				int number = 0;
				if (lastVoidedDocument != null) {
					String[] splits = lastVoidedDocument.getDocumentId().split("-");
					series = Integer.parseInt(splits[0].substring(1));
					number = Integer.parseInt(splits[1]);
				}

				int nextNumber = SunatUtils.getNextNumber(number, 99_999_999);
				int nextSeries = SunatUtils.getNextSerie(series, number, 999, 99_999_999);
				StringBuilder documentId = new StringBuilder();
				documentId.append(voidedDocumentCode.getMask().substring(0, 1));
				documentId.append(StringUtils.padLeft(String.valueOf(nextSeries), 3, "0"));
				documentId.append("-");
				documentId.append(StringUtils.padLeft(String.valueOf(nextNumber), 8, "0"));

				return documentId.toString();
			}
		};
	}

	@Override
	public UBLReader<VoidedDocumentsType> reader() {
		return null;
	}

	@Override
	public UBLWriter<VoidedDocumentsType> writer() {
		return null;
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
							.addSendEvent(organization, SendResultType.SUCCESS, voidedDocument);
					sunatResponse.setTicket(response);
					sunatResponse.setVoidedDocumentsSendEvent(summaryDocumentsSendEvent);
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
							.addSendEvent(organization, SendResultType.ERROR, voidedDocument);
					sunatResponse.setVoidedDocumentsSendEvent(summaryDocumentsSendEvent);
					sunatResponse.setErrorMessage(soapFault.getFaultString());
					sunatResponse.setResponseCode(soapFault.getFaultCode());
				}
				return summaryDocumentsSendEvent;
			}

			@Override
			public SendEventModel sendToCustomer(OrganizationModel organization, VoidedDocumentModel t)
					throws SendException {
				return null;
			}
		};
	}

}
