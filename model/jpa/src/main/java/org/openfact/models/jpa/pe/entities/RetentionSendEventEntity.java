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

@Entity
@Table(name = "RETENTION_SEND_EVENT")
@NamedQueries({
		@NamedQuery(name = "getAllSendEventsByRetention", query = "select i from RetentionSendEventEntity i join i.retentions a where a.id = :retentionId order by i.createdTimestamp") })

public class RetentionSendEventEntity {
	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Access(AccessType.PROPERTY)
	private String id;
	@ManyToMany(cascade = { CascadeType.ALL })
	protected List<RetentionEntity> retentions = new ArrayList<>();
	@Column(name = "ID_UBL")
	protected String ID;
	@Column(name = "IS_ACCEPTED")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	boolean accepted;
	@Column(name = "DESCRIPTION")
	protected String Description;
	@Column(name = "NOTE")
	protected String Note;
	@Column(name = "RESPONSE_CODE")
	protected String ResponseCode;
	@Column(name = "ERROR")
	protected String Error;
	@Column(name = "DIGEST_VALUE")
	protected String DigestValue;
	@Column(name = "BAR_CODE")
	protected String barCode;
	@Lob
	@Column(name = "XML_DOCUMENT")
	byte[] xmlDoument;
	@Lob
	@Column(name = "DOCUMENT_RESPONSE")
	byte[] documentResponse;
	@Lob
	@Column(name = "CUSTOMER_DOCUMENT")
	byte[] customerDoument;
	@ElementCollection
	@MapKeyColumn(name = "NAME")
	@Column(name = "VALUE")
	@CollectionTable(name = "SEND_WARNING", joinColumns = { @JoinColumn(name = "CREDIT_NOTE_SEND_EVENT_ID") })
	private Map<String, String> sendWarning = new HashMap<String, String>();
	@Type(type = "org.hibernate.type.LocalDateTimeType")
	@Column(name = "CREATED_TIMESTAMP")
	private LocalDateTime createdTimestamp;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<RetentionEntity> getRetentions() {
		return retentions;
	}

	public void setRetentions(List<RetentionEntity> retentions) {
		this.retentions = retentions;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getNote() {
		return Note;
	}

	public void setNote(String note) {
		Note = note;
	}

	public String getResponseCode() {
		return ResponseCode;
	}

	public void setResponseCode(String responseCode) {
		ResponseCode = responseCode;
	}

	public String getError() {
		return Error;
	}

	public void setError(String error) {
		Error = error;
	}

	public String getDigestValue() {
		return DigestValue;
	}

	public void setDigestValue(String digestValue) {
		DigestValue = digestValue;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}	

	public byte[] getXmlDoument() {
		return xmlDoument;
	}

	public void setXmlDoument(byte[] xmlDoument) {
		this.xmlDoument = xmlDoument;
	}

	public byte[] getDocumentResponse() {
		return documentResponse;
	}

	public void setDocumentResponse(byte[] documentResponse) {
		this.documentResponse = documentResponse;
	}

	public byte[] getCustomerDoument() {
		return customerDoument;
	}

	public void setCustomerDoument(byte[] customerDoument) {
		this.customerDoument = customerDoument;
	}

	public Map<String, String> getSendWarning() {
		return sendWarning;
	}

	public void setSendWarning(Map<String, String> sendWarning) {
		this.sendWarning = sendWarning;
	}

	public LocalDateTime getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(LocalDateTime createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}
}
