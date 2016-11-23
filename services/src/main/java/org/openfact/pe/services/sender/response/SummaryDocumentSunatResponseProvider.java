package org.openfact.pe.services.sender.response;

import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.enums.RequiredActionDocument;
import org.openfact.models.ubl.SendEventModel;
import org.openfact.models.utils.RepresentationToModel;
import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.SummaryDocumentModel;
import org.openfact.pe.services.util.DocumentResponseUtil;
import org.openfact.representations.idm.ubl.SendEventRepresentation;
import org.openfact.ubl.UblSenderException;
import org.openfact.ubl.UblSenderResponseProvider;

public class SummaryDocumentSunatResponseProvider implements UblSenderResponseProvider {
	private OpenfactSession session;

	public SummaryDocumentSunatResponseProvider(OpenfactSession session) {
		this.session = session;
	}

	@Override
	public void close() {

	}

	@Override
	public SendEventModel senderResponse(OrganizationModel organization, Object model, byte[] xmlSubmitted,
			byte[] response, String... fault) throws UblSenderException {
		SendEventRepresentation rep = null;
		SummaryDocumentModel summaryDocument = null;
		try {
			if (response != null) {
				rep = DocumentResponseUtil.byteToResponse(response);
			} else {
				rep = DocumentResponseUtil.faultToResponse(fault);
			}
			if (rep.isAccepted()) {
				summaryDocument = (SummaryDocumentModel) model;
				summaryDocument.removeRequiredAction(RequiredActionDocument.SEND_SOA_DOCUMENT);
			}
			SendEventModel sendEvent = session.sendEvents("summary").addEvent(organization, summaryDocument,
					xmlSubmitted, response, rep.isAccepted());
			RepresentationToModel.toModel(rep, sendEvent);
			return sendEvent;
		} catch (Exception e) {
			return null;
		}
	}
}
