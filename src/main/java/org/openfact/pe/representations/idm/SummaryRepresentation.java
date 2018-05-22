package org.openfact.pe.representations.idm;

import java.time.LocalDateTime;
import java.util.List;

public class SummaryRepresentation {

    protected String serie;
    protected String numero;
    protected LocalDateTime fechaDeEmision;
    protected LocalDateTime fechaDeReferencia;
    protected boolean enviarAutomaticamenteASunat;
    protected boolean enviarAutomaticamenteAlCliente;
    protected String codigoUnico;
    protected List<SummaryLineRepresentation> line;

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDateTime getFechaDeEmision() {
        return fechaDeEmision;
    }

    public void setFechaDeEmision(LocalDateTime fechaDeEmision) {
        this.fechaDeEmision = fechaDeEmision;
    }

    public LocalDateTime getFechaDeReferencia() {
        return fechaDeReferencia;
    }

    public void setFechaDeReferencia(LocalDateTime fechaDeReferencia) {
        this.fechaDeReferencia = fechaDeReferencia;
    }

    public boolean isEnviarAutomaticamenteASunat() {
        return enviarAutomaticamenteASunat;
    }

    public void setEnviarAutomaticamenteASunat(boolean enviarAutomaticamenteASunat) {
        this.enviarAutomaticamenteASunat = enviarAutomaticamenteASunat;
    }

    public boolean isEnviarAutomaticamenteAlCliente() {
        return enviarAutomaticamenteAlCliente;
    }

    public void setEnviarAutomaticamenteAlCliente(boolean enviarAutomaticamenteAlCliente) {
        this.enviarAutomaticamenteAlCliente = enviarAutomaticamenteAlCliente;
    }

    public String getCodigoUnico() {
        return codigoUnico;
    }

    public void setCodigoUnico(String codigoUnico) {
        this.codigoUnico = codigoUnico;
    }

    public List<SummaryLineRepresentation> getLine() {
        return line;
    }

    public void setLine(List<SummaryLineRepresentation> line) {
        this.line = line;
    }

}
