package org.openfact.pe.services.sender;

import org.openfact.Config.Scope;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OpenfactSessionFactory;
import org.openfact.ubl.UblTemplateProvider;
import org.openfact.ubl.UblTemplateProviderFactory;

public class SunatTemplateProviderFactory  implements UblTemplateProviderFactory{

	@Override
	public UblTemplateProvider create(OpenfactSession session) {
		return new SunatTemplateProvider(session);
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
