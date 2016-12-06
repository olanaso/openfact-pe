package org.openfact.pe.services.ubl;

import org.openfact.Config.Scope;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OpenfactSessionFactory;
import org.openfact.pe.models.UBLPerceptionProvider;
import org.openfact.pe.models.UBLPerceptionProviderFactory;

public class SunatUBLPerceptionProviderFactory implements UBLPerceptionProviderFactory {

	@Override
	public UBLPerceptionProvider create(OpenfactSession session) {
		return new SunatUBLPerceptionProvider(session);
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
