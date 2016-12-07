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

	// @ManyToOne(targetEntity = ExchangeRateEntity.class, cascade = {
	// CascadeType.ALL })
	// @JoinColumn(name = "EXCHANGERATE_PERCEPTIONINFORMATION_ID")
	// protected ExchangeRateEntity exchangeRate=new ExchangeRateEntity();

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

}
