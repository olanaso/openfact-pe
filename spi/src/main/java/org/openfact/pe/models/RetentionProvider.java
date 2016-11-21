package org.openfact.pe.models;

import java.util.List;

import org.openfact.models.OrganizationModel;
import org.openfact.models.search.SearchCriteriaModel;
import org.openfact.models.search.SearchResultsModel;
import org.openfact.provider.Provider;

public interface RetentionProvider extends Provider {

    List<RetentionModel> getRetentions(OrganizationModel organization, Integer firstResult,
            Integer maxResults);

    SearchResultsModel<RetentionModel> searchForRetention(SearchCriteriaModel criteria,
            OrganizationModel organization);

    List<RetentionModel> searchForRetention(String filterText, OrganizationModel organization,
            Integer firstResult, Integer maxResults);

    SearchResultsModel<RetentionModel> searchForRetention(String filterText, SearchCriteriaModel criteria,
            OrganizationModel organization);

}