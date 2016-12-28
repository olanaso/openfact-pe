package org.openfact.pe.models.jpa.ubl;

import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.jpa.JpaModel;
import org.openfact.models.jpa.StorageFileAdapter;
import javax.persistence.EntityManager;

import org.jboss.logging.Logger;
import org.openfact.models.*;
import org.openfact.pe.models.jpa.entities.SunatSendEventEntity;
import org.openfact.pe.models.jpa.entities.SunatStorageFileEntity;
import org.openfact.ubl.SendEventModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lxpary on 28/12/16.
 */
public class SunatSendEventAdapter implements SendEventModel, JpaModel<SunatSendEventEntity> {
    protected static final Logger logger = Logger.getLogger(SunatSendEventAdapter.class);
    protected OrganizationModel organization;
    protected SunatSendEventEntity sendEvent;
    protected EntityManager em;
    protected OpenfactSession session;

    public SunatSendEventAdapter(OpenfactSession session, OrganizationModel organization, EntityManager em, SunatSendEventEntity sendEvent) {
        this.session = session;
        this.em = em;
        this.organization = organization;
        this.sendEvent = sendEvent;
    }

    public static SunatSendEventEntity toEntity(SendEventModel model, EntityManager em) {
        if (model instanceof SunatSendEventAdapter) {
            return ((SunatSendEventAdapter) model).getEntity();
        }
        return em.getReference(SunatSendEventEntity.class, model.getId());
    }

    @Override
    public SunatSendEventEntity getEntity() {
        return sendEvent;
    }

    @Override
    public String getId() {
        return sendEvent.getId();
    }

    @Override
    public boolean getResult() {
        return sendEvent.isResult();
    }

    @Override
    public void setResult(boolean result) {
        sendEvent.setResult(result);
    }

    @Override
    public String getDescription() {
        return sendEvent.getDescription();
    }

    @Override
    public void setDescription(String description) {
        sendEvent.setDescription(description);
    }

    @Override
    public OrganizationModel getOrganization() {
        return organization;
    }

    @Override
    public List<StorageFileModel> getFileAttatchments() {
        List<StorageFileModel> files = new ArrayList<>();
        for (Map.Entry<String, SunatStorageFileEntity> entry : sendEvent.getFileAttatchments().entrySet()) {
            files.add(new SunatStorageFileAdapter(session, em, entry.getValue()));
        }
        return files;
    }

    @Override
    public StorageFileModel addFileAttatchments(FileModel file) {
        SunatStorageFileEntity entity = new SunatStorageFileEntity();
        entity.setFileName(file.getFileName());
        entity.setMimeType(file.getMimeType());
        entity.setFile(file.getFile());
        em.persist(entity);
        em.flush();

        sendEvent.getFileAttatchments().put(entity.getId(), entity);
        return new SunatStorageFileAdapter(session, em, entity);
    }

    @Override
    public String getType() {
        return sendEvent.getType();
    }

    @Override
    public void setType(String type) {
        sendEvent.setType(type);
    }

    @Override
    public Map<String, String> getDestity() {
        return sendEvent.getDestiny();
    }

    @Override
    public void setDestiny(Map<String, String> destiny) {
        sendEvent.setDestiny(destiny);
    }

    @Override
    public LocalDateTime getCreatedTimestamp() {
        return sendEvent.getCreatedTimestamp();
    }

    @Override
    public List<StorageFileModel> getFileResponseAttatchments() {
        List<StorageFileModel> files = new ArrayList<>();
        for (Map.Entry<String, SunatStorageFileEntity> entry : sendEvent.getFileResponseAttatchments().entrySet()) {
            files.add(new SunatStorageFileAdapter(session, em, entry.getValue()));
        }
        return files;
    }

    @Override
    public StorageFileModel addFileResponseAttatchments(FileModel file) {
        SunatStorageFileEntity entity = new SunatStorageFileEntity();
        entity.setFileName(file.getFileName());
        entity.setMimeType(file.getMimeType());
        entity.setFile(file.getFile());
        em.persist(entity);
        em.flush();

        sendEvent.getFileResponseAttatchments().put(entity.getId(), entity);
        return new SunatStorageFileAdapter(session, em, entity);
    }

    @Override
    public Map<String, String> getResponse() {
        return sendEvent.getResponse();
    }

    @Override
    public void setResponse(Map<String, String> response) {
        sendEvent.setResponse(response);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof SendEventModel)) return false;

        SendEventModel that = (SendEventModel) o;
        return that.getId().equals(getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

}
