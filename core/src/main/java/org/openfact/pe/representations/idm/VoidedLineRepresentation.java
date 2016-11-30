package org.openfact.pe.representations.idm;

public class VoidedLineRepresentation {
	protected String lineID;
	protected String codigoDocumento;
	protected String serieDocumento;
	protected String numeroDocumento;
	protected String descripcion;

	public String getLineID() {
		return lineID;
	}

	public void setLineID(String lineID) {
		this.lineID = lineID;
	}

	public String getCodigoDocumento() {
		return codigoDocumento;
	}

	public void setCodigoDocumento(String codigoDocumento) {
		this.codigoDocumento = codigoDocumento;
	}

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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
