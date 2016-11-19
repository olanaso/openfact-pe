package org.openfact.models.jpa.pe;

import java.util.ArrayList;
import java.util.List;

import org.openfact.connections.jpa.entityprovider.JpaEntityProvider;
import org.openfact.models.jpa.pe.entities.PerceptionDocumentReferenceEntity;
import org.openfact.models.jpa.pe.entities.PerceptionEntity;
import org.openfact.models.jpa.pe.entities.PerceptionInformationEntity;
import org.openfact.models.jpa.pe.entities.PerceptionRequiredActionEntity;
import org.openfact.models.jpa.pe.entities.RetentionEntity;
import org.openfact.models.jpa.pe.entities.RetentionRequiredActionEntity;
import org.openfact.models.jpa.pe.entities.SummaryDocumentsEntity;
import org.openfact.models.jpa.pe.entities.SummaryDocumentsRequiredActionEntity;
import org.openfact.models.jpa.pe.entities.SunatRetencionInformationEntity;
import org.openfact.models.jpa.pe.entities.SunatRetentionDocumentReferenceEntity;
import org.openfact.models.jpa.pe.entities.VoidedDocumentsEntity;
import org.openfact.models.jpa.pe.entities.VoidedDocumentsLineEntity;
import org.openfact.models.jpa.pe.entities.VoidedDocumentsRequiredActionEntity;

public class JpaEntityProvider_PE implements JpaEntityProvider {

    @Override
    public List<Class<?>> getEntities() {
        List<Class<?>> entities = new ArrayList<>();
        entities.add(PerceptionEntity.class);
        entities.add(PerceptionDocumentReferenceEntity.class);
        entities.add(PerceptionInformationEntity.class);
        entities.add(PerceptionRequiredActionEntity.class);

        entities.add(RetentionEntity.class);
        entities.add(RetentionRequiredActionEntity.class);
        entities.add(SunatRetencionInformationEntity.class);
        entities.add(SunatRetentionDocumentReferenceEntity.class);

        entities.add(SummaryDocumentsEntity.class);
        entities.add(SummaryDocumentsRequiredActionEntity.class);

        entities.add(VoidedDocumentsEntity.class);
        entities.add(VoidedDocumentsLineEntity.class);
        entities.add(VoidedDocumentsRequiredActionEntity.class);

        return entities;
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
