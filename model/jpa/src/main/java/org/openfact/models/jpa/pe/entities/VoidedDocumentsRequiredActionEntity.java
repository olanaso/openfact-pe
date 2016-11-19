package org.openfact.models.jpa.pe.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "VOIDED_DOCUMENT_REQUIRED_ACTION")
@IdClass(VoidedDocumentsRequiredActionEntity.Key.class)
public class VoidedDocumentsRequiredActionEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VOIDED_DOCUMENT_ID")
    protected VoidedDocumentsEntity voidedDocument;

    @Id
    @Column(name = "REQUIRED_ACTION")
    protected String action;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public VoidedDocumentsEntity getVoidedDocument() {
        return voidedDocument;
    }

    public void setVoidedDocument(VoidedDocumentsEntity voidedDocument) {
        this.voidedDocument = voidedDocument;
    }

    public static class Key implements Serializable {

        private static final long serialVersionUID = 1L;

        protected VoidedDocumentsEntity voidedDocument;
        protected String action;

        public Key() {
        }

        public Key(VoidedDocumentsEntity voidedDocument, String action) {
            this.voidedDocument = voidedDocument;
            this.action = action;
        }

        public VoidedDocumentsEntity getVoidedDocument() {
            return voidedDocument;
        }

        public String getAction() {
            return action;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            Key key = (Key) o;

            if (action != key.action)
                return false;
            if (voidedDocument != null
                    ? !voidedDocument.getId().equals(key.voidedDocument != null ? key.voidedDocument.getId() : null)
                    : key.voidedDocument != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = voidedDocument != null ? voidedDocument.getId().hashCode() : 0;
            result = 31 * result + (action != null ? action.hashCode() : 0);
            return result;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (!(o instanceof VoidedDocumentsRequiredActionEntity))
            return false;

        VoidedDocumentsRequiredActionEntity key = (VoidedDocumentsRequiredActionEntity) o;

        if (action != key.action)
            return false;
        if (voidedDocument != null
                ? !voidedDocument.getId().equals(key.voidedDocument != null ? key.voidedDocument.getId() : null)
                : key.voidedDocument != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = voidedDocument != null ? voidedDocument.getId().hashCode() : 0;
        result = 31 * result + (action != null ? action.hashCode() : 0);
        return result;
    }

}
