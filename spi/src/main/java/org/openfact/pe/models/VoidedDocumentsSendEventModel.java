package org.openfact.pe.models;

import java.util.List;

import org.openfact.models.ubl.SendEventModel;

public interface VoidedDocumentsSendEventModel  extends SendEventModel {
	List<VoidedDocumentModel> getSummaryDocuments();

	void setSummaryDocuments(List<VoidedDocumentModel> summaryDocuments);
}