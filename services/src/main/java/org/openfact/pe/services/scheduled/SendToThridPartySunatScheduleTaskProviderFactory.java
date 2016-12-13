package org.openfact.pe.services.scheduled;

import org.openfact.Config.Scope;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OpenfactSessionFactory;
import org.openfact.models.OrganizationScheduleTaskProvider;
import org.openfact.models.OrganizationScheduleTaskProviderFactory;

public class SendToThridPartySunatScheduleTaskProviderFactory implements OrganizationScheduleTaskProviderFactory {

	  protected boolean isActive;
	@Override
	public OrganizationScheduleTaskProvider create(OpenfactSession session) {
		return new SendToThridPartySunatScheduleTaskProvider(isActive);
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
		return "sendToThridPartySunat";
	}

}
