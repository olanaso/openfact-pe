package org.openfact.pe.services.scheduled;

import org.openfact.Config.Scope;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OpenfactSessionFactory;
import org.openfact.models.OrganizationScheduleTaskProvider;
import org.openfact.models.OrganizationScheduleTaskProviderFactory;

public class SendToThridPartySunatScheduleTaskProviderFactory implements OrganizationScheduleTaskProviderFactory {

    protected boolean isActive;
    protected int retries;

    @Override
    public OrganizationScheduleTaskProvider create(OpenfactSession session) {
        return new SendToThridPartySunatScheduleTaskProvider(isActive, retries);
    }

    @Override
    public void init(Scope config) {
        isActive = config.getBoolean("active", true);
        retries = config.getInt("retries", 30);
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
