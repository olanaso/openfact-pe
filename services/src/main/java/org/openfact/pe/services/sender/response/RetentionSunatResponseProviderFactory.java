package org.openfact.pe.services.sender.response;

import org.openfact.Config.Scope;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OpenfactSessionFactory;
import org.openfact.ubl.UblSenderResponseProvider;
import org.openfact.ubl.UblSenderResponseProviderFactory;

public class RetentionSunatResponseProviderFactory  implements UblSenderResponseProviderFactory {
	@Override
	public UblSenderResponseProvider create(OpenfactSession session) {
		return new RetentionSunatResponseProvider(session);
	}

	@Override
	public void init(Scope config) {
	}

	@Override
	public void postInit(OpenfactSessionFactory factory) {
	}

	@Override
	public void close() {
	}

	@Override
	public String getId() {
		return "pe";
	}
}
