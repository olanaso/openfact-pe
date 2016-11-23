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
import org.openfact.models.jpa.pe.entities.SummaryDocumentsSendEventEntity;
import org.openfact.models.ubl.SendEventModel;
import org.openfact.pe.models.SummaryDocumentModel;
import org.openfact.ubl.UblSendEventProvider;
import org.openfact.ubl.UblSenderException;

public class JpaSummaryDocumentSendEventProvider extends AbstractHibernateStorage implements UblSendEventProvider {
	protected static final Logger logger = Logger.getLogger(JpaSummaryDocumentSendEventProvider.class);

	private final OpenfactSession session;
	protected EntityManager em;

	public JpaSummaryDocumentSendEventProvider(OpenfactSession session, EntityManager em) {
		this.session = session;
		this.em = em;
	}

	@Override
	public void close() {

	}

	@Override
	public SendEventModel addEvent(OrganizationModel organization, Object model, byte[] xmlSubmitted, byte[] response,
			boolean isAccepted) throws UblSenderException {
		SummaryDocumentModel summaryDocument = (SummaryDocumentModel) model;
		SummaryDocumentsSendEventEntity sendEventEntity = new SummaryDocumentsSendEventEntity();
		sendEventEntity.setSummaryDocuments(Arrays.asList(SummaryDocumentAdapter.toEntity(summaryDocument, em)));
		sendEventEntity.setXmlDoument(xmlSubmitted);
		if (response != null) {
			sendEventEntity.setDocumentResponse(response);
		}
		sendEventEntity.setID(summaryDocument.getDocumentId());
		sendEventEntity.setAccepted(isAccepted);
		sendEventEntity.setCreatedTimestamp(LocalDateTime.now());
		em.persist(sendEventEntity);
		em.flush();
		final SendEventModel adapter = new SummaryDocumentSendEventAdapter(session, organization, em, sendEventEntity);
		return adapter;
	}

	@Override
	public List<SendEventModel> getEvents(OrganizationModel organization, Object model) throws UblSenderException {
		SummaryDocumentModel summaryDocument = (SummaryDocumentModel) model;
		String queryName = "getAllSendEventsBySummaryDocuments";
		TypedQuery<SummaryDocumentsSendEventEntity> query = em.createNamedQuery(queryName,
				SummaryDocumentsSendEventEntity.class);
		query.setParameter("summaryDocumentsId", summaryDocument.getId());

		List<SummaryDocumentsSendEventEntity> results = query.getResultList();
		List<SendEventModel> sendEvents = results.stream()
				.map(f -> new SummaryDocumentSendEventAdapter(session, organization, em, f))
				.collect(Collectors.toList());
		return sendEvents;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
}
