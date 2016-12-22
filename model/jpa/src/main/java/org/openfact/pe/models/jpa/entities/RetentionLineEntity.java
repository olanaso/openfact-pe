package org.openfact.pe.models.jpa.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "RETENTION_LINE")
public class RetentionLineEntity {

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Access(AccessType.PROPERTY)
	protected String id;
		@Column(name = "RELATED_DOCUMENT_TYPE")
	protected String relatedDocumentType;
	@Column(name = "RELATED_DOCUMENT_NUMBER")
	protected String relatedDocumentNumber;
	@Column(name = "RELATED_ISSUE_DATE")
	@Type(type = "org.hibernate.type.LocalDateTimeType")
	protected LocalDateTime relatedIssueDateTime;
	@Column(name = "RELATED_DOCUMENT_CURRENCY")
	protected String relatedDocumentCurrency;
	@Column(name = "TOTAL_DOCUMENT_RELATED")
	protected BigDecimal totalDocumentRelated;
	@Column(name = "TYPE_CHANGE")
	protected BigDecimal typeChange;
	@Column(name = "CHANGE_ISSUE_DATE")
	@Type(type = "org.hibernate.type.LocalDateTimeType")
	protected LocalDateTime changeIssueDateTime;
	@Column(name = "TOTAL_RETENTION_PAYMENT")
	protected BigDecimal totalRetentionPayment;
	@Column(name = "RETENTION_PAYEMENT_NUMBER")
	protected String retentionPaymentNumber;
	@Column(name = "RETENTION_ISSUE_DATE")
	@Type(type = "org.hibernate.type.LocalDateTimeType")
	protected LocalDateTime retentionIssueDateTime;
	@Column(name = "SUNAT_NET_RETENTION_AMOUNT")
	protected BigDecimal sunatNetRetentionAmount;
	@Column(name = "SUNAT_NET_CASHED")
	protected BigDecimal sunatNetCashed;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(foreignKey = @ForeignKey, name = "RETENTION_ID")
	private RetentionEntity retention;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRelatedDocumentType() {
		return relatedDocumentType;
	}

	public void setRelatedDocumentType(String relatedDocumentType) {
		this.relatedDocumentType = relatedDocumentType;
	}

	public String getRelatedDocumentNumber() {
		return relatedDocumentNumber;
	}

	public void setRelatedDocumentNumber(String relatedDocumentNumber) {
		this.relatedDocumentNumber = relatedDocumentNumber;
	}

	public LocalDateTime getRelatedIssueDateTime() {
		return relatedIssueDateTime;
	}

	public void setRelatedIssueDateTime(LocalDateTime relatedIssueDateTime) {
		this.relatedIssueDateTime = relatedIssueDateTime;
	}

	public LocalDateTime getChangeIssueDateTime() {
		return changeIssueDateTime;
	}

	public void setChangeIssueDateTime(LocalDateTime changeIssueDateTime) {
		this.changeIssueDateTime = changeIssueDateTime;
	}

	public LocalDateTime getRetentionIssueDateTime() {
		return retentionIssueDateTime;
	}

	public void setRetentionIssueDateTime(LocalDateTime retentionIssueDateTime) {
		this.retentionIssueDateTime = retentionIssueDateTime;
	}

	public String getRelatedDocumentCurrency() {
		return relatedDocumentCurrency;
	}

	public void setRelatedDocumentCurrency(String relatedDocumentCurrency) {
		this.relatedDocumentCurrency = relatedDocumentCurrency;
	}

	public BigDecimal getTotalDocumentRelated() {
		return totalDocumentRelated;
	}

	public void setTotalDocumentRelated(BigDecimal totalDocumentRelated) {
		this.totalDocumentRelated = totalDocumentRelated;
	}

	public BigDecimal getTypeChange() {
		return typeChange;
	}

	public void setTypeChange(BigDecimal typeChange) {
		this.typeChange = typeChange;
	}

	public BigDecimal getTotalRetentionPayment() {
		return totalRetentionPayment;
	}

	public void setTotalRetentionPayment(BigDecimal totalRetentionPayment) {
		this.totalRetentionPayment = totalRetentionPayment;
	}

	public String getRetentionPaymentNumber() {
		return retentionPaymentNumber;
	}

	public void setRetentionPaymentNumber(String retentionPaymentNumber) {
		this.retentionPaymentNumber = retentionPaymentNumber;
	}	

	public BigDecimal getSunatNetRetentionAmount() {
		return sunatNetRetentionAmount;
	}

	public void setSunatNetRetentionAmount(BigDecimal sunatNetRetentionAmount) {
		this.sunatNetRetentionAmount = sunatNetRetentionAmount;
	}

	public BigDecimal getSunatNetCashed() {
		return sunatNetCashed;
	}

	public void setSunatNetCashed(BigDecimal sunatNetCashed) {
		this.sunatNetCashed = sunatNetCashed;
	}

	public RetentionEntity getRetention() {
		return retention;
	}

	public void setRetention(RetentionEntity retention) {
		this.retention = retention;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RetentionLineEntity other = (RetentionLineEntity) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}
}