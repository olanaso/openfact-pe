package org.openfact.pe.services.ubl;

import org.openfact.Config.Scope;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OpenfactSessionFactory;
import org.openfact.pe.models.UBLVoidedDocumentProvider;
import org.openfact.pe.models.UBLVoidedDocumentProviderFactory;

public class SunatUBLVoidedDocumentProviderFactory implements UBLVoidedDocumentProviderFactory {

	@Override
	public UBLVoidedDocumentProvider create(OpenfactSession session) {
		return new SunatUBLVoidedDocumentProvider(session);
	}

	@Override
	public void init(Scope config) {

	}

	@Override
	public void postInit(OpenfactSessionFactory factory) {

	}

	@Override
	public void close() {

	}

	@Override
	public String getId() {
		return "sunat";
	}

}
