package org.openfact.pe.provider;

import org.openfact.models.OrganizationModel;
import org.openfact.models.ubl.SendEventModel;
import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.RetentionModel;
import org.openfact.pe.models.SummaryDocumentModel;
import org.openfact.pe.models.VoidedDocumentModel;
import org.openfact.ubl.UblSenderException;
import org.openfact.ubl.UblSenderResponseProvider;

public interface UblSunatSenderResponseProvider extends UblSenderResponseProvider {
	public SendEventModel perceptionSenderResponse(OrganizationModel organization, PerceptionModel perception,
			byte[] xmlSubmitted, byte[] response, String... fault) throws UblSenderException;

	public SendEventModel retentionSenderResponse(OrganizationModel organization, RetentionModel retention,
			byte[] xmlSubmitted, byte[] response, String... fault) throws UblSenderException;

	public SendEventModel voidedDocumentSenderResponse(OrganizationModel organization,
			VoidedDocumentModel voidedDocument, byte[] xmlSubmitted, String response, String... fault)
			throws UblSenderException;

	public SendEventModel summaryDocumentSenderResponse(OrganizationModel organization,
			SummaryDocumentModel summaryDocument, byte[] xmlSubmitted, String response, String... fault)
			throws UblSenderException;
}
