package org.openfact.pe.models;

import org.openfact.ubl.SendEventModel;


public interface SummaryDocumentsSendEventModel extends SendEventModel {
	SummaryDocumentModel getSummaryDocument();

	void setSummaryDocument(SummaryDocumentModel summaryDocument);
}
