package org.openfact.pe.models;

import java.util.List;

import org.openfact.models.OrganizationModel;
import org.openfact.models.ScrollModel;
import org.openfact.models.enums.RequiredAction;
import org.openfact.models.search.SearchCriteriaModel;
import org.openfact.models.search.SearchResultsModel;
import org.openfact.provider.Provider;

public interface PerceptionProvider extends Provider {

	PerceptionModel addPerception(OrganizationModel organization, String ID);

	PerceptionModel getPerceptionById(OrganizationModel organization, String id);

	PerceptionModel getPerceptionByID(OrganizationModel organization, String ID);

	boolean removePerception(OrganizationModel organization, String id);

	boolean removePerception(OrganizationModel organization, PerceptionModel perception);

	int getPerceptionsCount(OrganizationModel organization);

	List<PerceptionModel> getPerceptions(OrganizationModel organization);

	List<PerceptionModel> getPerceptions(OrganizationModel organization, List<RequiredAction> requeridAction,
			boolean intoRequeridAction);

	List<PerceptionModel> getPerceptions(OrganizationModel organization, Integer firstResult, Integer maxResults);

	List<PerceptionModel> searchForPerception(OrganizationModel organization, String filterText);

	List<PerceptionModel> searchForPerception(OrganizationModel organization, String filterText, Integer firstResult,
			Integer maxResults);

	SearchResultsModel<PerceptionModel> searchForPerception(OrganizationModel organization,
			SearchCriteriaModel criteria);

	SearchResultsModel<PerceptionModel> searchForPerception(OrganizationModel organization,
			SearchCriteriaModel criteria, String filterText);

	ScrollModel<PerceptionModel> getPerceptionsScroll(OrganizationModel organization);

	ScrollModel<PerceptionModel> getPerceptionsScroll(OrganizationModel organization, boolean asc);

	ScrollModel<PerceptionModel> getPerceptionsScroll(OrganizationModel organization, boolean asc, int scrollSize);

	ScrollModel<PerceptionModel> getPerceptionsScroll(OrganizationModel organization, boolean asc, int scrollSize,
			int fetchSize);

}