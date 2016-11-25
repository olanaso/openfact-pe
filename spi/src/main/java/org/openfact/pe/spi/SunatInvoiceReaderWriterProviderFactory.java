package org.openfact.pe.spi;

import org.openfact.Config.Scope;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OpenfactSessionFactory;
import org.openfact.ubl.InvoiceReaderWriterProvider;
import org.openfact.ubl.InvoiceReaderWriterProviderFactory;

/**
 * Created by admin on 11/22/16.
 */
public class SunatInvoiceReaderWriterProviderFactory implements InvoiceReaderWriterProviderFactory {

    @Override
    public InvoiceReaderWriterProvider create(OpenfactSession session) {
        return new SunatInvoiceReaderWriterProvider(session);
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
