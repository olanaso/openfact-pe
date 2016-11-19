package org.openfact.models.jpa.pe.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.openfact.models.jpa.entities.ubl.common.AllowanceChargeEntity;
import org.openfact.models.jpa.entities.ubl.common.BillingReferenceEntity;
import org.openfact.models.jpa.entities.ubl.common.CustomerPartyEntity;
import org.openfact.models.jpa.entities.ubl.common.PaymentEntity;
import org.openfact.models.jpa.entities.ubl.common.StatusEntity;
import org.openfact.models.jpa.entities.ubl.common.TaxTotalEntity;

@Entity
@Table(name = "SUMMARY_DOCUMENTS_LINE")
public class SumaryDocumentsLineEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Access(AccessType.PROPERTY)
    protected String id;

    @Column(name = "DOCUMENT_ID")
    protected String documentId;

    @Column(name = "LINE_ID")
    protected String lineId;

    @Column(name = "DOCUMENT_TYPE_CODE")
    protected String documentTypeCode;

    @Column(name = "DOCUMENT_SERIAL_ID")
    protected String documentSerialId;

    @Column(name = "START_DOCUMENT_NUMBER_ID")
    protected String startDocumentNumberId;

    @Column(name = "END_DOCUMENT_NUMBER_ID")
    protected String endDocumentNumberId;

    @Column(name = "TOTAL_AMOUNT")
    protected BigDecimal totalAmount;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SUMMARY_DOCUMENT_ID", foreignKey = @ForeignKey)
    protected SummaryDocumentsEntity summaryDocument = new SummaryDocumentsEntity();

    /**
     * Openfact Core
     */
    @ManyToOne(targetEntity = CustomerPartyEntity.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "ACCOUNTINGCUSTOMERPARTY_SUMMARY")
    protected CustomerPartyEntity accountingCustomerParty;

    @ManyToOne(targetEntity = BillingReferenceEntity.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "BILLINGREFERENCE")
    protected BillingReferenceEntity billingReference;

    @ManyToOne(targetEntity = StatusEntity.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "STATUS_SUMMARY")
    protected StatusEntity status;

    @OneToMany(targetEntity = PaymentEntity.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "BILLINGPAYMENT_SUMMARY")
    protected List<PaymentEntity> billingPayment = new ArrayList<>();

    @OneToMany(targetEntity = AllowanceChargeEntity.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "ALLOWANCECHARGE_SUMMARY")
    protected List<AllowanceChargeEntity> allowanceCharge = new ArrayList<>();

    @OneToMany(targetEntity = TaxTotalEntity.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "TAXTOTAL_SUMMARY")
    protected List<TaxTotalEntity> taxTotal = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public String getDocumentTypeCode() {
        return documentTypeCode;
    }

    public void setDocumentTypeCode(String documentTypeCode) {
        this.documentTypeCode = documentTypeCode;
    }

    public String getDocumentSerialId() {
        return documentSerialId;
    }

    public void setDocumentSerialId(String documentSerialId) {
        this.documentSerialId = documentSerialId;
    }

    public String getStartDocumentNumberId() {
        return startDocumentNumberId;
    }

    public void setStartDocumentNumberId(String startDocumentNumberId) {
        this.startDocumentNumberId = startDocumentNumberId;
    }

    public String getEndDocumentNumberId() {
        return endDocumentNumberId;
    }

    public void setEndDocumentNumberId(String endDocumentNumberId) {
        this.endDocumentNumberId = endDocumentNumberId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public SummaryDocumentsEntity getSummaryDocument() {
        return summaryDocument;
    }

    public void setSummaryDocument(SummaryDocumentsEntity summaryDocument) {
        this.summaryDocument = summaryDocument;
    }

    public CustomerPartyEntity getAccountingCustomerParty() {
        return accountingCustomerParty;
    }

    public void setAccountingCustomerParty(CustomerPartyEntity accountingCustomerParty) {
        this.accountingCustomerParty = accountingCustomerParty;
    }

    public BillingReferenceEntity getBillingReference() {
        return billingReference;
    }

    public void setBillingReference(BillingReferenceEntity billingReference) {
        this.billingReference = billingReference;
    }

    public StatusEntity getStatus() {
        return status;
    }

    public void setStatus(StatusEntity status) {
        this.status = status;
    }

    public List<PaymentEntity> getBillingPayment() {
        return billingPayment;
    }

    public void setBillingPayment(List<PaymentEntity> billingPayment) {
        this.billingPayment = billingPayment;
    }

    public List<AllowanceChargeEntity> getAllowanceCharge() {
        return allowanceCharge;
    }

    public void setAllowanceCharge(List<AllowanceChargeEntity> allowanceCharge) {
        this.allowanceCharge = allowanceCharge;
    }

    public List<TaxTotalEntity> getTaxTotal() {
        return taxTotal;
    }

    public void setTaxTotal(List<TaxTotalEntity> taxTotal) {
        this.taxTotal = taxTotal;
    }

}
