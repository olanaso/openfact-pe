package org.openfact.representations.idm.ubl.common.pe;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.openfact.representations.idm.ubl.common.ExchangeRateRepresentation;

public class RetentionInformationRepresentation {
	protected String id;
	protected BigDecimal sunatRetentionAmount;
	protected LocalDateTime sunatRetentionDate;
	protected BigDecimal sunatNetTotalPaid;
	protected ExchangeRateRepresentation exchangeRate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigDecimal getSunatRetentionAmount() {
		return sunatRetentionAmount;
	}

	public void setSunatRetentionAmount(BigDecimal sunatRetentionAmount) {
		this.sunatRetentionAmount = sunatRetentionAmount;
	}

	public LocalDateTime getSunatRetentionDate() {
		return sunatRetentionDate;
	}

	public void setSunatRetentionDate(LocalDateTime sunatRetentionDate) {
		this.sunatRetentionDate = sunatRetentionDate;
	}

	public BigDecimal getSunatNetTotalPaid() {
		return sunatNetTotalPaid;
	}

	public void setSunatNetTotalPaid(BigDecimal sunatNetTotalPaid) {
		this.sunatNetTotalPaid = sunatNetTotalPaid;
	}

	public ExchangeRateRepresentation getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(ExchangeRateRepresentation exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

}
