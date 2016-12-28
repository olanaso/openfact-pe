package org.openfact.pe.models;

import org.openfact.pe.models.types.retention.RetentionType;
import org.openfact.provider.Provider;
import org.openfact.ubl.UBLIDGenerator;
import org.openfact.ubl.UBLReader;
import org.openfact.ubl.UBLSender;
import org.openfact.ubl.UBLWriter;

public interface UBLRetentionProvider extends Provider {

	UBLIDGenerator<RetentionType> idGenerator();

	UBLReader<RetentionType> reader();

	UBLWriter<RetentionType> writer();

	UBLSender<RetentionModel> sender();
}
