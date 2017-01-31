package org.openfact.pe.services.ubl;

import org.openfact.Config.Scope;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OpenfactSessionFactory;
import org.openfact.ubl.UBLCreditNoteProvider;
import org.openfact.ubl.UBLCreditNoteProviderFactory;

public class SunatUBLCreditNoteProviderFactory implements UBLCreditNoteProviderFactory {

    @Override
    public UBLCreditNoteProvider create(OpenfactSession session) {
        return new SunatUBLCreditNoteProvider(session);
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
