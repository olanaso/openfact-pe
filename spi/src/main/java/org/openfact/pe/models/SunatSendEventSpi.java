package org.openfact.pe.models;

import org.openfact.provider.Provider;
import org.openfact.provider.ProviderFactory;
import org.openfact.provider.Spi;

public class SunatSendEventSpi implements Spi{

	@Override
	public boolean isInternal() {
		return false;
	}

	@Override
	public String getName() {
		return "sunatSendEvent";
	}

	@Override
	public Class<? extends Provider> getProviderClass() {
//		return SunatSendEventProvider.class;
		return null;
	}

	@Override
	public Class<? extends ProviderFactory> getProviderFactoryClass() {
		return SunatSendEventProviderFactory.class;
	}

}
