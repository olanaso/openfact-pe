package org.openfact.pe.models.jpa;

import org.openfact.Config.Scope;
import org.openfact.connections.jpa.entityprovider.JpaEntityProvider;
import org.openfact.connections.jpa.entityprovider.JpaEntityProviderFactory;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OpenfactSessionFactory;

public class SunatJpaEntityProviderFactory implements JpaEntityProviderFactory {

    protected static final String ID = "entity-provider-sunat";

    @Override
    public JpaEntityProvider create(OpenfactSession session) {
        return new SunatJpaEntityProvider();
    }

    @Override
    public String getId() {
        return ID;
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
}
