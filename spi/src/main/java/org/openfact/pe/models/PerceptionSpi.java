package org.openfact.pe.models;

import org.openfact.provider.Provider;
import org.openfact.provider.ProviderFactory;
import org.openfact.provider.Spi;

public class PerceptionSpi implements Spi {

    @Override
    public boolean isInternal() {
        return false;
    }

    @Override
    public String getName() {
        return "perception";
    }

    @Override
    public Class<? extends Provider> getProviderClass() {
        return PerceptionProvider.class;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Class<? extends ProviderFactory> getProviderFactoryClass() {
        return PerceptionProviderFactory.class;
    }

}