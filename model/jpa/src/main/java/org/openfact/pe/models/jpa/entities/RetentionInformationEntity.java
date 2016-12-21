package org.openfact.pe.models.jpa.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "RETENTION_INFORMATION")
public class RetentionInformationEntity {

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Access(AccessType.PROPERTY)
	protected String id;

	@Column(name = "RETENTION_AMOUNT")
	protected BigDecimal sunatRetentionAmount;

	@Column(name = "RETENTION_DATE")
	@Type(type = "org.hibernate.type.LocalDateType")
	protected LocalDate sunatRetentionDate;

	@Column(name = "NET_TOTAL_PAID")
	protected BigDecimal sunatNetTotalPaid;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigDecimal getSunatRetentionAmount() {
		return sunatRetentionAmount;
	}

	public void setSunatRetentionAmount(BigDecimal sunatRetentionAmount) {
		this.sunatRetentionAmount = sunatRetentionAmount;
	}

	public LocalDate getSunatRetentionDate() {
		return sunatRetentionDate;
	}

	public void setSunatRetentionDate(LocalDate sunatRetentionDate) {
		this.sunatRetentionDate = sunatRetentionDate;
	}

	public BigDecimal getSunatNetTotalPaid() {
		return sunatNetTotalPaid;
	}

	public void setSunatNetTotalPaid(BigDecimal sunatNetTotalPaid) {
		this.sunatNetTotalPaid = sunatNetTotalPaid;
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
		RetentionInformationEntity other = (RetentionInformationEntity) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}

}
