package org.openfact.models.jpa.ubl.common;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.jboss.logging.Logger;
import org.openfact.models.OpenfactSession;
import org.openfact.models.jpa.JpaModel;
import org.openfact.models.jpa.entities.ubl.common.AllowanceChargeEntity;
import org.openfact.models.jpa.entities.ubl.common.PaymentEntity;
import org.openfact.models.jpa.entities.ubl.common.SummaryDocumentsLineEntity;
import org.openfact.models.jpa.entities.ubl.common.TaxTotalEntity;
import org.openfact.models.ubl.common.AllowanceChargeModel;
import org.openfact.models.ubl.common.BillingReferenceModel;
import org.openfact.models.ubl.common.CustomerPartyModel;
import org.openfact.models.ubl.common.PaymentModel;
import org.openfact.models.ubl.common.StatusModel;
import org.openfact.models.ubl.common.SummaryDocumentsLineModel;
import org.openfact.models.ubl.common.TaxTotalModel;

public class SummaryDocumentsLineAdapter implements SummaryDocumentsLineModel, JpaModel<SummaryDocumentsLineEntity> {
	protected static final Logger logger = Logger.getLogger(SummaryDocumentsLineAdapter.class);

	protected SummaryDocumentsLineEntity summaryDocumentsLine;
	protected EntityManager em;
	protected OpenfactSession session;

	public SummaryDocumentsLineAdapter(OpenfactSession session, EntityManager em,
			SummaryDocumentsLineEntity summaryDocumentsLine) {
		this.session = session;
		this.em = em;
		this.summaryDocumentsLine = summaryDocumentsLine;
	}

	@Override
	public SummaryDocumentsLineEntity getEntity() {
		return summaryDocumentsLine;
	}

	@Override
	public String getLineID() {
		return summaryDocumentsLine.getLineID();
	}

	@Override
	public void setLineID(String value) {
		summaryDocumentsLine.setLineID(value);
	}

	@Override
	public String getDocumentTypeCode() {
		return summaryDocumentsLine.getDocumentTypeCode();
	}

	@Override
	public void setDocumentTypeCode(String value) {
		summaryDocumentsLine.setDocumentTypeCode(value);
	}

	@Override
	public String getDocumentSerialID() {
		return summaryDocumentsLine.getDocumentSerialID();
	}

	@Override
	public void setDocumentSerialID(String value) {
		summaryDocumentsLine.setDocumentSerialID(value);
	}

	@Override
	public String getStartDocumentNumberID() {
		return summaryDocumentsLine.getStartDocumentNumberID();
	}

	@Override
	public void setStartDocumentNumberID(String value) {
		summaryDocumentsLine.setStartDocumentNumberID(value);
	}

	@Override
	public String getEndDocumentNumberID() {
		return summaryDocumentsLine.getEndDocumentNumberID();
	}

	@Override
	public void setEndDocumentNumberID(String value) {
		summaryDocumentsLine.setEndDocumentNumberID(value);
	}

	@Override
	public BigDecimal getTotalAmount() {
		return summaryDocumentsLine.getTotalAmount();
	}

	@Override
	public void setTotalAmount(BigDecimal value) {
		summaryDocumentsLine.setTotalAmount(value);
	}

	@Override
	public List<PaymentModel> getBillingPayment() {
		return summaryDocumentsLine.getBillingPayment().stream().map(f -> new PaymentAdapter(session, em, f))
				.collect(Collectors.toList());
	}

	@Override
	public void setBillingPayment(List<PaymentModel> billingPayment) {
		List<PaymentEntity> entities = billingPayment.stream().map(f -> PaymentAdapter.toEntity(f, em))
				.collect(Collectors.toList());
		summaryDocumentsLine.setBillingPayment(entities);
	}

	@Override
	public List<AllowanceChargeModel> getAllowanceCharge() {
		return summaryDocumentsLine.getAllowanceCharge().stream().map(f -> new AllowanceChargeAdapter(session, em, f))
				.collect(Collectors.toList());
	}

	@Override
	public void setAllowanceCharge(List<AllowanceChargeModel> allowanceCharge) {
		List<AllowanceChargeEntity> entities = allowanceCharge.stream().map(f -> AllowanceChargeAdapter.toEntity(f, em))
				.collect(Collectors.toList());
		summaryDocumentsLine.setAllowanceCharge(entities);
	}

	@Override
	public List<TaxTotalModel> getTaxTotal() {
		return summaryDocumentsLine.getTaxTotal().stream().map(f -> new TaxTotalAdapter(session, em, f))
				.collect(Collectors.toList());
	}

	@Override
	public void setTaxTotal(List<TaxTotalModel> taxTotal) {
		List<TaxTotalEntity> entities = taxTotal.stream().map(f -> TaxTotalAdapter.toEntity(f, em))
				.collect(Collectors.toList());
		summaryDocumentsLine.setTaxTotal(entities);
	}

	@Override
	public String getId() {
		return summaryDocumentsLine.getId();
	}

	public static SummaryDocumentsLineEntity toEntity(SummaryDocumentsLineModel model, EntityManager em) {
		if (model instanceof SummaryDocumentsLineAdapter) {
			return ((SummaryDocumentsLineAdapter) model).getEntity();
		}
		return em.getReference(SummaryDocumentsLineEntity.class, model.getId());
	}

	@Override
	public String getID() {
		return summaryDocumentsLine.getID();
	}

	@Override
	public void setID(String ID) {
		summaryDocumentsLine.setID(ID);
	}

	@Override
	public CustomerPartyModel getAccountingCustomerParty() {
		return new CustomerPartyAdapter(session, em, summaryDocumentsLine.getAccountingCustomerParty());
	}

	@Override
	public void setAccountingCustomerParty(CustomerPartyModel accountingCustomerParty) {
		summaryDocumentsLine.setAccountingCustomerParty(CustomerPartyAdapter.toEntity(accountingCustomerParty, em));
	}

	@Override
	public BillingReferenceModel getBillingReference() {
		return new BillingReferenceAdapter(session, em, summaryDocumentsLine.getBillingReference());
	}

	@Override
	public void setBillingReference(BillingReferenceModel billingReference) {
		summaryDocumentsLine.setBillingReference(BillingReferenceAdapter.toEntity(billingReference, em));
	}

	@Override
	public StatusModel getStatus() {
		return new StatusAdapter(session, em, summaryDocumentsLine.getStatus());
	}

	@Override
	public void setStatus(StatusModel status) {
		summaryDocumentsLine.setStatus(StatusAdapter.toEntity(status, em));
	}

}
