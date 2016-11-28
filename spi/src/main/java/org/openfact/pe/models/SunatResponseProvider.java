package org.openfact.pe.models;

import org.openfact.models.CreditNoteModel;
import org.openfact.models.DebitNoteModel;
import org.openfact.models.InvoiceModel;
import org.openfact.models.OrganizationModel;
import org.openfact.models.enums.SendResultType;
import org.openfact.provider.Provider;

public interface SunatResponseProvider extends Provider {

	SunatResponseModel addSendEvent(OrganizationModel organization, SendResultType type, InvoiceModel invoice);

	SunatResponseModel addSendEvent(OrganizationModel organization, SendResultType type, CreditNoteModel creditNote);

	SunatResponseModel addSendEvent(OrganizationModel organization, SendResultType type, DebitNoteModel debitNote);

	SunatResponseModel addSendEvent(OrganizationModel organization, SendResultType type, PerceptionModel perception);

	SunatResponseModel addSendEvent(OrganizationModel organization, SendResultType type, RetentionModel retention);

	SunatResponseModel addSendEvent(OrganizationModel organization, SendResultType type,
			SummaryDocumentModel summaryDocument);

	SunatResponseModel addSendEvent(OrganizationModel organization, SendResultType type,
			VoidedDocumentModel voidedDocument);
}
