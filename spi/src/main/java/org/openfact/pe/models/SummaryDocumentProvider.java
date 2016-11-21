package org.openfact.pe.models;

import java.util.List;

import org.openfact.models.OrganizationModel;
import org.openfact.models.search.SearchCriteriaModel;
import org.openfact.models.search.SearchResultsModel;
import org.openfact.provider.Provider;

public interface SummaryDocumentProvider extends Provider {

    List<SummaryDocumentModel> getSummaryDocuments(OrganizationModel organization, Integer firstResult,
            Integer maxResults);

    SearchResultsModel<SummaryDocumentModel> searchForSummaryDocument(SearchCriteriaModel criteria,
            OrganizationModel organization);

    List<SummaryDocumentModel> searchForSummaryDocument(String filterText, OrganizationModel organization,
            Integer firstResult, Integer maxResults);

    SearchResultsModel<SummaryDocumentModel> searchForSummaryDocument(String filterText,
            SearchCriteriaModel criteria, OrganizationModel organization);

}