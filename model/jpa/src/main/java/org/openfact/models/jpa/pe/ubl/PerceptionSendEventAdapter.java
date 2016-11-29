package org.openfact.models.jpa.pe.ubl;

import javax.persistence.EntityManager;

import org.jboss.logging.Logger;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.jpa.JpaModel;
import org.openfact.models.jpa.pe.entities.PerceptionSendEventEntity;
import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.PerceptionSendEventModel;

public class PerceptionSendEventAdapter implements PerceptionSendEventModel, JpaModel<PerceptionSendEventEntity> {
	protected static final Logger logger = Logger.getLogger(PerceptionSendEventAdapter.class);

	protected OrganizationModel organization;
	protected PerceptionSendEventEntity sendEvent;
	protected EntityManager em;
	protected OpenfactSession session;

	public PerceptionSendEventAdapter(OpenfactSession session, OrganizationModel organization, EntityManager em,
			PerceptionSendEventEntity sendEvent) {
		this.organization = organization;
		this.session = session;
		this.em = em;
		this.sendEvent = sendEvent;
	}

	@Override
	public PerceptionSendEventEntity getEntity() {
		return sendEvent;
	}

	@Override
	public String getId() {
		return sendEvent.getId();
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
	public void setDescription(String description) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public OrganizationModel getOrganization() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PerceptionModel getPerception() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPerception(PerceptionModel perception) {
		// TODO Auto-generated method stub
		
	}	

}
