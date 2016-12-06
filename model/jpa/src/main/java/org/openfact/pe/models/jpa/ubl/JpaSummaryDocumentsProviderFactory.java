package org.openfact.pe.models.jpa.ubl;

import javax.persistence.EntityManager;

import org.openfact.Config.Scope;
import org.openfact.connections.jpa.JpaConnectionProvider;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OpenfactSessionFactory;
import org.openfact.pe.models.SummaryDocumentProvider;
import org.openfact.pe.models.SummaryDocumentProviderFactory;

public class JpaSummaryDocumentsProviderFactory implements SummaryDocumentProviderFactory {

	@Override
	public SummaryDocumentProvider create(OpenfactSession session) {
		EntityManager em = session.getProvider(JpaConnectionProvider.class).getEntityManager();
		return new JpaSummaryDocumentsProvider(session, em);
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
