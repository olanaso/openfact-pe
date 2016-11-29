package org.openfact.models.jpa.pe.ubl;

import javax.persistence.EntityManager;

import org.jboss.logging.Logger;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.jpa.JpaModel;
import org.openfact.models.jpa.OrganizationAdapter;
import org.openfact.models.jpa.pe.entities.VoidedDocumentsSendEventEntity;
import org.openfact.pe.models.VoidedDocumentModel;
import org.openfact.pe.models.VoidedDocumentsSendEventModel;

public class VoidedDocumentSendEventAdapter
		implements VoidedDocumentsSendEventModel, JpaModel<VoidedDocumentsSendEventEntity> {
	protected static final Logger logger = Logger.getLogger(VoidedDocumentSendEventAdapter.class);

	protected OrganizationModel organization;
	protected VoidedDocumentsSendEventEntity sendEvent;
	protected EntityManager em;
	protected OpenfactSession session;

	public VoidedDocumentSendEventAdapter(OpenfactSession session, OrganizationModel organization, EntityManager em,
			VoidedDocumentsSendEventEntity sendEvent) {
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
	public void setDescription(String description) {
		sendEvent.setDescription(description);
	}

	@Override
	public VoidedDocumentsSendEventEntity getEntity() {
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
	public OrganizationModel getOrganization() {
		if (sendEvent.getOrganization() == null) {
			return null;
		}
		return new OrganizationAdapter(session, em, sendEvent.getOrganization());
	}

	@Override
	public VoidedDocumentModel getVoidedDocument() {
		if (sendEvent.getVoidedDocuments() == null) {
			return null;
		}
		return new VoidedDocumentAdapter(session, organization, em, sendEvent.getVoidedDocuments());
	}

	@Override
	public void setVoidedDocument(VoidedDocumentModel voidedDocument) {
		sendEvent.setVoidedDocuments(VoidedDocumentAdapter.toEntity(voidedDocument, em));
	}

	public static VoidedDocumentsSendEventEntity toEntity(VoidedDocumentsSendEventModel model, EntityManager em) {
		if (model instanceof VoidedDocumentSendEventAdapter) {
			return ((VoidedDocumentSendEventAdapter) model).getEntity();
		}
		return em.getReference(VoidedDocumentsSendEventEntity.class, model.getId());
	}
}
