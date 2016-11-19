package org.openfact.models.jpa.pe;

import java.util.Collections;
import java.util.List;

import org.openfact.connections.jpa.entityprovider.JpaEntityProvider;
import org.openfact.models.jpa.pe.entities.RetentionEntity;

public class JpaEntityProvider_PE implements JpaEntityProvider {

    @Override
    public List<Class<?>> getEntities() {
        return Collections.<Class<?>> singletonList(RetentionEntity.class);
    }

    @Override
    public String getChangelogLocation() {
        return "META-INF/changelog-pe.xml";
    }

    @Override
    public void close() {
    }

    @Override
    public String getFactoryId() {
        return JpaEntityProviderFactory_PE.ID;
    }

}
