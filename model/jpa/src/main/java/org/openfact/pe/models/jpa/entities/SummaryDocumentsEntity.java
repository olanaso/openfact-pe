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

	@ManyToMany(mappedBy = "summaryDocuments", cascade = { CascadeType.ALL })
	protected List<SummaryDocumentsSendEventEntity> sendEvents = new ArrayList<>();

	@Column(name = "DOCUMENT_ID")
	protected String documentId;

	@Column(name = "UBL_VERSIONID")
	protected String ublVersionId;

	@Column(name = "CUSTOMIZATIONID")
	protected String customizationId;

	@Column(name = "REFERENCE_DATE")
	@Type(type = "org.hibernate.type.LocalDateType")
	protected LocalDate referenceDate;

	@Column(name = "DOCUMENT_CURRENCY_CODE")
	protected String documentCurrencyCode;

	@Column(name = "ISSUE_DATE")
	@Type(type = "org.hibernate.type.LocalDateTimeType")
	protected LocalDateTime issueDate;

	// @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, mappedBy =
	// "summaryDocument", cascade = CascadeType.ALL)
	// protected List<SumaryDocumenstLineEntity> summaryDocumentsLines = new
	// ArrayList<>();

	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "summaryDocument", cascade = CascadeType.REMOVE)
	protected Collection<SummaryDocumentsRequiredActionEntity> requiredActions = new ArrayList<>();
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(foreignKey = @ForeignKey, name = "ORGANIZATION_ID")
	private OrganizationEntity organization;

	@Type(type = "org.hibernate.type.LocalDateTimeType")
	@Column(name = "CREATED_TIMESTAMP")
	private LocalDateTime createdTimestamp;
	@Lob
	@Column(name = "XML_DOCUMENT")
	protected byte[] xmlDocument;

	@NotNull
	@Column(name = "ORGANIZATION_ID")
	protected String organizationId;

	/**
	 * Openfact core
	 */
	@ManyToOne(targetEntity = SupplierPartyEntity.class, cascade = { CascadeType.ALL })
	@JoinColumn(name = "ACCOUNTINGSUPPLIERPARTY_SUMMARYDOCUMENTS_ID")
	protected SupplierPartyEntity accountingSupplierParty;

	public String getId() {
		return id;
	}

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

	public List<SummaryDocumentsSendEventEntity> getSendEvents() {
		return sendEvents;
	}

	public void setSendEvents(List<SummaryDocumentsSendEventEntity> sendEvents) {
		this.sendEvents = sendEvents;
	}

	public Collection<SummaryDocumentsRequiredActionEntity> getRequiredActions() {
		return requiredActions;
	}

	public void setRequiredActions(Collection<SummaryDocumentsRequiredActionEntity> requiredActions) {
		this.requiredActions = requiredActions;
	}

	public SupplierPartyEntity getAccountingSupplierParty() {
		return accountingSupplierParty;
	}

	public void setAccountingSupplierParty(SupplierPartyEntity accountingSupplierParty) {
		this.accountingSupplierParty = accountingSupplierParty;
	}

}