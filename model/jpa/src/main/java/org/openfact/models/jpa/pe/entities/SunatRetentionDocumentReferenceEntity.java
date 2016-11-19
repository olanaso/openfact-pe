package org.openfact.models.jpa.pe.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.openfact.models.jpa.entities.ubl.common.PaymentEntity;

@Entity
@Table(name = "SUNAT_RETENTION_DOCUMENT_REFERENCE")
public class SunatRetentionDocumentReferenceEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Access(AccessType.PROPERTY)
    protected String id;

    @Column(name = "DOCUMENT_ID")
    protected String documentId;

    @Column(name = "ISSUE_DATE")
    @Type(type = "org.hibernate.type.LocalDateTimeType")
    protected LocalDateTime issueDate;

    @Column(name = "TOTAL_INVOICE_AMOUNT")
    protected BigDecimal totalInvoiceAmount;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RETENCION_INFORMATION_ID", foreignKey = @ForeignKey)
    protected SunatRetencionInformationEntity sunatRetentionInformation;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RETENTION_ID", foreignKey = @ForeignKey)
    private RetentionEntity retention;

    /**
     * Openfact Core
     */
    @ManyToOne(targetEntity = PaymentEntity.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "PAYMENT_SUNATRETENTIONDOCUMENTREFERENCE")
    protected PaymentEntity payment;

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

    public LocalDateTime getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDateTime issueDate) {
        this.issueDate = issueDate;
    }

    public BigDecimal getTotalInvoiceAmount() {
        return totalInvoiceAmount;
    }

    public void setTotalInvoiceAmount(BigDecimal totalInvoiceAmount) {
        this.totalInvoiceAmount = totalInvoiceAmount;
    }

    public SunatRetencionInformationEntity getSunatRetentionInformation() {
        return sunatRetentionInformation;
    }

    public void setSunatRetentionInformation(SunatRetencionInformationEntity sunatRetentionInformation) {
        this.sunatRetentionInformation = sunatRetentionInformation;
    }

    public RetentionEntity getRetention() {
        return retention;
    }

    public void setRetention(RetentionEntity retention) {
        this.retention = retention;
    }

    public PaymentEntity getPayment() {
        return payment;
    }

    public void setPayment(PaymentEntity payment) {
        this.payment = payment;
    }

}
