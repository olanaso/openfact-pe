package org.openfact.pe.models.jpa.ubl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.jboss.logging.Logger;
import org.openfact.models.FileModel;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.StorageFileModel;
import org.openfact.models.jpa.JpaModel;
import org.openfact.models.jpa.OrganizationAdapter;
import org.openfact.pe.models.RetentionModel;
import org.openfact.pe.models.RetentionSendEventModel;
import org.openfact.pe.models.jpa.entities.RetentionSendEventEntity;

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
	public OrganizationModel getOrganization() {
		if (sendEvent.getOrganization() == null) {
			return null;
		}
		return new OrganizationAdapter(session, em, sendEvent.getOrganization());
	}

	@Override
	public RetentionModel getRetention() {
		if (sendEvent.getRetention() == null) {
			return null;
		}
		return new RetentionAdapter(session, organization, em, sendEvent.getRetention());
	}

	@Override
	public void setRetention(RetentionModel retention) {
		sendEvent.setRetention(RetentionAdapter.toEntity(retention, em));
	}

	public static RetentionSendEventEntity toEntity(RetentionSendEventModel model, EntityManager em) {
		if (model instanceof RetentionSendEventAdapter) {
			return ((RetentionSendEventAdapter) model).getEntity();
		}
		return em.getReference(RetentionSendEventEntity.class, model.getId());
	}


	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setType(String type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, String> getDestity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDestiny(Map<String, String> destiny) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public LocalDateTime getCreatedTimestamp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StorageFileModel addFileAttatchments(FileModel file) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StorageFileModel> getFileAttatchments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StorageFileModel> getFileResponseAttatchments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StorageFileModel addFileResponseAttatchments(FileModel file) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getResponse() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setResponse(Map<String, String> response) {
		// TODO Auto-generated method stub
		
	}

}
