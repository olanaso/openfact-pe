package org.openfact.pe.models;

import org.openfact.provider.Provider;
import org.openfact.provider.ProviderFactory;
import org.openfact.provider.Spi;

public class VoidedDocumentSpi implements Spi {

    @Override
    public boolean isInternal() {
        return false;
    }

    @Override
    public String getName() {
        return "voided-document";
    }

    @Override
    public Class<? extends Provider> getProviderClass() {
        return VoidedDocumentProvider.class;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Class<? extends ProviderFactory> getProviderFactoryClass() {
        return VoidedDocumentProviderFactory.class;
    }

}