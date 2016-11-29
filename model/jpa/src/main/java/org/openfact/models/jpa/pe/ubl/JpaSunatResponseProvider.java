package org.openfact.models.jpa.pe.ubl;

import org.openfact.models.CreditNoteModel;
import org.openfact.models.DebitNoteModel;
import org.openfact.models.InvoiceModel;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.enums.SendResultType;
import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.RetentionModel;
import org.openfact.pe.models.SummaryDocumentModel;
import org.openfact.pe.models.SunatResponseModel;
import org.openfact.pe.models.SunatResponseProvider;
import org.openfact.pe.models.VoidedDocumentModel;

public class JpaSunatResponseProvider implements SunatResponseProvider {

	private OpenfactSession session;

	public JpaSunatResponseProvider(OpenfactSession session) {
		this.session = session;
	}

	@Override
	public void close() {
	}

	@Override
	public SunatResponseModel addSendEvent(OrganizationModel organization, SendResultType type, InvoiceModel invoice) {
		return null;
	}

	@Override
	public SunatResponseModel addSendEvent(OrganizationModel organization, SendResultType type,
			CreditNoteModel creditNote) {
		return null;
	}

	@Override
	public SunatResponseModel addSendEvent(OrganizationModel organization, SendResultType type,
			DebitNoteModel debitNote) {
		return null;
	}

	@Override
	public SunatResponseModel addSendEvent(OrganizationModel organization, SendResultType type,
			PerceptionModel perception) {
		return null;
	}

	@Override
	public SunatResponseModel addSendEvent(OrganizationModel organization, SendResultType type,
			RetentionModel retention) {
		return null;
	}

	@Override
	public SunatResponseModel addSendEvent(OrganizationModel organization, SendResultType type,
			SummaryDocumentModel summaryDocument) {
		return null;
	}

	@Override
	public SunatResponseModel addSendEvent(OrganizationModel organization, SendResultType type,
			VoidedDocumentModel voidedDocument) {
		return null;
	}

}
