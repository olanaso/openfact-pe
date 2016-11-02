package org.openfact.representations.idm.ubl.common.pe;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.openfact.representations.idm.ubl.common.ExchangeRateRepresentation;

public class PerceptionInformationRepresentation {
	protected String id;
	protected BigDecimal sunatPerceptionAmount;
	protected LocalDateTime sunatPerceptionDate;
	protected BigDecimal sunatNetTotalPaid;
	protected ExchangeRateRepresentation exchangeRate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigDecimal getSunatPerceptionAmount() {
		return sunatPerceptionAmount;
	}

	public void setSunatPerceptionAmount(BigDecimal sunatPerceptionAmount) {
		this.sunatPerceptionAmount = sunatPerceptionAmount;
	}

	public LocalDateTime getSunatPerceptionDate() {
		return sunatPerceptionDate;
	}

	public void setSunatPerceptionDate(LocalDateTime sunatPerceptionDate) {
		this.sunatPerceptionDate = sunatPerceptionDate;
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
