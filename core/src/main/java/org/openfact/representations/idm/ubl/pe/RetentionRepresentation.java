package org.openfact.representations.idm.ubl.pe;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.openfact.representations.idm.ubl.common.PartyRepresentation;
import org.openfact.representations.idm.ubl.common.SignatureRepresentation;
import org.openfact.representations.idm.ubl.common.UBLExtensionsRepresentation;
import org.openfact.representations.idm.ubl.common.pe.RetentionDocumentReferenceRepresentation;

public class RetentionRepresentation {
	protected String id;
	protected UBLExtensionsRepresentation ublExtensions;
	protected String ublVersionID;
	protected String customizationID;
	protected List<SignatureRepresentation> signature;
	protected String idUbl;
	protected LocalDateTime issueDateTime;
	protected List<String> note;
	protected String SUNATRetentionSystemCode;
	protected BigDecimal SUNATRetentionPercent;
	protected PartyRepresentation agentParty;
	protected PartyRepresentation receiverParty;
	protected BigDecimal totalInvoiceAmount;
	protected BigDecimal SUNATTotalPaid;
	protected List<RetentionDocumentReferenceRepresentation> SUNATRetentionDocumentReference;
	protected byte[] xmlDocument;

	public void addSignature(SignatureRepresentation representation) {
		if (signature == null) {
			signature = new ArrayList<>();
		}
		signature.add(representation);
	}
	public void addRetentionDocumentReference(RetentionDocumentReferenceRepresentation representation) {
		if (SUNATRetentionDocumentReference == null) {
			SUNATRetentionDocumentReference = new ArrayList<>();
		}
		SUNATRetentionDocumentReference.add(representation);
	}
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

	public List<SignatureRepresentation> getSignature() {
		return signature;
	}

	public void setSignature(List<SignatureRepresentation> signature) {
		this.signature = signature;
	}

	public String getIdUbl() {
		return idUbl;
	}

	public void setIdUbl(String idUbl) {
		this.idUbl = idUbl;
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

	public String getSUNATRetentionSystemCode() {
		return SUNATRetentionSystemCode;
	}

	public void setSUNATRetentionSystemCode(String sUNATRetentionSystemCode) {
		SUNATRetentionSystemCode = sUNATRetentionSystemCode;
	}

	public BigDecimal getSUNATRetentionPercent() {
		return SUNATRetentionPercent;
	}

	public void setSUNATRetentionPercent(BigDecimal sUNATRetentionPercent) {
		SUNATRetentionPercent = sUNATRetentionPercent;
	}

	public PartyRepresentation getAgentParty() {
		return agentParty;
	}

	public void setAgentParty(PartyRepresentation agentParty) {
		this.agentParty = agentParty;
	}

	public PartyRepresentation getReceiverParty() {
		return receiverParty;
	}

	public void setReceiverParty(PartyRepresentation receiverParty) {
		this.receiverParty = receiverParty;
	}

	public BigDecimal getTotalInvoiceAmount() {
		return totalInvoiceAmount;
	}

	public void setTotalInvoiceAmount(BigDecimal totalInvoiceAmount) {
		this.totalInvoiceAmount = totalInvoiceAmount;
	}

	public BigDecimal getSUNATTotalPaid() {
		return SUNATTotalPaid;
	}

	public void setSUNATTotalPaid(BigDecimal sUNATTotalPaid) {
		SUNATTotalPaid = sUNATTotalPaid;
	}

	public List<RetentionDocumentReferenceRepresentation> getSUNATRetentionDocumentReference() {
		return SUNATRetentionDocumentReference;
	}

	public void setSUNATRetentionDocumentReference(
			List<RetentionDocumentReferenceRepresentation> sUNATRetentionDocumentReference) {
		SUNATRetentionDocumentReference = sUNATRetentionDocumentReference;
	}

	public byte[] getXmlDocument() {
		return xmlDocument;
	}

	public void setXmlDocument(byte[] xmlDocument) {
		this.xmlDocument = xmlDocument;
	}	

}
