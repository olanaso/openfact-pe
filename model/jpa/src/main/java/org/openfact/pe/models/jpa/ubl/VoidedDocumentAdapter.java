package org.openfact.pe.models.jpa.ubl;

import java.time.LocalDate;
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
import org.openfact.pe.models.VoidedDocumentModel;
import org.openfact.pe.models.VoidedDocumentsLineModel;
import org.openfact.pe.models.jpa.entities.VoidedDocumentsEntity;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDocumentId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOrganizationId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDocumentCurrencyCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDocumentCurrencyCode(String value) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getUblVersionId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setUblVersionId(String ublVersionId) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getCustomizationId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCustomizationId(String customizationId) {
		// TODO Auto-generated method stub

	}

	@Override
	public LocalDate getReferenceDate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setReferenceDate(LocalDate referenceDate) {
		// TODO Auto-generated method stub

	}

	@Override
	public LocalDate getIssueDate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setIssueDate(LocalDate issueDate) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<String> getNotes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setNote(List<String> notes) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<VoidedDocumentsLineModel> getVoidedDocumentsLine() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VoidedDocumentsLineModel addVoidedDocumentsLine() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getXmlDocument() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setXmlDocument(byte[] bytes) {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<String> getRequiredActions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addRequiredAction(String action) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeRequiredAction(String action) {
		// TODO Auto-generated method stub

	}

	@Override
	public SupplierPartyModel getAccountingSupplierParty() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SupplierPartyModel addAccountingSupplierParty() {
		// TODO Auto-generated method stub
		return null;
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

}
