package org.openfact.pe.representations.idm;

import java.math.BigDecimal;
import java.util.List;

public class SummaryLineRepresentation {
	protected String lineID;
	protected String codigoDocumento;
	protected String serieDocumento;
	protected String numeroInicioDocumento;
	protected String numeroFinDocumento;
	protected BigDecimal totalAmount;
	protected String moneda;
	protected boolean chargeIndicator;
	protected BigDecimal chargeAmount;
	protected List<BillingPaymentRepresentation> payment;
	protected List<TaxTotalRepresentation> taxTotal;

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

	public String getNumeroInicioDocumento() {
		return numeroInicioDocumento;
	}

	public void setNumeroInicioDocumento(String numeroInicioDocumento) {
		this.numeroInicioDocumento = numeroInicioDocumento;
	}

	public String getNumeroFinDocumento() {
		return numeroFinDocumento;
	}

	public void setNumeroFinDocumento(String numeroFinDocumento) {
		this.numeroFinDocumento = numeroFinDocumento;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public boolean isChargeIndicator() {
		return chargeIndicator;
	}

	public void setChargeIndicator(boolean chargeIndicator) {
		this.chargeIndicator = chargeIndicator;
	}

	public BigDecimal getChargeAmount() {
		return chargeAmount;
	}

	public void setChargeAmount(BigDecimal chargeAmount) {
		this.chargeAmount = chargeAmount;
	}

	public List<BillingPaymentRepresentation> getPayment() {
		return payment;
	}

	public void setPayment(List<BillingPaymentRepresentation> payment) {
		this.payment = payment;
	}

	public List<TaxTotalRepresentation> getTaxTotal() {
		return taxTotal;
	}

	public void setTaxTotal(List<TaxTotalRepresentation> taxTotal) {
		this.taxTotal = taxTotal;
	}

}
