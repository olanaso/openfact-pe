package org.openfact.pe.models;

import java.util.List;

import org.openfact.models.ubl.SendEventModel;

public interface SummaryDocumentsSendEventModel extends SendEventModel {
	List<SummaryDocumentModel> getSummaryDocuments();

	void setSummaryDocuments(List<SummaryDocumentModel> summaryDocuments);
}
