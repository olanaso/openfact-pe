package org.openfact.pe.models.jpa.ubl;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.EntityManager;

import org.jboss.logging.Logger;
import org.openfact.models.OpenfactSession;
import org.openfact.models.jpa.JpaModel;
import org.openfact.pe.models.RetentionDocumentReferenceModel;
import org.openfact.pe.models.RetentionInformationModel;
import org.openfact.pe.models.jpa.entities.RetentionDocumentReferenceEntity;

public class RetentionDocumentReferenceAdapter
		implements RetentionDocumentReferenceModel, JpaModel<RetentionDocumentReferenceEntity> {

	protected static final Logger logger = Logger.getLogger(RetentionDocumentReferenceAdapter.class);

	protected RetentionDocumentReferenceEntity retentionDocumentReference;
	protected EntityManager em;
	protected OpenfactSession session;

	public RetentionDocumentReferenceAdapter(OpenfactSession session, EntityManager em,
			RetentionDocumentReferenceEntity retentionDocumentReference) {
		this.session = session;
		this.em = em;
		this.retentionDocumentReference = retentionDocumentReference;
	}

	@Override
	public RetentionDocumentReferenceEntity getEntity() {
		return retentionDocumentReference;
	}

	@Override
	public RetentionInformationModel getSunatRetentionInformation() {
		return new RetentionInformationAdapter(session, em, retentionDocumentReference.getSunatRetentionInformation());
	}

	@Override
	public void setSunatRetentionInformation(RetentionInformationModel sunatRetentionInformation) {
		retentionDocumentReference
				.setSunatRetentionInformation(RetentionInformationAdapter.toEntity(sunatRetentionInformation, em));
	}

	public static RetentionDocumentReferenceEntity toEntity(RetentionDocumentReferenceModel model, EntityManager em) {
		if (model instanceof RetentionDocumentReferenceAdapter) {
			return ((RetentionDocumentReferenceAdapter) model).getEntity();
		}
		return em.getReference(RetentionDocumentReferenceEntity.class, model.getId());
	}

	@Override
	public String getId() {
		return retentionDocumentReference.getId();
	}

	@Override
	public String getDocumentId() {
		return retentionDocumentReference.getID();
	}

	@Override
	public LocalDateTime getIssueDate() {
		return retentionDocumentReference.getIssueDateTime();
	}

	@Override
	public void setIssueDateTime(LocalDateTime issueDateTime) {
		retentionDocumentReference.setIssueDateTime(issueDateTime);
	}

	@Override
	public BigDecimal getTotalInvoiceAmount() {
		return retentionDocumentReference.getTotalInvoiceAmount();
	}

	@Override
	public void setTotalInvoiceAmount(BigDecimal totalInvoiceAmount) {
		retentionDocumentReference.setTotalInvoiceAmount(totalInvoiceAmount);
	}

}
