package org.openfact.models.jpa.pe.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "BAJA_ACCION_REQUERIDA")
@IdClass(BajaAccionRequeridaEntity.Key.class)
public class BajaAccionRequeridaEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BAJA_ID")
    protected BajaAccionRequeridaEntity baja;

    @Id
    @Column(name = "ACCION_REQUERIDA")
    protected String action;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public BajaAccionRequeridaEntity getBaja() {
        return baja;
    }

    public void setInvoice(BajaAccionRequeridaEntity baja) {
        this.baja = baja;
    }

    public static class Key implements Serializable {

        protected BajaAccionRequeridaEntity baja;

        protected String action;

        public Key() {
        }

        public Key(BajaAccionRequeridaEntity baja, String action) {
            this.baja = baja;
            this.action = action;
        }

        public BajaAccionRequeridaEntity getBaja() {
            return baja;
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
            if (baja != null ? !baja.getId().equals(key.baja != null ? key.baja.getId() : null)
                    : key.baja != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = baja != null ? baja.getId().hashCode() : 0;
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
        if (!(o instanceof BajaAccionRequeridaEntity))
            return false;

        BajaAccionRequeridaEntity key = (BajaAccionRequeridaEntity) o;

        if (action != key.action)
            return false;
        if (baja != null ? !baja.getId().equals(key.baja != null ? key.baja.getId() : null)
                : key.baja != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = baja != null ? baja.getId().hashCode() : 0;
        result = 31 * result + (action != null ? action.hashCode() : 0);
        return result;
    }

}
