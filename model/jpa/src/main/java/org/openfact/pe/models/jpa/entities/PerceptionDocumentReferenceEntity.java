package org.openfact.pe.models.jpa.entities;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "PERCEPTION_DOCUMENT_REFERENCE")
public class PerceptionDocumentReferenceEntity {
	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Access(AccessType.PROPERTY)
	protected String id;

	@Column(name = "ID_UBL")
	protected String ID;
	

	@Column(name = "ISSUE_DATE")
	@Type(type = "org.hibernate.type.LocalDateType")
	protected LocalDate issueDate;

	@Column(name = "TOTAL_INVOICE_AMOUNT")
	protected BigDecimal totalInvoiceAmount;

//	@ManyToOne(targetEntity = PaymentEntity.class, cascade = { CascadeType.ALL })
//	@JoinColumn(name = "PAYMENT_PERCEPTIONDOCUMENTREFERENCE")
//	protected PaymentEntity payment = new PaymentEntity();

	@ManyToOne(targetEntity = PerceptionInformationEntity.class, cascade = { CascadeType.ALL })
	@JoinColumn(name = "PERCEPTIONINFORMATION_PERCEPTIONDOCUMENTREFERENCE")
	protected PerceptionInformationEntity sunatPerceptionInformation=new PerceptionInformationEntity();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public LocalDate getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(LocalDate issueDate) {
		this.issueDate = issueDate;
	}

	public BigDecimal getTotalInvoiceAmount() {
		return totalInvoiceAmount;
	}

	public void setTotalInvoiceAmount(BigDecimal totalInvoiceAmount) {
		this.totalInvoiceAmount = totalInvoiceAmount;
	}	

	public PerceptionInformationEntity getSunatPerceptionInformation() {
		return sunatPerceptionInformation;
	}

	public void setSunatPerceptionInformation(PerceptionInformationEntity sunatPerceptionInformation) {
		this.sunatPerceptionInformation = sunatPerceptionInformation;
	}
	
}
