package org.openfact.pe.representations.idm;

public class VoidedLineRepresentation {
    protected String id;
    private String tipoDocumentoRelacionado;
    private String numeroDocumentoRelacionado;
    protected String descripcionDocumentoRelacionado;
    private String fechaEmision;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipoDocumentoRelacionado() {
        return tipoDocumentoRelacionado;
    }

    public void setTipoDocumentoRelacionado(String tipoDocumentoRelacionado) {
        this.tipoDocumentoRelacionado = tipoDocumentoRelacionado;
    }

    public String getNumeroDocumentoRelacionado() {
        return numeroDocumentoRelacionado;
    }

    public void setNumeroDocumentoRelacionado(String numeroDocumentoRelacionado) {
        this.numeroDocumentoRelacionado = numeroDocumentoRelacionado;
    }

    public String getDescripcionDocumentoRelacionado() {
        return descripcionDocumentoRelacionado;
    }

    public void setDescripcionDocumentoRelacionado(String descripcionDocumentoRelacionado) {
        this.descripcionDocumentoRelacionado = descripcionDocumentoRelacionado;
    }

    public String getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }
}
