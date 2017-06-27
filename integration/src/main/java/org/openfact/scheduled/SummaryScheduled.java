package org.openfact.scheduled;

import org.openfact.models.OrganizationModel;
import org.openfact.models.OrganizationScheduledTask;

import javax.ejb.Stateless;

@Stateless
public class SummaryScheduled implements OrganizationScheduledTask {

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "Sunat Scheduled tasks";
    }

    @Override
    public String getDescription() {
        return "Use to send summary documents";
    }

    @Override
    public void executeTask(OrganizationModel organizationModel) {
        System.out.println("-------------------------------------------");
        System.out.println("Enviando");
    }

}
