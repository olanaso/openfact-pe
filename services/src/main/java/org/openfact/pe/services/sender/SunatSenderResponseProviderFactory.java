package org.openfact.pe.services.sender;

import org.openfact.Config.Scope;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OpenfactSessionFactory;
import org.openfact.pe.provider.UblSunatSenderResponseProvider;
import org.openfact.pe.provider.UblSunatSenderResponseProviderFactory;

public class SunatSenderResponseProviderFactory implements UblSunatSenderResponseProviderFactory {

	@Override
	public UblSunatSenderResponseProvider create(OpenfactSession session) {
		return new SunatSenderResponseProvider(session);
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
