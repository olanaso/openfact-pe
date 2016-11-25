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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "PERCEPTION", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "ORGANIZATION_ID", "DOCUMENT_ID" }) })
@NamedQueries({
        @NamedQuery(name = "getAllPerceptionsByOrganization", query = "select p from PerceptionEntity p where p.organizationId = :organizationId order by p.issueDate"),
        @NamedQuery(name = "getOrganizationPerceptionById", query = "select p from PerceptionEntity p where p.id = :id and p.organizationId = :organizationId"),
        @NamedQuery(name = "getOrganizationPerceptionByDocumentId", query = "select p from PerceptionEntity p where p.documentId = :documentId and p.organizationId = :organizationId"),
        @NamedQuery(name = "searchForPerception", query = "select p from PerceptionEntity p where p.organizationId = :organizationId and p.ID like :search order by p.issueDate"),
        @NamedQuery(name = "getOrganizationPerceptionCount", query = "select count(i) from PerceptionEntity p where p.organizationId = :organizationId"),
        @NamedQuery(name = "getLastPerceptionByOrganization", query = "select p from PerceptionEntity p where p.organizationId = :organizationId and length(p.documentId)=:documentIdLength and p.documentId like :formatter order by p.issueDate desc") })
public class PerceptionEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Access(AccessType.PROPERTY)
    protected String id;

    @Column(name = "DOCUMENT_ID")
    protected String documentId;

    @Column(name = "UBL_VERSIONID")
    protected String ublVersionId;

    @Column(name = "CUSTOMIZATION_ID")
    protected String customizationId;

    @Column(name = "ISSUE_DATE")
    @Type(type = "org.hibernate.type.LocalDateTimeType")
    protected LocalDateTime issueDate;

    @Column(name = "SUNAT_PERCEPTION_SYSTEM_CODE")
    protected String sunatPerceptionSystemCode;

    @Column(name = "SUNAT_PERCEPTION_PERCENT")
    protected BigDecimal sunatPerceptionPercent;

    @Column(name = "TOTAL_INVOICE_AMOUNT")
    protected BigDecimal totalInvoiceAmount;

    @Column(name = "SUNAT_TOTAL_CASHED")
    protected BigDecimal sunatTotalCashed;

    @Lob
    @Column(name = "XML_DOCUMENT")
    protected byte[] xmlDocument;

    @NotNull
    @Column(name = "ORGANIZATION_ID")
    protected String organizationId;

    @ElementCollection
    @Column(name = "VALUE")
    @CollectionTable(name = "PERCEPTION_NOTE", joinColumns = { @JoinColumn(name = "PERCEPTION_ID") })
    protected List<String> notes = new ArrayList<>();

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

    public String getSunatPerceptionSystemCode() {
        return sunatPerceptionSystemCode;
    }

    public void setSunatPerceptionSystemCode(String sunatPerceptionSystemCode) {
        this.sunatPerceptionSystemCode = sunatPerceptionSystemCode;
    }

    public BigDecimal getSunatPerceptionPercent() {
        return sunatPerceptionPercent;
    }

    public void setSunatPerceptionPercent(BigDecimal sunatPerceptionPercent) {
        this.sunatPerceptionPercent = sunatPerceptionPercent;
    }

    public BigDecimal getTotalInvoiceAmount() {
        return totalInvoiceAmount;
    }

    public void setTotalInvoiceAmount(BigDecimal totalInvoiceAmount) {
        this.totalInvoiceAmount = totalInvoiceAmount;
    }

    public BigDecimal getSunatTotalCashed() {
        return sunatTotalCashed;
    }

    public void setSunatTotalCashed(BigDecimal sunatTotalCashed) {
        this.sunatTotalCashed = sunatTotalCashed;
    }

    public byte[] getXmlDocument() {
        return xmlDocument;
    }

    public void setXmlDocument(byte[] xmlDocument) {
        this.xmlDocument = xmlDocument;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public List<String> getNotes() {
        return notes;
    }

    public void setNotes(List<String> notes) {
        this.notes = notes;
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
        PerceptionEntity other = (PerceptionEntity) obj;
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