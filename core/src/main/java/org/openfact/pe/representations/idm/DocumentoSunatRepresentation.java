package org.openfact.pe.representations.idm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DocumentoSunatRepresentation {
    private String entidadTipoDeDocumento;
    private String entidadNumeroDeDocumento;
    private String entidadDenominacion;
    protected String entidadDireccion;
    private String entidadEmail;
    private String serieDocumento;
    private String numeroDocumento;
    private String monedaDocumento;
    private BigDecimal tasaDocumento;
    private boolean enviarAutomaticamenteASunat;
    private boolean enviarAutomaticamenteAlCliente;
    private String observaciones;
    private BigDecimal totalPago;
    private BigDecimal totalDocumentoSunat;
    private Date fechaDeEmision;
    private String codigoUnico;
    private List<DocumentoSunatLineRepresentation> detalle;

    public void addDetalle(DocumentoSunatLineRepresentation representation) {
        if (detalle == null) {
            detalle=new ArrayList<>();
        }
        detalle.add(representation);
    }

    public String getEntidadTipoDeDocumento() {
        return entidadTipoDeDocumento;
    }

    public void setEntidadTipoDeDocumento(String entidadTipoDeDocumento) {
        this.entidadTipoDeDocumento = entidadTipoDeDocumento;
    }

    public String getEntidadNumeroDeDocumento() {
        return entidadNumeroDeDocumento;
    }

    public void setEntidadNumeroDeDocumento(String entidadNumeroDeDocumento) {
        this.entidadNumeroDeDocumento = entidadNumeroDeDocumento;
    }

    public String getEntidadDenominacion() {
        return entidadDenominacion;
    }

    public void setEntidadDenominacion(String entidadDenominacion) {
        this.entidadDenominacion = entidadDenominacion;
    }

    public String getEntidadDireccion() {
        return entidadDireccion;
    }

    public void setEntidadDireccion(String entidadDireccion) {
        this.entidadDireccion = entidadDireccion;
    }

    public String getEntidadEmail() {
        return entidadEmail;
    }

    public void setEntidadEmail(String entidadEmail) {
        this.entidadEmail = entidadEmail;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public String getSerieDocumento() {
        return serieDocumento;
    }

    public void setSerieDocumento(String serieDocumento) {
        this.serieDocumento = serieDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getMonedaDocumento() {
        return monedaDocumento;
    }

    public void setMonedaDocumento(String monedaDocumento) {
        this.monedaDocumento = monedaDocumento;
    }

    public BigDecimal getTasaDocumento() {
        return tasaDocumento;
    }

    public void setTasaDocumento(BigDecimal tasaDocumento) {
        this.tasaDocumento = tasaDocumento;
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

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public BigDecimal getTotalPago() {
        return totalPago;
    }

    public void setTotalPago(BigDecimal totalPago) {
        this.totalPago = totalPago;
    }

    public BigDecimal getTotalDocumentoSunat() {
        return totalDocumentoSunat;
    }

    public void setTotalDocumentoSunat(BigDecimal totalDocumentoSunat) {
        this.totalDocumentoSunat = totalDocumentoSunat;
    }

    public Date getFechaDeEmision() {
        return fechaDeEmision;
    }

    public void setFechaDeEmision(Date fechaDeEmision) {
        this.fechaDeEmision = fechaDeEmision;
    }

    public String getCodigoUnico() {
        return codigoUnico;
    }

    public void setCodigoUnico(String codigoUnico) {
        this.codigoUnico = codigoUnico;
    }

    public List<DocumentoSunatLineRepresentation> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<DocumentoSunatLineRepresentation> detalle) {
        this.detalle = detalle;
    }
}
