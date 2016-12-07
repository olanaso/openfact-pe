package org.openfact.pe.representations.idm;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RetentionRepresentation {
	protected String tipo;
	protected String serie;
	protected String numero;
	protected String entidadNumeroDeDocumento;
	protected String entidadTipoDeDocumento;
	protected String entidadDenominacion;
	protected String entidadDireccion;
	protected String entidadEmail;
	protected LocalDateTime fechaDeEmision;
	protected String moneda;
	protected String codigoDocumento;
	protected BigDecimal tasaDocumento;
	protected String observaciones;
	protected boolean enviarAutomaticamenteASunat;
	protected boolean enviarAutomaticamenteAlCliente;
	protected boolean cancelado;
	protected String codigoUnico;
	List<DocumentReferenceRepresentation> reference;

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

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

	public String getEntidadNumeroDeDocumento() {
		return entidadNumeroDeDocumento;
	}

	public void setEntidadNumeroDeDocumento(String entidadNumeroDeDocumento) {
		this.entidadNumeroDeDocumento = entidadNumeroDeDocumento;
	}

	public String getEntidadTipoDeDocumento() {
		return entidadTipoDeDocumento;
	}

	public void setEntidadTipoDeDocumento(String entidadTipoDeDocumento) {
		this.entidadTipoDeDocumento = entidadTipoDeDocumento;
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

	public LocalDateTime getFechaDeEmision() {
		return fechaDeEmision;
	}

	public void setFechaDeEmision(LocalDateTime fechaDeEmision) {
		this.fechaDeEmision = fechaDeEmision;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
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

	public boolean isCancelado() {
		return cancelado;
	}

	public void setCancelado(boolean cancelado) {
		this.cancelado = cancelado;
	}

	public String getCodigoUnico() {
		return codigoUnico;
	}

	public void setCodigoUnico(String codigoUnico) {
		this.codigoUnico = codigoUnico;
	}

	public List<DocumentReferenceRepresentation> getReference() {
		return reference;
	}

	public void setReference(List<DocumentReferenceRepresentation> reference) {
		this.reference = reference;
	}

	public String getCodigoDocumento() {
		return codigoDocumento;
	}

	public void setCodigoDocumento(String codigoDocumento) {
		this.codigoDocumento = codigoDocumento;
	}

	public BigDecimal getTasaDocumento() {
		return tasaDocumento;
	}

	public void setTasaDocumento(BigDecimal tasaDocumento) {
		this.tasaDocumento = tasaDocumento;
	}

	public void addDocumentReference(DocumentReferenceRepresentation representation) {
		if (reference == null) {
			reference = new ArrayList<>();
		}
		reference.add(representation);
	}
}
