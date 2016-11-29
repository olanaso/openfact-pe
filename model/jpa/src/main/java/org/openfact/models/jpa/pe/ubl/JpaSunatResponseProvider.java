package org.openfact.models.jpa.pe.ubl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openfact.models.CreditNoteSendEventModel;
import org.openfact.models.DebitNoteSendEventModel;
import org.openfact.models.InvoiceSendEventModel;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.ScrollModel;
import org.openfact.models.enums.SendResultType;
import org.openfact.models.jpa.AbstractHibernateStorage;
import org.openfact.models.jpa.CreditNoteSendEventAdapter;
import org.openfact.models.jpa.DebitNoteSendEventAdapter;
import org.openfact.models.jpa.InvoiceSendEventAdapter;
import org.openfact.models.jpa.JpaScrollAdapter;
import org.openfact.models.jpa.pe.entities.SunatResponseEntity;
import org.openfact.models.search.SearchCriteriaFilterOperator;
import org.openfact.models.search.SearchCriteriaModel;
import org.openfact.models.search.SearchResultsModel;
import org.openfact.pe.models.PerceptionSendEventModel;
import org.openfact.pe.models.RetentionSendEventModel;
import org.openfact.pe.models.SummaryDocumentsSendEventModel;
import org.openfact.pe.models.SunatResponseModel;
import org.openfact.pe.models.SunatResponseProvider;
import org.openfact.pe.models.VoidedDocumentsSendEventModel;

public class JpaSunatResponseProvider extends AbstractHibernateStorage implements SunatResponseProvider {

	private OpenfactSession session;
	protected EntityManager em;

	public JpaSunatResponseProvider(OpenfactSession session, EntityManager em) {
		this.session = session;
		this.em = em;
	}

	@Override
	public void close() {
	}

	@Override
	public SunatResponseModel addSunatResponse(OrganizationModel organization, SendResultType type,
			InvoiceSendEventModel invoiceSendEvent) {
		SunatResponseEntity response = new SunatResponseEntity();
		buildResponse(response, organization, type);
		response.setInvoiceSendEvent(InvoiceSendEventAdapter.toEntity(invoiceSendEvent, em));
		em.persist(response);
		em.flush();
		return new SunatResponseAdapter(session, organization, em, response);
	}

	@Override
	public SunatResponseModel addSunatResponse(OrganizationModel organization, SendResultType type,
			CreditNoteSendEventModel creditNoteSendEvent) {
		SunatResponseEntity response = new SunatResponseEntity();
		buildResponse(response, organization, type);
		response.setCreditNoteSendEvent(CreditNoteSendEventAdapter.toEntity(creditNoteSendEvent, em));
		em.persist(response);
		em.flush();
		return new SunatResponseAdapter(session, organization, em, response);
	}

	@Override
	public SunatResponseModel addSunatResponse(OrganizationModel organization, SendResultType type,
			DebitNoteSendEventModel debitNoteSendEvent) {
		SunatResponseEntity response = new SunatResponseEntity();
		buildResponse(response, organization, type);
		response.setDebitNoteSendEvent(DebitNoteSendEventAdapter.toEntity(debitNoteSendEvent, em));
		em.persist(response);
		em.flush();
		return new SunatResponseAdapter(session, organization, em, response);
	}

	@Override
	public SunatResponseModel addSunatResponse(OrganizationModel organization, SendResultType type,
			PerceptionSendEventModel perceptionSendEvent) {
		SunatResponseEntity response = new SunatResponseEntity();
		buildResponse(response, organization, type);
		response.setPerceptionSendEvent(PerceptionSendEventAdapter.toEntity(perceptionSendEvent, em));
		em.persist(response);
		em.flush();
		return new SunatResponseAdapter(session, organization, em, response);
	}

	@Override
	public SunatResponseModel addSunatResponse(OrganizationModel organization, SendResultType type,
			RetentionSendEventModel retentionSendEvent) {
		SunatResponseEntity response = new SunatResponseEntity();
		buildResponse(response, organization, type);
		response.setRetentionSendEvent(RetentionSendEventAdapter.toEntity(retentionSendEvent, em));
		em.persist(response);
		em.flush();
		return new SunatResponseAdapter(session, organization, em, response);
	}

	@Override
	public SunatResponseModel addSunatResponse(OrganizationModel organization, SendResultType type,
			SummaryDocumentsSendEventModel summaryDocumentsSendEvent) {
		SunatResponseEntity response = new SunatResponseEntity();
		buildResponse(response, organization, type);
		response.setSummaryDocumentsSendEvent(SummaryDocumentSendEventAdapter.toEntity(summaryDocumentsSendEvent, em));
		em.persist(response);
		em.flush();
		return new SunatResponseAdapter(session, organization, em, response);
	}

	@Override
	public SunatResponseModel addSunatResponse(OrganizationModel organization, SendResultType type,
			VoidedDocumentsSendEventModel voidedDocumentsSendEvent) {
		SunatResponseEntity response = new SunatResponseEntity();
		buildResponse(response, organization, type);
		response.setVoidedDocumentsSendEvent(VoidedDocumentSendEventAdapter.toEntity(voidedDocumentsSendEvent, em));
		em.persist(response);
		em.flush();
		return new SunatResponseAdapter(session, organization, em, response);
	}

	private void buildResponse(SunatResponseEntity response, OrganizationModel organization, SendResultType type) {
		response.setCreatedTimestamp(LocalDateTime.now());
		response.setResult(type.equals(SendResultType.SUCCESS));
		// response.setOrganization(OrganizationAdapter.toEntity(organization,
		// em));
	}

	@Override
	public SunatResponseModel getSunatResponseById(OrganizationModel organization, String id) {
		TypedQuery<SunatResponseEntity> query = em.createNamedQuery("getOrganizationSunatResponseById",
				SunatResponseEntity.class);
		query.setParameter("id", id);
		query.setParameter("organizationId", organization.getId());
		List<SunatResponseEntity> entities = query.getResultList();
		if (entities.size() == 0)
			return null;
		return new SunatResponseAdapter(session, organization, em, entities.get(0));
	}

	@Override
	public boolean removeSunatResponse(OrganizationModel organization, String id) {
		return removeSunatResponse(organization, getSunatResponseById(organization, id));
	}

	@Override
	public boolean removeSunatResponse(OrganizationModel organization, SunatResponseModel sunatResponse) {
		SunatResponseEntity sunatResponseEntity = em.find(SunatResponseEntity.class, sunatResponse.getId());
		if (sunatResponseEntity == null)
			return false;

		em.remove(sunatResponseEntity);
		em.flush();
		return true;
	}

	@Override
	public int getSunatResponsesCount(OrganizationModel organization) {
		return 0;
	}

	@Override
	public List<SunatResponseModel> getSunatResponses(OrganizationModel organization) {
		return getSunatResponse(organization, -1, -1);
	}

	@Override
	public List<SunatResponseModel> getSunatResponse(OrganizationModel organization, Integer firstResult,
			Integer maxResults) {
		String queryName = "getAllSendEventsByOrganization";

		TypedQuery<SunatResponseEntity> query = em.createNamedQuery(queryName, SunatResponseEntity.class);
		query.setParameter("organizationId", organization.getId());
		if (firstResult != -1) {
			query.setFirstResult(firstResult);
		}
		if (maxResults != -1) {
			query.setMaxResults(maxResults);
		}
		List<SunatResponseEntity> results = query.getResultList();
		List<SunatResponseModel> invoices = results.stream()
				.map(f -> new SunatResponseAdapter(session, organization, em, f)).collect(Collectors.toList());
		return invoices;
	}

	@Override
	public SearchResultsModel<SunatResponseModel> searchForSunatResponse(OrganizationModel organization,
			SearchCriteriaModel criteria) {
		criteria.addFilter("organization.id", organization.getId(), SearchCriteriaFilterOperator.eq);

		SearchResultsModel<SunatResponseEntity> entityResult = find(criteria, SunatResponseEntity.class);
		List<SunatResponseEntity> entities = entityResult.getModels();

		SearchResultsModel<SunatResponseModel> searchResult = new SearchResultsModel<>();
		List<SunatResponseModel> models = searchResult.getModels();

		entities.forEach(f -> models.add(new SunatResponseAdapter(session, organization, em, f)));
		searchResult.setTotalSize(entityResult.getTotalSize());
		return searchResult;
	}

	@Override
	public ScrollModel<SunatResponseModel> getSunatResponsesScroll(OrganizationModel organization) {
		return getSunatResponsesScroll(organization, true);
	}

	@Override
	public ScrollModel<SunatResponseModel> getSunatResponsesScroll(OrganizationModel organization, boolean asc) {
		return getSunatResponsesScroll(organization, asc, -1);
	}

	@Override
	public ScrollModel<SunatResponseModel> getSunatResponsesScroll(OrganizationModel organization, boolean asc,
			int scrollSize) {
		return getSunatResponsesScroll(organization, asc, scrollSize, -1);
	}

	@Override
	public ScrollModel<SunatResponseModel> getSunatResponsesScroll(OrganizationModel organization, boolean asc,
			int scrollSize, int fetchSize) {
		if (scrollSize == -1) {
			scrollSize = 5;
		}
		if (fetchSize == -1) {
			scrollSize = 1;
		}

		Criteria criteria = getSession().createCriteria(SunatResponseEntity.class)
				.add(Restrictions.eq("organization.id", organization.getId()))
				.addOrder(asc ? Order.asc("createdTimestamp") : Order.desc("createdTimestamp"));

		JpaScrollAdapter<SunatResponseModel, SunatResponseEntity> result = new JpaScrollAdapter<>(criteria, scrollSize,
				f -> new SunatResponseAdapter(session, organization, em, f));
		return result;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

}
