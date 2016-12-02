package org.openfact.models.jpa.pe.ubl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.jboss.logging.Logger;
import org.openfact.models.FileModel;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.jpa.JpaModel;
import org.openfact.models.jpa.OrganizationAdapter;
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
		return sendEvent.isResult();
	}

	@Override
	public void setResult(boolean result) {
		sendEvent.setResult(result);		
	}

	@Override
	public String getDescription() {
		return sendEvent.getDescription();
	}

	@Override
	public void setDescription(String description) {
		sendEvent.setDescription(description);
	}

	@Override
	public OrganizationModel getOrganization() {
		if (sendEvent.getOrganization() == null) {
			return null;
		}
		return new OrganizationAdapter(session, em, sendEvent.getOrganization());
	}

	@Override
	public PerceptionModel getPerception() {
		if (sendEvent.getPerception() == null) {
			return null;
		}
		return new PerceptionAdapter(session,organization, em, sendEvent.getPerception());
	}

	@Override
	public void setPerception(PerceptionModel perception) {
		sendEvent.setPerception(PerceptionAdapter.toEntity(perception, em));		
	}

	public static PerceptionSendEventEntity toEntity(PerceptionSendEventModel model, EntityManager em) {
		if (model instanceof PerceptionSendEventAdapter) {
			return ((PerceptionSendEventAdapter) model).getEntity();
		}
		return em.getReference(PerceptionSendEventEntity.class, model.getId());
	}

	@Override
	public List<FileModel> getFileAttatchments() {
		return null;
	}

	@Override
	public void setFileAttatchments(List<FileModel> files) {
				
	}

	@Override
	public String getType() {
		return sendEvent.getType();
	}

	@Override
	public void setType(String type) {
		sendEvent.setType(type);
	}

	@Override
	public Map<String, String> getDestity() {
		return sendEvent.getDestiny();
	}

	@Override
	public void setDestiny(Map<String, String> destiny) {
		sendEvent.setDestiny(destiny);		
	}

	@Override
	public LocalDateTime getCreatedTimestamp() {
		return sendEvent.getCreatedTimestamp();
	}

}
