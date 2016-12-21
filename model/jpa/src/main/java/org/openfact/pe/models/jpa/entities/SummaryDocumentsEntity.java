package org.openfact.pe.models.jpa.entities;

import java.time.LocalDate;
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
@Table(name = "SUMMARY_DOCUMENTS", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "ORGANIZATION_ID", "DOCUMENT_ID" }) })
@NamedQueries({
		@NamedQuery(name = "getAllSummaryDocumentsByOrganization", query = "select i from SummaryDocumentsEntity i where i.organizationId = :organizationId order by i.createdTimestamp"),
		@NamedQuery(name = "getAllSummaryDocumentsByOrganizationDesc", query = "select i from SummaryDocumentsEntity i where i.organizationId = :organizationId order by i.createdTimestamp desc"),
		@NamedQuery(name = "getOrganizationSummaryDocumentsById", query = "select i from SummaryDocumentsEntity i where i.id = :id and i.organizationId = :organizationId"),
		@NamedQuery(name = "getOrganizationSummaryDocumentsByID", query = "select i from SummaryDocumentsEntity i where i.documentId = :documentId and i.organizationId = :organizationId"),
		//@NamedQuery(name = "getAllSummaryDocumentsByRequiredActionAndOrganization", query = "select c from SummaryDocumentsEntity c inner join c.requiredActions r where c.organizationId = :organizationId and r.action in :requiredAction order by c.issueDateTime"),
		@NamedQuery(name = "searchForSummaryDocuments", query = "select i from SummaryDocumentsEntity i where i.organizationId = :organizationId and i.documentId like :search order by i.issueDate"),
		@NamedQuery(name = "getOrganizationSummaryDocumentsCount", query = "select count(i) from SummaryDocumentsEntity i where i.organizationId = :organizationId"),
		@NamedQuery(name = "getLastSummaryDocumentsByOrganization", query = "select i from SummaryDocumentsEntity i where i.organizationId = :organizationId and length(i.documentId)=:documentIdLength and i.documentId like :formatter order by i.issueDate desc"), })
public class SummaryDocumentsEntity {

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

	@Column(name = "CUSTOMIZATIONID")
	private String customizationId;

	@Column(name = "REFERENCE_DATE")
	@Type(type = "org.hibernate.type.LocalDateType")
	private LocalDate referenceDate;

	@Column(name = "DOCUMENT_CURRENCY_CODE")
	private String documentCurrencyCode;

	@Column(name = "ISSUE_DATE")
	@Type(type = "org.hibernate.type.LocalDateType")
	private LocalDate issueDate;

	@OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy="summaryDocuments")
	private Collection<SummaryDocumentsRequiredActionEntity> requiredActions = new ArrayList<>();

	@OneToMany(cascade = { CascadeType.REMOVE }, orphanRemoval = true, mappedBy = "summaryDocuments", fetch = FetchType.LAZY)
	private Collection<SummaryDocumentsSendEventEntity> sendEvents = new ArrayList<>();

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

	public Collection<SummaryDocumentsRequiredActionEntity> getRequiredActions() {
		return requiredActions;
	}

	public void setRequiredActions(Collection<SummaryDocumentsRequiredActionEntity> requiredActions) {
		this.requiredActions = requiredActions;
	}

	public Collection<SummaryDocumentsSendEventEntity> getSendEvents() {
		return sendEvents;
	}

	public void setSendEvents(Collection<SummaryDocumentsSendEventEntity> sendEvents) {
		this.sendEvents = sendEvents;
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

	/**
	 * Openfact core
	 */
}