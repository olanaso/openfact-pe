package org.openfact.pe.models;

import org.openfact.pe.model.types.PerceptionType;
import org.openfact.pe.models.PerceptionModel;
import org.openfact.provider.Provider;
import org.openfact.ubl.UBLIDGenerator;
import org.openfact.ubl.UBLReader;
import org.openfact.ubl.UBLSender;
import org.openfact.ubl.UBLWriter;

public interface UBLPerceptionProvider extends Provider {

    UBLIDGenerator<PerceptionType> idGenerator();

    UBLReader<PerceptionType> reader();

    UBLWriter<PerceptionType> writer();

    UBLSender<PerceptionModel> sender();

}