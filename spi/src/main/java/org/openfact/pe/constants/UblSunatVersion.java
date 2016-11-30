package org.openfact.pe.constants;

public enum UblSunatVersion {
	VERSION_ID("2.0"), CUSTOMIZATION_ID("1.0"), ID_SEPARATOR("-");

	private final String codigo;

	public String getCodigo() {
		return codigo;
	}

	private UblSunatVersion(String codigo) {
		this.codigo = codigo;
	}
}
