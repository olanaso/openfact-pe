package org.openfact.pe.services.constants;

public enum SunatEventType {
	PERCEPTION(true),

	RETENTION(true),

	SUMMARY_DOCUMENT(true),
	
	VOIDED_DOCUMENT(true);

	private boolean saveByDefault;

	SunatEventType(boolean saveByDefault) {
		this.saveByDefault = saveByDefault;
	}

	public boolean isSaveByDefault() {
		return saveByDefault;
	}
}
