package org.openfact.pe.provider;

import org.openfact.provider.Provider;
import org.openfact.provider.ProviderFactory;
import org.openfact.provider.Spi;

public class UblSunatSenderResponseSpi implements Spi {

	@Override
	public boolean isInternal() {
		return false;
	}

	@Override
    public String getName() {
        return "ulbSunatResponse";
    }

    @Override
    public Class<? extends Provider> getProviderClass() {
        return UblSunatSenderResponseProvider.class;
    }

    @Override
    public Class<? extends ProviderFactory> getProviderFactoryClass() {
        return UblSunatSenderResponseProviderFactory.class;
    }

}
