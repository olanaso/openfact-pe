package org.openfact.pe.models;

import org.openfact.models.OrganizationModel;
import org.openfact.ubl.SendEventModel;
import org.openfact.models.enums.SendResultType;
import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.RetentionModel;
import org.openfact.pe.models.SummaryDocumentModel;
import org.openfact.pe.models.VoidedDocumentModel;
import org.openfact.ubl.SendEventProvider;

public interface SunatSendEventProvider extends SendEventProvider {
	SendEventModel addSendEvent(OrganizationModel organization, SendResultType type, PerceptionModel perception);

	SendEventModel addSendEvent(OrganizationModel organization, SendResultType type, RetentionModel retention);

	SendEventModel addSendEvent(OrganizationModel organization, SendResultType type,
			SummaryDocumentModel summaryDocument);

	SendEventModel addSendEvent(OrganizationModel organization, SendResultType type,
			VoidedDocumentModel voidedDocument);
}
