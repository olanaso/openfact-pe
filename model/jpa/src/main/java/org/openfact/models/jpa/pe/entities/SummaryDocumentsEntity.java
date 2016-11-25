package org.openfact.models.jpa.pe.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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
@Table(name = "SUMMARY_DOCUMENTS", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "ORGANIZATION_ID", "DOCUMENT_ID" }) })
@NamedQueries({
        @NamedQuery(name = "getAllSummaryDocumentsByOrganization", query = "select i from SummaryDocumentsEntity i where i.organizationId = :organizationId order by i.issueDate"),
        @NamedQuery(name = "getOrganizationSummaryDocumentsById", query = "select i from SummaryDocumentsEntity i where i.id = :id and i.organizationId = :organizationId"),
        @NamedQuery(name = "getOrganizationSummaryDocumentsByID", query = "select i from SummaryDocumentsEntity i where i.documentId = :documentId and i.organizationId = :organizationId"),
        @NamedQuery(name = "searchForSummaryDocuments", query = "select i from SummaryDocumentsEntity i where i.organizationId = :organizationId and i.documentId like :search order by i.issueDate"),
        @NamedQuery(name = "getOrganizationSummaryDocumentsCount", query = "select count(i) from SummaryDocumentsEntity i where i.organizationId = :organizationId"),
        @NamedQuery(name = "getLastSummaryDocumentsByOrganization", query = "select i from SummaryDocumentsEntity i where i.organizationId = :organizationId and length(i.documentId)=:documentIdLength and i.documentId like :formatter order by i.issueDate desc"), })
public class SummaryDocumentsEntity {

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

    @Column(name = "CUSTOMIZATIONID")
    protected String customizationId;

    @Column(name = "REFERENCE_DATE")
    @Type(type = "org.hibernate.type.LocalDateTimeType")
    protected LocalDateTime referenceDate;

    @Column(name = "DOCUMENT_CURRENCY_CODE")
    protected String documentCurrencyCode;

    @Column(name = "ISSUE_DATE")
    @Type(type = "org.hibernate.type.LocalDateTimeType")
    protected LocalDateTime issueDate;

    @Lob
    @Column(name = "XML_DOCUMENT")
    protected byte[] xmlDocument;

    @NotNull
    @Column(name = "ORGANIZATION_ID")
    protected String organizationId;

    /**
     * Openfact core
     */    
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

    public LocalDateTime getReferenceDate() {
        return referenceDate;
    }

    public void setReferenceDate(LocalDateTime referenceDate) {
        this.referenceDate = referenceDate;
    }

    public String getDocumentCurrencyCode() {
        return documentCurrencyCode;
    }

    public void setDocumentCurrencyCode(String documentCurrencyCode) {
        this.documentCurrencyCode = documentCurrencyCode;
    }

    public LocalDateTime getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDateTime issueDate) {
        this.issueDate = issueDate;
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

}