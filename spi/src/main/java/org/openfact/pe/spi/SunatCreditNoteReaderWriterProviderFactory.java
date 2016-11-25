package org.openfact.pe.spi;

import org.openfact.Config.Scope;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OpenfactSessionFactory;
import org.openfact.ubl.CreditNoteReaderWriterProvider;
import org.openfact.ubl.CreditNoteReaderWriterProviderFactory;

public class SunatCreditNoteReaderWriterProviderFactory implements CreditNoteReaderWriterProviderFactory {

    @Override
    public CreditNoteReaderWriterProvider create(OpenfactSession session) {
        return new SunatCreditNoteReaderWriterProvider(session);
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
