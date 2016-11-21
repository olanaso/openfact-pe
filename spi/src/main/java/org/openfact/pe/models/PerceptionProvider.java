package org.openfact.pe.models;

import java.util.List;

import org.openfact.models.OrganizationModel;
import org.openfact.models.search.SearchCriteriaModel;
import org.openfact.models.search.SearchResultsModel;
import org.openfact.provider.Provider;

public interface PerceptionProvider extends Provider {

    PerceptionModel getInvoiceByDocumentId(String documentId, OrganizationModel organization);

    PerceptionModel addPerception(OrganizationModel organization, String documentId);

    boolean removePerception(PerceptionModel perception, OrganizationModel organization);

    List<PerceptionModel> getPerceptions(OrganizationModel organization, Integer firstResult,
            Integer maxResults);

    SearchResultsModel<PerceptionModel> searchForPerception(SearchCriteriaModel criteria,
            OrganizationModel organization);

    List<PerceptionModel> searchForPerception(String filterText, OrganizationModel organization,
            Integer firstResult, Integer maxResults);

    SearchResultsModel<PerceptionModel> searchForPerception(String filterText, SearchCriteriaModel criteria,
            OrganizationModel organization);

}