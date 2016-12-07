package org.openfact.pe.models.jpa.entities;

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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.openfact.models.jpa.entities.OrganizationEntity;
import org.openfact.models.jpa.entities.SendEventEntity;

@Entity
@Table(name = "SUNAT_RESPONSE")
public class SunatResponseEntity {
	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Access(AccessType.PROPERTY)
	private String id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(foreignKey = @ForeignKey, name = "SEND_EVENT_ID")
	private SendEventEntity SendEvent;	
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey, name = "ORGANIZATION_ID")
    private OrganizationEntity organization;
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

	public SendEventEntity getSendEvent() {
		return SendEvent;
	}

	public void setSendEvent(SendEventEntity sendEvent) {
		SendEvent = sendEvent;
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

	public OrganizationEntity getOrganization() {
		return organization;
	}

	public void setOrganization(OrganizationEntity organization) {
		this.organization = organization;
	}

}
