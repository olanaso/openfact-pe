package org.openfact.pe.services.ubl;

import org.openfact.Config.Scope;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OpenfactSessionFactory;
import org.openfact.ubl.UBLDebitNoteProvider;
import org.openfact.ubl.UBLDebitNoteProviderFactory;

public class SunatUBLDebitNoteProviderFactory implements UBLDebitNoteProviderFactory {

    @Override
    public UBLDebitNoteProvider create(OpenfactSession session) {
        return new SunatUBLDebitNoteProvider(session);
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
