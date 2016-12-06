package org.openfact.pe.models.jpa.entities;

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
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.openfact.models.jpa.entities.OrganizationEntity;

@Entity
@Table(name = "VOIDED_DOCUMENTS", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "ORGANIZATION_ID", "DOCUMENT_ID" }) })
@NamedQueries({
        @NamedQuery(name = "getAllVoidedDocumentsByOrganization", query = "select i from VoidedDocumentsEntity i where i.organizationId = :organizationId order by i.issueDate"),
        @NamedQuery(name = "getOrganizationVoidedDocumentsById", query = "select i from VoidedDocumentsEntity i where i.id = :id and i.organizationId = :organizationId"),
        @NamedQuery(name = "getOrganizationVoidedDocumentsByID", query = "select i from VoidedDocumentsEntity i where i.documentId = :documentId and i.organizationId = :organizationId"),
        @NamedQuery(name = "searchForVoidedDocuments", query = "select i from VoidedDocumentsEntity i where i.organizationId = :organizationId and i.documentId like :search order by i.issueDate"),
        @NamedQuery(name = "getOrganizationVoidedDocumentsCount", query = "select count(i) from VoidedDocumentsEntity i where i.organizationId = :organizationId"),
        @NamedQuery(name = "getLastVoidedDocumentsByOrganization", query = "select i from VoidedDocumentsEntity i where i.organizationId = :organizationId and length(i.documentId)=:documentIdLength and i.documentId like :formatter order by i.issueDate desc") })
public class VoidedDocumentsEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Access(AccessType.PROPERTY)
    protected String id;
    
    @ManyToMany(mappedBy = "voidedDocuments", cascade = { CascadeType.ALL })
	protected List<VoidedDocumentsSendEventEntity> sendEvents = new ArrayList<>();
    
    @Column(name = "DOCUMENT_ID")
    protected String documentId;

    @Column(name = "UBL_VERSION_ID")
    protected String ublVersionId;

    @Column(name = "CUSTOMIZATION_ID")
    protected String customizationId;

    @Column(name = "REFERENCE_DATE")
    @Type(type = "org.hibernate.type.LocalDateTimeType")
    protected LocalDateTime referenceDate;

    @Column(name = "ISSUE_DATE")
    @Type(type = "org.hibernate.type.LocalDateTimeType")
    protected LocalDateTime issueDate;

    @Column(name = "DOCUMENT_CURRENCY_CODE")
    protected String documentCurrencyCode;

    @ElementCollection
    @Column(name = "VALUE")
    @CollectionTable(name = "BAJA_NOTE", joinColumns = { @JoinColumn(name = "BAJA_ID") })
    protected List<String> notes = new ArrayList<>();

//    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "voidedDocument", cascade = CascadeType.REMOVE)
//    protected Collection<VoidedDocumentsRequiredActionEntity> requiredActions = new ArrayList<>();
//
//    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "voidedDocument", cascade = CascadeType.ALL)
//    protected List<VoidedDocumentsLineEntity> voidedDocumentLines = new ArrayList<>();

    @Lob
    @Column(name = "XML_DOCUMENT")
    protected byte[] xmlDocument;

    @NotNull
    @Column(name = "ORGANIZATION_ID")
    protected String organizationId;

    /** Openfact Core relations */
//    @ManyToOne(targetEntity = UBLExtensionsEntity.class, cascade = { CascadeType.ALL })
//    @JoinColumn(name = "UBLEXTENSIONS_VOIDED_DOCUMENTS_ID")
//    protected UBLExtensionsEntity ublExtensions;
//
//    @ManyToOne(targetEntity = SupplierPartyEntity.class, cascade = { CascadeType.ALL })
//    @JoinColumn(name = "ACCOUNTINGSUPPLIERPARTY_VOIDEDDOCUMENTS")
//    protected SupplierPartyEntity accountingSupplierParty = new SupplierPartyEntity();
//
//    @OneToMany(targetEntity = SignatureEntity.class, cascade = { CascadeType.ALL })
//    @JoinColumn(name = "SIGNATURE_VOIDEDDOCUMENT")
//    protected List<SignatureEntity> signature = new ArrayList<>();
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey, name = "ORGANIZATION_ID")
    private OrganizationEntity organization;

    @Type(type = "org.hibernate.type.LocalDateTimeType")
    @Column(name = "CREATED_TIMESTAMP")
    private LocalDateTime createdTimestamp;
    
    public OrganizationEntity getOrganization() {
		return organization;
	}

	public void setOrganization(OrganizationEntity organization) {
		this.organization = organization;
	}

	public LocalDateTime getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(LocalDateTime createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

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

    public LocalDateTime getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDateTime issueDate) {
        this.issueDate = issueDate;
    }

    public String getDocumentCurrencyCode() {
        return documentCurrencyCode;
    }

    public void setDocumentCurrencyCode(String documentCurrencyCode) {
        this.documentCurrencyCode = documentCurrencyCode;
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

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }
    
    public List<VoidedDocumentsSendEventEntity> getSendEvents() {
		return sendEvents;
	}

	public void setSendEvents(List<VoidedDocumentsSendEventEntity> sendEvents) {
		this.sendEvents = sendEvents;
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
        VoidedDocumentsEntity other = (VoidedDocumentsEntity) obj;
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
