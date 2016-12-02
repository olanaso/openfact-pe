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
		return sendEvent.isResult();
	}

	@Override
	public void setResult(boolean result) {
		sendEvent.setResult(result);
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
	public SummaryDocumentModel getSummaryDocument() {
		if (sendEvent.getSummaryDocuments() == null) {
			return null;
		}
		return new SummaryDocumentAdapter(session, organization, em, sendEvent.getSummaryDocuments());
	}

	@Override
	public void setSummaryDocument(SummaryDocumentModel summaryDocument) {
		sendEvent.setSummaryDocuments(SummaryDocumentAdapter.toEntity(summaryDocument, em));

	}

	public static SummaryDocumentsSendEventEntity toEntity(SummaryDocumentsSendEventModel model, EntityManager em) {
		if (model instanceof SummaryDocumentSendEventAdapter) {
			return ((SummaryDocumentSendEventAdapter) model).getEntity();
		}
		return em.getReference(SummaryDocumentsSendEventEntity.class, model.getId());
	}

	@Override
	public List<FileModel> getFileAttatchments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFileAttatchments(List<FileModel> files) {
		// TODO Auto-generated method stub
		
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
}
