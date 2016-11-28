package org.openfact.pe.models;

import java.util.List;

import org.openfact.models.OrganizationModel;
import org.openfact.models.ScrollModel;
import org.openfact.models.enums.RequiredAction;
import org.openfact.models.search.SearchCriteriaModel;
import org.openfact.models.search.SearchResultsModel;
import org.openfact.provider.Provider;

public interface VoidedDocumentProvider extends Provider {

	VoidedDocumentModel addVoidedDocument(OrganizationModel organization, String ID);

	VoidedDocumentModel getVoidedDocumentById(OrganizationModel organization, String id);

	VoidedDocumentModel getVoidedDocumentByID(OrganizationModel organization, String ID);

	boolean removeVoidedDocument(OrganizationModel organization, String id);

	boolean removeVoidedDocument(OrganizationModel organization, VoidedDocumentModel voidedDocument);

	int getVoidedDocumentsCount(OrganizationModel organization);

	List<VoidedDocumentModel> getVoidedDocuments(OrganizationModel organization);

	List<VoidedDocumentModel> getVoidedDocuments(OrganizationModel organization, List<RequiredAction> requeridAction,
			boolean intoRequeridAction);

	List<VoidedDocumentModel> getVoidedDocuments(OrganizationModel organization, Integer firstResult,
			Integer maxResults);

	List<VoidedDocumentModel> searchForVoidedDocument(OrganizationModel organization, String filterText);

	List<VoidedDocumentModel> searchForVoidedDocument(OrganizationModel organization, String filterText,
			Integer firstResult, Integer maxResults);

	SearchResultsModel<VoidedDocumentModel> searchForVoidedDocument(OrganizationModel organization,
			SearchCriteriaModel criteria);

	SearchResultsModel<VoidedDocumentModel> searchForVoidedDocument(OrganizationModel organization,
			SearchCriteriaModel criteria, String filterText);

	ScrollModel<VoidedDocumentModel> getVoidedDocumentsScroll(OrganizationModel organization);

	ScrollModel<VoidedDocumentModel> getVoidedDocumentsScroll(OrganizationModel organization, boolean asc);

	ScrollModel<VoidedDocumentModel> getVoidedDocumentsScroll(OrganizationModel organization, boolean asc,
			int scrollSize);

	ScrollModel<VoidedDocumentModel> getVoidedDocumentsScroll(OrganizationModel organization, boolean asc,
			int scrollSize, int fetchSize);

}