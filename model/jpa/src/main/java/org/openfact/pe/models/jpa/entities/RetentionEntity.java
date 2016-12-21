package org.openfact.pe.models.jpa.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
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
import org.openfact.models.jpa.entities.PartyEntity;

@Entity
@Table(name = "RETENTION", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "ORGANIZATION_ID", "DOCUMENT_ID" }) })
@NamedQueries({
		@NamedQuery(name = "getAllRetentionsByOrganization", query = "select r from RetentionEntity r where r.organizationId = :organizationId order by r.createdTimestamp"),
		@NamedQuery(name = "getAllRetentionsByOrganizationDesc", query = "select r from RetentionEntity r where r.organizationId = :organizationId order by r.createdTimestamp desc"),
		@NamedQuery(name = "getOrganizationRetentionById", query = "select r from RetentionEntity r where r.id = :id and r.organizationId = :organizationId"),
//		@NamedQuery(name = "getAllRetentionsByRequiredActionAndOrganization", query = "select c from RetentionEntity c inner join c.requiredActions r where c.organizationId = :organizationId and r.action in :requiredAction order by c.issueDateTime"),
		@NamedQuery(name = "getOrganizationRetentionByDocumentId", query = "select r from RetentionEntity r where r.documentId = :documentId and r.organizationId = :organizationId"),
		@NamedQuery(name = "searchForRetention", query = "select r from RetentionEntity r where r.organizationId = :organizationId and r.documentId like :search order by r.issueDate"),
		@NamedQuery(name = "getOrganizationRetentionCount", query = "select count(r) from RetentionEntity r where r.organizationId = :organizationId"),
		@NamedQuery(name = "getLastRetentionByOrganization", query = "select r from RetentionEntity r where r.organizationId = :organizationId and length(r.documentId)=:documentIdLength and r.documentId like :formatter order by r.issueDate desc"),
	})
public class RetentionEntity {

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Access(AccessType.PROPERTY)
	private String id;

	@Column(name = "DOCUMENT_ID")
	private String documentId;

	@Column(name = "UBL_VERSION_ID")
	private String ublVersionId;

	@Column(name = "DOCUMENT_CURRENCY_CODE")
	private String documentCurrencyCode;

	@Column(name = "CUSTOMIZATION_ID")
	private String customizationId;

	@Column(name = "ISSUE_DATE")
	@Type(type = "org.hibernate.type.LocalDateType")
	private LocalDate issueDate;

	@Column(name = "TOTAL_INVOICE_AMOUNT")
	private BigDecimal totalInvoiceAmount;

	@Column(name = "TOTAL_PAID")
	private BigDecimal totalPaid;

	@Column(name = "SUNAT_RETENTION_SYSTEM_CODE")
	private String sunatRetentionSystemCode;

	@Column(name = "SUNAT_RETENTION_PERCENT")
	private BigDecimal sunatRetentionPercent;

	@ElementCollection
	@Column(name = "VALUE")
	@CollectionTable(name = "RETENTION_NOTE", joinColumns = { @JoinColumn(name = "RETENTION_ID") })
	private List<String> notes = new ArrayList<>();

	@OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy="retention")
	private Collection<RetentionRequiredActionEntity> requiredActions = new ArrayList<>();

	@OneToMany(cascade = { CascadeType.REMOVE }, orphanRemoval = true, mappedBy = "retention", fetch = FetchType.LAZY)
	private Collection<RetentionSendEventEntity> sendEvents = new ArrayList<>();

	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "retention", cascade = CascadeType.ALL)
	private List<RetentionDocumentReferenceEntity> sunatRetentionDocumentReferences = new ArrayList<>();

	/*@ManyToOne(targetEntity = PartyEntity.class, cascade = { CascadeType.ALL })
	@JoinColumn(name = "AGENTPARTY_PERCEPTION_ID")
	private PartyEntity agentParty;

	@ManyToOne(targetEntity = PartyEntity.class, cascade = { CascadeType.ALL })
	@JoinColumn(name = "RECEIVERPARTY_PERCEPTION_ID")
	private PartyEntity receiverParty;*/

	@Type(type = "org.hibernate.type.LocalDateTimeType")
	@Column(name = "CREATED_TIMESTAMP")
	private LocalDateTime createdTimestamp;
	@Lob
	@Column(name = "XML_DOCUMENT")
	private byte[] xmlDocument;

	@NotNull
	@Column(name = "ORGANIZATION_ID")
	private String organizationId;

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

	public String getDocumentCurrencyCode() {
		return documentCurrencyCode;
	}

	public void setDocumentCurrencyCode(String documentCurrencyCode) {
		this.documentCurrencyCode = documentCurrencyCode;
	}

	public String getCustomizationId() {
		return customizationId;
	}

	public void setCustomizationId(String customizationId) {
		this.customizationId = customizationId;
	}

	public LocalDate getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(LocalDate issueDate) {
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

	public List<RetentionDocumentReferenceEntity> getSunatRetentionDocumentReferences() {
		return sunatRetentionDocumentReferences;
	}

	public void setSunatRetentionDocumentReferences(List<RetentionDocumentReferenceEntity> sunatRetentionDocumentReferences) {
		this.sunatRetentionDocumentReferences = sunatRetentionDocumentReferences;
	}

	public LocalDateTime getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(LocalDateTime createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
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
