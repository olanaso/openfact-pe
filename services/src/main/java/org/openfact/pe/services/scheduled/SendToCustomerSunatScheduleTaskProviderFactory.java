package org.openfact.pe.services.scheduled;

import org.openfact.Config.Scope;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OpenfactSessionFactory;
import org.openfact.models.OrganizationScheduleTaskProvider;
import org.openfact.models.OrganizationScheduleTaskProviderFactory;

public class SendToCustomerSunatScheduleTaskProviderFactory implements OrganizationScheduleTaskProviderFactory {

	protected boolean isActive;

	@Override
	public OrganizationScheduleTaskProvider create(OpenfactSession session) {
		return new SendToCustomerSunatScheduleTaskProvider(isActive);
	}

	@Override
	public void init(Scope config) {
		isActive = config.getBoolean("active", true);

	}

	@Override
	public void postInit(OpenfactSessionFactory factory) {

	}

	@Override
	public void close() {

	}

	@Override
	public String getId() {
		return "sendToCustomerSunat";
	}

}
