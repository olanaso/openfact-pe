package org.openfact.pe.models;

import java.util.List;

import org.openfact.models.DebitNoteModel;
import org.openfact.models.OrganizationModel;
import org.openfact.models.ScrollModel;
import org.openfact.models.enums.RequiredAction;
import org.openfact.models.search.SearchCriteriaModel;
import org.openfact.models.search.SearchResultsModel;
import org.openfact.provider.Provider;

public interface SummaryDocumentProvider extends Provider {

	 SummaryDocumentModel addSummaryDocument(OrganizationModel organization, String ID);

	    SummaryDocumentModel getSummaryDocumentById(OrganizationModel organization, String id);

	    SummaryDocumentModel getSummaryDocumentByID(OrganizationModel organization, String ID);

	    boolean removeSummaryDocument(OrganizationModel organization, String id);

	    boolean removeSummaryDocument(OrganizationModel organization, SummaryDocumentModel summaryDocument);

	    int getSummaryDocumentsCount(OrganizationModel organization);

	    List<SummaryDocumentModel> getSummaryDocuments(OrganizationModel organization);

	    List<SummaryDocumentModel> getSummaryDocuments(OrganizationModel organization,
	            List<RequiredAction> requeridAction, boolean intoRequeridAction);

	    List<SummaryDocumentModel> getSummaryDocuments(OrganizationModel organization, Integer firstResult, Integer maxResults);

	    List<SummaryDocumentModel> searchForSummaryDocument(OrganizationModel organization, String filterText);

	    List<SummaryDocumentModel> searchForSummaryDocument(OrganizationModel organization, String filterText,
	            Integer firstResult, Integer maxResults);

	    SearchResultsModel<SummaryDocumentModel> searchForSummaryDocument(OrganizationModel organization,
	            SearchCriteriaModel criteria);

	    SearchResultsModel<SummaryDocumentModel> searchForSummaryDocument(OrganizationModel organization,
	            SearchCriteriaModel criteria, String filterText);

	    ScrollModel<SummaryDocumentModel> getSummaryDocumentsScroll(OrganizationModel organization);

	    ScrollModel<SummaryDocumentModel> getSummaryDocumentsScroll(OrganizationModel organization, boolean asc);

	    ScrollModel<SummaryDocumentModel> getSummaryDocumentsScroll(OrganizationModel organization, boolean asc, int scrollSize);
	    ScrollModel<List<SummaryDocumentModel>> getSummaryDocumentsScroll(OrganizationModel organization, int scrollSize, String... requiredAction);
}