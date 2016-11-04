package org.openfact.representations.idm.ubl.pe;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.openfact.representations.idm.ubl.common.SignatureRepresentation;
import org.openfact.representations.idm.ubl.common.SummaryDocumentsLineRepresentation;
import org.openfact.representations.idm.ubl.common.SupplierPartyRepresentation;
import org.openfact.representations.idm.ubl.common.UBLExtensionsRepresentation;

public class SummaryDocumentsRepresentation {
	protected String id;
	protected UBLExtensionsRepresentation ublExtensions;
	protected String ublVersionID;
	protected String customizationID;
	protected String IdUbl;
	protected String documentCurrencyCode;
	protected LocalDateTime referenceDateTime;
	protected LocalDateTime issueDateTime;
	protected List<SignatureRepresentation> signature;
	protected SupplierPartyRepresentation accountingSupplierParty;
	protected List<SummaryDocumentsLineRepresentation> summaryDocumentsLines;
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

	public LocalDateTime getReferenceDateTime() {
		return referenceDateTime;
	}

	public void setReferenceDateTime(LocalDateTime referenceDateTime) {
		this.referenceDateTime = referenceDateTime;
	}

	public LocalDateTime getIssueDateTime() {
		return issueDateTime;
	}

	public String getDocumentCurrencyCode() {
		return documentCurrencyCode;
	}

	public void setDocumentCurrencyCode(String documentCurrencyCode) {
		this.documentCurrencyCode = documentCurrencyCode;
	}

	public void setIssueDateTime(LocalDateTime issueDateTime) {
		this.issueDateTime = issueDateTime;
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

	public List<SummaryDocumentsLineRepresentation> getSummaryDocumentsLines() {
		return summaryDocumentsLines;
	}

	public void setSummaryDocumentsLines(List<SummaryDocumentsLineRepresentation> summaryDocumentsLines) {
		this.summaryDocumentsLines = summaryDocumentsLines;
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

	public void addSummaryDocumentsLine(SummaryDocumentsLineRepresentation representation) {
		if (summaryDocumentsLines == null) {
			summaryDocumentsLines = new ArrayList<>();
		}
		summaryDocumentsLines.add(representation);
	}
}
