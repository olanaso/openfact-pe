package org.openfact.pe.models.jpa.ubl;

import javax.persistence.EntityManager;

import org.openfact.Config.Scope;
import org.openfact.connections.jpa.JpaConnectionProvider;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OpenfactSessionFactory;
import org.openfact.pe.models.RetentionProvider;
import org.openfact.pe.models.RetentionProviderFactory;

public class JpaRetentionProviderFactory implements RetentionProviderFactory {

	@Override
	public RetentionProvider create(OpenfactSession session) {
		EntityManager em = session.getProvider(JpaConnectionProvider.class).getEntityManager();
		return new JpaRetentionProvider(session, em);
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
