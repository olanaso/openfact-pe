package org.openfact.pe.models;

import java.util.List;

import org.openfact.models.CreditNoteModel;
import org.openfact.models.CreditNoteSendEventModel;
import org.openfact.models.DebitNoteModel;
import org.openfact.models.DebitNoteSendEventModel;
import org.openfact.models.InvoiceModel;
import org.openfact.models.InvoiceSendEventModel;
import org.openfact.models.OrganizationModel;
import org.openfact.models.enums.SendResultType;
import org.openfact.pe.constants.CodigoTipoDocumento;
import org.openfact.provider.Provider;

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

	boolean removeSunatResponse(OrganizationModel organization, String id);

	boolean removeSunatResponse(OrganizationModel organization, SunatResponseModel sunatResponse);

	SunatResponseModel getSunatResponseById(OrganizationModel organization, String id);

	SunatResponseModel getSunatResponseByDocument(OrganizationModel organization, CodigoTipoDocumento documentType,
			String id);

	List<SunatResponseModel> getSunatResponsesByDocument(OrganizationModel organization,
			CodigoTipoDocumento documentType, String id);

}
