package org.openfact.pe.models.jpa.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.openfact.models.jpa.entities.SendEventEntity;

@Entity
@DiscriminatorValue(value = "SUMMARY_DOCUMENT")
public class SummaryDocumentsSendEventEntity extends SunatSendEventEntity{

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(foreignKey = @ForeignKey, name = "SUMMARY_DOCUMENT_ID")
	private SummaryDocumentsEntity summaryDocuments;


	public SummaryDocumentsEntity getSummaryDocuments() {
		return summaryDocuments;
	}

	public void setSummaryDocuments(SummaryDocumentsEntity summaryDocuments) {
		this.summaryDocuments = summaryDocuments;
	}
}
