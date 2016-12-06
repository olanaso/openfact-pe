package org.openfact.pe.models.jpa.ubl;

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
import org.openfact.pe.models.PerceptionDocumentReferenceModel;
import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.jpa.entities.PerceptionEntity;

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOrganizationId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDocumentId(String documentId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getUblVersionID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setUblVersionID(String ublVersionID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCustomizationID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCustomizationID(String customizationID) {
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
	public String getSUNATPerceptionSystemCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSUNATPerceptionSystemCode(String sUNATPerceptionSystemCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BigDecimal getSUNATPerceptionPercent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSUNATPerceptionPercent(BigDecimal sUNATPerceptionPercent) {
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
	public BigDecimal getSUNATTotalCashed() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSUNATTotalCashed(BigDecimal sUNATTotalCashed) {
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
	public List<PerceptionDocumentReferenceModel> getSunatPerceptionDocumentReference() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PerceptionDocumentReferenceModel addSunatPerceptionDocumentReference() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getXmlDocument() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setXmlDocument(byte[] object) {
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
