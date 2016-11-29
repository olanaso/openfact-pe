package org.openfact.pe.models;

import java.util.List;

import org.openfact.models.CreditNoteSendEventModel;
import org.openfact.models.DebitNoteSendEventModel;
import org.openfact.models.InvoiceSendEventModel;
import org.openfact.models.OrganizationModel;
import org.openfact.models.ScrollModel;
import org.openfact.models.enums.SendResultType;
import org.openfact.models.search.SearchCriteriaModel;
import org.openfact.models.search.SearchResultsModel;
import org.openfact.provider.Provider;
import org.openfact.ubl.SendEventModel;

public interface SunatResponseProvider extends Provider {

	SunatResponseModel addSunatResponse(OrganizationModel organization, SendResultType type,
			InvoiceSendEventModel invoiceSendEvent);

	SunatResponseModel addSunatResponse(OrganizationModel organization, SendResultType type,
			CreditNoteSendEventModel creditNoteSendEvent);

	SunatResponseModel addSunatResponse(OrganizationModel organization, SendResultType type,
			DebitNoteSendEventModel debitNoteSendEvent);

	SunatResponseModel addSunatResponse(OrganizationModel organization, SendResultType type,
			PerceptionSendEventModel perceptionSendEvent);

	SunatResponseModel addSunatResponse(OrganizationModel organization, SendResultType type,
			RetentionSendEventModel retentionSendEvent);

	SunatResponseModel addSunatResponse(OrganizationModel organization, SendResultType type,
			SummaryDocumentsSendEventModel summaryDocumentsSendEvent);

	SunatResponseModel addSunatResponse(OrganizationModel organization, SendResultType type,
			VoidedDocumentsSendEventModel voidedDocumentsSendEvent);

	SunatResponseModel getSunatResponseById(OrganizationModel organization, String id);

	boolean removeSunatResponse(OrganizationModel organization, String id);

	boolean removeSunatResponse(OrganizationModel organization, SunatResponseModel sunatResponse);

	int getSunatResponsesCount(OrganizationModel organization);

	List<SunatResponseModel> getSunatResponses(OrganizationModel organization);

	List<SunatResponseModel> getSunatResponse(OrganizationModel organization, Integer firstResult, Integer maxResults);

	SearchResultsModel<SunatResponseModel> searchForSunatResponse(OrganizationModel organization,
			SearchCriteriaModel criteria);

	ScrollModel<SunatResponseModel> getSunatResponsesScroll(OrganizationModel organization);

	ScrollModel<SunatResponseModel> getSunatResponsesScroll(OrganizationModel organization, boolean asc);

	ScrollModel<SunatResponseModel> getSunatResponsesScroll(OrganizationModel organization, boolean asc,
			int scrollSize);

	ScrollModel<SunatResponseModel> getSunatResponsesScroll(OrganizationModel organization, boolean asc, int scrollSize,
			int fetchSize);
}
