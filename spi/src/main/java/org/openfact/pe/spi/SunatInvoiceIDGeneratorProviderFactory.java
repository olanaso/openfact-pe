package org.openfact.pe.spi;

import org.openfact.Config.Scope;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OpenfactSessionFactory;
import org.openfact.ubl.InvoiceIDGeneratorProvider;
import org.openfact.ubl.InvoiceIDGeneratorProviderFactory;

public class SunatInvoiceIDGeneratorProviderFactory implements InvoiceIDGeneratorProviderFactory {

    @Override
    public InvoiceIDGeneratorProvider create(OpenfactSession session) {
        return new SunatInvoiceIDGeneratorProvider(session);
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
