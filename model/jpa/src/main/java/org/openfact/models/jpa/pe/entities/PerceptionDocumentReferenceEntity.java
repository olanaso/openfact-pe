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
@Table(name = "PERCEPTION_DOCUMENT_REFERENCE")
public class PerceptionDocumentReferenceEntity {

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
    protected LocalDateTime issueDateTime;

    @Column(name = "TOTAL_INVOICE_AMOUNT")
    protected BigDecimal totalInvoiceAmount;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PERCEPTION_INFORMATION_ID", foreignKey = @ForeignKey)
    protected PerceptionInformationEntity sunatPerceptionInformation;

    /**
     * Openfact core
     */
    @ManyToOne(targetEntity = PaymentEntity.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "PAYMENT_PERCEPTIONDOCUMENTREFERENCE")
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

    public LocalDateTime getIssueDateTime() {
        return issueDateTime;
    }

    public void setIssueDateTime(LocalDateTime issueDateTime) {
        this.issueDateTime = issueDateTime;
    }

    public BigDecimal getTotalInvoiceAmount() {
        return totalInvoiceAmount;
    }

    public void setTotalInvoiceAmount(BigDecimal totalInvoiceAmount) {
        this.totalInvoiceAmount = totalInvoiceAmount;
    }

    public PerceptionInformationEntity getSunatPerceptionInformation() {
        return sunatPerceptionInformation;
    }

    public void setSunatPerceptionInformation(PerceptionInformationEntity sunatPerceptionInformation) {
        this.sunatPerceptionInformation = sunatPerceptionInformation;
    }

    public PaymentEntity getPayment() {
        return payment;
    }

    public void setPayment(PaymentEntity payment) {
        this.payment = payment;
    }

}
