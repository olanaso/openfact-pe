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
import org.openfact.models.jpa.entities.*;

@Entity
@Table(name = "PERCEPTION", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "ORGANIZATION_ID", "DOCUMENT_ID" })
})
@NamedQueries({
		@NamedQuery(name = "getAllPerceptionsByOrganization", query = "select p from PerceptionEntity p where p.organizationId = :organizationId order by p.createdTimestamp"),
		@NamedQuery(name = "getAllPerceptionsByOrganizationDesc", query = "select p from PerceptionEntity p where p.organizationId = :organizationId order by p.createdTimestamp desc"),
		@NamedQuery(name = "getOrganizationPerceptionById", query = "select p from PerceptionEntity p where p.id = :id and p.organizationId = :organizationId"),
		//@NamedQuery(name = "getAllPerceptionsByRequiredActionAndOrganization", query = "select c from PerceptionEntity c inner join c.requiredActions r where c.organizationId = :organizationId and r.action in :requiredAction order by c.issueDateTime"),
		@NamedQuery(name = "getOrganizationPerceptionByDocumentId", query = "select p from PerceptionEntity p where p.documentId = :documentId and p.organizationId = :organizationId"),
		//@NamedQuery(name = "searchForPerception", query = "select p from PerceptionEntity p where p.organizationId = :organizationId and p.ID like :search order by p.issueDate"),
		@NamedQuery(name = "getOrganizationPerceptionCount", query = "select count(i) from PerceptionEntity p where p.organizationId = :organizationId"),
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

	@Column(name = "DOCUMENT_CURRENCY_CODE")
	private String documentCurrencyCode;

	@Column(name = "ISSUE_DATE")
	@Type(type = "org.hibernate.type.LocalDateType")
	private LocalDate issueDate;

	@Column(name = "SUNAT_PERCEPTION_SYSTEM_CODE")
	private String sunatPerceptionSystemCode;

	@Column(name = "SUNAT_PERCEPTION_PERCENT")
	private BigDecimal sunatPerceptionPercent;

	@Column(name = "TOTAL_INVOICE_AMOUNT")
	private BigDecimal totalInvoiceAmount;

	@Column(name = "SUNAT_TOTAL_CASHED")
	private BigDecimal sunatTotalCashed;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "XML_DOCUMENT")
	private byte[] xmlDocument;

	@NotNull
	@Column(name = "ORGANIZATION_ID")
	private String organizationId;

	@ElementCollection
	@Column(name = "VALUE")
	@CollectionTable(name = "PERCEPTION_NOTE", joinColumns = { @JoinColumn(name = "PERCEPTION_ID") })
	private List<String> notes = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "perception")
	private List<PerceptionDocumentReferenceEntity> sunatPerceptionDocumentReferences = new ArrayList<>();

	@OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy="perception")
	private Collection<PerceptionRequiredActionEntity> requiredActions = new ArrayList<>();

	@OneToMany(cascade = { CascadeType.REMOVE }, orphanRemoval = true, mappedBy = "perception", fetch = FetchType.LAZY)
	private Collection<PerceptionSendEventEntity> sendEvents = new ArrayList<>();

	@Type(type = "org.hibernate.type.LocalDateTimeType")
	@Column(name = "CREATED_TIMESTAMP")
	private LocalDateTime createdTimestamp;

	/*@ManyToOne(targetEntity = PartyEntity.class, cascade = { CascadeType.ALL })
	@JoinColumn(name = "AGENTPARTY_PERCEPTION_ID")
	private PartyEntity agentParty;*/

	/*@ManyToOne(targetEntity = PartyEntity.class, cascade = { CascadeType.ALL })
	@JoinColumn(name = "RECEIVERPARTY_PERCEPTION_ID")
	private PartyEntity receiverParty;*/


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

	public String getDocumentCurrencyCode() {
		return documentCurrencyCode;
	}

	public void setDocumentCurrencyCode(String documentCurrencyCode) {
		this.documentCurrencyCode = documentCurrencyCode;
	}

	public LocalDate getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(LocalDate issueDate) {
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

	public List<PerceptionDocumentReferenceEntity> getSunatPerceptionDocumentReferences() {
		return sunatPerceptionDocumentReferences;
	}

	public void setSunatPerceptionDocumentReferences(List<PerceptionDocumentReferenceEntity> sunatPerceptionDocumentReferences) {
		this.sunatPerceptionDocumentReferences = sunatPerceptionDocumentReferences;
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

	/**
	 * Openfact core
	 */
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