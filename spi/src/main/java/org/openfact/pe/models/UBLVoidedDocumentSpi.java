package org.openfact.pe.models;

import org.openfact.provider.Provider;
import org.openfact.provider.ProviderFactory;
import org.openfact.provider.Spi;

public class UBLVoidedDocumentSpi implements Spi {

	@Override
	public boolean isInternal() {
		return false;
	}

	@Override
	public String getName() {
		return "ublVoidedDocument";
	}

	@Override
	public Class<? extends Provider> getProviderClass() {
		return UBLVoidedDocumentProvider.class;
	}

	@Override
	public Class<? extends ProviderFactory> getProviderFactoryClass() {
		return UBLVoidedDocumentProviderFactory.class;
	}

}
