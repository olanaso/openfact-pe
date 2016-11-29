package org.openfact.models.jpa.pe.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.openfact.models.jpa.entities.CreditNoteSendEventEntity;
import org.openfact.models.jpa.entities.DebitNoteSendEventEntity;
import org.openfact.models.jpa.entities.InvoiceSendEventEntity;

@Entity
@Table(name = "SUNAT_RESPONSE")
public class SunatResponseEntity {
	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Access(AccessType.PROPERTY)
	private String id;

	@ManyToMany(cascade = { CascadeType.ALL })
	protected List<InvoiceSendEventEntity> invoiceSendEvents = new ArrayList<>();

	@ManyToMany(cascade = { CascadeType.ALL })
	protected List<CreditNoteSendEventEntity> creditNoteSendEvents = new ArrayList<>();

	@ManyToMany(cascade = { CascadeType.ALL })
	protected List<DebitNoteSendEventEntity> debitNoteSendEvents = new ArrayList<>();

	@ManyToMany(cascade = { CascadeType.ALL })
	protected List<PerceptionSendEventEntity> perceptionSendEvents = new ArrayList<>();

	@ManyToMany(cascade = { CascadeType.ALL })
	protected List<RetentionSendEventEntity> retentionSendEvents = new ArrayList<>();

	@ManyToMany(cascade = { CascadeType.ALL })
	protected List<SummaryDocumentsSendEventEntity> summaryDocumentsSendEvents = new ArrayList<>();

	@ManyToMany(cascade = { CascadeType.ALL })
	protected List<VoidedDocumentsSendEventEntity> voidedDocumentsSendEvents = new ArrayList<>();

	@Lob
	@Column(name = "DOCUMENT_RESPONSE")
	protected byte[] documentResponse;
	@Column(name = "TICKET")
	protected String ticked;
	@Column(name = "RESPONSE_CODE")
	protected String responseCode;
	@Column(name = "ERROR_MESSAGE")
	protected String errorMessage;
	@Type(type = "org.hibernate.type.LocalDateTimeType")
	@Column(name = "CREATED_TIMESTAMP")
	protected LocalDateTime createdTimestamp;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<InvoiceSendEventEntity> getInvoiceSendEvents() {
		return invoiceSendEvents;
	}

	public void setInvoiceSendEvents(List<InvoiceSendEventEntity> invoiceSendEvents) {
		this.invoiceSendEvents = invoiceSendEvents;
	}

	public List<CreditNoteSendEventEntity> getCreditNoteSendEvents() {
		return creditNoteSendEvents;
	}

	public void setCreditNoteSendEvents(List<CreditNoteSendEventEntity> creditNoteSendEvents) {
		this.creditNoteSendEvents = creditNoteSendEvents;
	}

	public List<DebitNoteSendEventEntity> getDebitNoteSendEvents() {
		return debitNoteSendEvents;
	}

	public void setDebitNoteSendEvents(List<DebitNoteSendEventEntity> debitNoteSendEvents) {
		this.debitNoteSendEvents = debitNoteSendEvents;
	}

	public List<PerceptionSendEventEntity> getPerceptionSendEvents() {
		return perceptionSendEvents;
	}

	public void setPerceptionSendEvents(List<PerceptionSendEventEntity> perceptionSendEvents) {
		this.perceptionSendEvents = perceptionSendEvents;
	}

	public List<RetentionSendEventEntity> getRetentionSendEvents() {
		return retentionSendEvents;
	}

	public void setRetentionSendEvents(List<RetentionSendEventEntity> retentionSendEvents) {
		this.retentionSendEvents = retentionSendEvents;
	}

	public List<SummaryDocumentsSendEventEntity> getSummaryDocumentsSendEvents() {
		return summaryDocumentsSendEvents;
	}

	public void setSummaryDocumentsSendEvents(List<SummaryDocumentsSendEventEntity> summaryDocumentsSendEvents) {
		this.summaryDocumentsSendEvents = summaryDocumentsSendEvents;
	}

	public List<VoidedDocumentsSendEventEntity> getVoidedDocumentsSendEvents() {
		return voidedDocumentsSendEvents;
	}

	public void setVoidedDocumentsSendEvents(List<VoidedDocumentsSendEventEntity> voidedDocumentsSendEvents) {
		this.voidedDocumentsSendEvents = voidedDocumentsSendEvents;
	}

	public byte[] getDocumentResponse() {
		return documentResponse;
	}

	public void setDocumentResponse(byte[] documentResponse) {
		this.documentResponse = documentResponse;
	}

	public String getTicked() {
		return ticked;
	}

	public void setTicked(String ticked) {
		this.ticked = ticked;
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

	public LocalDateTime getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(LocalDateTime createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

}
