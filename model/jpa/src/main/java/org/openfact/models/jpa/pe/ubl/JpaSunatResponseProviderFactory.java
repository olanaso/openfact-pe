package org.openfact.models.jpa.pe.ubl;

import org.openfact.Config.Scope;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OpenfactSessionFactory;
import org.openfact.pe.models.SunatResponseProvider;
import org.openfact.pe.models.SunatResponseProviderFactory;

public class JpaSunatResponseProviderFactory implements SunatResponseProviderFactory {

	@Override
	public SunatResponseProvider create(OpenfactSession session) {
		return new JpaSunatResponseProvider(session);
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
