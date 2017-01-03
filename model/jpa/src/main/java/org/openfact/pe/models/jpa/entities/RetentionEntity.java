package org.openfact.pe.models.jpa.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.openfact.models.jpa.entities.OrganizationEntity;
import org.openfact.models.jpa.entities.PartyEntity;

@Entity
@Table(name = "RETENTION", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "ORGANIZATION_ID", "DOCUMENT_ID" }) })
@NamedQueries({
		@NamedQuery(name = "getAllRetentionsByOrganization", query = "select r from RetentionEntity r where r.organizationId = :organizationId order by r.createdTimestamp"),
		@NamedQuery(name = "getAllRetentionsIdsByOrganization", query = "select r.documentId from RetentionEntity r where r.organizationId = :organizationId order by r.createdTimestamp"),
		@NamedQuery(name = "getAllRetentionsByOrganizationDesc", query = "select r from RetentionEntity r where r.organizationId = :organizationId order by r.createdTimestamp desc"),
		@NamedQuery(name = "getOrganizationRetentionById", query = "select r from RetentionEntity r where r.id = :id and r.organizationId = :organizationId"),
		@NamedQuery(name = "getOrganizationRetentionByDocumentId", query = "select r from RetentionEntity r where r.documentId = :documentId and r.organizationId = :organizationId"),
		@NamedQuery(name = "searchForRetention", query = "select r from RetentionEntity r where r.organizationId = :organizationId and r.documentId like :search order by r.issueDateTime"),
		@NamedQuery(name = "getOrganizationRetentionCount", query = "select count(r) from RetentionEntity r where r.organizationId = :organizationId"),
		@NamedQuery(name = "getLastRetentionByOrganization", query = "select r from RetentionEntity r where r.organizationId = :organizationId and length(r.documentId)=:documentIdLength and r.documentId like :formatter order by r.issueDateTime desc"),
	})
public class RetentionEntity {


	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Access(AccessType.PROPERTY)
	private String id;

	@NotNull
	@Column(name = "DOCUMENT_ID")
	private String documentId;
	@Column(name = "UBL_VERSIONID")
	private String ublVersionId;
	@Column(name = "CUSTOMIZATION_ID")
	private String customizationId;

	@NotNull
	@Column(name = "ORGANIZATION_ID")
	private String organizationId;

	@Column(name = "SUNAT_RETENTION_SYSTEM_CODE")
	private String sunatRetentionSystemCode;

	@Column(name = "ENTITY_DOCUMENT_TYPE")
	private String entityDocumentType;

	@Column(name = "ENTITY_DOCUMENT_NUMBER")
	private String entityDocumentNuber;

	@Column(name = "ENTITY_NAME")
	private String entityName;

	@Column(name = "ENTITY_ADDRESS")
	private String entityAddress;

	@Column(name = "ENTITY_EMAIL")
	private String entityEmail;

	@Column(name = "DOCUMENT_CURRENCY_CODE")
	private String documentCurrencyCode;

	@Column(name = "SUNAT_RETENTION_PERCENT")
	private BigDecimal sunatRetentionPercent;

	@Column(name = "NOTE")
	private String note;

	@Column(name = "TOTAL_RETENTION_AMOUNT")
	private BigDecimal totalRetentionAmount;

	@Column(name = "SUNAT_TOTAL_CASHED")
	private BigDecimal totalCashed;

	@Column(name = "ISSUE_DATE")
	@Type(type = "org.hibernate.type.LocalDateTimeType")
	private LocalDateTime issueDateTime;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "retention")
	private List<RetentionLineEntity> retentionLines = new ArrayList<>();

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "XML_DOCUMENT")
	private byte[] xmlDocument;

	@OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy="retention")
	private Collection<RetentionRequiredActionEntity> requiredActions = new ArrayList<>();

	@OneToMany(cascade = { CascadeType.REMOVE }, orphanRemoval = true, mappedBy = "retention", fetch = FetchType.LAZY)
	private Collection<RetentionSendEventEntity> sendEvents = new ArrayList<>();

	@Type(type = "org.hibernate.type.LocalDateTimeType")
	@Column(name = "CREATED_TIMESTAMP")
	private LocalDateTime createdTimestamp;

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

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getSunatRetentionSystemCode() {
		return sunatRetentionSystemCode;
	}

	public void setSunatRetentionSystemCode(String sunatRetentionSystemCode) {
		this.sunatRetentionSystemCode = sunatRetentionSystemCode;
	}

	public String getEntityDocumentType() {
		return entityDocumentType;
	}

	public void setEntityDocumentType(String entityDocumentType) {
		this.entityDocumentType = entityDocumentType;
	}

	public String getEntityDocumentNuber() {
		return entityDocumentNuber;
	}

	public void setEntityDocumentNuber(String entityDocumentNuber) {
		this.entityDocumentNuber = entityDocumentNuber;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getEntityAddress() {
		return entityAddress;
	}

	public void setEntityAddress(String entityAddress) {
		this.entityAddress = entityAddress;
	}

	public String getEntityEmail() {
		return entityEmail;
	}

	public void setEntityEmail(String entityEmail) {
		this.entityEmail = entityEmail;
	}

	public String getDocumentCurrencyCode() {
		return documentCurrencyCode;
	}

	public void setDocumentCurrencyCode(String documentCurrencyCode) {
		this.documentCurrencyCode = documentCurrencyCode;
	}

	public BigDecimal getSunatRetentionPercent() {
		return sunatRetentionPercent;
	}

	public void setSunatRetentionPercent(BigDecimal sunatRetentionPercent) {
		this.sunatRetentionPercent = sunatRetentionPercent;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public BigDecimal getTotalRetentionAmount() {
		return totalRetentionAmount;
	}

	public void setTotalRetentionAmount(BigDecimal totalRetentionAmount) {
		this.totalRetentionAmount = totalRetentionAmount;
	}

	public BigDecimal getTotalCashed() {
		return totalCashed;
	}

	public void setTotalCashed(BigDecimal totalCashed) {
		this.totalCashed = totalCashed;
	}

	public LocalDateTime getIssueDateTime() {
		return issueDateTime;
	}

	public void setIssueDateTime(LocalDateTime issueDateTime) {
		this.issueDateTime = issueDateTime;
	}

	public List<RetentionLineEntity> getRetentionLines() {
		return retentionLines;
	}

	public void setRetentionLines(List<RetentionLineEntity> retentionLines) {
		this.retentionLines = retentionLines;
	}

	public byte[] getXmlDocument() {
		return xmlDocument;
	}

	public void setXmlDocument(byte[] xmlDocument) {
		this.xmlDocument = xmlDocument;
	}

	public Collection<RetentionRequiredActionEntity> getRequiredActions() {
		return requiredActions;
	}

	public void setRequiredActions(Collection<RetentionRequiredActionEntity> requiredActions) {
		this.requiredActions = requiredActions;
	}

	public Collection<RetentionSendEventEntity> getSendEvents() {
		return sendEvents;
	}

	public void setSendEvents(Collection<RetentionSendEventEntity> sendEvents) {
		this.sendEvents = sendEvents;
	}

	public LocalDateTime getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(LocalDateTime createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getDocumentId() == null) ? 0 : getDocumentId().hashCode());
		result = prime * result + ((getOrganizationId() == null) ? 0 : getOrganizationId().hashCode());
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
		if (getDocumentId() == null) {
			if (other.getDocumentId() != null)
				return false;
		} else if (!getDocumentId().equals(other.getDocumentId()))
			return false;
		if (getOrganizationId() == null) {
			if (other.getOrganizationId() != null)
				return false;
		} else if (!getOrganizationId().equals(other.getOrganizationId()))
			return false;
		return true;
	}
}
