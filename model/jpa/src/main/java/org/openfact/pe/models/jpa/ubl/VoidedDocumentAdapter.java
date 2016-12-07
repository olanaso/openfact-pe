package org.openfact.pe.models.jpa.ubl;

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
import org.openfact.models.SupplierPartyModel;
import org.openfact.models.enums.RequiredAction;
import org.openfact.models.jpa.JpaModel;
import org.openfact.models.jpa.SendEventAdapter;
import org.openfact.models.jpa.SupplierPartyAdapter;
import org.openfact.models.jpa.entities.SupplierPartyEntity;
import org.openfact.pe.models.VoidedDocumentModel;
import org.openfact.pe.models.jpa.entities.VoidedDocumentsEntity;
import org.openfact.pe.models.jpa.entities.VoidedDocumentsRequiredActionEntity;
import org.openfact.ubl.SendEventModel;

public class VoidedDocumentAdapter implements VoidedDocumentModel, JpaModel<VoidedDocumentsEntity> {

	protected static final Logger logger = Logger.getLogger(VoidedDocumentAdapter.class);

	protected OrganizationModel organization;
	protected VoidedDocumentsEntity voidedDocuments;
	protected EntityManager em;
	protected OpenfactSession session;

	public VoidedDocumentAdapter(OpenfactSession session, OrganizationModel organization, EntityManager em,
			VoidedDocumentsEntity voidedDocuments) {
		this.organization = organization;
		this.session = session;
		this.em = em;
		this.voidedDocuments = voidedDocuments;
	}

	@Override
	public VoidedDocumentsEntity getEntity() {
		return voidedDocuments;
	}

	public static VoidedDocumentsEntity toEntity(VoidedDocumentModel model, EntityManager em) {
		if (model instanceof VoidedDocumentAdapter) {
			return ((VoidedDocumentAdapter) model).getEntity();
		}
		return em.getReference(VoidedDocumentsEntity.class, model.getId());
	}

	@Override
	public String getId() {
		return voidedDocuments.getId();
	}

	@Override
	public String getDocumentId() {
		return voidedDocuments.getDocumentId();
	}

	@Override
	public String getOrganizationId() {
		return voidedDocuments.getOrganizationId();
	}

	@Override
	public String getDocumentCurrencyCode() {
		return voidedDocuments.getDocumentCurrencyCode();
	}

	@Override
	public void setDocumentCurrencyCode(String value) {
		voidedDocuments.setDocumentCurrencyCode(value);
	}

	@Override
	public String getUblVersionId() {
		return voidedDocuments.getUblVersionId();
	}

	@Override
	public void setUblVersionId(String ublVersionId) {
		voidedDocuments.setUblVersionId(ublVersionId);
	}

	@Override
	public String getCustomizationId() {
		return voidedDocuments.getCustomizationId();
	}

	@Override
	public void setCustomizationId(String customizationId) {
		voidedDocuments.setCustomizationId(customizationId);
	}

	@Override
	public LocalDate getReferenceDate() {
		return voidedDocuments.getReferenceDate();
	}

	@Override
	public void setReferenceDate(LocalDate referenceDate) {
		voidedDocuments.setReferenceDate(referenceDate);
	}

	@Override
	public LocalDate getIssueDate() {
		return voidedDocuments.getIssueDate();
	}

	@Override
	public void setIssueDate(LocalDate issueDate) {
		voidedDocuments.setIssueDate(issueDate);
	}

	@Override
	public List<String> getNotes() {
		return voidedDocuments.getNotes();
	}

	@Override
	public void setNote(List<String> notes) {
		voidedDocuments.setNotes(notes);
	}

	@Override
	public byte[] getXmlDocument() {
		return voidedDocuments.getXmlDocument();
	}

	@Override
	public void setXmlDocument(byte[] bytes) {
		voidedDocuments.setXmlDocument(bytes);
	}

	@Override
	public Set<String> getRequiredActions() {
		Set<String> result = new HashSet<>();
		for (VoidedDocumentsRequiredActionEntity attr : voidedDocuments.getRequiredActions()) {
			result.add(attr.getAction());
		}
		return result;
	}

	@Override
	public void addRequiredAction(String actionName) {
		for (VoidedDocumentsRequiredActionEntity attr : voidedDocuments.getRequiredActions()) {
			if (attr.getAction().equals(actionName)) {
				return;
			}
		}
		VoidedDocumentsRequiredActionEntity attr = new VoidedDocumentsRequiredActionEntity();
		attr.setAction(actionName);
		attr.setVoidedDocuments(voidedDocuments);
		em.persist(attr);
		voidedDocuments.getRequiredActions().add(attr);

	}

	@Override
	public void removeRequiredAction(String action) {
		Iterator<VoidedDocumentsRequiredActionEntity> it = voidedDocuments.getRequiredActions().iterator();
		while (it.hasNext()) {
			VoidedDocumentsRequiredActionEntity attr = it.next();
			if (attr.getAction().equals(action)) {
				it.remove();
				em.remove(attr);
			}
		}
	}

	@Override
	public SupplierPartyModel getAccountingSupplierParty() {
		if (voidedDocuments.getAccountingSupplierParty() == null) {
			return null;
		}
		return new SupplierPartyAdapter(session, em, voidedDocuments.getAccountingSupplierParty());
	}

	@Override
	public SupplierPartyModel getAccountingSupplierPartyAsNotNull() {
		if (voidedDocuments.getAccountingSupplierParty() == null) {
			SupplierPartyEntity entity = new SupplierPartyEntity();
			voidedDocuments.setAccountingSupplierParty(entity);
		}
		return new SupplierPartyAdapter(session, em, voidedDocuments.getAccountingSupplierParty());
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
		return voidedDocuments.getSendEvents().stream().map(f -> new SendEventAdapter(session, organization, em, f))
				.collect(Collectors.toList());
	}

	@Override
	public void setDocumentId(String documentId) {
		voidedDocuments.setDocumentId(documentId);
	}

}
