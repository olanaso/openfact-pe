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
import org.openfact.models.jpa.pe.entities.RetentionSendEventEntity;
import org.openfact.models.ubl.SendEventModel;
import org.openfact.pe.models.RetentionModel;
import org.openfact.ubl.UblSendEventProvider;
import org.openfact.ubl.UblSenderException;

public class JpaRetentionSendEventProvider extends AbstractHibernateStorage implements UblSendEventProvider {
	protected static final Logger logger = Logger.getLogger(JpaRetentionSendEventProvider.class);

	private final OpenfactSession session;
	protected EntityManager em;

	public JpaRetentionSendEventProvider(OpenfactSession session, EntityManager em) {
		this.session = session;
		this.em = em;
	}

	@Override
	public void close() {
	}

	@Override
	public SendEventModel addEvent(OrganizationModel organization, Object model, byte[] xmlSubmitted, byte[] response,
			boolean isAccepted) throws UblSenderException {
		RetentionModel retention = (RetentionModel) model;
		RetentionSendEventEntity sendEventEntity = new RetentionSendEventEntity();
		sendEventEntity.setRetentions(Arrays.asList(RetentionAdapter.toEntity(retention, em)));
		sendEventEntity.setXmlDoument(xmlSubmitted);
		if (response != null) {
			sendEventEntity.setDocumentResponse(response);
		}
		sendEventEntity.setID(retention.getDocumentId());
		sendEventEntity.setAccepted(isAccepted);
		sendEventEntity.setCreatedTimestamp(LocalDateTime.now());
		em.persist(sendEventEntity);
		em.flush();
		final SendEventModel adapter = new RetentionSendEventAdapter(session, organization, em, sendEventEntity);
		return adapter;
	}

	@Override
	public List<SendEventModel> getEvents(OrganizationModel organization, Object model) throws UblSenderException {
		RetentionModel retention = (RetentionModel) model;
		String queryName = "getAllSendEventsByRetention";
		TypedQuery<RetentionSendEventEntity> query = em.createNamedQuery(queryName, RetentionSendEventEntity.class);
		query.setParameter("retentionId", retention.getId());

		List<RetentionSendEventEntity> results = query.getResultList();
		List<SendEventModel> sendEvents = results.stream()
				.map(f -> new RetentionSendEventAdapter(session, organization, em, f)).collect(Collectors.toList());
		return sendEvents;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
}
