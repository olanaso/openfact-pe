package org.openfact.models.jpa.pe.ubl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.jboss.logging.Logger;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.PartyModel;
import org.openfact.models.enums.RequiredAction;
import org.openfact.models.jpa.JpaModel;
import org.openfact.models.jpa.pe.entities.RetentionEntity;
import org.openfact.pe.models.RetentionDocumentReferenceModel;
import org.openfact.pe.models.RetentionModel;

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
	public String getDocumentCurrencyCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDocumentCurrencyCode(String value) {
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
	public String getSunatRetentionSystemCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSunatRetentionSystemCode(String code) {
		// TODO Auto-generated method stub

	}

	@Override
	public BigDecimal getSunatRetentionPercent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSunatRetentionPercent(BigDecimal percent) {
		// TODO Auto-generated method stub

	}

	@Override
	public BigDecimal getTotalInvoiceAmount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTotalInvoiceAmount(BigDecimal totalInvoiceAmount) {
		// TODO Auto-generated method stub

	}

	@Override
	public BigDecimal getSunatTotalPaid() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSunatTotalPaid(BigDecimal totalPaid) {
		// TODO Auto-generated method stub

	}


	@Override
	public List<String> getNotes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setNotes(List<String> notes) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<RetentionDocumentReferenceModel> getSunatRetentionDocumentReference() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RetentionDocumentReferenceModel addSunatRetentionDocumentReference() {
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
	public PartyModel getAgentParty() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PartyModel getAgentPartyAsNotNull() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PartyModel getReceiverParty() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PartyModel getReceiverPartyAsNotNull() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addRequiredAction(RequiredAction action) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeRequiredAction(RequiredAction action) {
		// TODO Auto-generated method stub
		
	}

	

}
