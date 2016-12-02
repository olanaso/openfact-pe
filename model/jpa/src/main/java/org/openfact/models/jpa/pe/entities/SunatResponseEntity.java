package org.openfact.models.jpa.pe.entities;

import java.time.LocalDateTime;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
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
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.openfact.models.jpa.entities.CreditNoteSendEventEntity;
import org.openfact.models.jpa.entities.DebitNoteSendEventEntity;
import org.openfact.models.jpa.entities.InvoiceSendEventEntity;

@Entity
@Table(name = "SUNAT_RESPONSE")
@NamedQueries({
		@NamedQuery(name = "getAllSunatResponseEventsByInvoice", query = "select s from SunatResponseEntity s join s.invoiceSendEvent e join i.invoice i where i.id = :Id order by s.createdTimestamp desc"),
		@NamedQuery(name = "getAllSunatResponseEventsByCreditNote", query = "select s from SunatResponseEntity s join s.creditNoteSendEvent e join i.creditNote i where i.id = :Id order by s.createdTimestamp desc"),
		@NamedQuery(name = "getAllSunatResponseEventsByDebitNote", query = "select s from SunatResponseEntity s join s.debitNoteSendEvent e join i.debitNote i where i.id = :Id order by s.createdTimestamp desc"),
		@NamedQuery(name = "getAllSunatResponseEventsByPerception", query = "select s from SunatResponseEntity s join s.perceptionSendEvent e join i.perception i where i.id = :Id order by s.createdTimestamp desc"),
		@NamedQuery(name = "getAllSunatResponseEventsByRetention", query = "select s from SunatResponseEntity s join s.retentionSendEvent e join i.retention i where i.id = :Id order by s.createdTimestamp desc"),
		@NamedQuery(name = "getAllSunatResponseEventsBySummary", query = "select s from SunatResponseEntity s join s.summaryDocumentsSendEvent e join i.summaryDocuments i where i.id = :Id order by s.createdTimestamp desc"),
		@NamedQuery(name = "getAllSunatResponseEventsByVoided", query = "select s from SunatResponseEntity s join s.voidedDocumentsSendEvent e join i.voidedDocuments i where i.id = :Id order by s.createdTimestamp desc"),
		@NamedQuery(name = "getCDRInvoice", query = "select s from SunatResponseEntity s join s.invoiceSendEvent e join i.invoice i where i.id = :Id and s.result = true and s.documentResponse not null"),
		@NamedQuery(name = "getCDRCreditNote", query = "select s from SunatResponseEntity s join s.creditNoteSendEvent e join i.creditNote i where i.id = :Id and s.result = true and s.documentResponse not null"),
		@NamedQuery(name = "getCDRDebitNote", query = "select s from SunatResponseEntity s join s.debitNoteSendEvent e join i.debitNote i where i.id = :Id and s.result = true and s.documentResponse not null"),
		@NamedQuery(name = "getCDRPerception", query = "select s from SunatResponseEntity s join s.perceptionSendEvent e join i.perception i where i.id = :Id and s.result = true and s.documentResponse not null"),
		@NamedQuery(name = "getCDRRetention", query = "select s from SunatResponseEntity s join s.retentionSendEvent e join i.retention i where i.id = :Id and s.result = true and s.documentResponse not null"),
		@NamedQuery(name = "getTicketSummary", query = "select s from SunatResponseEntity s join s.summaryDocumentsSendEvent e join i.summaryDocuments i where i.id = :Id and s.result = true and s.ticked not null"),
		@NamedQuery(name = "getTicketVoided", query = "select s from SunatResponseEntity s join s.voidedDocumentsSendEvent e join i.voidedDocuments i where i.id = :Id and s.result = true and s.ticked not null") })
public class SunatResponseEntity {
	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Access(AccessType.PROPERTY)
	private String id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(foreignKey = @ForeignKey, name = "INVOICE_SEND_EVENT_ID")
	private InvoiceSendEventEntity invoiceSendEvent;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(foreignKey = @ForeignKey, name = "CREDIT_NOTE_SEND_EVENT_ID")
	private CreditNoteSendEventEntity creditNoteSendEvent;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(foreignKey = @ForeignKey, name = "DEBIT_NOTE_SEND_EVENT_ID")
	private DebitNoteSendEventEntity debitNoteSendEvent;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(foreignKey = @ForeignKey, name = "PERCEPTION_SEND_EVENT_ID")
	private PerceptionSendEventEntity perceptionSendEvent;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(foreignKey = @ForeignKey, name = "RETENTION_SEND_EVENT_ID")
	private RetentionSendEventEntity retentionSendEvent;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(foreignKey = @ForeignKey, name = "SUMMARY_DOCUMENT_SEND_EVENT_ID")
	private SummaryDocumentsSendEventEntity summaryDocumentsSendEvent;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(foreignKey = @ForeignKey, name = "VOIDED_DOCUMENT_SEND_EVENT_ID")
	private VoidedDocumentsSendEventEntity voidedDocumentsSendEvent;

	@Column(name = "RESULT")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	protected boolean result;
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

	public InvoiceSendEventEntity getInvoiceSendEvent() {
		return invoiceSendEvent;
	}

	public void setInvoiceSendEvent(InvoiceSendEventEntity invoiceSendEvent) {
		this.invoiceSendEvent = invoiceSendEvent;
	}

	public CreditNoteSendEventEntity getCreditNoteSendEvent() {
		return creditNoteSendEvent;
	}

	public void setCreditNoteSendEvent(CreditNoteSendEventEntity creditNoteSendEvent) {
		this.creditNoteSendEvent = creditNoteSendEvent;
	}

	public DebitNoteSendEventEntity getDebitNoteSendEvent() {
		return debitNoteSendEvent;
	}

	public void setDebitNoteSendEvent(DebitNoteSendEventEntity debitNoteSendEvent) {
		this.debitNoteSendEvent = debitNoteSendEvent;
	}

	public PerceptionSendEventEntity getPerceptionSendEvent() {
		return perceptionSendEvent;
	}

	public void setPerceptionSendEvent(PerceptionSendEventEntity perceptionSendEvent) {
		this.perceptionSendEvent = perceptionSendEvent;
	}

	public RetentionSendEventEntity getRetentionSendEvent() {
		return retentionSendEvent;
	}

	public void setRetentionSendEvent(RetentionSendEventEntity retentionSendEvent) {
		this.retentionSendEvent = retentionSendEvent;
	}

	public SummaryDocumentsSendEventEntity getSummaryDocumentsSendEvent() {
		return summaryDocumentsSendEvent;
	}

	public void setSummaryDocumentsSendEvent(SummaryDocumentsSendEventEntity summaryDocumentsSendEvent) {
		this.summaryDocumentsSendEvent = summaryDocumentsSendEvent;
	}

	public VoidedDocumentsSendEventEntity getVoidedDocumentsSendEvent() {
		return voidedDocumentsSendEvent;
	}

	public void setVoidedDocumentsSendEvent(VoidedDocumentsSendEventEntity voidedDocumentsSendEvent) {
		this.voidedDocumentsSendEvent = voidedDocumentsSendEvent;
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

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

}
