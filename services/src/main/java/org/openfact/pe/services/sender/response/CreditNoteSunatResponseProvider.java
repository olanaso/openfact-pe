package org.openfact.pe.services.sender.response;

import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.enums.RequiredActionDocument;
import org.openfact.models.ubl.CreditNoteModel;
import org.openfact.models.ubl.SendEventModel;
import org.openfact.models.utils.RepresentationToModel;
import org.openfact.pe.services.util.DocumentResponseUtil;
import org.openfact.representations.idm.ubl.SendEventRepresentation;
import org.openfact.ubl.UblSenderException;
import org.openfact.ubl.UblSenderResponseProvider;

public class CreditNoteSunatResponseProvider implements UblSenderResponseProvider {
	private OpenfactSession session;

	public CreditNoteSunatResponseProvider(OpenfactSession session) {
		this.session = session;
	}

	@Override
	public void close() {

	}

	@Override
	public SendEventModel senderResponse(OrganizationModel organization, Object model, byte[] xmlSubmitted,
			byte[] response, String... fault) throws UblSenderException {
		SendEventRepresentation rep = null;
		CreditNoteModel creditNote = null;
		try {
			if (response != null) {
				rep = DocumentResponseUtil.byteToResponse(response);
			} else {
				rep = DocumentResponseUtil.faultToResponse(fault);
			}
			if (rep.isAccepted()) {
				creditNote = (CreditNoteModel) model;
				creditNote.removeRequiredAction(RequiredActionDocument.SEND_SOA_DOCUMENT);
			}
			SendEventModel sendEvent = session.sendEvents("creditnote").addEvent(organization, creditNote, xmlSubmitted,
					response, rep.isAccepted());
			RepresentationToModel.toModel(rep, sendEvent);
			return sendEvent;
		} catch (Exception e) {
			return null;
		}

	}
}
