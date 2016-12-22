package org.openfact.pe.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface RetentionLineModel {
	String getId();

	String getRelatedDocumentType();

	void setRelatedDocumentType(String relatedDocumentType);

	String getRelatedDocumentNumber();

	void setRelatedDocumentNumber(String relatedDocumentNumber);

	LocalDateTime getRelatedIssueDateTime();

	void setRelatedIssueDateTime(LocalDateTime relatedIssueDateTime);

	String getRelatedDocumentCurrency();

	void setRelatedDocumentCurrency(String relatedDocumentCurrency);

	BigDecimal getTotalDocumentRelated();

	void setTotalDocumentRelated(BigDecimal totalDocumentRelated);

	BigDecimal getTypeChange();

	void setTypeChange(BigDecimal typeChange);

	LocalDateTime getChangeIssueDateTime();

	void setChangeIssueDateTime(LocalDateTime changeIssueDateTime);

	BigDecimal getTotalRetentionPayment();

	void setTotalRetentionPayment(BigDecimal totalRetentionPayment);

	String getRetentionPaymentNumber();

	void setRetentionPaymentNumber(String retentionPaymentNumber);

	LocalDateTime getRetentionIssueDateTime();

	void setRetentionIssueDateTime(LocalDateTime retentionIssueDateTime);

	BigDecimal getSunatNetRetentionAmount();

	void setSunatNetRetentionAmount(BigDecimal sunatNetRetentionAmount);

	BigDecimal getSunatNetCashed();

	void setSunatNetCashed(BigDecimal netCashed);

}
