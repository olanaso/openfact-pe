package org.openfact.representations.idm.ubl.common;

import javax.persistence.Column;

public class VoidedDocumentsLineRepresentation {
	protected String id;
    protected String IdUbl;    
	protected String lineID;	
	protected String documentTypeCode;	
	protected String documentSerialID;	
	protected String documentNumberID;	
	protected String voidReasonDescription;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIdUbl() {
		return IdUbl;
	}
	public void setIdUbl(String idUbl) {
		IdUbl = idUbl;
	}
	public String getLineID() {
		return lineID;
	}
	public void setLineID(String lineID) {
		this.lineID = lineID;
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
	
}
