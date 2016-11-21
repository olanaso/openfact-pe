package org.openfact.pe.models;

import java.util.List;

import org.openfact.models.OrganizationModel;
import org.openfact.models.search.SearchCriteriaModel;
import org.openfact.models.search.SearchResultsModel;
import org.openfact.provider.Provider;

public interface VoidedDocumentProvider extends Provider {

    VoidedDocumentModel addVoidedDocument(OrganizationModel organization, String value);

    VoidedDocumentModel getVoidedDocumentByDocumentId(String documentId, OrganizationModel organization);

    List<VoidedDocumentModel> getVoidedDocuments(OrganizationModel organization, Integer firstResult,
            Integer maxResults);

    SearchResultsModel<VoidedDocumentModel> searchForVoidedDocument(SearchCriteriaModel criteria,
            OrganizationModel organization);

    List<VoidedDocumentModel> searchForVoidedDocument(String filterText, OrganizationModel organization,
            Integer firstResult, Integer maxResults);

    SearchResultsModel<VoidedDocumentModel> searchForVoidedDocument(String filterText,
            SearchCriteriaModel criteria, OrganizationModel organization);

    
}