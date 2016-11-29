package org.openfact.models.jpa.pe.ubl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.jboss.logging.Logger;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.jpa.JpaModel;
import org.openfact.models.jpa.pe.entities.SummaryDocumentsEntity;
import org.openfact.models.jpa.pe.entities.SummaryDocumentsSendEventEntity;
import org.openfact.pe.models.SummaryDocumentModel;
import org.openfact.pe.models.SummaryDocumentsSendEventModel;

public class SummaryDocumentSendEventAdapter
		implements SummaryDocumentsSendEventModel, JpaModel<SummaryDocumentsSendEventEntity> {
	protected static final Logger logger = Logger.getLogger(SummaryDocumentSendEventAdapter.class);

	protected OrganizationModel organization;
	protected SummaryDocumentsSendEventEntity sendEvent;
	protected EntityManager em;
	protected OpenfactSession session;

	public SummaryDocumentSendEventAdapter(OpenfactSession session, OrganizationModel organization, EntityManager em,
			SummaryDocumentsSendEventEntity sendEvent) {
		this.organization = organization;
		this.session = session;
		this.em = em;
		this.sendEvent = sendEvent;
	}

	@Override
	public String getId() {
		return sendEvent.getId();
	}

	@Override
	public String getDescription() {
		return sendEvent.getDescription();
	}

	@Override
	public SummaryDocumentsSendEventEntity getEntity() {
		return sendEvent;
	}

	@Override
	public boolean getResult() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setResult(boolean result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDescription(String description) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public OrganizationModel getOrganization() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SummaryDocumentModel getSummaryDocument() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSummaryDocument(SummaryDocumentModel summaryDocument) {
		// TODO Auto-generated method stub
		
	}
}
