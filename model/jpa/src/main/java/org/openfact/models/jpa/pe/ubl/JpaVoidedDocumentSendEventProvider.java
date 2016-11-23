package org.openfact.models.jpa.pe.ubl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.jboss.logging.Logger;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.jpa.AbstractHibernateStorage;
import org.openfact.models.jpa.pe.entities.VoidedDocumentsSendEventEntity;
import org.openfact.models.ubl.SendEventModel;
import org.openfact.pe.models.VoidedDocumentModel;
import org.openfact.ubl.UblSendEventProvider;
import org.openfact.ubl.UblSenderException;

public class JpaVoidedDocumentSendEventProvider extends AbstractHibernateStorage implements UblSendEventProvider {

	protected static final Logger logger = Logger.getLogger(JpaVoidedDocumentSendEventProvider.class);

	private final OpenfactSession session;
	protected EntityManager em;

	public JpaVoidedDocumentSendEventProvider(OpenfactSession session, EntityManager em) {
		this.session = session;
		this.em = em;
	}

	@Override
	public void close() {

	}

	@Override
	public SendEventModel addEvent(OrganizationModel organization, Object model, byte[] xmlSubmitted, byte[] response,
			boolean isAccepted) throws UblSenderException {
		VoidedDocumentModel voidedDocument = (VoidedDocumentModel) model;
		VoidedDocumentsSendEventEntity sendEventEntity = new VoidedDocumentsSendEventEntity();
		sendEventEntity.setVoidedDocuments(Arrays.asList(VoidedDocumentAdapter.toEntity(voidedDocument, em)));
		sendEventEntity.setXmlDoument(xmlSubmitted);
		if (response != null) {
			sendEventEntity.setDocumentResponse(response);
		}
		sendEventEntity.setID(voidedDocument.getDocumentId());
		sendEventEntity.setAccepted(isAccepted);
		sendEventEntity.setCreatedTimestamp(LocalDateTime.now());
		em.persist(sendEventEntity);
		em.flush();
		final SendEventModel adapter = new VoidedDocumentSendEventAdapter(session, organization, em, sendEventEntity);
		return adapter;
	}

	@Override
	public List<SendEventModel> getEvents(OrganizationModel organization, Object model) throws UblSenderException {
		VoidedDocumentModel voidedDocument = (VoidedDocumentModel) model;
		String queryName = "getAllSendEventsByVoidedDocuments";
		TypedQuery<VoidedDocumentsSendEventEntity> query = em.createNamedQuery(queryName, VoidedDocumentsSendEventEntity.class);
		query.setParameter("voidedDocumentsId", voidedDocument.getId());

		List<VoidedDocumentsSendEventEntity> results = query.getResultList();
		List<SendEventModel> sendEvents = results.stream()
				.map(f -> new VoidedDocumentSendEventAdapter(session, organization, em, f)).collect(Collectors.toList());
		return sendEvents;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

}
