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
@Table(name = "PERCEPTION_REQUIRED_ACTION")
@IdClass(PerceptionRequiredActionEntity.Key.class)
public class PerceptionRequiredActionEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PERCEPTION_ID")
    protected PerceptionEntity perception;

    @Id
    @Column(name = "REQUIRED_ACTION")
    protected String action;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public PerceptionEntity getPerception() {
        return perception;
    }

    public void setPerception(PerceptionEntity perception) {
        this.perception = perception;
    }

    public static class Key implements Serializable {

        private static final long serialVersionUID = 1L;

        protected PerceptionEntity perception;
        protected String action;

        public Key() {
        }

        public Key(PerceptionEntity perception, String action) {
            this.perception = perception;
            this.action = action;
        }

        public PerceptionEntity getPerception() {
            return perception;
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
            if (perception != null
                    ? !perception.getId().equals(key.perception != null ? key.perception.getId() : null)
                    : key.perception != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = perception != null ? perception.getId().hashCode() : 0;
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
        if (!(o instanceof PerceptionRequiredActionEntity))
            return false;

        PerceptionRequiredActionEntity key = (PerceptionRequiredActionEntity) o;

        if (action != key.action)
            return false;
        if (perception != null
                ? !perception.getId().equals(key.perception != null ? key.perception.getId() : null)
                : key.perception != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = perception != null ? perception.getId().hashCode() : 0;
        result = 31 * result + (action != null ? action.hashCode() : 0);
        return result;
    }

}
