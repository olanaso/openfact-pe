package org.openfact.models.jpa.pe.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "RESUMEN_DIARIO_ACCION_REQUERIDA")
@IdClass(ResumenDiarioAccionRequeridaEntity.Key.class)
public class ResumenDiarioAccionRequeridaEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RESUMEN_DIARIO_ID")
    protected ResumenDiarioAccionRequeridaEntity resumenDiario;

    @Id
    @Column(name = "ACCION_REQUERIDA")
    protected String action;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public ResumenDiarioAccionRequeridaEntity getResumenDiario() {
        return resumenDiario;
    }

    public void setInvoice(ResumenDiarioAccionRequeridaEntity resumenDiario) {
        this.resumenDiario = resumenDiario;
    }

    public static class Key implements Serializable {

        protected ResumenDiarioAccionRequeridaEntity resumenDiario;

        protected String action;

        public Key() {
        }

        public Key(ResumenDiarioAccionRequeridaEntity resumenDiario, String action) {
            this.resumenDiario = resumenDiario;
            this.action = action;
        }

        public ResumenDiarioAccionRequeridaEntity getResumenDiario() {
            return resumenDiario;
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
            if (resumenDiario != null
                    ? !resumenDiario.getId().equals(key.resumenDiario != null ? key.resumenDiario.getId() : null)
                    : key.resumenDiario != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = resumenDiario != null ? resumenDiario.getId().hashCode() : 0;
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
        if (!(o instanceof ResumenDiarioAccionRequeridaEntity))
            return false;

        ResumenDiarioAccionRequeridaEntity key = (ResumenDiarioAccionRequeridaEntity) o;

        if (action != key.action)
            return false;
        if (resumenDiario != null
                ? !resumenDiario.getId().equals(key.resumenDiario != null ? key.resumenDiario.getId() : null)
                : key.resumenDiario != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = resumenDiario != null ? resumenDiario.getId().hashCode() : 0;
        result = 31 * result + (action != null ? action.hashCode() : 0);
        return result;
    }

}
