package org.openfact.models.jpa.pe.entities;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "VOIDED_DOCUMENTS_LINE")
public class VoidedDocumentsLineEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Access(AccessType.PROPERTY)
    protected String id;

    @Column(name = "LINE_ID")
    protected String lineId;

    @Column(name = "DOCUMENT_TYPE_CODE")
    protected String documentTypeCode;

    @Column(name = "DOCUMENT_SERIAL_ID")
    protected String documentSerialId;

    @Column(name = "DOCUMENT_NUMBER_ID")
    protected String documentNumberId;

    @Column(name = "VOID_REASON_DESCRIPTION")
    protected String voidReasonDescription;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VOIDED_DOCUMENT_ID", foreignKey = @ForeignKey)
    protected VoidedDocumentsEntity voidedDocument;

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

    public String getDocumentSerialId() {
        return documentSerialId;
    }

    public void setDocumentSerialId(String documentSerialId) {
        this.documentSerialId = documentSerialId;
    }

    public String getDocumentNumberId() {
        return documentNumberId;
    }

    public void setDocumentNumberId(String documentNumberId) {
        this.documentNumberId = documentNumberId;
    }

    public String getVoidReasonDescription() {
        return voidReasonDescription;
    }

    public void setVoidReasonDescription(String voidReasonDescription) {
        this.voidReasonDescription = voidReasonDescription;
    }

    public VoidedDocumentsEntity getVoidedDocument() {
        return voidedDocument;
    }

    public void setVoidedDocument(VoidedDocumentsEntity voidedDocument) {
        this.voidedDocument = voidedDocument;
    }

}