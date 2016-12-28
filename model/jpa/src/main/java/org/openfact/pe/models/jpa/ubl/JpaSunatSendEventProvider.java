package org.openfact.pe.models.jpa.ubl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.jboss.logging.Logger;
import org.openfact.models.CreditNoteModel;
import org.openfact.models.DebitNoteModel;
import org.openfact.models.InvoiceModel;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.enums.SendResultType;
import org.openfact.models.jpa.*;
import org.openfact.models.jpa.entities.CreditNoteSendEventEntity;
import org.openfact.models.jpa.entities.DebitNoteSendEventEntity;
import org.openfact.models.jpa.entities.InvoiceSendEventEntity;
import org.openfact.models.jpa.entities.SendEventEntity;
import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.RetentionModel;
import org.openfact.pe.models.SummaryDocumentModel;
import org.openfact.pe.models.SunatSendEventProvider;
import org.openfact.pe.models.VoidedDocumentModel;
import org.openfact.pe.models.jpa.entities.*;
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

//	@Override
//	public SearchResultsModel<SendEventModel> searchForSendEvent(OrganizationModel organization,
//			SearchCriteriaModel criteria) {
//		criteria.addFilter("organization.id", organization.getId(), SearchCriteriaFilterOperator.eq);
//
//		SearchResultsModel<SendEventEntity> entityResult = find(criteria, SendEventEntity.class);
//		List<SendEventEntity> entities = entityResult.getModels();
//
//		SearchResultsModel<SendEventModel> searchResult = new SearchResultsModel<>();
//		List<SendEventModel> models = searchResult.getModels();
//
//		entities.forEach(f -> models.add(new SunatSendEventAdapter(session, organization, em, f)));
//		searchResult.setTotalSize(entityResult.getTotalSize());
//		return searchResult;
//	}

//	@Override
//	public ScrollModel<SendEventModel> getSendEventsScroll(OrganizationModel organization) {
//		return getSendEventsScroll(organization, true);
//	}

//	@Override
//	public ScrollModel<SendEventModel> getSendEventsScroll(OrganizationModel organization, boolean asc) {
//		return getSendEventsScroll(organization, asc, -1);
//	}

//	@Override
//	public ScrollModel<SendEventModel> getSendEventsScroll(OrganizationModel organization, boolean asc,
//			int scrollSize) {
//		if (scrollSize == -1) {
//			scrollSize = 10;
//		}
//
//		TypedQuery<String> query = em.createNamedQuery("getAllSendEventsByOrganization", String.class);
//		query.setParameter("organizationId", organization.getId());
//
//		ScrollAdapter<SendEventModel, String> result = new ScrollAdapter<>(String.class, query, f -> {
//			SendEventEntity entity = em.find(SendEventEntity.class, f);
//			return new SunatSendEventAdapter(session, organization, em, entity);
//		});
//
//		// Iterator<SendEventModel> iterator = result.iterator();
//		// while (iterator.hasNext()) {
//		// SendEventModel perceptionModel = iterator.next();
//		// System.out.println("-------------------");
//		// System.out.println(perceptionModel.getRequiredActions());
//		// }
//
//		return result;
//	}

	@Override
	public SendEventModel addSendEvent(OrganizationModel organization, SendResultType type,
			PerceptionModel perception) {
		PerceptionSendEventEntity sendEvent = new PerceptionSendEventEntity();
		buildSendEvent(sendEvent, organization, type);
		sendEvent.setPerception(PerceptionAdapter.toEntity(perception, em));
		em.persist(sendEvent);
		em.flush();
		return new SunatSendEventAdapter(session, organization, em, sendEvent);
	}

	@Override
	public SendEventModel addSendEvent(OrganizationModel organization, SendResultType type, RetentionModel retention) {
		RetentionSendEventEntity sendEvent = new RetentionSendEventEntity();
		buildSendEvent(sendEvent, organization, type);
		sendEvent.setRetention(RetentionAdapter.toEntity(retention, em));
		em.persist(sendEvent);
		em.flush();

		return new SunatSendEventAdapter(session, organization, em, sendEvent);
	}

	@Override
	public SendEventModel addSendEvent(OrganizationModel organization, SendResultType type,
			SummaryDocumentModel summaryDocument) {
		SummaryDocumentsSendEventEntity sendEvent = new SummaryDocumentsSendEventEntity();
		buildSendEvent(sendEvent, organization, type);
		sendEvent.setSummaryDocuments(SummaryDocumentAdapter.toEntity(summaryDocument, em));
		em.persist(sendEvent);
		em.flush();

		return new SunatSendEventAdapter(session, organization, em, sendEvent);
	}

	@Override
	public SendEventModel addSendEvent(OrganizationModel organization, SendResultType type,
			VoidedDocumentModel voidedDocument) {
		VoidedDocumentsSendEventEntity sendEvent = new VoidedDocumentsSendEventEntity();
		buildSendEvent(sendEvent, organization, type);
		sendEvent.setVoidedDocuments(VoidedDocumentAdapter.toEntity(voidedDocument, em));
		em.persist(sendEvent);
		em.flush();
		return new SunatSendEventAdapter(session, organization, em, sendEvent);
	}

	private void buildSendEvent(SunatSendEventEntity sendEvent, OrganizationModel organization, SendResultType type) {
		sendEvent.setCreatedTimestamp(LocalDateTime.now());
		sendEvent.setResult(type.equals(SendResultType.SUCCESS));
		sendEvent.setOrganizationId(OrganizationAdapter.toEntity(organization, em).getId());
	}
	private void buildSendEvent(SendEventEntity sendEvent, OrganizationModel organization, SendResultType type) {
		sendEvent.setCreatedTimestamp(LocalDateTime.now());
		sendEvent.setResult(type.equals(SendResultType.SUCCESS));
		sendEvent.setOrganization(OrganizationAdapter.toEntity(organization, em));
	}

	@Override
	public SendEventModel getSunatSendEventById(OrganizationModel organization, String id) {
		TypedQuery<SunatSendEventEntity> query = em.createNamedQuery("getOrganizationSunatSendEventById", SunatSendEventEntity.class);
		query.setParameter("id", id);
		query.setParameter("organizationId", organization.getId());
		List<SunatSendEventEntity> entities = query.getResultList();
		if (entities.size() == 0)
			return null;
		return new SunatSendEventAdapter(session, organization, em, entities.get(0));
	}

	@Override
	public boolean removeSunatSendEvent(OrganizationModel organization, String id) {
		return removeSunatSendEvent(organization, getSendEventById(organization, id));	}

	@Override
	public boolean removeSunatSendEvent(OrganizationModel organization, SendEventModel sendEvent) {
		SunatSendEventEntity sendEventEntity = em.find(SunatSendEventEntity.class, sendEvent.getId());
		if (sendEventEntity == null)
			return false;

		em.remove(sendEventEntity);
		em.flush();
		return true;
	}

	@Override
	public List<SendEventModel> getSunatSendEvents(OrganizationModel organization) {
		return getSunatSendEvents(organization, -1, -1);
	}

	@Override
	public List<SendEventModel> getSunatSendEvents(OrganizationModel organization, Integer firstResult, Integer maxResults) {
		String queryName = "getAllSunatSendEventsByOrganization";

		TypedQuery<SunatSendEventEntity> query = em.createNamedQuery(queryName, SunatSendEventEntity.class);
		query.setParameter("organizationId", organization.getId());
		if (firstResult != -1) {
			query.setFirstResult(firstResult);
		}
		if (maxResults != -1) {
			query.setMaxResults(maxResults);
		}
		List<SunatSendEventEntity> results = query.getResultList();
		List<SendEventModel> invoices = results.stream().map(f -> new SunatSendEventAdapter(session, organization, em, f))
				.collect(Collectors.toList());
		return invoices;
	}
}
