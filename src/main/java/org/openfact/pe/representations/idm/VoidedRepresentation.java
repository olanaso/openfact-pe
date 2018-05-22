package org.openfact.pe.representations.idm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class VoidedRepresentation {

    private String serieDocumento;
    private String numeroDocumento;
    private Date fechaDeEmision;
    protected boolean enviarAutomaticamenteASunat;
    protected boolean enviarAutomaticamenteAlCliente;
    protected String codigoUnico;
    private String observaciones;
    protected Set<String> requiredActions;
    protected List<VoidedLineRepresentation> detalle;

    public String getSerieDocumento() {
        return serieDocumento;
    }

    public void setSerieDocumento(String serieDocumento) {
        this.serieDocumento = serieDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public Date getFechaDeEmision() {
        return fechaDeEmision;
    }

    public void setFechaDeEmision(Date fechaDeEmision) {
        this.fechaDeEmision = fechaDeEmision;
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

    public List<VoidedLineRepresentation> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<VoidedLineRepresentation> detalle) {
        this.detalle = detalle;
    }

    public void addDetalle(VoidedLineRepresentation representation) {
        if (detalle == null) {
            detalle = new ArrayList<>();
        }
        detalle.add(representation);
    }

    public Set<String> getRequiredActions() {
        return requiredActions;
    }

    public void setRequiredActions(Set<String> requiredActions) {
        this.requiredActions = requiredActions;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
