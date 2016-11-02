package org.openfact.representations.idm.ubl.common;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class StatusRepresentation {
	protected String id;
	protected String conditionCode;
	protected LocalDate referenceDate;
	protected LocalTime referenceTime;
	protected String description;
	protected String statusReasonCode;
	protected String statusReason;
	protected String sequenceID;
	protected String text;
	protected boolean indicationIndicator;
	protected BigDecimal percent;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getConditionCode() {
		return conditionCode;
	}

	public void setConditionCode(String conditionCode) {
		this.conditionCode = conditionCode;
	}

	public LocalDate getReferenceDate() {
		return referenceDate;
	}

	public void setReferenceDate(LocalDate referenceDate) {
		this.referenceDate = referenceDate;
	}

	public LocalTime getReferenceTime() {
		return referenceTime;
	}

	public void setReferenceTime(LocalTime referenceTime) {
		this.referenceTime = referenceTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatusReasonCode() {
		return statusReasonCode;
	}

	public void setStatusReasonCode(String statusReasonCode) {
		this.statusReasonCode = statusReasonCode;
	}

	public String getStatusReason() {
		return statusReason;
	}

	public void setStatusReason(String statusReason) {
		this.statusReason = statusReason;
	}

	public String getSequenceID() {
		return sequenceID;
	}

	public void setSequenceID(String sequenceID) {
		this.sequenceID = sequenceID;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isIndicationIndicator() {
		return indicationIndicator;
	}

	public void setIndicationIndicator(boolean indicationIndicator) {
		this.indicationIndicator = indicationIndicator;
	}

	public BigDecimal getPercent() {
		return percent;
	}

	public void setPercent(BigDecimal percent) {
		this.percent = percent;
	}

}
