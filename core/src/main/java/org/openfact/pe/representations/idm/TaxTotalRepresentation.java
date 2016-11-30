package org.openfact.pe.representations.idm;

import java.math.BigDecimal;

public class TaxTotalRepresentation {
	protected BigDecimal taxAmount;
	protected String taxSchemeID;
	protected String taxSchemeName;
	protected String taxTypeCode;

	public BigDecimal getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}

	public String getTaxSchemeID() {
		return taxSchemeID;
	}

	public void setTaxSchemeID(String taxSchemeID) {
		this.taxSchemeID = taxSchemeID;
	}

	public String getTaxSchemeName() {
		return taxSchemeName;
	}

	public void setTaxSchemeName(String taxSchemeName) {
		this.taxSchemeName = taxSchemeName;
	}

	public String getTaxTypeCode() {
		return taxTypeCode;
	}

	public void setTaxTypeCode(String taxTypeCode) {
		this.taxTypeCode = taxTypeCode;
	}

}
