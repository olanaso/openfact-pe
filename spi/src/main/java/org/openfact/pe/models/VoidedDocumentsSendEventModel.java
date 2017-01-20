package org.openfact.pe.models;

import org.openfact.models.SendEventModel;

public interface VoidedDocumentsSendEventModel extends SendEventModel {
	VoidedDocumentModel getVoidedDocument();

	void setVoidedDocument(VoidedDocumentModel voidedDocument);
}