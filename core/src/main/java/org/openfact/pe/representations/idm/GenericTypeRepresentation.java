package org.openfact.pe.representations.idm;

import java.math.BigDecimal;

/**
 * Created by lxpary on 11/01/17.
 */
public class GenericTypeRepresentation {
    private String id;
    private String codigo;
    private String abreviatura;
    private String denominacion;
    private String grupo;
    private int length;
    private boolean afectaIgv;
    private BigDecimal valor;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public String getDenominacion() {
        return denominacion;
    }

    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isAfectaIgv() {
        return afectaIgv;
    }

    public void setAfectaIgv(boolean afectaIgv) {
        this.afectaIgv = afectaIgv;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
