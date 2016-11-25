package org.openfact.models.jpa.pe.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "RETENTION", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "ORGANIZATION_ID", "DOCUMENT_ID" }) })
@NamedQueries({
        @NamedQuery(name = "getAllRetentionsByOrganization", query = "select r from RetentionEntity r where r.organizationId = :organizationId order by r.issueDate"),
        @NamedQuery(name = "getOrganizationRetentionById", query = "select r from RetentionEntity r where r.id = :id and r.organizationId = :organizationId"),
        @NamedQuery(name = "getOrganizationRetentionByDocumentId", query = "select r from RetentionEntity i where r.documentId = :documentId and r.organizationId = :organizationId"),
        @NamedQuery(name = "searchForRetention", query = "select r from RetentionEntity i where r.organizationId = :organizationId and r.documentId like :search order by r.issueDate"),
        @NamedQuery(name = "getOrganizationRetentionCount", query = "select count(r) from RetentionEntity r where r.organizationId = :organizationId"),
        @NamedQuery(name = "getLastRetentionByOrganization", query = "select r from RetentionEntity r where r.organizationId = :organizationId and length(r.documentId)=:documentIdLength and r.documentId like :formatter order by r.issueDate desc"), })
public class RetentionEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Access(AccessType.PROPERTY)
    protected String id;

    @Column(name = "DOCUMENT_ID")
    protected String documentId;

    @Column(name = "UBL_VERSION_ID")
    protected String ublVersionId;

    @Column(name = "CUSTOMIZATION_ID")
    protected String customizationId;

    @Column(name = "ISSUE_DATE")
    @Type(type = "org.hibernate.type.LocalDateTimeType")
    protected LocalDateTime issueDate;

    @Column(name = "TOTAL_INVOICE_AMOUNT")
    protected BigDecimal totalInvoiceAmount;

    @Column(name = "TOTAL_PAID")
    protected BigDecimal totalPaid;

    @Column(name = "SUNAT_RETENTION_SYSTEM_CODE")
    protected String sunatRetentionSystemCode;

    @Column(name = "SUNAT_RETENTION_PERCENT")
    protected BigDecimal sunatRetentionPercent;

    @ElementCollection
    @Column(name = "VALUE")
    @CollectionTable(name = "RETENTION_NOTE", joinColumns = { @JoinColumn(name = "RETENTION_ID") })
    protected List<String> notes = new ArrayList<>();

    @Lob
    @Column(name = "XML_DOCUMENT")
    protected byte[] xmlDocument;

    @NotNull
    @Column(name = "ORGANIZATION_ID")
    protected String organizationId;

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

    public String getUblVersionId() {
        return ublVersionId;
    }

    public void setUblVersionId(String ublVersionId) {
        this.ublVersionId = ublVersionId;
    }

    public String getCustomizationId() {
        return customizationId;
    }

    public void setCustomizationId(String customizationId) {
        this.customizationId = customizationId;
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

    public BigDecimal getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(BigDecimal totalPaid) {
        this.totalPaid = totalPaid;
    }

    public String getSunatRetentionSystemCode() {
        return sunatRetentionSystemCode;
    }

    public void setSunatRetentionSystemCode(String sunatRetentionSystemCode) {
        this.sunatRetentionSystemCode = sunatRetentionSystemCode;
    }

    public BigDecimal getSunatRetentionPercent() {
        return sunatRetentionPercent;
    }

    public void setSunatRetentionPercent(BigDecimal sunatRetentionPercent) {
        this.sunatRetentionPercent = sunatRetentionPercent;
    }

    public List<String> getNotes() {
        return notes;
    }

    public void setNotes(List<String> notes) {
        this.notes = notes;
    }

    public byte[] getXmlDocument() {
        return xmlDocument;
    }

    public void setXmlDocument(byte[] xmlDocument) {
        this.xmlDocument = xmlDocument;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((documentId == null) ? 0 : documentId.hashCode());
        result = prime * result + ((organizationId == null) ? 0 : organizationId.hashCode());
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
        RetentionEntity other = (RetentionEntity) obj;
        if (documentId == null) {
            if (other.documentId != null)
                return false;
        } else if (!documentId.equals(other.documentId))
            return false;
        if (organizationId == null) {
            if (other.organizationId != null)
                return false;
        } else if (!organizationId.equals(other.organizationId))
            return false;
        return true;
    }

}
