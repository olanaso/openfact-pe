package org.openfact.pe.services.sender.response;

import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.ubl.SendEventModel;
import org.openfact.ubl.UblSenderException;
import org.openfact.ubl.UblSenderResponseProvider;

public class RetentionSunatResponseProvider implements UblSenderResponseProvider{
	private OpenfactSession session;

	public RetentionSunatResponseProvider(OpenfactSession session) {
		this.session = session;
	}

	@Override
	public void close() {

	}

	@Override
	public SendEventModel senderResponse(OrganizationModel organization,Object model, byte[] xmlSubmitted, byte[] response, String... fault)
			throws UblSenderException {
		return null;
	}
}
