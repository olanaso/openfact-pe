package org.openfact.pe.models;

import org.openfact.provider.Provider;
import org.openfact.provider.ProviderFactory;
import org.openfact.provider.Spi;

public class UBLPerceptionSpi implements Spi {

	@Override
	public boolean isInternal() {
		return false;
	}

	@Override
	public String getName() {
		return "ublPerception";
	}

	@Override
	public Class<? extends Provider> getProviderClass() {
		return UBLPerceptionProvider.class;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class<? extends ProviderFactory> getProviderFactoryClass() {
		return UBLPerceptionProviderFactory.class;
	}

}
