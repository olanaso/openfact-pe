package org.openfact.pe.models;

import org.openfact.provider.Provider;
import org.openfact.provider.ProviderFactory;
import org.openfact.provider.Spi;

public class UBLSummaryDocumentSpi implements Spi {

	@Override
	public boolean isInternal() {
		return false;
	}

	@Override
	public String getName() {
		return "ublSummaryDocument";
	}

	@Override
	public Class<? extends Provider> getProviderClass() {
		return UBLSummaryDocumentProvider.class;
	}

	@Override
	public Class<? extends ProviderFactory> getProviderFactoryClass() {
		return UBLSummaryDocumentProviderFactory.class;
	}

}
