package org.openfact.pe.representations.idm;

import java.math.BigDecimal;

public class GenericTypeRepresentation {

    private String id;
    private String codigo;
    private String abreviatura;
    private String denominacion;
    private String grupo;
    private Integer length;
    private Boolean afectaIgv;
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

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Boolean getAfectaIgv() {
        return afectaIgv;
    }

    public void setAfectaIgv(Boolean afectaIgv) {
        this.afectaIgv = afectaIgv;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
