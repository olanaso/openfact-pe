package org.openfact.pe.representations.idm;

import java.math.BigDecimal;
import java.util.Date;

public class DocumentoSunatLineRepresentation {

    private String tipoDocumentoRelacionado;
    private String numeroDocumentoRelacionado;
    private Date fechaDocumentoRelacionado;
    private String monedaDocumentoRelacionado;
    private BigDecimal totalDocumentoRelacionado;

    private BigDecimal tipoCambio;
    private Date fechaCambio;
    private BigDecimal pagoDocumentoSunat;
    private String numeroPago;
    private Date fechaDocumentoSunat;
    private BigDecimal importeDocumentoSunat;
    private BigDecimal importePago;

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

    public Date getFechaDocumentoRelacionado() {
        return fechaDocumentoRelacionado;
    }

    public void setFechaDocumentoRelacionado(Date fechaDocumentoRelacionado) {
        this.fechaDocumentoRelacionado = fechaDocumentoRelacionado;
    }

    public String getMonedaDocumentoRelacionado() {
        return monedaDocumentoRelacionado;
    }

    public void setMonedaDocumentoRelacionado(String monedaDocumentoRelacionado) {
        this.monedaDocumentoRelacionado = monedaDocumentoRelacionado;
    }

    public BigDecimal getTotalDocumentoRelacionado() {
        return totalDocumentoRelacionado;
    }

    public void setTotalDocumentoRelacionado(BigDecimal totalDocumentoRelacionado) {
        this.totalDocumentoRelacionado = totalDocumentoRelacionado;
    }

    public BigDecimal getTipoCambio() {
        return tipoCambio;
    }

    public void setTipoCambio(BigDecimal tipoCambio) {
        this.tipoCambio = tipoCambio;
    }

    public Date getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(Date fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

    public BigDecimal getPagoDocumentoSunat() {
        return pagoDocumentoSunat;
    }

    public void setPagoDocumentoSunat(BigDecimal pagoDocumentoSunat) {
        this.pagoDocumentoSunat = pagoDocumentoSunat;
    }

    public String getNumeroPago() {
        return numeroPago;
    }

    public void setNumeroPago(String numeroPago) {
        this.numeroPago = numeroPago;
    }

    public Date getFechaDocumentoSunat() {
        return fechaDocumentoSunat;
    }

    public void setFechaDocumentoSunat(Date fechaDocumentoSunat) {
        this.fechaDocumentoSunat = fechaDocumentoSunat;
    }

    public BigDecimal getImporteDocumentoSunat() {
        return importeDocumentoSunat;
    }

    public void setImporteDocumentoSunat(BigDecimal importeDocumentoSunat) {
        this.importeDocumentoSunat = importeDocumentoSunat;
    }

    public BigDecimal getImportePago() {
        return importePago;
    }

    public void setImportePago(BigDecimal importePago) {
        this.importePago = importePago;
    }
}
