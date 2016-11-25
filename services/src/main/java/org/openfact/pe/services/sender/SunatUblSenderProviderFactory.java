package org.openfact.pe.services.sender;

import org.openfact.Config.Scope;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OpenfactSessionFactory;
import org.openfact.pe.provider.UblSunatSenderProvider;
import org.openfact.pe.provider.UblSunatSenderProviderFactory;
import org.openfact.ubl.UblSenderProvider;
import org.openfact.ubl.UblSenderProviderFactory;

public class SunatUblSenderProviderFactory implements UblSunatSenderProviderFactory {
	@Override
	public UblSunatSenderProvider create(OpenfactSession session) {
		return new SunatUblSenderProvider(session);
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
