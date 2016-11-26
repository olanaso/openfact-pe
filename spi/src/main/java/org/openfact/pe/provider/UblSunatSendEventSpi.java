package org.openfact.pe.provider;

import org.openfact.provider.Provider;
import org.openfact.provider.ProviderFactory;
import org.openfact.provider.Spi;

public class UblSunatSendEventSpi implements Spi{

	@Override
	public boolean isInternal() {
		return false;
	}

	@Override
	public String getName() {
		return "ublSunatSendEvent";
	}

	@Override
	public Class<? extends Provider> getProviderClass() {
		return UblSunatSendEventProvider.class;
	}

	@Override
	public Class<? extends ProviderFactory> getProviderFactoryClass() {
		return UblSunatSendEventProviderFactory.class;
	}

}
