package org.openfact.pe.models.jpa.ubl;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.EntityManager;

import org.jboss.logging.Logger;
import org.openfact.models.OpenfactSession;
import org.openfact.models.jpa.JpaModel;
import org.openfact.pe.models.PerceptionLineModel;
import org.openfact.pe.models.jpa.entities.PerceptionLineEntity;

public class PerceptionLineAdapter
		implements PerceptionLineModel, JpaModel<PerceptionLineEntity> {

	protected static final Logger logger = Logger.getLogger(PerceptionLineAdapter.class);

	protected PerceptionLineEntity perceptionLine;
	protected EntityManager em;
	protected OpenfactSession session;

	public PerceptionLineAdapter(OpenfactSession session, EntityManager em,
								 PerceptionLineEntity perceptionLine) {
		this.session = session;
		this.em = em;
		this.perceptionLine = perceptionLine;
	}

	@Override
	public PerceptionLineEntity getEntity() {
		return perceptionLine;
	}

	@Override
	public String getId() {
		return perceptionLine.getId();
	}

	@Override
	public String getRelatedDocumentType() {
		return perceptionLine.getRelatedDocumentType();
	}

	@Override
	public void setRelatedDocumentType(String relatedDocumentType) {
		perceptionLine.setRelatedDocumentType(relatedDocumentType);
	}

	@Override
	public String getRelatedDocumentNumber() {
		return perceptionLine.getRelatedDocumentNumber();
	}

	@Override
	public void setRelatedDocumentNumber(String relatedDocumentNumber) {
		perceptionLine.setRelatedDocumentNumber(relatedDocumentNumber);
	}

	@Override
	public LocalDateTime getRelatedIssueDateTime() {
		return perceptionLine.getRelatedIssueDateTime();
	}

	@Override
	public void setRelatedIssueDateTime(LocalDateTime relatedIssueDateTime) {
		perceptionLine.setRelatedIssueDateTime(relatedIssueDateTime);
	}

	@Override
	public String getRelatedDocumentCurrency() {
		return perceptionLine.getRelatedDocumentCurrency();
	}

	@Override
	public void setRelatedDocumentCurrency(String relatedDocumentCurrency) {
		perceptionLine.setRelatedDocumentCurrency(relatedDocumentCurrency);
	}

	@Override
	public BigDecimal getTotalDocumentRelated() {
		return perceptionLine.getTotalDocumentRelated();
	}

	@Override
	public void setTotalDocumentRelated(BigDecimal totalDocumentRelated) {
		perceptionLine.setTotalDocumentRelated(totalDocumentRelated);
	}

	@Override
	public BigDecimal getTypeChange() {
		return perceptionLine.getTypeChange();
	}

	@Override
	public void setTypeChange(BigDecimal typeChange) {
		perceptionLine.setTypeChange(typeChange);
	}

	@Override
	public LocalDateTime getChangeIssueDateTime() {
		return perceptionLine.getChangeIssueDateTime();
	}

	@Override
	public void setChangeIssueDateTime(LocalDateTime changeIssueDateTime) {
		perceptionLine.setChangeIssueDateTime(changeIssueDateTime);
	}

	@Override
	public BigDecimal getTotalPerceptionPayment() {
		return perceptionLine.getTotalPerceptionPayment();
	}

	@Override
	public void setTotalPerceptionPayment(BigDecimal totalPerceptionPayment) {
		perceptionLine.setTotalPerceptionPayment(totalPerceptionPayment);
	}

	@Override
	public String getPerceptionPaymentNumber() {
		return perceptionLine.getPerceptionPaymentNumber();
	}

	@Override
	public void setPerceptionPaymentNumber(String perceptionPaymentNumber) {
		perceptionLine.setPerceptionPaymentNumber(perceptionPaymentNumber);
	}

	@Override
	public LocalDateTime getPerceptionIssueDateTime() {
		return perceptionLine.getPerceptionIssueDateTime();
	}

	@Override
	public void setPerceptionIssueDateTime(LocalDateTime perceptionIssueDateTime) {
		perceptionLine.setPerceptionIssueDateTime(perceptionIssueDateTime);
	}

	@Override
	public BigDecimal getSunatNetPerceptionAmount() {
		return perceptionLine.getSunatNetPerceptionAmount();
	}

	@Override
	public void setSunatNetPerceptionAmount(BigDecimal sunatNetPerceptionAmount) {
		perceptionLine.setSunatNetPerceptionAmount(sunatNetPerceptionAmount);
	}

	@Override
	public BigDecimal getSunatNetCashed() {
		return perceptionLine.getSunatNetCashed();
	}

	@Override
	public void setSunatNetCashed(BigDecimal netCashed) {
		perceptionLine.setSunatNetCashed(netCashed);
	}

}
