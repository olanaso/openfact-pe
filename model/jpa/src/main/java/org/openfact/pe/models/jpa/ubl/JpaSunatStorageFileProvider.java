package org.openfact.pe.models.jpa.ubl;

import org.jboss.logging.Logger;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.StorageFileModel;
import org.openfact.models.StorageFileProvider;
import org.openfact.models.jpa.AbstractHibernateStorage;
import org.openfact.models.jpa.JpaStorageFileProvider;
import org.openfact.models.jpa.StorageFileAdapter;
import org.openfact.models.jpa.entities.StorageFileEntity;
import org.openfact.pe.models.SunatStorageFileProvider;
import org.openfact.pe.models.jpa.entities.SunatStorageFileEntity;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lxpary on 28/12/16.
 */
public class JpaSunatStorageFileProvider extends AbstractHibernateStorage implements SunatStorageFileProvider {
    protected static final Logger logger = Logger.getLogger(JpaSunatStorageFileProvider.class);

    private static final String FILE_NAME = "fileName";

    private final OpenfactSession session;
    protected EntityManager em;

    public JpaSunatStorageFileProvider(OpenfactSession session, EntityManager em) {
        this.session = session;
        this.em = em;
    }

    @Override
    public StorageFileModel createFile(OrganizationModel organization, String fileName, byte[] file) {
        SunatStorageFileEntity entity = new SunatStorageFileEntity();
        entity.setFileName(fileName);
        entity.setFile(file);
        em.persist(file);
        em.flush();

        return new SunatStorageFileAdapter(session, em, entity);
    }

    @Override
    public StorageFileModel getFileById(OrganizationModel organization, String id) {
        SunatStorageFileEntity entity = em.find(SunatStorageFileEntity.class, id);
        if(entity != null) {
            return new SunatStorageFileAdapter(session, em, entity);
        }
        return null;
    }

    @Override
    public List<StorageFileModel> getFiles(OrganizationModel organization) {
        return getFiles(organization, -1, -1);
    }

    @Override
    public List<StorageFileModel> getFiles(OrganizationModel organization, Integer firstResult, Integer maxResults) {
        TypedQuery<SunatStorageFileEntity> query = em.createNamedQuery("getAllSunatStorageFiles", SunatStorageFileEntity.class);
        if (firstResult != -1) {
            query.setFirstResult(firstResult);
        }
        if (maxResults != -1) {
            query.setMaxResults(maxResults);
        }

        List<SunatStorageFileEntity> entities = query.getResultList();
        return entities.stream().map(f -> new SunatStorageFileAdapter(session, em, f)).collect(Collectors.toList());
    }

    @Override
    public boolean removeFile(OrganizationModel organization, String id) {
        StorageFileEntity entity = em.find(StorageFileEntity.class, id);
        em.remove(entity);
        return true;
    }

    @Override
    public boolean removeFile(OrganizationModel organization, StorageFileModel file) {
        return removeFile(organization, file.getId());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public void close() {

    }
}
