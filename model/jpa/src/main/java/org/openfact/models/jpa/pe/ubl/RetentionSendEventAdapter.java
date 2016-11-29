package org.openfact.models.jpa.pe.ubl;

import javax.persistence.EntityManager;

import org.jboss.logging.Logger;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.jpa.JpaModel;
import org.openfact.models.jpa.pe.entities.RetentionSendEventEntity;
import org.openfact.pe.models.RetentionModel;
import org.openfact.pe.models.RetentionSendEventModel;

public class RetentionSendEventAdapter implements RetentionSendEventModel, JpaModel<RetentionSendEventEntity> {
	protected static final Logger logger = Logger.getLogger(RetentionSendEventAdapter.class);

	protected OrganizationModel organization;
	protected RetentionSendEventEntity sendEvent;
	protected EntityManager em;
	protected OpenfactSession session;

	public RetentionSendEventAdapter(OpenfactSession session, OrganizationModel organization, EntityManager em,
			RetentionSendEventEntity sendEvent) {
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
	public void setDescription(String description) {
		sendEvent.setDescription(description);
	}	

	@Override
	public RetentionSendEventEntity getEntity() {
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
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public OrganizationModel getOrganization() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public RetentionModel getRetention() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setRetention(RetentionModel retention) {
		// TODO Auto-generated method stub
		
	}

	

}
