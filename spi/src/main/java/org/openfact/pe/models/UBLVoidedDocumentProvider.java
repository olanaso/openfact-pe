package org.openfact.pe.models;

import org.openfact.pe.model.types.VoidedDocumentsType;
import org.openfact.pe.models.VoidedDocumentModel;
import org.openfact.provider.Provider;
import org.openfact.ubl.UBLIDGenerator;
import org.openfact.ubl.UBLReader;
import org.openfact.ubl.UBLSender;
import org.openfact.ubl.UBLWriter;

public interface UBLVoidedDocumentProvider extends Provider {

	UBLIDGenerator<VoidedDocumentsType> idGenerator();

	UBLReader<VoidedDocumentsType> reader();

	UBLWriter<VoidedDocumentsType> writer();

	UBLSender<VoidedDocumentModel> sender();
}
