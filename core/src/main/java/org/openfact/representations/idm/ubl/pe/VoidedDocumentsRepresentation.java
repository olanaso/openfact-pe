package org.openfact.representations.idm.ubl.pe;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.openfact.representations.idm.ubl.common.SignatureRepresentation;
import org.openfact.representations.idm.ubl.common.SupplierPartyRepresentation;
import org.openfact.representations.idm.ubl.common.UBLExtensionsRepresentation;
import org.openfact.representations.idm.ubl.common.VoidedDocumentsLineRepresentation;

public class VoidedDocumentsRepresentation {
	protected String id;
	protected UBLExtensionsRepresentation ublExtensions;
	protected String ublVersionID;
	protected String customizationID;
	protected String IdUbl;
	protected LocalDateTime referenceDate;
	protected LocalDateTime issueDateTime;
	protected List<String> note;
	protected List<SignatureRepresentation> signature;
	protected SupplierPartyRepresentation accountingSupplierParty;
	protected List<VoidedDocumentsLineRepresentation> voidedDocumentsLine;
	protected byte[] xmlDocument;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public UBLExtensionsRepresentation getUblExtensions() {
		return ublExtensions;
	}

	public void setUblExtensions(UBLExtensionsRepresentation ublExtensions) {
		this.ublExtensions = ublExtensions;
	}

	public String getUblVersionID() {
		return ublVersionID;
	}

	public void setUblVersionID(String ublVersionID) {
		this.ublVersionID = ublVersionID;
	}

	public String getCustomizationID() {
		return customizationID;
	}

	public void setCustomizationID(String customizationID) {
		this.customizationID = customizationID;
	}

	public String getIdUbl() {
		return IdUbl;
	}

	public void setIdUbl(String idUbl) {
		IdUbl = idUbl;
	}

	public LocalDateTime getReferenceDate() {
		return referenceDate;
	}

	public void setReferenceDate(LocalDateTime referenceDate) {
		this.referenceDate = referenceDate;
	}

	public LocalDateTime getIssueDateTime() {
		return issueDateTime;
	}

	public void setIssueDateTime(LocalDateTime issueDateTime) {
		this.issueDateTime = issueDateTime;
	}

	public List<String> getNote() {
		return note;
	}

	public void setNote(List<String> note) {
		this.note = note;
	}

	public List<SignatureRepresentation> getSignature() {
		return signature;
	}

	public void setSignature(List<SignatureRepresentation> signature) {
		this.signature = signature;
	}

	public SupplierPartyRepresentation getAccountingSupplierParty() {
		return accountingSupplierParty;
	}

	public void setAccountingSupplierParty(SupplierPartyRepresentation accountingSupplierParty) {
		this.accountingSupplierParty = accountingSupplierParty;
	}

	public List<VoidedDocumentsLineRepresentation> getVoidedDocumentsLine() {
		return voidedDocumentsLine;
	}

	public void setVoidedDocumentsLine(List<VoidedDocumentsLineRepresentation> voidedDocumentsLine) {
		this.voidedDocumentsLine = voidedDocumentsLine;
	}

	public byte[] getXmlDocument() {
		return xmlDocument;
	}

	public void setXmlDocument(byte[] xmlDocument) {
		this.xmlDocument = xmlDocument;
	}

	public void addSignature(SignatureRepresentation representation) {
		if (signature == null) {
			signature = new ArrayList<>();
		}
		signature.add(representation);
	}

	public void addVoidedDocumentsLine(VoidedDocumentsLineRepresentation representation) {
		if (voidedDocumentsLine == null) {
			voidedDocumentsLine = new ArrayList<>();
		}
		voidedDocumentsLine.add(representation);
	}

}
