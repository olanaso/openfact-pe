package org.openfact.pe.models.jpa.ubl;

import org.openfact.Config;
import org.openfact.connections.jpa.JpaConnectionProvider;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OpenfactSessionFactory;
import org.openfact.models.StorageFileProvider;
import org.openfact.models.StorageFileProviderFactory;
import org.openfact.pe.models.SunatStorageFileProvider;
import org.openfact.pe.models.SunatStorageFileProviderFactory;

import javax.persistence.EntityManager;

/**
 * Created by lxpary on 28/12/16.
 */
public class JpaSunatStorageFileProviderFactory implements SunatStorageFileProviderFactory {

    @Override
    public void init(Config.Scope config) {
    }

    @Override
    public void postInit(OpenfactSessionFactory factory) {

    }

    @Override
    public String getId() {
        return "jpa";
    }
    @Override
    public void close() {
    }

    @Override
    public SunatStorageFileProvider create(OpenfactSession session) {
        EntityManager em = session.getProvider(JpaConnectionProvider.class).getEntityManager();
        return new JpaSunatStorageFileProvider(session, em);

    }
/* @Override
    public StorageFileProvider create(OpenfactSession session) {
        EntityManager em = session.getProvider(JpaConnectionProvider.class).getEntityManager();
        return new JpaSunatStorageFileProvider(session, em);
    }*/
}
