package org.openfact.pe.services.ubl;

import org.openfact.Config.Scope;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OpenfactSessionFactory;
import org.openfact.ubl.UBLInvoiceProvider;
import org.openfact.ubl.UBLInvoiceProviderFactory;

/**
 * Created by admin on 11/22/16.
 */
public class SunatUBLInvoiceProviderFactory implements UBLInvoiceProviderFactory {

    @Override
    public UBLInvoiceProvider create(OpenfactSession session) {
        return new SunatUBLInvoiceProvider(session);
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
