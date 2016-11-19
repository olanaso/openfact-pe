package org.openfact.pe.models;

import java.util.List;

import org.openfact.models.OrganizationModel;
import org.openfact.provider.Provider;

public interface PerceptionService extends Provider {

    List<PerceptionModel> getPerceptions(OrganizationModel organization, Integer firstResult, Integer maxResults);

    List<PerceptionModel> searchForPerception(String filterText, Integer firstResult, Integer maxResults,
            OrganizationModel organization);

    /*
     * List<CompanyRepresentation> listCompanies();
     * 
     * CompanyRepresentation findCompany(String id);
     * 
     * CompanyRepresentation addCompany(CompanyRepresentation company);
     */

}