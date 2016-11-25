package org.openfact.pe.spi;

import org.openfact.Config.Scope;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OpenfactSessionFactory;
import org.openfact.ubl.DebitNoteReaderWriterProvider;
import org.openfact.ubl.DebitNoteReaderWriterProviderFactory;

public class SunatDebitNoteReaderWriterProviderFactory implements DebitNoteReaderWriterProviderFactory {

    @Override
    public DebitNoteReaderWriterProvider create(OpenfactSession session) {
        return new SunatDebitNoteReaderWriterProvider(session);
    }

    @Override
    public void init(Scope config) {
    }

    @Override
    public void postInit(OpenfactSessionFactory factory) {
    }

    @Override
    public void close() {
    }

    @Override
    public String getId() {
        return "sunat";
    }

}
