package org.openfact.pe.models.jpa.ubl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.jboss.logging.Logger;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.SendEventModel;
import org.openfact.models.SupplierPartyModel;
import org.openfact.models.enums.RequiredAction;
import org.openfact.models.jpa.JpaModel;
import org.openfact.models.jpa.SendEventAdapter;
import org.openfact.models.jpa.SupplierPartyAdapter;
import org.openfact.models.jpa.entities.SupplierPartyEntity;
import org.openfact.pe.models.SummaryDocumentModel;
import org.openfact.pe.models.jpa.entities.SummaryDocumentsEntity;
import org.openfact.pe.models.jpa.entities.SummaryDocumentsRequiredActionEntity;

public class SummaryDocumentAdapter implements SummaryDocumentModel, JpaModel<SummaryDocumentsEntity> {

	protected static final Logger logger = Logger.getLogger(SummaryDocumentAdapter.class);

	protected OrganizationModel organization;
	protected SummaryDocumentsEntity summaryDocuments;
	protected EntityManager em;
	protected OpenfactSession session;

	public SummaryDocumentAdapter(OpenfactSession session, OrganizationModel organization, EntityManager em,
			SummaryDocumentsEntity summaryDocuments) {
		this.organization = organization;
		this.session = session;
		this.em = em;
		this.summaryDocuments = summaryDocuments;
	}

	@Override
	public SummaryDocumentsEntity getEntity() {
		return summaryDocuments;
	}

	public static SummaryDocumentsEntity toEntity(SummaryDocumentModel model, EntityManager em) {
		if (model instanceof SummaryDocumentAdapter) {
			return ((SummaryDocumentAdapter) model).getEntity();
		}
		return em.getReference(SummaryDocumentsEntity.class, model.getId());
	}

	@Override
	public String getId() {
		return summaryDocuments.getId();
	}

	@Override
	public String getDocumentId() {
		return summaryDocuments.getDocumentId();
	}

	@Override
	public String getOrganizationId() {
		return summaryDocuments.getOrganizationId();
	}

	@Override
	public String getDocumentCurrencyCode() {
		return summaryDocuments.getDocumentCurrencyCode();
	}

	@Override
	public void setDocumentCurrencyCode(String value) {
		summaryDocuments.setDocumentCurrencyCode(value);
	}

	@Override
	public String getUblVersionId() {
		return summaryDocuments.getUblVersionId();
	}

	@Override
	public void setUblVersionId(String ublVersionId) {
		summaryDocuments.setUblVersionId(ublVersionId);
	}

	@Override
	public String getCustomizationI() {
		return summaryDocuments.getCustomizationId();
	}

	@Override
	public void setCustomizationId(String customizationId) {
		summaryDocuments.setCustomizationId(customizationId);
	}

	@Override
	public LocalDate getReferenceDate() {
		return summaryDocuments.getReferenceDate();
	}

	@Override
	public void setReferenceDate(LocalDate referenceDate) {
		summaryDocuments.setReferenceDate(referenceDate);
	}

	@Override
	public LocalDate getIssueDate() {
		return summaryDocuments.getIssueDate();
	}

	@Override
	public void setIssueDate(LocalDate issueDate) {
		summaryDocuments.setIssueDate(issueDate);
	}

	@Override
	public byte[] getXmlDocument() {
		return summaryDocuments.getXmlDocument();
	}

	@Override
	public void setXmlDocument(byte[] bytes) {
		summaryDocuments.setXmlDocument(bytes);
	}

	@Override
	public Set<String> getRequiredActions() {
		Set<String> result = new HashSet<>();
		for (SummaryDocumentsRequiredActionEntity attr : summaryDocuments.getRequiredActions()) {
			result.add(attr.getAction());
		}
		return result;
	}

	@Override
	public void addRequiredAction(String actionName) {
		for (SummaryDocumentsRequiredActionEntity attr : summaryDocuments.getRequiredActions()) {
			if (attr.getAction().equals(actionName)) {
				return;
			}
		}
		SummaryDocumentsRequiredActionEntity attr = new SummaryDocumentsRequiredActionEntity();
		attr.setAction(actionName);
		attr.setSummaryDocuments(summaryDocuments);
		em.persist(attr);
		summaryDocuments.getRequiredActions().add(attr);
	}

	@Override
	public void removeRequiredAction(String actionName) {
		Iterator<SummaryDocumentsRequiredActionEntity> it = summaryDocuments.getRequiredActions().iterator();
		while (it.hasNext()) {
			SummaryDocumentsRequiredActionEntity attr = it.next();
			if (attr.getAction().equals(actionName)) {
				it.remove();
				em.remove(attr);
			}
		}
	}

	@Override
	public void addRequiredAction(RequiredAction action) {
		String actionName = action.name();
		addRequiredAction(actionName);
	}

	@Override
	public void removeRequiredAction(RequiredAction action) {
		String actionName = action.name();
		removeRequiredAction(actionName);
	}

	@Override
	public List<SendEventModel> getSendEvents() {
		/*return summaryDocuments.getSendEvents().stream().map(f -> new SunatSendEventAdapter(session, organization, em, f))
				.collect(Collectors.toList());*/
		return null;
	}

	@Override
	public void setDocumentId(String documentId) {
		summaryDocuments.setDocumentId(documentId);
	}	
}
