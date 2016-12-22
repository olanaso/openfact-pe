package org.openfact.pe.models.jpa.ubl;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.EntityManager;

import org.jboss.logging.Logger;
import org.openfact.models.OpenfactSession;
import org.openfact.models.jpa.JpaModel;
import org.openfact.pe.models.RetentionLineModel;
import org.openfact.pe.models.jpa.entities.RetentionLineEntity;

public class RetentionLineAdapter
		implements RetentionLineModel, JpaModel<RetentionLineEntity> {

	protected static final Logger logger = Logger.getLogger(RetentionLineAdapter.class);

	protected RetentionLineEntity retentionLine;
	protected EntityManager em;
	protected OpenfactSession session;

	public RetentionLineAdapter(OpenfactSession session, EntityManager em,
								RetentionLineEntity retentionLine) {
		this.session = session;
		this.em = em;
		this.retentionLine = retentionLine;
	}
	@Override
	public RetentionLineEntity getEntity() {
		return retentionLine;
	}

	@Override
	public String getId() {
		return retentionLine.getId();
	}

	@Override
	public String getRelatedDocumentType() {
		return retentionLine.getRelatedDocumentType();
	}

	@Override
	public void setRelatedDocumentType(String relatedDocumentType) {
		retentionLine.setRelatedDocumentType(relatedDocumentType);
	}

	@Override
	public String getRelatedDocumentNumber() {
		return retentionLine.getRelatedDocumentNumber();
	}

	@Override
	public void setRelatedDocumentNumber(String relatedDocumentNumber) {
		retentionLine.setRelatedDocumentNumber(relatedDocumentNumber);
	}

	@Override
	public LocalDateTime getRelatedIssueDateTime() {
		return retentionLine.getRelatedIssueDateTime();
	}

	@Override
	public void setRelatedIssueDateTime(LocalDateTime relatedIssueDateTime) {
		retentionLine.setRelatedIssueDateTime(relatedIssueDateTime);
	}

	@Override
	public String getRelatedDocumentCurrency() {
		return retentionLine.getRelatedDocumentCurrency();
	}

	@Override
	public void setRelatedDocumentCurrency(String relatedDocumentCurrency) {
		retentionLine.setRelatedDocumentCurrency(relatedDocumentCurrency);
	}

	@Override
	public BigDecimal getTotalDocumentRelated() {
		return retentionLine.getTotalDocumentRelated();
	}

	@Override
	public void setTotalDocumentRelated(BigDecimal totalDocumentRelated) {
		retentionLine.setTotalDocumentRelated(totalDocumentRelated);
	}

	@Override
	public BigDecimal getTypeChange() {
		return retentionLine.getTypeChange();
	}

	@Override
	public void setTypeChange(BigDecimal typeChange) {
		retentionLine.setTypeChange(typeChange);
	}

	@Override
	public LocalDateTime getChangeIssueDateTime() {
		return retentionLine.getChangeIssueDateTime();
	}

	@Override
	public void setChangeIssueDateTime(LocalDateTime changeIssueDateTime) {
		retentionLine.setChangeIssueDateTime(changeIssueDateTime);
	}

	@Override
	public BigDecimal getTotalRetentionPayment() {
		return retentionLine.getTotalRetentionPayment();
	}

	@Override
	public void setTotalRetentionPayment(BigDecimal totalRetentionPayment) {
		retentionLine.setTotalRetentionPayment(totalRetentionPayment);
	}

	@Override
	public String getRetentionPaymentNumber() {
		return retentionLine.getRetentionPaymentNumber();
	}

	@Override
	public void setRetentionPaymentNumber(String retentionPaymentNumber) {
		retentionLine.setRetentionPaymentNumber(retentionPaymentNumber);
	}

	@Override
	public LocalDateTime getRetentionIssueDateTime() {
		return retentionLine.getRetentionIssueDateTime();
	}

	@Override
	public void setRetentionIssueDateTime(LocalDateTime retentionIssueDateTime) {
		retentionLine.setRetentionIssueDateTime(retentionIssueDateTime);
	}

	@Override
	public BigDecimal getSunatNetRetentionAmount() {
		return retentionLine.getSunatNetRetentionAmount();
	}

	@Override
	public void setSunatNetRetentionAmount(BigDecimal sunatNetRetentionAmount) {
		retentionLine.setSunatNetRetentionAmount(sunatNetRetentionAmount);
	}

	@Override
	public BigDecimal getSunatNetCashed() {
		return retentionLine.getSunatNetCashed();
	}

	@Override
	public void setSunatNetCashed(BigDecimal netCashed) {
		retentionLine.setSunatNetCashed(netCashed);
	}


}
