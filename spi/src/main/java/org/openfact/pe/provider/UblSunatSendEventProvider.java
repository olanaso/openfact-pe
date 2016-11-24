package org.openfact.pe.provider;

import java.util.List;

import org.openfact.models.OrganizationModel;
import org.openfact.models.ubl.SendEventModel;
import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.RetentionModel;
import org.openfact.pe.models.SummaryDocumentModel;
import org.openfact.pe.models.VoidedDocumentModel;
import org.openfact.ubl.UblSendEventProvider;
import org.openfact.ubl.UblSenderException;

public interface UblSunatSendEventProvider extends UblSendEventProvider {
	SendEventModel addPerceptionSendEvent(OrganizationModel organization, PerceptionModel perception,
			byte[] xmlSubmitted, byte[] response, boolean isAccepted) throws UblSenderException;

	SendEventModel addRetentionSendEvent(OrganizationModel organization, RetentionModel retention, byte[] xmlSubmitted,
			byte[] response, boolean isAccepted) throws UblSenderException;

	SendEventModel addSummaryDocumentSendEvent(OrganizationModel organization, SummaryDocumentModel summaryDocument,
			byte[] xmlSubmitted, byte[] response, boolean isAccepted) throws UblSenderException;

	SendEventModel addVoidedDocumentSendEvent(OrganizationModel organization, VoidedDocumentModel voidedDocument,
			byte[] xmlSubmitted, byte[] response, boolean isAccepted) throws UblSenderException;

	List<SendEventModel> getPerceptionSendEvents(OrganizationModel organization, PerceptionModel perception)
			throws UblSenderException;

	List<SendEventModel> getRetentionSendEvents(OrganizationModel organization, RetentionModel retention)
			throws UblSenderException;

	List<SendEventModel> getSummaryDocumentSendEvents(OrganizationModel organization,
			SummaryDocumentModel summaryDocument) throws UblSenderException;

	List<SendEventModel> getVoidedDocumentSendEvents(OrganizationModel organization, VoidedDocumentModel voidedDocument)
			throws UblSenderException;
}
