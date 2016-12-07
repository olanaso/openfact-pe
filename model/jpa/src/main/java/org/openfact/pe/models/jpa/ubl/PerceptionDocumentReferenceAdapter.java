package org.openfact.pe.models.jpa.ubl;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.EntityManager;

import org.jboss.logging.Logger;
import org.openfact.models.OpenfactSession;
import org.openfact.models.jpa.JpaModel;
import org.openfact.pe.models.PerceptionDocumentReferenceModel;
import org.openfact.pe.models.PerceptionInformationModel;
import org.openfact.pe.models.jpa.entities.PerceptionDocumentReferenceEntity;
import org.openfact.pe.models.jpa.entities.PerceptionInformationEntity;

public class PerceptionDocumentReferenceAdapter
		implements PerceptionDocumentReferenceModel, JpaModel<PerceptionDocumentReferenceEntity> {

	protected static final Logger logger = Logger.getLogger(PerceptionDocumentReferenceAdapter.class);

	protected PerceptionDocumentReferenceEntity perceptionDocumentReference;
	protected EntityManager em;
	protected OpenfactSession session;

	public PerceptionDocumentReferenceAdapter(OpenfactSession session, EntityManager em,
			PerceptionDocumentReferenceEntity perceptionDocumentReference) {
		this.session = session;
		this.em = em;
		this.perceptionDocumentReference = perceptionDocumentReference;
	}

	@Override
	public PerceptionDocumentReferenceEntity getEntity() {
		return perceptionDocumentReference;
	}

	@Override
	public String getId() {
		return perceptionDocumentReference.getId();
	}

	@Override
	public BigDecimal getTotalInvoiceAmount() {
		return perceptionDocumentReference.getTotalInvoiceAmount();
	}

	@Override
	public void setTotalInvoiceAmount(BigDecimal totalInvoiceAmount) {
		perceptionDocumentReference.setTotalInvoiceAmount(totalInvoiceAmount);
	}

	@Override
	public PerceptionInformationModel getSunatPerceptionInformation() {
		return new PerceptionInformationAdapter(session, em,
				perceptionDocumentReference.getSunatPerceptionInformation());
	}	
	public static PerceptionDocumentReferenceEntity toEntity(PerceptionDocumentReferenceModel model, EntityManager em) {
		if (model instanceof PerceptionDocumentReferenceAdapter) {
			return ((PerceptionDocumentReferenceAdapter) model).getEntity();
		}
		return em.getReference(PerceptionDocumentReferenceEntity.class, model.getId());
	}

	@Override
	public String getDocumentId() {
		return perceptionDocumentReference.getID();
	}

	@Override
	public LocalDate getIssueDate() {
		return perceptionDocumentReference.getIssueDate();
	}

	@Override
	public void setIssueDate(LocalDate issueDate) {
		perceptionDocumentReference.setIssueDate(issueDate);
	}

	@Override
	public PerceptionInformationModel getSunatPerceptionInformationAsNotNull() {
		if (perceptionDocumentReference.getSunatPerceptionInformation() == null) {
			PerceptionInformationEntity entity = new PerceptionInformationEntity();
			perceptionDocumentReference.setSunatPerceptionInformation(entity);
		}
		return new PerceptionInformationAdapter(session, em,
				perceptionDocumentReference.getSunatPerceptionInformation());
	}

	@Override
	public void setDocumentId(String documentId) {
		perceptionDocumentReference.setID(documentId);
	}

}
