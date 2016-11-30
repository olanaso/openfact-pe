package org.openfact.pe.representations.idm;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DocumentReferenceRepresentation {
	protected String tipoDocumentRelacionado;
	protected String numeroDocumentRelacionado;
	protected LocalDateTime fechaDeDocumentoRelacionado;
	protected BigDecimal totalDocumentoRelacionado;
	protected String monedaDocumentRelacionado;
	protected LocalDateTime fechaPago;
	protected String numeroPago;
	protected BigDecimal totalPago;
	protected String monedaPago;
	protected BigDecimal tipoDeCambio;

	public String getTipoDocumentRelacionado() {
		return tipoDocumentRelacionado;
	}

	public void setTipoDocumentRelacionado(String tipoDocumentRelacionado) {
		this.tipoDocumentRelacionado = tipoDocumentRelacionado;
	}

	public String getNumeroDocumentRelacionado() {
		return numeroDocumentRelacionado;
	}

	public void setNumeroDocumentRelacionado(String numeroDocumentRelacionado) {
		this.numeroDocumentRelacionado = numeroDocumentRelacionado;
	}

	public LocalDateTime getFechaDeDocumentoRelacionado() {
		return fechaDeDocumentoRelacionado;
	}

	public void setFechaDeDocumentoRelacionado(LocalDateTime fechaDeDocumentoRelacionado) {
		this.fechaDeDocumentoRelacionado = fechaDeDocumentoRelacionado;
	}

	public BigDecimal getTotalDocumentoRelacionado() {
		return totalDocumentoRelacionado;
	}

	public void setTotalDocumentoRelacionado(BigDecimal totalDocumentoRelacionado) {
		this.totalDocumentoRelacionado = totalDocumentoRelacionado;
	}

	public String getMonedaDocumentRelacionado() {
		return monedaDocumentRelacionado;
	}

	public void setMonedaDocumentRelacionado(String monedaDocumentRelacionado) {
		this.monedaDocumentRelacionado = monedaDocumentRelacionado;
	}

	public LocalDateTime getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(LocalDateTime fechaPago) {
		this.fechaPago = fechaPago;
	}

	public String getNumeroPago() {
		return numeroPago;
	}

	public void setNumeroPago(String numeroPago) {
		this.numeroPago = numeroPago;
	}

	public BigDecimal getTotalPago() {
		return totalPago;
	}

	public void setTotalPago(BigDecimal totalPago) {
		this.totalPago = totalPago;
	}

	public String getMonedaPago() {
		return monedaPago;
	}

	public void setMonedaPago(String monedaPago) {
		this.monedaPago = monedaPago;
	}

	public BigDecimal getTipoDeCambio() {
		return tipoDeCambio;
	}

	public void setTipoDeCambio(BigDecimal tipoDeCambio) {
		this.tipoDeCambio = tipoDeCambio;
	}
}
