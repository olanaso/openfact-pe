package org.openfact.pe.models.jpa;

import java.util.ArrayList;
import java.util.List;

import org.openfact.connections.jpa.entityprovider.JpaEntityProvider;
import org.openfact.pe.models.jpa.entities.PerceptionEntity;

public class SunatJpaEntityProvider implements JpaEntityProvider {

    @Override
    public List<Class<?>> getEntities() {
        List<Class<?>> entities = new ArrayList<>();
        entities.add(PerceptionEntity.class);
        return entities;
    }

    @Override
    public String getChangelogLocation() {
        return "META-INF/changelog-sunat.xml";
    }

    @Override
    public void close() {
    }

    @Override
    public String getFactoryId() {
        return SunatJpaEntityProviderFactory.ID;
    }

}
