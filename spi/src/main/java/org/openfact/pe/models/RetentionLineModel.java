package org.openfact.pe.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface RetentionLineModel {
	String getId();

	String getRelatedDocumentType();

	void setRelatedDocumentType(String relatedDocumentType);

	String getRelatedDocumentNumber();

	void setRelatedDocumentNumber(String relatedDocumentNumber);

	LocalDate getRelatedIssueDate();

	void setRelatedIssueDate(LocalDate relatedIssueDate);

	String getRelatedDocumentCurrency();

	void setRelatedDocumentCurrency(String relatedDocumentCurrency);

	BigDecimal getTotalDocumentRelated();

	void setTotalDocumentRelated(BigDecimal totalDocumentRelated);

	BigDecimal getTypeChange();

	void setTypeChange(BigDecimal typeChange);

	LocalDate getChangeIssueDate();

	void setChangeIssueDate(LocalDate changeIssueDate);

	BigDecimal getTotalRetentionPayment();

	void setTotalRetentionPayment(BigDecimal totalRetentionPayment);

	String getRetentionPaymentNumber();

	void setRetentionPaymentNumber(String retentionPaymentNumber);

	LocalDate getRetentionIssueDate();

	void setRetentionIssueDate(LocalDate retentionIssueDate);

	BigDecimal getSunatNetRetentionAmount();

	void setSunatNetRetentionAmount(BigDecimal sunatNetRetentionAmount);

	BigDecimal getSunatNetCashed();

	void setSunatNetCashed(BigDecimal netCashed);

}
