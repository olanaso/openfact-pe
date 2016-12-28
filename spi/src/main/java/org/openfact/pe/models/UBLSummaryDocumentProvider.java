package org.openfact.pe.models;

import org.openfact.pe.models.types.summary.SummaryDocumentsType;
import org.openfact.provider.Provider;
import org.openfact.ubl.UBLIDGenerator;
import org.openfact.ubl.UBLReader;
import org.openfact.ubl.UBLSender;
import org.openfact.ubl.UBLWriter;

public interface UBLSummaryDocumentProvider extends Provider {

	UBLIDGenerator<SummaryDocumentsType> idGenerator();

	UBLReader<SummaryDocumentsType> reader();

	UBLWriter<SummaryDocumentsType> writer();

	UBLSender<SummaryDocumentModel> sender();
}
