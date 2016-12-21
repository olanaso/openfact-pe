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
import org.openfact.models.jpa.entities.InvoiceEntity;

@Entity
@Table(name = "PERCEPTION_INFORMATION")
public class PerceptionInformationEntity {

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Access(AccessType.PROPERTY)
	protected String id;

	@Column(name = "PERCEPTION_AMOUNT")
	protected BigDecimal sunatPerceptionAmount;

	@Column(name = "PERCEPTION_DATE")
	@Type(type = "org.hibernate.type.LocalDateType")
	protected LocalDate sunatPerceptionDate;

	@Column(name = "NET_TOTAL_CASHED")
	protected BigDecimal sunatNetTotalCashed;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigDecimal getSunatPerceptionAmount() {
		return sunatPerceptionAmount;
	}

	public void setSunatPerceptionAmount(BigDecimal sunatPerceptionAmount) {
		this.sunatPerceptionAmount = sunatPerceptionAmount;
	}

	public LocalDate getSunatPerceptionDate() {
		return sunatPerceptionDate;
	}

	public void setSunatPerceptionDate(LocalDate sunatPerceptionDate) {
		this.sunatPerceptionDate = sunatPerceptionDate;
	}

	public BigDecimal getSunatNetTotalCashed() {
		return sunatNetTotalCashed;
	}

	public void setSunatNetTotalCashed(BigDecimal sunatNetTotalCashed) {
		this.sunatNetTotalCashed = sunatNetTotalCashed;
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
		PerceptionInformationEntity other = (PerceptionInformationEntity) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}

}
