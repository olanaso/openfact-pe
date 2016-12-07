package org.openfact.pe.models.jpa.entities;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.openfact.models.jpa.entities.SendEventEntity;

@Entity
@Table(name = "RETENTION_SEND_EVENT")
public class RetentionSendEventEntity extends SendEventEntity{
	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Access(AccessType.PROPERTY)
	private String id;
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey, name = "RETENTION_ID")
    private RetentionEntity retention;	
	
	/**
     * Sunat Response
     */
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "retentionSendEvents", cascade = { CascadeType.ALL })
//    protected Collection<SunatResponseEntity> sunatResponses = new ArrayList<>();	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public RetentionEntity getRetention() {
		return retention;
	}

	public void setRetention(RetentionEntity retention) {
		this.retention = retention;
	}

//	public Collection<SunatResponseEntity> getSunatResponses() {
//		return sunatResponses;
//	}
//
//	public void setSunatResponses(Collection<SunatResponseEntity> sunatResponses) {
//		this.sunatResponses = sunatResponses;
//	}

}
