package org.openfact.models.jpa.pe.ubl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.jboss.logging.Logger;
import org.openfact.models.CreditNoteModel;
import org.openfact.models.DebitNoteModel;
import org.openfact.models.InvoiceModel;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.ScrollModel;
import org.openfact.models.enums.SendResultType;
import org.openfact.models.jpa.AbstractHibernateStorage;
import org.openfact.models.jpa.CreditNoteAdapter;
import org.openfact.models.jpa.DebitNoteAdapter;
import org.openfact.models.jpa.InvoiceAdapter;
import org.openfact.models.jpa.JpaScrollAdapter;
import org.openfact.models.jpa.OrganizationAdapter;
import org.openfact.models.jpa.SendEventAdapter;
import org.openfact.models.jpa.entities.CreditNoteSendEventEntity;
import org.openfact.models.jpa.entities.DebitNoteSendEventEntity;
import org.openfact.models.jpa.entities.InvoiceSendEventEntity;
import org.openfact.models.jpa.entities.SendEventEntity;
import org.openfact.models.jpa.pe.entities.PerceptionSendEventEntity;
import org.openfact.models.jpa.pe.entities.RetentionSendEventEntity;
import org.openfact.models.jpa.pe.entities.SummaryDocumentsSendEventEntity;
import org.openfact.models.jpa.pe.entities.VoidedDocumentsSendEventEntity;
import org.openfact.models.search.SearchCriteriaFilterOperator;
import org.openfact.models.search.SearchCriteriaModel;
import org.openfact.models.search.SearchResultsModel;
import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.RetentionModel;
import org.openfact.pe.models.SummaryDocumentModel;
import org.openfact.pe.models.SunatSendEventProvider;
import org.openfact.pe.models.VoidedDocumentModel;
import org.openfact.ubl.SendEventModel;

public class JpaSunatSendEventProvider extends AbstractHibernateStorage implements SunatSendEventProvider {
	protected static final Logger logger = Logger.getLogger(JpaSunatSendEventProvider.class);

	private final OpenfactSession session;
	protected EntityManager em;

	public JpaSunatSendEventProvider(OpenfactSession session, EntityManager em) {
		this.session = session;
		this.em = em;
	}

	@Override
	public void close() {
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public SendEventModel addSendEvent(OrganizationModel organization, SendResultType type, InvoiceModel invoice) {
		InvoiceSendEventEntity sendEvent = new InvoiceSendEventEntity();
		buildSendEvent(sendEvent, organization, type);
		sendEvent.setInvoice(InvoiceAdapter.toEntity(invoice, em));
		em.persist(sendEvent);
		em.flush();
		return new SendEventAdapter(session, organization, em, sendEvent);
	}

	@Override
	public SendEventModel addSendEvent(OrganizationModel organization, SendResultType type,
			CreditNoteModel creditNote) {
		CreditNoteSendEventEntity sendEvent = new CreditNoteSendEventEntity();
		buildSendEvent(sendEvent, organization, type);
		sendEvent.setCreditNote(CreditNoteAdapter.toEntity(creditNote, em));
		em.persist(sendEvent);
		em.flush();

		return new SendEventAdapter(session, organization, em, sendEvent);
	}

	@Override
	public SendEventModel addSendEvent(OrganizationModel organization, SendResultType type, DebitNoteModel debitNote) {
		DebitNoteSendEventEntity sendEvent = new DebitNoteSendEventEntity();
		buildSendEvent(sendEvent, organization, type);
		sendEvent.setDebitNote(DebitNoteAdapter.toEntity(debitNote, em));
		em.persist(sendEvent);
		em.flush();

		return new SendEventAdapter(session, organization, em, sendEvent);
	}

	@Override
	public SendEventModel getSendEventById(OrganizationModel organization, String id) {
		TypedQuery<SendEventEntity> query = em.createNamedQuery("getOrganizationSendEventById", SendEventEntity.class);
		query.setParameter("id", id);
		query.setParameter("organizationId", organization.getId());
		List<SendEventEntity> entities = query.getResultList();
		if (entities.size() == 0)
			return null;
		return new SendEventAdapter(session, organization, em, entities.get(0));
	}

	@Override
	public boolean removeSendEvent(OrganizationModel organization, String id) {
		return removeSendEvent(organization, getSendEventById(organization, id));
	}

	@Override
	public boolean removeSendEvent(OrganizationModel organization, SendEventModel sendEvent) {
		SendEventEntity sendEventEntity = em.find(SendEventEntity.class, sendEvent.getId());
		if (sendEventEntity == null)
			return false;

		em.remove(sendEventEntity);
		em.flush();
		return true;
	}

	@Override
	public int getSendEventsCount(OrganizationModel organization) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<SendEventModel> getSendEvents(OrganizationModel organization) {
		return getSendEvents(organization, -1, -1);
	}

	@Override
	public List<SendEventModel> getSendEvents(OrganizationModel organization, Integer firstResult, Integer maxResults) {
		String queryName = "getAllSendEventsByOrganization";

		TypedQuery<SendEventEntity> query = em.createNamedQuery(queryName, SendEventEntity.class);
		query.setParameter("organizationId", organization.getId());
		if (firstResult != -1) {
			query.setFirstResult(firstResult);
		}
		if (maxResults != -1) {
			query.setMaxResults(maxResults);
		}
		List<SendEventEntity> results = query.getResultList();
		List<SendEventModel> invoices = results.stream().map(f -> new SendEventAdapter(session, organization, em, f))
				.collect(Collectors.toList());
		return invoices;
	}

	@Override
	public SearchResultsModel<SendEventModel> searchForSendEvent(OrganizationModel organization,
			SearchCriteriaModel criteria) {
		criteria.addFilter("organization.id", organization.getId(), SearchCriteriaFilterOperator.eq);

		SearchResultsModel<SendEventEntity> entityResult = find(criteria, SendEventEntity.class);
		List<SendEventEntity> entities = entityResult.getModels();

		SearchResultsModel<SendEventModel> searchResult = new SearchResultsModel<>();
		List<SendEventModel> models = searchResult.getModels();

		entities.forEach(f -> models.add(new SendEventAdapter(session, organization, em, f)));
		searchResult.setTotalSize(entityResult.getTotalSize());
		return searchResult;
	}

	@Override
	public ScrollModel<SendEventModel> getSendEventsScroll(OrganizationModel organization) {
		return getSendEventsScroll(organization, true);
	}

	@Override
	public ScrollModel<SendEventModel> getSendEventsScroll(OrganizationModel organization, boolean asc) {
		return getSendEventsScroll(organization, asc, -1);
	}

	@Override
	public ScrollModel<SendEventModel> getSendEventsScroll(OrganizationModel organization, boolean asc,
			int scrollSize) {
		return getSendEventsScroll(organization, asc, scrollSize, -1);
	}

	@Override
	public ScrollModel<SendEventModel> getSendEventsScroll(OrganizationModel organization, boolean asc, int scrollSize,
			int fetchSize) {
		if (scrollSize == -1) {
			scrollSize = 5;
		}
		if (fetchSize == -1) {
			scrollSize = 1;
		}

		Criteria criteria = getSession().createCriteria(SendEventEntity.class)
				.add(Restrictions.eq("organization.id", organization.getId()))
				.addOrder(asc ? Order.asc("createdTimestamp") : Order.desc("createdTimestamp"));

		JpaScrollAdapter<SendEventModel, SendEventEntity> result = new JpaScrollAdapter<>(criteria, scrollSize,
				f -> new SendEventAdapter(session, organization, em, f));
		return result;
	}

	@Override
	public SendEventModel addSendEvent(OrganizationModel organization, SendResultType type,
			PerceptionModel perception) {
		PerceptionSendEventEntity sendEvent = new PerceptionSendEventEntity();
		buildSendEvent(sendEvent, organization, type);
		sendEvent.setPerception(PerceptionAdapter.toEntity(perception, em));
		em.persist(sendEvent);
		em.flush();

		return new SendEventAdapter(session, organization, em, sendEvent);
	}

	@Override
	public SendEventModel addSendEvent(OrganizationModel organization, SendResultType type, RetentionModel retention) {
		RetentionSendEventEntity sendEvent = new RetentionSendEventEntity();
		buildSendEvent(sendEvent, organization, type);
		sendEvent.setRetention(RetentionAdapter.toEntity(retention, em));
		em.persist(sendEvent);
		em.flush();

		return new SendEventAdapter(session, organization, em, sendEvent);
	}

	@Override
	public SendEventModel addSendEvent(OrganizationModel organization, SendResultType type,
			SummaryDocumentModel summaryDocument) {
		SummaryDocumentsSendEventEntity sendEvent = new SummaryDocumentsSendEventEntity();
		buildSendEvent(sendEvent, organization, type);
		sendEvent.setSummaryDocuments(SummaryDocumentAdapter.toEntity(summaryDocument, em));
		em.persist(sendEvent);
		em.flush();

		return new SendEventAdapter(session, organization, em, sendEvent);
	}

	@Override
	public SendEventModel addSendEvent(OrganizationModel organization, SendResultType type,
			VoidedDocumentModel voidedDocument) {
		VoidedDocumentsSendEventEntity sendEvent = new VoidedDocumentsSendEventEntity();
		buildSendEvent(sendEvent, organization, type);
		sendEvent.setVoidedDocuments(VoidedDocumentAdapter.toEntity(voidedDocument, em));
		em.persist(sendEvent);
		em.flush();

		return new SendEventAdapter(session, organization, em, sendEvent);
	}

	private void buildSendEvent(SendEventEntity sendEvent, OrganizationModel organization, SendResultType type) {
		sendEvent.setCreatedTimestamp(LocalDateTime.now());
		sendEvent.setResult(type.equals(SendResultType.SUCCESS));
		sendEvent.setOrganization(OrganizationAdapter.toEntity(organization, em));
	}

}
