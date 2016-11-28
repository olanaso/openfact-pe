package org.openfact.pe.models;

import java.util.List;

import org.openfact.models.OrganizationModel;
import org.openfact.models.ScrollModel;
import org.openfact.models.enums.RequiredAction;
import org.openfact.models.search.SearchCriteriaModel;
import org.openfact.models.search.SearchResultsModel;
import org.openfact.provider.Provider;

public interface RetentionProvider extends Provider {

	 RetentionModel addRetention(OrganizationModel organization, String ID);

	    RetentionModel getRetentionById(OrganizationModel organization, String id);

	    RetentionModel getRetentionByID(OrganizationModel organization, String ID);

	    boolean removeRetention(OrganizationModel organization, String id);

	    boolean removeRetention(OrganizationModel organization, RetentionModel retention);

	    int getRetentionsCount(OrganizationModel organization);

	    List<RetentionModel> getRetentions(OrganizationModel organization);

	    List<RetentionModel> getRetentions(OrganizationModel organization,
	            List<RequiredAction> requeridAction, boolean intoRequeridAction);

	    List<RetentionModel> getRetentions(OrganizationModel organization, Integer firstResult, Integer maxResults);

	    List<RetentionModel> searchForRetention(OrganizationModel organization, String filterText);

	    List<RetentionModel> searchForRetention(OrganizationModel organization, String filterText,
	            Integer firstResult, Integer maxResults);

	    SearchResultsModel<RetentionModel> searchForRetention(OrganizationModel organization,
	            SearchCriteriaModel criteria);

	    SearchResultsModel<RetentionModel> searchForRetention(OrganizationModel organization,
	            SearchCriteriaModel criteria, String filterText);

	    ScrollModel<RetentionModel> getRetentionsScroll(OrganizationModel organization);

	    ScrollModel<RetentionModel> getRetentionsScroll(OrganizationModel organization, boolean asc);

	    ScrollModel<RetentionModel> getRetentionsScroll(OrganizationModel organization, boolean asc, int scrollSize);

	    ScrollModel<RetentionModel> getRetentionsScroll(OrganizationModel organization, boolean asc, int scrollSize,
	            int fetchSize);

}