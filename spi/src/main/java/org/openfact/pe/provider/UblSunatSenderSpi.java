package org.openfact.pe.provider;

import org.openfact.provider.Provider;
import org.openfact.provider.ProviderFactory;
import org.openfact.provider.Spi;
import org.openfact.ubl.UblSenderProvider;
import org.openfact.ubl.UblSenderProviderFactory;

public class UblSunatSenderSpi implements Spi {


    @Override
    public boolean isInternal() {
        return false;
    }

    @Override
    public String getName() {
        return "ulbSunatSender";
    }

    @Override
    public Class<? extends Provider> getProviderClass() {
        return UblSunatSenderProvider.class;
    }

    @Override
    public Class<? extends ProviderFactory> getProviderFactoryClass() {
        return UblSunatSenderProviderFactory.class;
    }

}
