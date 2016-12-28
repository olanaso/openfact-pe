package org.openfact.pe.models;

import org.openfact.provider.Provider;
import org.openfact.provider.ProviderFactory;
import org.openfact.provider.Spi;

/**
 * Created by lxpary on 28/12/16.
 */
public class SunatStorageFileSpi implements Spi {
    @Override
    public boolean isInternal() {
        return false;
    }

    @Override
    public String getName() {
        return "sunat-storage-file";
    }

    @Override
    public Class<? extends Provider> getProviderClass() {
        return SunatStorageFileProvider.class;
    }

    @Override
    public Class<? extends ProviderFactory> getProviderFactoryClass() {
        return SunatStorageFileProviderFactory.class;
    }
}
