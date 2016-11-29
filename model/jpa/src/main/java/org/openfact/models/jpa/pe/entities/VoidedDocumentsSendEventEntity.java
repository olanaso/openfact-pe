package org.openfact.models.jpa.pe.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "VOIDED_DOCUMENTS_SEND_EVENT")
public class VoidedDocumentsSendEventEntity {
	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Access(AccessType.PROPERTY)
	private String id;
	@ManyToMany(cascade = { CascadeType.ALL })
	protected List<VoidedDocumentsEntity> voidedDocuments = new ArrayList<>();
	@ManyToMany(mappedBy = "voidedDocumentsSendEvents", cascade = { CascadeType.ALL })
	protected List<SunatResponseEntity> sunatResponses = new ArrayList<>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<VoidedDocumentsEntity> getVoidedDocuments() {
		return voidedDocuments;
	}

	public void setVoidedDocuments(List<VoidedDocumentsEntity> voidedDocuments) {
		this.voidedDocuments = voidedDocuments;
	}

	public List<SunatResponseEntity> getSunatResponses() {
		return sunatResponses;
	}

	public void setSunatResponses(List<SunatResponseEntity> sunatResponses) {
		this.sunatResponses = sunatResponses;
	}
}
