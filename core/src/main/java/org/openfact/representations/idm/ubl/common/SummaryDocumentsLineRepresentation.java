package org.openfact.representations.idm.ubl.common;

import java.math.BigDecimal;
import java.util.List;

import org.openfact.representations.idm.ubl.common.pe.PerceptionDocumentReferenceRepresentation;

public class SummaryDocumentsLineRepresentation {
	protected String id;
	protected String IdUbl;
	protected String lineID;
	protected String documentTypeCode;
	protected String documentSerialID;
	protected String startDocumentNumberID;
	protected String endDocumentNumberID;
	protected BigDecimal totalAmount;
	protected CustomerPartyRepresentation accountingCustomerParty;
	protected BillingReferenceRepresentation billingReference;
	protected PerceptionDocumentReferenceRepresentation perceptionDocumentReference;
	protected StatusRepresentation status;
	protected List<PaymentRepresentation> billingPayment;
	protected List<AllowanceChargeRepresentation> allowanceCharge;
	protected List<TaxTotalRepresentation> taxTotal;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdUbl() {
		return IdUbl;
	}

	public void setIdUbl(String idUbl) {
		IdUbl = idUbl;
	}

	public String getLineID() {
		return lineID;
	}

	public void setLineID(String lineID) {
		this.lineID = lineID;
	}

	public String getDocumentTypeCode() {
		return documentTypeCode;
	}

	public void setDocumentTypeCode(String documentTypeCode) {
		this.documentTypeCode = documentTypeCode;
	}

	public String getDocumentSerialID() {
		return documentSerialID;
	}

	public void setDocumentSerialID(String documentSerialID) {
		this.documentSerialID = documentSerialID;
	}

	public String getStartDocumentNumberID() {
		return startDocumentNumberID;
	}

	public void setStartDocumentNumberID(String startDocumentNumberID) {
		this.startDocumentNumberID = startDocumentNumberID;
	}

	public String getEndDocumentNumberID() {
		return endDocumentNumberID;
	}

	public void setEndDocumentNumberID(String endDocumentNumberID) {
		this.endDocumentNumberID = endDocumentNumberID;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public CustomerPartyRepresentation getAccountingCustomerParty() {
		return accountingCustomerParty;
	}

	public void setAccountingCustomerParty(CustomerPartyRepresentation accountingCustomerParty) {
		this.accountingCustomerParty = accountingCustomerParty;
	}

	public BillingReferenceRepresentation getBillingReference() {
		return billingReference;
	}

	public void setBillingReference(BillingReferenceRepresentation billingReference) {
		this.billingReference = billingReference;
	}

	public PerceptionDocumentReferenceRepresentation getPerceptionDocumentReference() {
		return perceptionDocumentReference;
	}

	public void setPerceptionDocumentReference(PerceptionDocumentReferenceRepresentation perceptionDocumentReference) {
		this.perceptionDocumentReference = perceptionDocumentReference;
	}

	public StatusRepresentation getStatus() {
		return status;
	}

	public void setStatus(StatusRepresentation status) {
		this.status = status;
	}

	public List<PaymentRepresentation> getBillingPayment() {
		return billingPayment;
	}

	public void setBillingPayment(List<PaymentRepresentation> billingPayment) {
		this.billingPayment = billingPayment;
	}

	public List<AllowanceChargeRepresentation> getAllowanceCharge() {
		return allowanceCharge;
	}

	public void setAllowanceCharge(List<AllowanceChargeRepresentation> allowanceCharge) {
		this.allowanceCharge = allowanceCharge;
	}

	public List<TaxTotalRepresentation> getTaxTotal() {
		return taxTotal;
	}

	public void setTaxTotal(List<TaxTotalRepresentation> taxTotal) {
		this.taxTotal = taxTotal;
	}

}
