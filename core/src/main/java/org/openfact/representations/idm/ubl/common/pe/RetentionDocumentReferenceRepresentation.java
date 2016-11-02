package org.openfact.representations.idm.ubl.common.pe;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.openfact.representations.idm.ubl.common.PaymentRepresentation;

public class RetentionDocumentReferenceRepresentation {
	protected String id;
	protected String idUbl;
	protected LocalDateTime issueDateTime;
	protected BigDecimal totalInvoiceAmount;
	protected PaymentRepresentation payment;
	protected RetentionInformationRepresentation sunatRetentionInformation;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdUbl() {
		return idUbl;
	}

	public void setIdUbl(String idUbl) {
		this.idUbl = idUbl;
	}

	public LocalDateTime getIssueDateTime() {
		return issueDateTime;
	}

	public void setIssueDateTime(LocalDateTime issueDateTime) {
		this.issueDateTime = issueDateTime;
	}

	public BigDecimal getTotalInvoiceAmount() {
		return totalInvoiceAmount;
	}

	public void setTotalInvoiceAmount(BigDecimal totalInvoiceAmount) {
		this.totalInvoiceAmount = totalInvoiceAmount;
	}

	public PaymentRepresentation getPayment() {
		return payment;
	}

	public void setPayment(PaymentRepresentation payment) {
		this.payment = payment;
	}

	public RetentionInformationRepresentation getSunatRetentionInformation() {
		return sunatRetentionInformation;
	}

	public void setSunatRetentionInformation(RetentionInformationRepresentation sunatRetentionInformation) {
		this.sunatRetentionInformation = sunatRetentionInformation;
	}

}
