package org.openfact.models.jpa.pe.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.openfact.models.jpa.entities.ubl.CreditNoteEntity;

@Entity
@Table(name = "VOIDED_DOCUMENTS_SEND_EVENT")
@NamedQueries({
		@NamedQuery(name = "getAllSendEventsByVoidedDocuments", query = "select i from VoidedDocumentsSendEventEntity i join i.voidedDocuments a where a.id = :voidedDocumentsId order by i.createdTimestamp") })

public class VoidedDocumentsSendEventEntity {
	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Access(AccessType.PROPERTY)
	private String id;
	@ManyToMany(cascade = { CascadeType.ALL })
	protected List<VoidedDocumentsEntity> voidedDocuments = new ArrayList<>();
	@Column(name = "ID_UBL")
	protected String documentId;
	@Column(name = "IS_ACCEPTED")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	boolean accepted;
	@Column(name = "DESCRIPTION")
	protected String description;
	@Column(name = "NOTE")
	protected String note;
	@Column(name = "RESPONSE_CODE")
	protected String responseCode;
	@Column(name = "ERROR_MESSAGE")
	protected String errorMessage;
	@Column(name = "DIGEST_VALUE")
	protected String digestValue;
	@Column(name = "BAR_CODE")
	protected String barCode;
	@Lob
	@Column(name = "DOCUMENT_SUBMITTED")
	protected byte[] documentSubmitted;
	@Lob
	@Column(name = "DOCUMENT_RESPONSE")
	protected byte[] documentResponse;
	@Lob
	@Column(name = "CUSTOMER_DOCUMENT")
	protected byte[] customerDocument;
	@ElementCollection
	@MapKeyColumn(name = "NAME")
	@Column(name = "VALUE")
	@CollectionTable(name = "WARNING", joinColumns = { @JoinColumn(name = "CREDIT_NOTE_SEND_EVENT_ID") })
	protected Map<String, String> warning = new HashMap<String, String>();
	@ElementCollection
	@MapKeyColumn(name = "NAME")
	@Column(name = "VALUE")
	@CollectionTable(name = "SUCCESS", joinColumns = { @JoinColumn(name = "CREDIT_NOTE_SEND_EVENT_ID") })
	protected Map<String, String> success = new HashMap<String, String>();
	@Type(type = "org.hibernate.type.LocalDateTimeType")
	@Column(name = "CREATED_TIMESTAMP")
	protected LocalDateTime createdTimestamp;

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

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getDigestValue() {
		return digestValue;
	}

	public void setDigestValue(String digestValue) {
		this.digestValue = digestValue;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public byte[] getDocumentSubmitted() {
		return documentSubmitted;
	}

	public void setDocumentSubmitted(byte[] documentSubmitted) {
		this.documentSubmitted = documentSubmitted;
	}

	public byte[] getDocumentResponse() {
		return documentResponse;
	}

	public void setDocumentResponse(byte[] documentResponse) {
		this.documentResponse = documentResponse;
	}

	public byte[] getCustomerDocument() {
		return customerDocument;
	}

	public void setCustomerDocument(byte[] customerDocument) {
		this.customerDocument = customerDocument;
	}

	public Map<String, String> getWarning() {
		return warning;
	}

	public void setWarning(Map<String, String> warning) {
		this.warning = warning;
	}

	public Map<String, String> getSuccess() {
		return success;
	}

	public void setSuccess(Map<String, String> success) {
		this.success = success;
	}

	public LocalDateTime getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(LocalDateTime createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public List<VoidedDocumentsEntity> getVoidedDocuments() {
		return voidedDocuments;
	}

	public void setVoidedDocuments(List<VoidedDocumentsEntity> voidedDocuments) {
		this.voidedDocuments = voidedDocuments;
	}
}
