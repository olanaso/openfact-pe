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
import org.openfact.pe.models.SummaryDocumentModel;
import org.openfact.pe.models.SummaryDocumentsLineModel;
import org.openfact.pe.models.jpa.entities.SummaryDocumentsEntity;
import org.openfact.ubl.SendEventModel;

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
	public String getCustomizationI() {
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
	public void setIssueDateTime(LocalDate issueDate) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<SummaryDocumentsLineModel> getSummaryDocumentsLines() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SummaryDocumentsLineModel addSummaryDocumentsLines() {
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
	public SupplierPartyModel getAccountingSupplierPartyAsNotNull() {
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
		 return summaryDocuments.getSendEvents().stream().map(f -> new SendEventAdapter(session, organization, em, f))
	                .collect(Collectors.toList());
	}
}
