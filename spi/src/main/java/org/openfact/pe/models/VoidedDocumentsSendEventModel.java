package org.openfact.pe.models;

import java.util.List;

import org.openfact.models.ubl.SendEventModel;

public interface VoidedDocumentsSendEventModel  extends SendEventModel {
	List<VoidedDocumentModel> getVoidedDocuments();

	void setVoidedDocuments(List<VoidedDocumentModel> voidedDocuments);
}