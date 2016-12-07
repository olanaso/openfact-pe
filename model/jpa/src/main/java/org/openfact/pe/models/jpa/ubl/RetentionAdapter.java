package org.openfact.pe.models.jpa.ubl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.jboss.logging.Logger;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.PartyModel;
import org.openfact.models.enums.RequiredAction;
import org.openfact.models.jpa.JpaModel;
import org.openfact.models.jpa.PartyAdapter;
import org.openfact.models.jpa.SendEventAdapter;
import org.openfact.models.jpa.entities.PartyEntity;
import org.openfact.pe.models.RetentionDocumentReferenceModel;
import org.openfact.pe.models.RetentionModel;
import org.openfact.pe.models.jpa.entities.RetentionDocumentReferenceEntity;
import org.openfact.pe.models.jpa.entities.RetentionEntity;
import org.openfact.pe.models.jpa.entities.RetentionRequiredActionEntity;
import org.openfact.ubl.SendEventModel;

public class RetentionAdapter implements RetentionModel, JpaModel<RetentionEntity> {
	protected static final Logger logger = Logger.getLogger(RetentionAdapter.class);

	protected OrganizationModel organization;
	protected RetentionEntity retention;
	protected EntityManager em;
	protected OpenfactSession session;

	public RetentionAdapter(OpenfactSession session, OrganizationModel organization, EntityManager em,
			RetentionEntity retention) {
		this.organization = organization;
		this.session = session;
		this.em = em;
		this.retention = retention;
	}

	public static RetentionEntity toEntity(RetentionModel model, EntityManager em) {
		if (model instanceof RetentionAdapter) {
			return ((RetentionAdapter) model).getEntity();
		}
		return em.getReference(RetentionEntity.class, model.getId());
	}

	@Override
	public RetentionEntity getEntity() {
		return retention;
	}

	@Override
	public String getId() {
		return retention.getId();
	}

	@Override
	public String getDocumentId() {
		return retention.getDocumentId();
	}

	@Override
	public String getOrganizationId() {
		return retention.getOrganizationId();
	}

	@Override
	public String getUblVersionId() {
		return retention.getUblVersionId();
	}

	@Override
	public void setUblVersionId(String ublVersionId) {
		retention.setUblVersionId(ublVersionId);
	}

	@Override
	public String getCustomizationId() {
		return retention.getCustomizationId();
	}

	@Override
	public void setCustomizationId(String customizationId) {
		retention.setCustomizationId(customizationId);
	}

	@Override
	public String getDocumentCurrencyCode() {
		return retention.getDocumentCurrencyCode();
	}

	@Override
	public void setDocumentCurrencyCode(String value) {
		retention.setDocumentCurrencyCode(value);
	}

	@Override
	public LocalDate getIssueDate() {
		return retention.getIssueDate();
	}

	@Override
	public void setIssueDate(LocalDate issueDate) {
		retention.setIssueDate(issueDate);
	}

	@Override
	public String getSunatRetentionSystemCode() {
		return retention.getSunatRetentionSystemCode();
	}

	@Override
	public void setSunatRetentionSystemCode(String code) {
		retention.setSunatRetentionSystemCode(code);
	}

	@Override
	public BigDecimal getSunatRetentionPercent() {
		return retention.getSunatRetentionPercent();
	}

	@Override
	public void setSunatRetentionPercent(BigDecimal percent) {
		retention.setSunatRetentionPercent(percent);
	}

	@Override
	public BigDecimal getTotalInvoiceAmount() {
		return retention.getTotalInvoiceAmount();
	}

	@Override
	public void setTotalInvoiceAmount(BigDecimal totalInvoiceAmount) {
		retention.setTotalInvoiceAmount(totalInvoiceAmount);
	}

	@Override
	public BigDecimal getSunatTotalPaid() {
		return retention.getTotalPaid();
	}

	@Override
	public void setSunatTotalPaid(BigDecimal totalPaid) {
		retention.setTotalPaid(totalPaid);
	}

	@Override
	public List<String> getNotes() {
		return retention.getNotes();
	}

	@Override
	public void setNotes(List<String> notes) {
		retention.setNotes(notes);
	}

	@Override
	public List<RetentionDocumentReferenceModel> getSunatRetentionDocumentReference() {
		return retention.getSunatRetentionDocumentReferences().stream()
				.map(f -> new RetentionDocumentReferenceAdapter(session, em, f)).collect(Collectors.toList());
	}

	@Override
	public RetentionDocumentReferenceModel addSunatRetentionDocumentReference() {
		List<RetentionDocumentReferenceEntity> entities = retention.getSunatRetentionDocumentReferences();

		RetentionDocumentReferenceEntity entity = new RetentionDocumentReferenceEntity();
		entities.add(entity);
		return new RetentionDocumentReferenceAdapter(session, em, entity);
	}

	@Override
	public byte[] getXmlDocument() {
		return retention.getXmlDocument();
	}

	@Override
	public void setXmlDocument(byte[] bytes) {
		retention.setXmlDocument(bytes);
	}

	@Override
	public Set<String> getRequiredActions() {
		Set<String> result = new HashSet<>();
		for (RetentionRequiredActionEntity attr : retention.getRequiredActions()) {
			result.add(attr.getAction());
		}
		return result;
	}

	@Override
	public void addRequiredAction(String actionName) {
		for (RetentionRequiredActionEntity attr : retention.getRequiredActions()) {
			if (attr.getAction().equals(actionName)) {
				return;
			}
		}
		RetentionRequiredActionEntity attr = new RetentionRequiredActionEntity();
		attr.setAction(actionName);
		attr.setRetention(retention);
		em.persist(attr);
		retention.getRequiredActions().add(attr);
	}

	@Override
	public void removeRequiredAction(String actionName) {
		Iterator<RetentionRequiredActionEntity> it = retention.getRequiredActions().iterator();
		while (it.hasNext()) {
			RetentionRequiredActionEntity attr = it.next();
			if (attr.getAction().equals(actionName)) {
				it.remove();
				em.remove(attr);
			}
		}
	}

	@Override
	public PartyModel getAgentParty() {
		if (retention.getAgentParty() == null) {
			return null;
		}
		return new PartyAdapter(session, em, retention.getAgentParty());
	}

	@Override
	public PartyModel getAgentPartyAsNotNull() {
		if (retention.getAgentParty() == null) {
			PartyEntity entity = new PartyEntity();
			retention.setAgentParty(entity);
		}
		return new PartyAdapter(session, em, retention.getAgentParty());
	}

	@Override
	public PartyModel getReceiverParty() {
		if (retention.getReceiverParty() == null) {
			return null;
		}
		return new PartyAdapter(session, em, retention.getReceiverParty());
	}

	@Override
	public PartyModel getReceiverPartyAsNotNull() {
		if (retention.getReceiverParty() == null) {
			PartyEntity entity = new PartyEntity();
			retention.setReceiverParty(entity);
		}
		return new PartyAdapter(session, em, retention.getReceiverParty());
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
		return retention.getSendEvents().stream().map(f -> new SendEventAdapter(session, organization, em, f))
				.collect(Collectors.toList());
	}

}
