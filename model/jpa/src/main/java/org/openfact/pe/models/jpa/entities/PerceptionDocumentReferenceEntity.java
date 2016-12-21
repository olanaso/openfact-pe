package org.openfact.pe.models.jpa.entities;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.openfact.models.jpa.entities.InvoiceEntity;

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

	@ManyToOne(targetEntity = PerceptionInformationEntity.class, cascade = { CascadeType.ALL })
	@JoinColumn(name = "PERCEPTIONINFORMATION_PERCEPTIONDOCUMENTREFERENCE")
	protected PerceptionInformationEntity sunatPerceptionInformation=new PerceptionInformationEntity();

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(foreignKey = @ForeignKey, name = "PERCEPTION_ID")
	private PerceptionEntity perception;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PerceptionDocumentReferenceEntity other = (PerceptionDocumentReferenceEntity) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}

	public PerceptionEntity getPerception() {
		return perception;
	}

	public void setPerception(PerceptionEntity perception) {
		this.perception = perception;
	}
}
