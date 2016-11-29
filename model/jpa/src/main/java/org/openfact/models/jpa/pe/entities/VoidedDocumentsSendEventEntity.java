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
import org.openfact.models.jpa.entities.SendEventEntity;

@Entity
@Table(name = "VOIDED_DOCUMENTS_SEND_EVENT")
public class VoidedDocumentsSendEventEntity extends SendEventEntity {
	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Access(AccessType.PROPERTY)
	private String id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(foreignKey = @ForeignKey, name = "VOIDED_DOCUMENTS_ID")
	private VoidedDocumentsEntity voidedDocuments;
	/**
	 * Sunat Response
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "voidedDocumentsSendEvents", cascade = { CascadeType.ALL })
	protected Collection<SunatResponseEntity> sunatResponses = new ArrayList<>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public VoidedDocumentsEntity getVoidedDocuments() {
		return voidedDocuments;
	}

	public void setVoidedDocuments(VoidedDocumentsEntity voidedDocuments) {
		this.voidedDocuments = voidedDocuments;
	}

	public Collection<SunatResponseEntity> getSunatResponses() {
		return sunatResponses;
	}

	public void setSunatResponses(Collection<SunatResponseEntity> sunatResponses) {
		this.sunatResponses = sunatResponses;
	}
}
