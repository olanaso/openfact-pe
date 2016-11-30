package org.openfact.pe.representations.idm;

import java.time.LocalDateTime;
import java.util.List;

public class VoidedRepresentation {
	protected String serie;
	protected String numero;
	protected LocalDateTime fechaDeDocumento;
	protected LocalDateTime fechaReferencia;
	protected boolean enviarAutomaticamenteASunat;
	protected boolean enviarAutomaticamenteAlCliente;
	protected String codigoUnico;
	protected List<VoidedLineRepresentation> line;

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

	public LocalDateTime getFechaDeDocumento() {
		return fechaDeDocumento;
	}

	public void setFechaDeDocumento(LocalDateTime fechaDeDocumento) {
		this.fechaDeDocumento = fechaDeDocumento;
	}

	public LocalDateTime getFechaReferencia() {
		return fechaReferencia;
	}

	public void setFechaReferencia(LocalDateTime fechaReferencia) {
		this.fechaReferencia = fechaReferencia;
	}

	public List<VoidedLineRepresentation> getLine() {
		return line;
	}

	public void setLine(List<VoidedLineRepresentation> line) {
		this.line = line;
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

}
