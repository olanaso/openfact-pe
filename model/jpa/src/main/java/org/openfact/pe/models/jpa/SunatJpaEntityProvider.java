package org.openfact.pe.models.jpa;

import java.util.ArrayList;
import java.util.List;

import org.openfact.connections.jpa.entityprovider.JpaEntityProvider;
import org.openfact.pe.models.jpa.entities.*;

public class SunatJpaEntityProvider implements JpaEntityProvider {

    @Override
    public List<Class<?>> getEntities() {
        List<Class<?>> entities = new ArrayList<>();

        entities.add(SunatSendEventEntity.class);
        entities.add(SunatStorageFileEntity.class);

        entities.add(PerceptionEntity.class);
        entities.add(PerceptionRequiredActionEntity.class);
        entities.add(PerceptionSendEventEntity.class);
        entities.add(PerceptionInformationEntity.class);
        entities.add(PerceptionDocumentReferenceEntity.class);

        entities.add(RetentionEntity.class);
        entities.add(RetentionRequiredActionEntity.class);
        entities.add(RetentionSendEventEntity.class);
        entities.add(RetentionInformationEntity.class);
        entities.add(RetentionDocumentReferenceEntity.class);

        entities.add(SummaryDocumentsEntity.class);
        entities.add(SummaryDocumentsRequiredActionEntity.class);
        entities.add(SummaryDocumentsSendEventEntity.class);

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
