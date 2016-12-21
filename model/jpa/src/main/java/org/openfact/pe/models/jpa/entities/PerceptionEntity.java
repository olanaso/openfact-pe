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

@Entity
@Table(name = "PERCEPTION", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "ORGANIZATION_ID", "DOCUMENT_ID" })
})
@NamedQueries({
		@NamedQuery(name = "getAllPerceptionsByOrganization", query = "select p from PerceptionEntity p where p.organizationId = :organizationId order by p.createdTimestamp"),
		@NamedQuery(name = "getAllPerceptionsByOrganizationDesc", query = "select p from PerceptionEntity p where p.organizationId = :organizationId order by p.createdTimestamp desc"),
		@NamedQuery(name = "getOrganizationPerceptionById", query = "select p from PerceptionEntity p where p.id = :id and p.organizationId = :organizationId"),
		@NamedQuery(name = "getOrganizationPerceptionByDocumentId", query = "select p from PerceptionEntity p where p.documentId = :documentId and p.organizationId = :organizationId"),
		@NamedQuery(name = "getOrganizationPerceptionCount", query = "select count(p) from PerceptionEntity p where p.organizationId = :organizationId"),
		@NamedQuery(name = "getLastPerceptionByOrganization", query = "select p from PerceptionEntity p where p.organizationId = :organizationId and length(p.documentId)=:documentIdLength and p.documentId like :formatter order by p.issueDate desc") })
public class PerceptionEntity {

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Access(AccessType.PROPERTY)
	private String id;

	@Column(name = "DOCUMENT_ID")
	private String documentId;

	@Column(name = "UBL_VERSIONID")
	private String ublVersionId;

	@Column(name = "CUSTOMIZATION_ID")
	private String customizationId;

	@NotNull
	@Column(name = "ORGANIZATION_ID")
	private String organizationId;

	@Column(name = "SUNAT_PERCEPTION_SYSTEM_CODE")
	private String sunatPerceptionSystemCode;

	@NotNull
	@Column(name = "ENTITY_DOCUMENT_TYPE")
	private String entityDocumentType;

	@NotNull
	@Column(name = "ENTITY_DOCUMENT_NUMBER")
	private String entityDocumentNuber;

	@NotNull
	@Column(name = "ENTITY_NAME")
	private String entityName;

	@NotNull
	@Column(name = "ENTITY_ADDRESS")
	private String entityAddress;

	@NotNull
	@Column(name = "ENTITY_EMAIL")
	private String entityEmail;

	@NotNull
	@Column(name = "PERCEPTION_DOCUMENT_NUMBER")
	private String perceptionDocumentNumber;

	@NotNull
	@Column(name = "PERCEPTION_DOCUMENT_CURRENCY")
	private String perceptionDocumentCurrency;

	@Column(name = "SUNAT_PERCEPTION_PERCENT")
	private BigDecimal sunatPerceptionPercent;

	@NotNull
	@Column(name = "NOTE")
	private String note;

	@Column(name = "TOTAL_PERCEPTION_AMOUNT")
	private BigDecimal totalPerceptionAmount;

	@Column(name = "SUNAT_TOTAL_CASHED")
	private BigDecimal totalCashed;

	@Column(name = "ISSUE_DATE")
	@Type(type = "org.hibernate.type.LocalDateType")
	private LocalDate issueDate;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "perception")
	private List<PerceptionLineEntity> perceptionLines = new ArrayList<>();

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "XML_DOCUMENT")
	private byte[] xmlDocument;

	@OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy="perception")
	private Collection<PerceptionRequiredActionEntity> requiredActions = new ArrayList<>();

	@OneToMany(cascade = { CascadeType.REMOVE }, orphanRemoval = true, mappedBy = "perception", fetch = FetchType.LAZY)
	private Collection<PerceptionSendEventEntity> sendEvents = new ArrayList<>();

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

	public String getSunatPerceptionSystemCode() {
		return sunatPerceptionSystemCode;
	}

	public void setSunatPerceptionSystemCode(String sunatPerceptionSystemCode) {
		this.sunatPerceptionSystemCode = sunatPerceptionSystemCode;
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

	public String getPerceptionDocumentNumber() {
		return perceptionDocumentNumber;
	}

	public void setPerceptionDocumentNumber(String perceptionDocumentNumber) {
		this.perceptionDocumentNumber = perceptionDocumentNumber;
	}

	public String getPerceptionDocumentCurrency() {
		return perceptionDocumentCurrency;
	}

	public void setPerceptionDocumentCurrency(String perceptionDocumentCurrency) {
		this.perceptionDocumentCurrency = perceptionDocumentCurrency;
	}

	public BigDecimal getSunatPerceptionPercent() {
		return sunatPerceptionPercent;
	}

	public void setSunatPerceptionPercent(BigDecimal sunatPerceptionPercent) {
		this.sunatPerceptionPercent = sunatPerceptionPercent;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public BigDecimal getTotalPerceptionAmount() {
		return totalPerceptionAmount;
	}

	public void setTotalPerceptionAmount(BigDecimal totalPerceptionAmount) {
		this.totalPerceptionAmount = totalPerceptionAmount;
	}

	public BigDecimal getTotalCashed() {
		return totalCashed;
	}

	public void setTotalCashed(BigDecimal totalCashed) {
		this.totalCashed = totalCashed;
	}

	public LocalDate getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(LocalDate issueDate) {
		this.issueDate = issueDate;
	}

	public List<PerceptionLineEntity> getPerceptionLines() {
		return perceptionLines;
	}

	public void setPerceptionLines(List<PerceptionLineEntity> perceptionLines) {
		this.perceptionLines = perceptionLines;
	}

	public byte[] getXmlDocument() {
		return xmlDocument;
	}

	public void setXmlDocument(byte[] xmlDocument) {
		this.xmlDocument = xmlDocument;
	}

	public Collection<PerceptionRequiredActionEntity> getRequiredActions() {
		return requiredActions;
	}

	public void setRequiredActions(Collection<PerceptionRequiredActionEntity> requiredActions) {
		this.requiredActions = requiredActions;
	}

	public Collection<PerceptionSendEventEntity> getSendEvents() {
		return sendEvents;
	}

	public void setSendEvents(Collection<PerceptionSendEventEntity> sendEvents) {
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
		PerceptionEntity other = (PerceptionEntity) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}


}