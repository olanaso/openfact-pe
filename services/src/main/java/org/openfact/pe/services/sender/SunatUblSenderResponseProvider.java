package org.openfact.pe.services.sender;

import java.util.Map;

import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.ubl.CreditNoteModel;
import org.openfact.models.ubl.DebitNoteModel;
import org.openfact.models.ubl.InvoiceModel;
import org.openfact.models.ubl.SendEventModel;
import org.openfact.models.utils.RepresentationToModel;
import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.RetentionModel;
import org.openfact.pe.models.SummaryDocumentModel;
import org.openfact.pe.models.VoidedDocumentModel;
import org.openfact.pe.provider.UblSunatSendEventProvider;
import org.openfact.pe.provider.UblSunatSenderResponseProvider;
import org.openfact.pe.services.util.DocumentResponseUtil;
import org.openfact.representations.idm.ubl.SendEventRepresentation;
import org.openfact.ubl.UblSenderException;

public class SunatUblSenderResponseProvider implements UblSunatSenderResponseProvider {
	private OpenfactSession session;

	public SunatUblSenderResponseProvider(OpenfactSession session) {
		this.session = session;
	}

	@Override
	public void close() {

	}

	private UblSunatSendEventProvider getSendEventProvider(OrganizationModel organization) {
		return session.getProvider(UblSunatSendEventProvider.class, organization.getDefaultLocale());
	}

	@Override
	public SendEventModel invoiceSenderResponse(OrganizationModel organization, InvoiceModel invoice, byte[] submitted,
			Map<String, Object> response, String... fault) throws UblSenderException {

		try {
			SendEventRepresentation rep = new SendEventRepresentation();
			byte[] document = null;
			if (response != null) {
				if (response.containsKey("success")) {
					document = (byte[]) response.get("success");
					rep = DocumentResponseUtil.byteToResponse(document);
					rep.setDocumentResponse(document);
				}
			} else if (fault.length > 0) {
				rep = DocumentResponseUtil.faultToResponse(fault);
			}
			rep.setDocumentSubmitted(submitted);
			SendEventModel sendEvent = getSendEventProvider(organization).addInvoiceSendEvent(organization, invoice,
					rep.isAccepted());
			RepresentationToModel.toModel(rep, sendEvent);
			return sendEvent;
		} catch (Exception e) {
			throw new UblSenderException(e);
		}
	}

	@Override
	public SendEventModel creditNoteSenderResponse(OrganizationModel organization, CreditNoteModel creditNote,
			byte[] submitted, Map<String, Object> response, String... fault) throws UblSenderException {
		SendEventRepresentation rep = null;
		byte[] document = null;
		try {
			if (response != null) {
				if (response.containsKey("success")) {
					document = (byte[]) response.get("success");
					rep = DocumentResponseUtil.byteToResponse(document);
					rep.setDocumentResponse(document);
				}
			} else if (fault.length > 0) {
				rep = DocumentResponseUtil.faultToResponse(fault);
			}
			rep.setDocumentSubmitted(submitted);
			SendEventModel sendEvent = getSendEventProvider(organization).addCreditNoteSendEvent(organization,
					creditNote, rep.isAccepted());
			RepresentationToModel.toModel(rep, sendEvent);
			return sendEvent;
		} catch (Exception e) {
			throw new UblSenderException(e);
		}
	}

	@Override
	public SendEventModel debitNoteSenderResponse(OrganizationModel organization, DebitNoteModel debitNote,
			byte[] submitted, Map<String, Object> response, String... fault) throws UblSenderException {
		SendEventRepresentation rep = null;
		byte[] document = null;
		try {
			if (response != null) {
				if (response.containsKey("success")) {
					document = (byte[]) response.get("success");
					rep = DocumentResponseUtil.byteToResponse(document);
					rep.setDocumentResponse(document);
				}
			} else if (fault.length > 0) {
				rep = DocumentResponseUtil.faultToResponse(fault);
			}
			rep.setDocumentSubmitted(submitted);
			SendEventModel sendEvent = getSendEventProvider(organization).addDebitNoteSendEvent(organization, debitNote,
					rep.isAccepted());
			RepresentationToModel.toModel(rep, sendEvent);
			return sendEvent;
		} catch (Exception e) {
			throw new UblSenderException(e);
		}
	}

	@Override
	public SendEventModel perceptionSenderResponse(OrganizationModel organization, PerceptionModel perception,
			byte[] submitted, byte[] response, String... fault) throws UblSenderException {
		SendEventRepresentation rep = null;
		try {
			if (response != null) {
				rep = DocumentResponseUtil.byteToResponse(response);
				rep.setDocumentResponse(response);
			} else if (fault.length > 0) {
				rep = DocumentResponseUtil.faultToResponse(fault);
			}
			rep.setDocumentSubmitted(submitted);
			SendEventModel sendEvent = getSendEventProvider(organization).addPerceptionSendEvent(organization,
					perception, rep.isAccepted());
			RepresentationToModel.toModel(rep, sendEvent);
			return sendEvent;
		} catch (Exception e) {
			throw new UblSenderException(e);
		}
	}

	@Override
	public SendEventModel retentionSenderResponse(OrganizationModel organization, RetentionModel retention,
			byte[] submitted, byte[] response, String... fault) throws UblSenderException {
		SendEventRepresentation rep = null;
		try {
			if (response != null) {
				rep = DocumentResponseUtil.byteToResponse(response);
				rep.setDocumentResponse(response);
			} else if (fault.length > 0) {
				rep = DocumentResponseUtil.faultToResponse(fault);
			}
			rep.setDocumentSubmitted(submitted);
			SendEventModel sendEvent = getSendEventProvider(organization).addRetentionSendEvent(organization, retention,
					rep.isAccepted());
			RepresentationToModel.toModel(rep, sendEvent);
			return sendEvent;
		} catch (Exception e) {
			throw new UblSenderException(e);
		}
	}

	@Override
	public SendEventModel voidedDocumentSenderResponse(OrganizationModel organization,
			VoidedDocumentModel voidedDocument, byte[] submitted, String response, String... fault)
			throws UblSenderException {
		SendEventRepresentation rep = null;
		try {
			if (response != null) {
				rep = new SendEventRepresentation();
				rep.setResponseCode(response);
			} else if (fault.length > 0) {
				rep = DocumentResponseUtil.faultToResponse(fault);
			}
			rep.setDocumentSubmitted(submitted);
			SendEventModel sendEvent = getSendEventProvider(organization).addVoidedDocumentSendEvent(organization,
					voidedDocument, rep.isAccepted());
			RepresentationToModel.toModel(rep, sendEvent);
			return sendEvent;
		} catch (Exception e) {
			throw new UblSenderException(e);
		}
	}

	@Override
	public SendEventModel summaryDocumentSenderResponse(OrganizationModel organization,
			SummaryDocumentModel summaryDocument, byte[] submitted, String response, String... fault)
			throws UblSenderException {
		SendEventRepresentation rep = null;
		try {
			if (response != null) {
				rep = new SendEventRepresentation();
				rep.setResponseCode(response);
			} else if (fault.length > 0) {
				rep = DocumentResponseUtil.faultToResponse(fault);
			}
			rep.setDocumentSubmitted(submitted);
			SendEventModel sendEvent = getSendEventProvider(organization).addSummaryDocumentSendEvent(organization,
					summaryDocument, rep.isAccepted());
			RepresentationToModel.toModel(rep, sendEvent);
			return sendEvent;
		} catch (Exception e) {
			throw new UblSenderException(e);
		}
	}

}
