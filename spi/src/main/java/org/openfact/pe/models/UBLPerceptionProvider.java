package org.openfact.pe.models;

import org.openfact.pe.models.types.perception.PerceptionType;
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