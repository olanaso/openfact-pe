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
import org.openfact.models.jpa.pe.entities.PerceptionSendEventEntity;
import org.openfact.models.ubl.SendEventModel;
import org.openfact.pe.models.PerceptionModel;
import org.openfact.ubl.UblSendEventProvider;
import org.openfact.ubl.UblSenderException;

public class JpaPerceptionSendEventProvider extends AbstractHibernateStorage implements UblSendEventProvider {

	protected static final Logger logger = Logger.getLogger(JpaPerceptionSendEventProvider.class);

	private final OpenfactSession session;
	protected EntityManager em;

	public JpaPerceptionSendEventProvider(OpenfactSession session, EntityManager em) {
		this.session = session;
		this.em = em;
	}

	@Override
	public void close() {

	}

	@Override
	public SendEventModel addEvent(OrganizationModel organization, Object model, byte[] xmlSubmitted, byte[] response,
			boolean isAccepted) throws UblSenderException {

		PerceptionModel perception = (PerceptionModel) model;
		PerceptionSendEventEntity sendEventEntity = new PerceptionSendEventEntity();
		sendEventEntity.setPerceptions(Arrays.asList(PerceptionAdapter.toEntity(perception, em)));
		sendEventEntity.setXmlDoument(xmlSubmitted);
		if (response != null) {
			sendEventEntity.setDocumentResponse(response);
		}
		sendEventEntity.setID(perception.getDocumentId());
		sendEventEntity.setAccepted(isAccepted);
		sendEventEntity.setCreatedTimestamp(LocalDateTime.now());
		em.persist(sendEventEntity);
		em.flush();
		final SendEventModel adapter = new PerceptionSendEventAdapter(session, organization, em, sendEventEntity);
		return adapter;
	}

	@Override
	public List<SendEventModel> getEvents(OrganizationModel organization, Object model) throws UblSenderException {
		PerceptionModel perception = (PerceptionModel) model;
		String queryName = "getAllSendEventsByPerception";
		TypedQuery<PerceptionSendEventEntity> query = em.createNamedQuery(queryName, PerceptionSendEventEntity.class);
		query.setParameter("perceptionId", perception.getId());

		List<PerceptionSendEventEntity> results = query.getResultList();
		List<SendEventModel> sendEvents = results.stream()
				.map(f -> new PerceptionSendEventAdapter(session, organization, em, f)).collect(Collectors.toList());
		return sendEvents;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

}
