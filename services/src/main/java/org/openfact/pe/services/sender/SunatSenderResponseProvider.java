package org.openfact.pe.services.sender;

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

public class SunatSenderResponseProvider implements UblSunatSenderResponseProvider {
	private OpenfactSession session;

	public SunatSenderResponseProvider(OpenfactSession session) {
		this.session = session;
	}

	@Override
	public void close() {

	}

	private UblSunatSendEventProvider getSunatSendEventProvider(OrganizationModel organization) {
		return session.getProvider(UblSunatSendEventProvider.class, organization.getDefaultLocale());
	}

	@Override
	public SendEventModel invoiceSenderResponse(OrganizationModel organization, InvoiceModel invoice,
			byte[] xmlSubmitted, byte[] response, String... fault) throws UblSenderException {
		SendEventRepresentation rep = null;
		try {
			if (response != null) {
				rep = DocumentResponseUtil.byteToResponse(response);
			} else {
				rep = DocumentResponseUtil.faultToResponse(fault);
			}
			// if (rep.isAccepted()) {
			// invoice.removeRequiredAction(RequiredActionDocument.SEND_SOA_DOCUMENT);
			// }
			SendEventModel sendEvent = getSunatSendEventProvider(organization).addInvoiceSendEvent(organization,
					invoice, xmlSubmitted, response, rep.isAccepted());
			RepresentationToModel.toModel(rep, sendEvent);
			return sendEvent;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public SendEventModel creditNoteSenderResponse(OrganizationModel organization, CreditNoteModel creditNote,
			byte[] xmlSubmitted, byte[] response, String... fault) throws UblSenderException {
		SendEventRepresentation rep = null;
		try {
			if (response != null) {
				rep = DocumentResponseUtil.byteToResponse(response);
			} else {
				rep = DocumentResponseUtil.faultToResponse(fault);
			}
			// if (rep.isAccepted()) {
			// creditNote.removeRequiredAction(RequiredActionDocument.SEND_SOA_DOCUMENT);
			// }
			SendEventModel sendEvent = getSunatSendEventProvider(organization).addCreditNoteSendEvent(organization,
					creditNote, xmlSubmitted, response, rep.isAccepted());
			RepresentationToModel.toModel(rep, sendEvent);
			return sendEvent;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public SendEventModel debitNoteSenderResponse(OrganizationModel organization, DebitNoteModel debitNote,
			byte[] xmlSubmitted, byte[] response, String... fault) throws UblSenderException {
		SendEventRepresentation rep = null;
		try {
			if (response != null) {
				rep = DocumentResponseUtil.byteToResponse(response);
			} else {
				rep = DocumentResponseUtil.faultToResponse(fault);
			}
			// if (rep.isAccepted()) {
			// debitNote.removeRequiredAction(RequiredActionDocument.SEND_SOA_DOCUMENT);
			// }
			SendEventModel sendEvent = getSunatSendEventProvider(organization).addDebitNoteSendEvent(organization,
					debitNote, xmlSubmitted, response, rep.isAccepted());
			RepresentationToModel.toModel(rep, sendEvent);
			return sendEvent;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public SendEventModel perceptionSenderResponse(OrganizationModel organization, PerceptionModel perception,
			byte[] xmlSubmitted, byte[] response, String... fault) throws UblSenderException {
		SendEventRepresentation rep = null;
		try {
			if (response != null) {
				rep = DocumentResponseUtil.byteToResponse(response);
			} else {
				rep = DocumentResponseUtil.faultToResponse(fault);
			}
			// if (rep.isAccepted()) {
			// perception.removeRequiredAction(RequiredActionDocument.SEND_SOA_DOCUMENT);
			// }
			SendEventModel sendEvent = getSunatSendEventProvider(organization).addPerceptionSendEvent(organization,
					perception, xmlSubmitted, response, rep.isAccepted());
			RepresentationToModel.toModel(rep, sendEvent);
			return sendEvent;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public SendEventModel retentionSenderResponse(OrganizationModel organization, RetentionModel retention,
			byte[] xmlSubmitted, byte[] response, String... fault) throws UblSenderException {
		SendEventRepresentation rep = null;
		try {
			if (response != null) {
				rep = DocumentResponseUtil.byteToResponse(response);
			} else {
				rep = DocumentResponseUtil.faultToResponse(fault);
			}
			// if (rep.isAccepted()) {
			// retention.removeRequiredAction(RequiredActionDocument.SEND_SOA_DOCUMENT);
			// }
			SendEventModel sendEvent = getSunatSendEventProvider(organization).addRetentionSendEvent(organization,
					retention, xmlSubmitted, response, rep.isAccepted());
			RepresentationToModel.toModel(rep, sendEvent);
			return sendEvent;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public SendEventModel voidedDocumentSenderResponse(OrganizationModel organization,
			VoidedDocumentModel voidedDocument, byte[] xmlSubmitted, String response, String... fault)
			throws UblSenderException {
		SendEventRepresentation rep = null;
		try {
			if (response != null) {
				rep = new SendEventRepresentation();
				rep.setResponseCode(response);
			} else {
				rep = DocumentResponseUtil.faultToResponse(fault);
			}
			// if (rep.isAccepted()) {
			// voidedDocument.removeRequiredAction(RequiredActionDocument.SEND_SOA_DOCUMENT);
			// }
			SendEventModel sendEvent = getSunatSendEventProvider(organization).addVoidedDocumentSendEvent(organization,
					voidedDocument, xmlSubmitted, null, rep.isAccepted());
			RepresentationToModel.toModel(rep, sendEvent);
			return sendEvent;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public SendEventModel summaryDocumentSenderResponse(OrganizationModel organization,
			SummaryDocumentModel summaryDocument, byte[] xmlSubmitted, String response, String... fault)
			throws UblSenderException {
		SendEventRepresentation rep = null;
		try {
			if (response != null) {
				rep = new SendEventRepresentation();
				rep.setResponseCode(response);
			} else {
				rep = DocumentResponseUtil.faultToResponse(fault);
			}
			// if (rep.isAccepted()) {
			// summaryDocument.removeRequiredAction(RequiredActionDocument.SEND_SOA_DOCUMENT);
			// }
			SendEventModel sendEvent = getSunatSendEventProvider(organization).addSummaryDocumentSendEvent(organization,
					summaryDocument, xmlSubmitted, null, rep.isAccepted());
			RepresentationToModel.toModel(rep, sendEvent);
			return sendEvent;
		} catch (Exception e) {
			return null;
		}
	}

}
