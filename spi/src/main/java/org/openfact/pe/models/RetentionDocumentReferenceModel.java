package org.openfact.pe.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface RetentionDocumentReferenceModel {

	String getId();

	String getDocumentId();

	void setDocumentId(String documentId);

	LocalDate getIssueDate();

	void setIssueDate(LocalDate issueDate);

	BigDecimal getTotalInvoiceAmount();

	void setTotalInvoiceAmount(BigDecimal totalInvoiceAmount);

	/*
	 * PaymentModel getPayment();
	 * 
	 * void setPayment(PaymentModel payment);
	 */

	RetentionInformationModel getSunatRetentionInformation();

	void setSunatRetentionInformation(RetentionInformationModel sunatRetentionInformation);

}
