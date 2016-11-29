package org.openfact.models.jpa.pe.entities;

import java.util.ArrayList;
import java.util.Collection;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey, name = "SUMMARY_DOCUMENTS_ID")
    private SummaryDocumentsEntity summaryDocuments;	
	/**
     * Sunat Response
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "summaryDocumentsSendEvents", cascade = { CascadeType.ALL })
    protected Collection<SunatResponseEntity> sunatResponses = new ArrayList<>();	
    
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public SummaryDocumentsEntity getSummaryDocuments() {
		return summaryDocuments;
	}
	public void setSummaryDocuments(SummaryDocumentsEntity summaryDocuments) {
		this.summaryDocuments = summaryDocuments;
	}
	public Collection<SunatResponseEntity> getSunatResponses() {
		return sunatResponses;
	}
	public void setSunatResponses(Collection<SunatResponseEntity> sunatResponses) {
		this.sunatResponses = sunatResponses;
	}
	
}
