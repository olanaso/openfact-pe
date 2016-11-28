package org.openfact.pe.models;

import org.openfact.provider.Provider;
import org.openfact.provider.ProviderFactory;
import org.openfact.provider.Spi;

public class UBLRetentionSpi implements Spi{

	@Override
	public boolean isInternal() {
		return false;
	}

	@Override
	public String getName() {
		return "ublRetention";
	}

	@Override
	public Class<? extends Provider> getProviderClass() {
		return UBLRetentionProvider.class;
	}

	@Override
	public Class<? extends ProviderFactory> getProviderFactoryClass() {
		return UBLRetentionProviderFactory.class;
	}

}
