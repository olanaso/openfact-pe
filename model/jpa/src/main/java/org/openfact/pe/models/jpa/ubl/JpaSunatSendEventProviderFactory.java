package org.openfact.pe.models.jpa.ubl;

import javax.persistence.EntityManager;

import org.openfact.Config.Scope;
import org.openfact.connections.jpa.JpaConnectionProvider;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OpenfactSessionFactory;
import org.openfact.pe.models.SunatSendEventProvider;
import org.openfact.pe.models.SunatSendEventProviderFactory;

public class JpaSunatSendEventProviderFactory  implements SunatSendEventProviderFactory {

	@Override
	public SunatSendEventProvider create(OpenfactSession session) {
		EntityManager em = session.getProvider(JpaConnectionProvider.class).getEntityManager();
		return new JpaSunatSendEventProvider(session, em);		
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
		return "pe";
	}

}
