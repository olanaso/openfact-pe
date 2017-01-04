package org.openfact.pe.models.jpa.entities;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "VOIDED_DOCUMENTS_LINE")
public class VoidedDocumentsLineEntity {

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Access(AccessType.PROPERTY)
	private String id;

	@Column(name = "LINE_ID")
	private String lineId;

	@Column(name = "DOCUMENT_TYPE_CODE")
	private String documentTypeCode;
	@Column(name = "DOCUMENT_SERIAL_ID")
	private String documentSerialID;
	@Column(name = "DOCUMENT_NUMBER_ID")
	private String documentNumberID;

	@Column(name = "VOID_REASON_DESCRIPTION")
	private String voidReasonDescription;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(foreignKey = @ForeignKey, name = "VOIDED_DOCUMENT_ID")
	private VoidedDocumentsEntity voidedDocuments;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLineId() {
		return lineId;
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}

	public String getDocumentTypeCode() {
		return documentTypeCode;
	}

	public void setDocumentTypeCode(String documentTypeCode) {
		this.documentTypeCode = documentTypeCode;
	}

	public String getDocumentSerialID() {
		return documentSerialID;
	}

	public void setDocumentSerialID(String documentSerialID) {
		this.documentSerialID = documentSerialID;
	}

	public String getDocumentNumberID() {
		return documentNumberID;
	}

	public void setDocumentNumberID(String documentNumberID) {
		this.documentNumberID = documentNumberID;
	}

	public String getVoidReasonDescription() {
		return voidReasonDescription;
	}

	public void setVoidReasonDescription(String voidReasonDescription) {
		this.voidReasonDescription = voidReasonDescription;
	}

	public VoidedDocumentsEntity getVoidedDocuments() {
		return voidedDocuments;
	}

	public void setVoidedDocuments(VoidedDocumentsEntity voidedDocuments) {
		this.voidedDocuments = voidedDocuments;
	}
}
