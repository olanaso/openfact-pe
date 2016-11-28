package org.openfact.pe.services.ubl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.soap.SOAPFault;
import javax.xml.transform.TransformerException;
import javax.xml.ws.soap.SOAPFaultException;

import org.openfact.common.converts.StringUtils;
import org.openfact.email.EmailException;
import org.openfact.email.EmailTemplateProvider;
import org.openfact.models.CustomerPartyModel;
import org.openfact.models.FileModel;
import org.openfact.models.InvoiceModel;
import org.openfact.models.ModelException;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.PartyLegalEntityModel;
import org.openfact.models.ScrollModel;
import org.openfact.ubl.SendEventModel;
import org.openfact.models.UserSenderModel;
import org.openfact.models.enums.InternetMediaType;
import org.openfact.models.enums.RequiredAction;
import org.openfact.models.enums.SendResultType;
import org.openfact.pe.constants.CodigoTipoDocumento;
import org.openfact.pe.model.types.PerceptionType;
import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.PerceptionProvider;
import org.openfact.pe.models.PerceptionSendEventModel;
import org.openfact.pe.models.SunatResponseModel;
import org.openfact.pe.models.SunatResponseProvider;
import org.openfact.pe.models.SunatSendEventProvider;
import org.openfact.pe.models.UBLPerceptionProvider;
import org.openfact.pe.services.util.SunatSenderUtils;
import org.openfact.pe.services.util.SunatTemplateUtils;
import org.openfact.pe.services.util.SunatUtils;
import org.openfact.ubl.SendEventProvider;
import org.openfact.ubl.SendException;
import org.openfact.ubl.UBLIDGenerator;
import org.openfact.ubl.UBLReader;
import org.openfact.ubl.UBLSender;
import org.openfact.ubl.UBLWriter;

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
				CodigoTipoDocumento perceptionCode = CodigoTipoDocumento.PERCEPCION;
				PerceptionModel lastPerception = null;
				ScrollModel<PerceptionModel> perceptions = session.getProvider(PerceptionProvider.class)
						.getPerceptionsScroll(organization, false, 4, 2);
				Iterator<PerceptionModel> iterator = perceptions.iterator();

				Pattern pattern = Pattern.compile(perceptionCode.getMask());
				while (iterator.hasNext()) {
					PerceptionModel perception = iterator.next();
					String documentId = perception.getDocumentId();

					Matcher matcher = pattern.matcher(documentId);
					if (matcher.find()) {
						lastPerception = perception;
						break;
					}
				}

				int series = 0;
				int number = 0;
				if (lastPerception != null) {
					String[] splits = lastPerception.getDocumentId().split("-");
					series = Integer.parseInt(splits[0].substring(1));
					number = Integer.parseInt(splits[1]);
				}

				int nextNumber = SunatUtils.getNextNumber(number, 99_999_999);
				int nextSeries = SunatUtils.getNextSerie(series, number, 999, 99_999_999);
				StringBuilder documentId = new StringBuilder();
				documentId.append(perceptionCode.getMask().substring(0, 1));
				documentId.append(StringUtils.padLeft(String.valueOf(nextSeries), 3, "0"));
				documentId.append("-");
				documentId.append(StringUtils.padLeft(String.valueOf(nextNumber), 8, "0"));

				return documentId.toString();
			}
		};
	}

	@Override
	public UBLReader<PerceptionType> reader() {
		return null;
	}

	@Override
	public UBLWriter<PerceptionType> writer() {
		return null;
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
				return null;
			}

			@Override
			public SendEventModel sendToThridParty(OrganizationModel organization, PerceptionModel perception)
					throws SendException {
				PerceptionSendEventModel perceptionSendEvent = null;
				byte[] zip = null;
				try {
					List<FileModel> files = new ArrayList<>();
					String fileName = SunatTemplateUtils.generateXmlFileName(organization, perception);
					zip = SunatTemplateUtils.generateZip(perception.getXmlDocument(), fileName);
					files.add(SunatTemplateUtils.toFileModel(InternetMediaType.ZIP.getMimeType(), fileName, zip));
					// sender
					byte[] response = new SunatSenderUtils(organization).sendBill(zip, fileName, InternetMediaType.ZIP);
					// Write event to the default database
					perceptionSendEvent = (PerceptionSendEventModel) session.getProvider(SunatSendEventProvider.class)
							.addSendEvent(organization, SendResultType.SUCCESS, perception);
					// perceptionSendEvent.setAttachments(files);
					perceptionSendEvent.setPerception(perception);
					// Write event to the extends database
					SunatResponseModel sunatResponse = session.getProvider(SunatResponseProvider.class)
							.addSendEvent(organization, SendResultType.SUCCESS, perception);
					sunatResponse.setDocumentResponse(response);
					sunatResponse.setPerceptionSendEvent(perceptionSendEvent);
				} catch (TransformerException e) {
					throw new SendException(e);
				} catch (IOException e) {
					throw new SendException(e);
				} catch (SOAPFaultException e) {
					SOAPFault soapFault = e.getFault();
					// Write event to the default database
					perceptionSendEvent = (PerceptionSendEventModel) session.getProvider(SunatSendEventProvider.class)
							.addSendEvent(organization, SendResultType.ERROR, perception);
					perceptionSendEvent.setPerception(perception);
					perceptionSendEvent.setDescription(soapFault.getFaultString());
					// Write event to the extends database
					SunatResponseModel sunatResponse = session.getProvider(SunatResponseProvider.class)
							.addSendEvent(organization, SendResultType.ERROR, perception);
					sunatResponse.setPerceptionSendEvent(perceptionSendEvent);
					sunatResponse.setErrorMessage(soapFault.getFaultString());
					sunatResponse.setResponseCode(soapFault.getFaultCode());
				}
				return perceptionSendEvent;
			}
		};
	}

}
