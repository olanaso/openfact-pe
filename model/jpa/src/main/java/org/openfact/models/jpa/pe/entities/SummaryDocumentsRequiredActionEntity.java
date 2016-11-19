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
@IdClass(SummaryDocumentsRequiredActionEntity.Key.class)
public class SummaryDocumentsRequiredActionEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VOIDED_DOCUMENT_ID")
    protected SummaryDocumentsEntity summaryDocument;

    @Id
    @Column(name = "REQUIRED_ACTION")
    protected String action;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public SummaryDocumentsEntity getSummaryDocument() {
        return summaryDocument;
    }

    public void setSummaryDocument(SummaryDocumentsEntity summaryDocument) {
        this.summaryDocument = summaryDocument;
    }

    public static class Key implements Serializable {

        private static final long serialVersionUID = 1L;

        protected SummaryDocumentsEntity summaryDocument;
        protected String action;

        public Key() {
        }

        public Key(SummaryDocumentsEntity summaryDocument, String action) {
            this.summaryDocument = summaryDocument;
            this.action = action;
        }

        public SummaryDocumentsEntity getSummaryDocument() {
            return summaryDocument;
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
            if (summaryDocument != null
                    ? !summaryDocument.getId().equals(key.summaryDocument != null ? key.summaryDocument.getId() : null)
                    : key.summaryDocument != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = summaryDocument != null ? summaryDocument.getId().hashCode() : 0;
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
        if (!(o instanceof SummaryDocumentsRequiredActionEntity))
            return false;

        SummaryDocumentsRequiredActionEntity key = (SummaryDocumentsRequiredActionEntity) o;

        if (action != key.action)
            return false;
        if (summaryDocument != null
                ? !summaryDocument.getId().equals(key.summaryDocument != null ? key.summaryDocument.getId() : null)
                : key.summaryDocument != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = summaryDocument != null ? summaryDocument.getId().hashCode() : 0;
        result = 31 * result + (action != null ? action.hashCode() : 0);
        return result;
    }

}
