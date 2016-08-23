package org.openfact.models.jpa;

import javax.persistence.EntityManager;

import org.jboss.logging.Logger;
import org.openfact.models.DocumentSimpleModel;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.enums.DocumentType;
import org.openfact.models.jpa.entities.DocumentSimpleEntity;

public class DocumentSimpleAdapter implements DocumentSimpleModel, JpaModel<DocumentSimpleEntity> {

    protected static final Logger logger = Logger.getLogger(DocumentSimpleAdapter.class);

    protected OrganizationModel organization;
    protected DocumentSimpleEntity document;
    protected EntityManager em;
    protected OpenfactSession session;

    public DocumentSimpleAdapter(OrganizationModel organization, OpenfactSession session, EntityManager em,
            DocumentSimpleEntity document) {
        this.organization = organization;
        this.session = session;
        this.em = em;
        this.document = document;
    }

    @Override
    public DocumentSimpleEntity getEntity() {
        return document;
    }

    public static DocumentSimpleEntity toEntity(DocumentSimpleModel model, EntityManager em) {
        if (model instanceof DocumentSimpleAdapter) {
            return ((DocumentSimpleAdapter) model).getEntity();
        }
        return em.getReference(DocumentSimpleEntity.class, model.getId());
    }

    @Override
    public String getId() {
        return document.getId();
    }

    @Override
    public String getName() {
        return document.getName();
    }

    @Override
    public void setName(String name) {
        document.setName(name);
    }

    @Override
    public String getDocumentId() {
        return document.getDocumentId();
    }

    @Override
    public void setDocumentId(String documentId) {
        document.setDocumentId(documentId);
    }

    @Override
    public String getDescription() {
        return document.getDescription();
    }

    @Override
    public void setDescription(String description) {
        document.setDescription(description);
    }

    @Override
    public DocumentType getType() {
        return document.getType();
    }

    @Override
    public void setType(DocumentType type) {
        document.setType(type);
    }

    @Override
    public String getCode() {
        return document.getCode();
    }

    @Override
    public void setCode(String code) {
        document.setCode(code);
    }

    @Override
    public OrganizationModel getOrganization() {
        return organization;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((document == null) ? 0 : document.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DocumentSimpleAdapter other = (DocumentSimpleAdapter) obj;
        if (document == null) {
            if (other.document != null)
                return false;
        } else if (!document.equals(other.document))
            return false;
        return true;
    }

}