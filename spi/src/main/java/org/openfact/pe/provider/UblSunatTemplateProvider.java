package org.openfact.pe.provider;

import org.openfact.models.OrganizationModel;
import org.openfact.models.ubl.SendEventModel;
import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.RetentionModel;
import org.openfact.pe.models.SummaryDocumentModel;
import org.openfact.pe.models.VoidedDocumentModel;
import org.openfact.ubl.UblSenderException;
import org.openfact.ubl.UblTemplateProvider;

public interface UblSunatTemplateProvider extends UblTemplateProvider {

	public SendEventModel sendPerception(OrganizationModel organization, PerceptionModel perception) throws UblSenderException;

	public SendEventModel sendRetention(OrganizationModel organization, RetentionModel retention) throws UblSenderException;

	public SendEventModel sendSummaryDocument(OrganizationModel organization, SummaryDocumentModel summaryDocument)
			throws UblSenderException;

	public SendEventModel sendVoidedDocument(OrganizationModel organization, VoidedDocumentModel voidedDocument)
			throws UblSenderException;
}
