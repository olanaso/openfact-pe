package org.openfact.models.jpa.pe.ubl;

import javax.persistence.EntityManager;

import org.openfact.Config.Scope;
import org.openfact.connections.jpa.JpaConnectionProvider;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OpenfactSessionFactory;
import org.openfact.pe.provider.UblSunatSendEventProvider;
import org.openfact.pe.provider.UblSunatSendEventProviderFactory;

public class JpaSunatSendEventProviderFactory  implements UblSunatSendEventProviderFactory {

	@Override
	public UblSunatSendEventProvider create(OpenfactSession session) {
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
