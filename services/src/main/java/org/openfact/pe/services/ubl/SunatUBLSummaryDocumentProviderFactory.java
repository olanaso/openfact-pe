package org.openfact.pe.services.ubl;

import org.openfact.Config.Scope;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OpenfactSessionFactory;
import org.openfact.pe.models.UBLSummaryDocumentProvider;
import org.openfact.pe.models.UBLSummaryDocumentProviderFactory;

public class SunatUBLSummaryDocumentProviderFactory implements UBLSummaryDocumentProviderFactory {

	@Override
	public UBLSummaryDocumentProvider create(OpenfactSession session) {
		return new SunatUBLSummaryDocumentProvider(session);
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