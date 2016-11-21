package org.openfact.pe.models;

import org.openfact.provider.Provider;
import org.openfact.provider.ProviderFactory;
import org.openfact.provider.Spi;

public class SummaryDocumentSpi implements Spi {

    @Override
    public boolean isInternal() {
        return false;
    }

    @Override
    public String getName() {
        return "summary-document";
    }

    @Override
    public Class<? extends Provider> getProviderClass() {
        return SummaryDocumentProvider.class;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Class<? extends ProviderFactory> getProviderFactoryClass() {
        return SummaryDocumentProviderFactory.class;
    }

}