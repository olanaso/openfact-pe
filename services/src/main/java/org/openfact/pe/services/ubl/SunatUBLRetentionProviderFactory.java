package org.openfact.pe.services.ubl;

import org.openfact.Config.Scope;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OpenfactSessionFactory;
import org.openfact.pe.models.UBLRetentionProvider;
import org.openfact.pe.models.UBLRetentionProviderFactory;

public class SunatUBLRetentionProviderFactory implements UBLRetentionProviderFactory {

	@Override
	public UBLRetentionProvider create(OpenfactSession session) {
		return new SunatUBLRetentionProvider(session);
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
		return "sunat";
	}

}
