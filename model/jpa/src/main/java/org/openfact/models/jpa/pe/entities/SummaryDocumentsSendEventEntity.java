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
import org.openfact.models.jpa.entities.SendEventEntity;

@Entity
@Table(name = "SUMMARY_DOCUMENTS_SEND_EVENT")
public class SummaryDocumentsSendEventEntity extends SendEventEntity{
	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Access(AccessType.PROPERTY)
	private String id;
	@ManyToMany(cascade = { CascadeType.ALL })
	protected List<SummaryDocumentsEntity> summaryDocuments = new ArrayList<>();
	@ManyToMany(mappedBy = "summaryDocumentsSendEvents", cascade = { CascadeType.ALL })
	protected List<SunatResponseEntity> sunatResponses = new ArrayList<>();
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<SummaryDocumentsEntity> getSummaryDocuments() {
		return summaryDocuments;
	}
	public void setSummaryDocuments(List<SummaryDocumentsEntity> summaryDocuments) {
		this.summaryDocuments = summaryDocuments;
	}
	public List<SunatResponseEntity> getSunatResponses() {
		return sunatResponses;
	}
	public void setSunatResponses(List<SunatResponseEntity> sunatResponses) {
		this.sunatResponses = sunatResponses;
	}
}
