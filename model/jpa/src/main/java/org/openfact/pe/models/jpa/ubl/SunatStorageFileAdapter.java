package org.openfact.pe.models.jpa.ubl;

import org.jboss.logging.Logger;
import org.openfact.models.OpenfactSession;
import org.openfact.models.StorageFileModel;
import org.openfact.models.jpa.JpaModel;
import org.openfact.models.jpa.StorageFileAdapter;
import org.openfact.models.jpa.entities.StorageFileEntity;
import org.openfact.pe.models.jpa.entities.SunatStorageFileEntity;

import javax.persistence.EntityManager;

/**
 * Created by lxpary on 28/12/16.
 */
public class SunatStorageFileAdapter  implements StorageFileModel, JpaModel<SunatStorageFileEntity> {
    protected static final Logger logger = Logger.getLogger(StorageFileAdapter.class);

    protected SunatStorageFileEntity file;
    protected EntityManager em;
    protected OpenfactSession session;

    public SunatStorageFileAdapter(OpenfactSession session, EntityManager em, SunatStorageFileEntity file) {
        this.session = session;
        this.em = em;
        this.file = file;
    }

    public static StorageFileEntity toEntity(StorageFileModel model, EntityManager em) {
        if (model instanceof StorageFileAdapter) {
            return ((StorageFileAdapter) model).getEntity();
        }
        return em.getReference(StorageFileEntity.class, model.getId());
    }

    @Override
    public SunatStorageFileEntity getEntity() {
        return file;
    }

    @Override
    public String getId() {
        return file.getId();
    }

    @Override
    public String getFileName() {
        return file.getFileName();
    }

    @Override
    public void setFileName(String fileName) {
        file.setFileName(fileName);
    }

    @Override
    public String getMimeType() {
        return file.getMimeType();
    }

    @Override
    public void setMimeType(String mimeType) {
        file.setMimeType(mimeType);
    }

    @Override
    public byte[] getFile() {
        return file.getFile();
    }

    @Override
    public void setFile(byte[] bytes) {
        file.setFile(bytes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof StorageFileModel)) return false;

        StorageFileModel that = (StorageFileModel) o;
        return that.getId().equals(getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

}
