package org.openfact.pe.constants;

public enum UblSunatConfiguration {
	VERSION_ID("2.0"), CUSTOMIZATION_ID("1.0"), ID_SEPARATOR("-"),ID_SIGN("IDSign"),URI_SIGN("#signature");

	private final String codigo;

	public String getCodigo() {
		return codigo;
	}

	private UblSunatConfiguration(String codigo) {
		this.codigo = codigo;
	}
}
