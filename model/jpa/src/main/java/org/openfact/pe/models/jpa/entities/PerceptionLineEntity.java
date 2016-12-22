package org.openfact.pe.models.jpa.entities;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "PERCEPTION_LINE")
public class PerceptionLineEntity {

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
    @Column(name = "TOTAL_PERCEPTION_PAYMENT")
    protected BigDecimal totalPerceptionPayment;
    @Column(name = "PERCEPTION_PAYEMENT_NUMBER")
    protected String perceptionPaymentNumber;
    @Column(name = "PERCEPTION_ISSUE_DATE")
    @Type(type = "org.hibernate.type.LocalDateTimeType")
    protected LocalDateTime perceptionIssueDateTime;
    @Column(name = "SUNAT_NET_PERCEPTION_AMOUNT")
    protected BigDecimal sunatNetPerceptionAmount;
    @Column(name = "SUNAT_NET_CASHED")
    protected BigDecimal sunatNetCashed;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey, name = "PERCEPTION_ID")
    private PerceptionEntity perception;

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

    public BigDecimal getTotalPerceptionPayment() {
        return totalPerceptionPayment;
    }

    public void setTotalPerceptionPayment(BigDecimal totalPerceptionPayment) {
        this.totalPerceptionPayment = totalPerceptionPayment;
    }

    public String getPerceptionPaymentNumber() {
        return perceptionPaymentNumber;
    }

    public void setPerceptionPaymentNumber(String perceptionPaymentNumber) {
        this.perceptionPaymentNumber = perceptionPaymentNumber;
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

    public LocalDateTime getPerceptionIssueDateTime() {
        return perceptionIssueDateTime;
    }

    public void setPerceptionIssueDateTime(LocalDateTime perceptionIssueDateTime) {
        this.perceptionIssueDateTime = perceptionIssueDateTime;
    }

    public BigDecimal getSunatNetPerceptionAmount() {
        return sunatNetPerceptionAmount;
    }

    public void setSunatNetPerceptionAmount(BigDecimal sunatNetPerceptionAmount) {
        this.sunatNetPerceptionAmount = sunatNetPerceptionAmount;
    }

    public BigDecimal getSunatNetCashed() {
        return sunatNetCashed;
    }

    public void setSunatNetCashed(BigDecimal sunatNetCashed) {
        this.sunatNetCashed = sunatNetCashed;
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
        PerceptionLineEntity other = (PerceptionLineEntity) obj;
        if (getId() == null) {
            if (other.getId() != null)
                return false;
        } else if (!getId().equals(other.getId()))
            return false;
        return true;
    }

    public PerceptionEntity getPerception() {
        return perception;
    }

    public void setPerception(PerceptionEntity perception) {
        this.perception = perception;
    }
}
