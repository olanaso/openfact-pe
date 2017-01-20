package org.openfact.pe.models;

import org.openfact.models.SendEventModel;

public interface SummaryDocumentsSendEventModel extends SendEventModel {
	SummaryDocumentModel getSummaryDocument();

	void setSummaryDocument(SummaryDocumentModel summaryDocument);
}
