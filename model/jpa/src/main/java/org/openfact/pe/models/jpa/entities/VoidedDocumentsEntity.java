package org.openfact.pe.models.jpa.entities;

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
import org.openfact.models.jpa.entities.SupplierPartyEntity;

@Entity
@Table(name = "VOIDED_DOCUMENTS", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "ORGANIZATION_ID", "DOCUMENT_ID" })
})
@NamedQueries({
		@NamedQuery(name = "getAllVoidedDocumentsByOrganization", query = "select i from VoidedDocumentsEntity i where i.organizationId = :organizationId order by i.createdTimestamp"),
		@NamedQuery(name = "getAllVoidedDocumentsByOrganizationDesc", query = "select i from VoidedDocumentsEntity i where i.organizationId = :organizationId order by i.createdTimestamp desc"),
		@NamedQuery(name = "getOrganizationVoidedDocumentsById", query = "select i from VoidedDocumentsEntity i where i.id = :id and i.organizationId = :organizationId"),
		@NamedQuery(name = "getOrganizationVoidedDocumentsByID", query = "select i from VoidedDocumentsEntity i where i.documentId = :documentId and i.organizationId = :organizationId"),
		@NamedQuery(name = "getAllVoidedDocumentsByRequiredActionAndOrganization", query = "select c from VoidedDocumentsEntity c inner join c.requiredActions r where c.organizationId = :organizationId and r.action in :requiredAction order by c.issueDateTime"),
		@NamedQuery(name = "searchForVoidedDocuments", query = "select i from VoidedDocumentsEntity i where i.organizationId = :organizationId and i.documentId like :search order by i.issueDate"),
		@NamedQuery(name = "getOrganizationVoidedDocumentsCount", query = "select count(i) from VoidedDocumentsEntity i where i.organizationId = :organizationId"),
		@NamedQuery(name = "getLastVoidedDocumentsByOrganization", query = "select i from VoidedDocumentsEntity i where i.organizationId = :organizationId and length(i.documentId)=:documentIdLength and i.documentId like :formatter order by i.issueDate desc") })
public class VoidedDocumentsEntity {

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

	@Column(name = "CUSTOMIZATION_ID")
	private String customizationId;

	@Column(name = "REFERENCE_DATE")
	@Type(type = "org.hibernate.type.LocalDateType")
	private LocalDate referenceDate;

	@Column(name = "ISSUE_DATE")
	@Type(type = "org.hibernate.type.LocalDateType")
	private LocalDate issueDate;

	@Column(name = "DOCUMENT_CURRENCY_CODE")
	private String documentCurrencyCode;

	@ElementCollection
	@Column(name = "VALUE")
	@CollectionTable(name = "BAJA_NOTE", joinColumns = { @JoinColumn(name = "BAJA_ID") })
	private List<String> notes = new ArrayList<>();

	@OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy="voidedDocuments")
	private Collection<VoidedDocumentsRequiredActionEntity> requiredActions = new ArrayList<>();

	@OneToMany(cascade = { CascadeType.REMOVE }, orphanRemoval = true, mappedBy = "voidedDocuments", fetch = FetchType.LAZY)
	private Collection<VoidedDocumentsSendEventEntity> sendEvents = new ArrayList<>();

	@Lob
	@Column(name = "XML_DOCUMENT")
	private byte[] xmlDocument;

	@NotNull
	@Column(name = "ORGANIZATION_ID")
	private String organizationId;

	/*@ManyToOne(targetEntity = SupplierPartyEntity.class, cascade = { CascadeType.ALL })
	@JoinColumn(name = "ACCOUNTINGSUPPLIERPARTY_VOIDEDDOCUMENTS")
	private SupplierPartyEntity accountingSupplierParty = new SupplierPartyEntity();*/

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

	public LocalDate getReferenceDate() {
		return referenceDate;
	}

	public void setReferenceDate(LocalDate referenceDate) {
		this.referenceDate = referenceDate;
	}

	public LocalDate getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(LocalDate issueDate) {
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

	public Collection<VoidedDocumentsRequiredActionEntity> getRequiredActions() {
		return requiredActions;
	}

	public void setRequiredActions(Collection<VoidedDocumentsRequiredActionEntity> requiredActions) {
		this.requiredActions = requiredActions;
	}

	public Collection<VoidedDocumentsSendEventEntity> getSendEvents() {
		return sendEvents;
	}

	public void setSendEvents(Collection<VoidedDocumentsSendEventEntity> sendEvents) {
		this.sendEvents = sendEvents;
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

	public LocalDateTime getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(LocalDateTime createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}
}
