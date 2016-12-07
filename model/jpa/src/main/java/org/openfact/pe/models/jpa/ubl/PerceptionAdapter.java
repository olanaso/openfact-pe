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
import org.openfact.pe.models.PerceptionDocumentReferenceModel;
import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.jpa.entities.PerceptionDocumentReferenceEntity;
import org.openfact.pe.models.jpa.entities.PerceptionEntity;
import org.openfact.pe.models.jpa.entities.PerceptionRequiredActionEntity;
import org.openfact.ubl.SendEventModel;

public class PerceptionAdapter implements PerceptionModel, JpaModel<PerceptionEntity> {
	protected static final Logger logger = Logger.getLogger(PerceptionAdapter.class);

	protected OrganizationModel organization;
	protected PerceptionEntity perception;
	protected EntityManager em;
	protected OpenfactSession session;

	public PerceptionAdapter(OpenfactSession session, OrganizationModel organization, EntityManager em,
			PerceptionEntity perception) {
		this.organization = organization;
		this.session = session;
		this.em = em;
		this.perception = perception;
	}

	@Override
	public PerceptionEntity getEntity() {
		return perception;
	}

	@Override
	public String getId() {
		return perception.getId();
	}

	public static PerceptionEntity toEntity(PerceptionModel model, EntityManager em) {
		if (model instanceof PerceptionAdapter) {
			return ((PerceptionAdapter) model).getEntity();
		}
		return em.getReference(PerceptionEntity.class, model.getId());
	}

	@Override
	public String getDocumentId() {
		return perception.getDocumentId();
	}

	@Override
	public String getOrganizationId() {
		return perception.getOrganizationId();
	}

	@Override
	public void setDocumentId(String documentId) {
		perception.setOrganizationId(documentId);
	}

	@Override
	public String getUblVersionID() {
		return perception.getUblVersionId();
	}

	@Override
	public void setUblVersionID(String ublVersionID) {
		perception.setUblVersionId(ublVersionID);
	}

	@Override
	public String getCustomizationID() {
		return perception.getCustomizationId();
	}

	@Override
	public void setCustomizationID(String customizationID) {
		perception.setCustomizationId(customizationID);
	}

	@Override
	public String getDocumentCurrencyCode() {
		return perception.getDocumentCurrencyCode();
	}

	@Override
	public void setDocumentCurrencyCode(String value) {
		perception.setDocumentCurrencyCode(value);
	}

	@Override
	public LocalDate getIssueDate() {
		return perception.getIssueDate();
	}

	@Override
	public void setIssueDate(LocalDate issueDate) {
		perception.setIssueDate(issueDate);
	}

	@Override
	public String getSUNATPerceptionSystemCode() {
		return perception.getSunatPerceptionSystemCode();
	}

	@Override
	public void setSUNATPerceptionSystemCode(String sUNATPerceptionSystemCode) {
		perception.setSunatPerceptionSystemCode(sUNATPerceptionSystemCode);
	}

	@Override
	public BigDecimal getSUNATPerceptionPercent() {
		return perception.getSunatPerceptionPercent();
	}

	@Override
	public void setSUNATPerceptionPercent(BigDecimal sUNATPerceptionPercent) {
		perception.setSunatPerceptionPercent(sUNATPerceptionPercent);
	}

	@Override
	public BigDecimal getTotalInvoiceAmount() {
		return perception.getTotalInvoiceAmount();
	}

	@Override
	public void setTotalInvoiceAmount(BigDecimal totalInvoiceAmount) {
		perception.setTotalInvoiceAmount(totalInvoiceAmount);
	}

	@Override
	public BigDecimal getSUNATTotalCashed() {
		return perception.getSunatTotalCashed();
	}

	@Override
	public void setSUNATTotalCashed(BigDecimal sUNATTotalCashed) {
		perception.setSunatTotalCashed(sUNATTotalCashed);
	}

	@Override
	public List<String> getNotes() {
		return perception.getNotes();
	}

	@Override
	public void setNotes(List<String> notes) {
		perception.setNotes(notes);
	}

	@Override
	public List<PerceptionDocumentReferenceModel> getSunatPerceptionDocumentReference() {
		return perception.getSunatPerceptionDocumentReferences().stream()
				.map(f -> new PerceptionDocumentReferenceAdapter(session, em, f)).collect(Collectors.toList());
	}

	@Override
	public PerceptionDocumentReferenceModel addSunatPerceptionDocumentReference() {
		List<PerceptionDocumentReferenceEntity> entities = perception.getSunatPerceptionDocumentReferences();

		PerceptionDocumentReferenceEntity entity = new PerceptionDocumentReferenceEntity();
		entities.add(entity);
		return new PerceptionDocumentReferenceAdapter(session, em, entity);
	}

	@Override
	public byte[] getXmlDocument() {
		return perception.getXmlDocument();
	}

	@Override
	public void setXmlDocument(byte[] object) {
		perception.setXmlDocument(object);
	}

	@Override
	public Set<String> getRequiredActions() {
		Set<String> result = new HashSet<>();
		for (PerceptionRequiredActionEntity attr : perception.getRequiredActions()) {
			result.add(attr.getAction());
		}
		return result;
	}

	@Override
	public void addRequiredAction(String actionName) {
		for (PerceptionRequiredActionEntity attr : perception.getRequiredActions()) {
			if (attr.getAction().equals(actionName)) {
				return;
			}
		}
		PerceptionRequiredActionEntity attr = new PerceptionRequiredActionEntity();
		attr.setAction(actionName);
		attr.setPerception(perception);
		em.persist(attr);
		perception.getRequiredActions().add(attr);

	}

	@Override
	public void removeRequiredAction(String actionName) {
		Iterator<PerceptionRequiredActionEntity> it = perception.getRequiredActions().iterator();
		while (it.hasNext()) {
			PerceptionRequiredActionEntity attr = it.next();
			if (attr.getAction().equals(actionName)) {
				it.remove();
				em.remove(attr);
			}
		}
	}

	@Override
	public PartyModel getAgentParty() {
		if (perception.getAgentParty() == null) {
			return null;
		}
		return new PartyAdapter(session, em, perception.getAgentParty());
	}

	@Override
	public PartyModel getAgentPartyAsNotNull() {
		if (perception.getAgentParty() == null) {
			PartyEntity entity = new PartyEntity();
			perception.setAgentParty(entity);
		}
		return new PartyAdapter(session, em, perception.getAgentParty());
	}

	@Override
	public PartyModel getReceiverParty() {
		if (perception.getReceiverParty() == null) {
			return null;
		}
		return new PartyAdapter(session, em, perception.getReceiverParty());
	}

	@Override
	public PartyModel getReceiverPartyAsNotNull() {
		if (perception.getReceiverParty() == null) {
			PartyEntity entity = new PartyEntity();
			perception.setReceiverParty(entity);
		}
		return new PartyAdapter(session, em, perception.getReceiverParty());
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
		return perception.getSendEvents().stream().map(f -> new SendEventAdapter(session, organization, em, f))
				.collect(Collectors.toList());
	}
}
