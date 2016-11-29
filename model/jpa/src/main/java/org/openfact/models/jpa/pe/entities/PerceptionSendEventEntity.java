package org.openfact.models.jpa.pe.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.openfact.models.jpa.entities.CreditNoteEntity;
import org.openfact.models.jpa.entities.InvoiceSendEventEntity;
import org.openfact.models.jpa.entities.SendEventEntity;

@Entity
@Table(name = "PERCEPTION_SEND_EVENT")
public class PerceptionSendEventEntity extends SendEventEntity{
	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Access(AccessType.PROPERTY)
	private String id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey, name = "PERCEPTION_ID")
    private PerceptionEntity perception;	
	/**
     * Sunat Response
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "perceptionSendEvents", cascade = { CascadeType.ALL })
    protected Collection<SunatResponseEntity> sunatResponses = new ArrayList<>();	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public PerceptionEntity getPerception() {
		return perception;
	}

	public void setPerception(PerceptionEntity perception) {
		this.perception = perception;
	}

	public Collection<SunatResponseEntity> getSunatResponses() {
		return sunatResponses;
	}

	public void setSunatResponses(Collection<SunatResponseEntity> sunatResponses) {
		this.sunatResponses = sunatResponses;
	}
	
}
