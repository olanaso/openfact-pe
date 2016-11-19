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
@Table(name = "RETENTION_REQUIRED_ACTION")
@IdClass(RetentionRequiredActionEntity.Key.class)
public class RetentionRequiredActionEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RETENTION_ID")
    protected RetentionEntity retention;

    @Id
    @Column(name = "REQUIRED_ACTION")
    protected String action;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public RetentionEntity getRetention() {
        return retention;
    }

    public void setRetention(RetentionEntity retention) {
        this.retention = retention;
    }

    public static class Key implements Serializable {

        private static final long serialVersionUID = 1L;

        protected RetentionEntity retention;
        protected String action;

        public Key() {
        }

        public Key(RetentionEntity retention, String action) {
            this.retention = retention;
            this.action = action;
        }

        public RetentionEntity getRetention() {
            return retention;
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
            if (retention != null
                    ? !retention.getId().equals(key.retention != null ? key.retention.getId() : null)
                    : key.retention != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = retention != null ? retention.getId().hashCode() : 0;
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
        if (!(o instanceof RetentionRequiredActionEntity))
            return false;

        RetentionRequiredActionEntity key = (RetentionRequiredActionEntity) o;

        if (action != key.action)
            return false;
        if (retention != null
                ? !retention.getId().equals(key.retention != null ? key.retention.getId() : null)
                : key.retention != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = retention != null ? retention.getId().hashCode() : 0;
        result = 31 * result + (action != null ? action.hashCode() : 0);
        return result;
    }

}
