package org.openfact.pe.provider;

import org.openfact.provider.Provider;
import org.openfact.provider.ProviderFactory;
import org.openfact.provider.Spi;

public class UblSunatTemplateSpi implements Spi {

	@Override
	public boolean isInternal() {
		return true;
	}

	@Override
	public String getName() {
		return "ulbSunatTemplate";
	}

	@Override
	public Class<? extends Provider> getProviderClass() {
		return UblSunatTemplateProvider.class;
	}

	@Override
	public Class<? extends ProviderFactory> getProviderFactoryClass() {
		return UblSunatTemplateProviderFactory.class;
	}

}