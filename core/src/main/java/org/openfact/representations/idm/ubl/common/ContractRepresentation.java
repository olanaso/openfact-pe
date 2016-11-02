package org.openfact.representations.idm.ubl.common;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ContractRepresentation {
	private String idUbl;
	private LocalDate issueDate;
	private LocalTime issueTime;
	private String contractTypeCode;
	private String contractType;
	private PeriodRepresentation validityPeriod;
	private List<DocumentReferenceRepresentation> contractDocumentReference;
	private String id;

	public void addDocumentReference(DocumentReferenceRepresentation representation) {
		if (contractDocumentReference == null) {
			contractDocumentReference = new ArrayList<>();
		}
		contractDocumentReference.add(representation);
	}

	public String getIdUbl() {
		return this.idUbl;
	}

	public void setIdUbl(String idUbl) {
		this.idUbl = idUbl;
	}

	public PeriodRepresentation getValidityPeriod() {
		return this.validityPeriod;
	}

	public void setValidityPeriod(PeriodRepresentation validityPeriod) {
		this.validityPeriod = validityPeriod;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LocalDate getIssueDate() {
		return this.issueDate;
	}

	public void setIssueDate(LocalDate issueDate) {
		this.issueDate = issueDate;
	}

	public String getContractTypeCode() {
		return contractTypeCode;
	}

	public void setContractTypeCode(String contractTypeCode) {
		this.contractTypeCode = contractTypeCode;
	}

	public String getContractType() {
		return contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	public List<DocumentReferenceRepresentation> getContractDocumentReference() {
		return this.contractDocumentReference;
	}

	public void setContractDocumentReference(List<DocumentReferenceRepresentation> contractDocumentReference) {
		this.contractDocumentReference = contractDocumentReference;
	}

	public LocalTime getIssueTime() {
		return this.issueTime;
	}

	public void setIssueTime(LocalTime issueTime) {
		this.issueTime = issueTime;
	}
}