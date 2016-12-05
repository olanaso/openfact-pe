package org.openfact.models.jpa.pe.ubl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openfact.models.ModelDuplicateException;
import org.openfact.models.ModelException;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.ScrollModel;
import org.openfact.models.enums.RequiredAction;
import org.openfact.models.jpa.AbstractHibernateStorage;
import org.openfact.models.jpa.JpaScrollAdapter;
import org.openfact.models.jpa.OrganizationAdapter;
import org.openfact.models.jpa.pe.entities.PerceptionEntity;
import org.openfact.models.search.SearchCriteriaFilterOperator;
import org.openfact.models.search.SearchCriteriaModel;
import org.openfact.models.search.SearchResultsModel;
import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.PerceptionProvider;

public class JpaPerceptionProvider extends AbstractHibernateStorage implements PerceptionProvider {

	private OpenfactSession session;
	protected EntityManager em;
	private static final String DOCUMENT_ID = "ID";

	public JpaPerceptionProvider(OpenfactSession session, EntityManager em) {
		this.session = session;
		this.em = em;
	}

	@Override
	public void close() {

	}

	private PerceptionProvider getPerceptions() {
		PerceptionProvider cache = session.getProvider(PerceptionProvider.class);
		if (cache != null) {
			return cache;
		} else {
			return session.getProvider(PerceptionProvider.class);
		}
	}

	@Override
	public PerceptionModel addPerception(OrganizationModel organization, String documentId) {
		if (documentId == null) {
			throw new ModelException("Invalid documentId, Null value");
		}

		if (getPerceptions().getPerceptionByID(organization, documentId) != null) {
			throw new ModelDuplicateException("Perception documentId existed");
		}

		PerceptionEntity perception = new PerceptionEntity();
		perception.setDocumentId(documentId);
		perception.setCreatedTimestamp(LocalDateTime.now());
		perception.setOrganization(OrganizationAdapter.toEntity(organization, em));
		em.persist(perception);
		em.flush();

		final PerceptionModel adapter = new PerceptionAdapter(session, organization, em, perception);
		session.getOpenfactSessionFactory().publish(new PerceptionModel.PerceptionCreationEvent() {
			@Override
			public PerceptionModel getCreatedPerception() {
				return adapter;
			}
		});
		return adapter;
	}

	@Override
	public PerceptionModel getPerceptionById(OrganizationModel organization, String id) {
		TypedQuery<PerceptionEntity> query = em.createNamedQuery("getOrganizationPerceptionById",
				PerceptionEntity.class);
		query.setParameter("id", id);
		query.setParameter("organizationId", organization.getId());
		List<PerceptionEntity> entities = query.getResultList();
		if (entities.size() == 0)
			return null;
		return new PerceptionAdapter(session, organization, em, entities.get(0));
	}

	@Override
	public PerceptionModel getPerceptionByID(OrganizationModel organization, String ID) {
		TypedQuery<PerceptionEntity> query = em.createNamedQuery("getOrganizationPerceptionByDocumentId",
				PerceptionEntity.class);
		query.setParameter("documentId", ID);
		query.setParameter("organizationId", organization.getId());
		List<PerceptionEntity> entities = query.getResultList();
		if (entities.size() == 0)
			return null;
		return new PerceptionAdapter(session, organization, em, entities.get(0));
	}

	@Override
	public boolean removePerception(OrganizationModel organization, String id) {
		return removePerception(organization, getPerceptionById(organization, id));
	}

	@Override
	public boolean removePerception(OrganizationModel organization, PerceptionModel perception) {
		PerceptionEntity perceptionEntity = em.find(PerceptionEntity.class, perception.getId());
		if (perceptionEntity == null)
			return false;
		removePerception(perceptionEntity);
		session.getOpenfactSessionFactory().publish(new PerceptionModel.PerceptionRemovedEvent() {

			@Override
			public PerceptionModel getPerception() {
				return perception;
			}

			@Override
			public OpenfactSession getOpenfactSession() {
				return session;
			}
		});
		return true;
	}

	private void removePerception(PerceptionEntity perception) {
		String id = perception.getId();
		perception = em.find(PerceptionEntity.class, id);
		if (perception != null) {
			em.remove(perception);
		}

		em.flush();
	}

	@Override
	public int getPerceptionsCount(OrganizationModel organization) {
		Query query = em.createNamedQuery("getOrganizationPerceptionCount");
		Long result = (Long) query.getSingleResult();
		return result.intValue();
	}

	@Override
	public List<PerceptionModel> getPerceptions(OrganizationModel organization) {
		return getPerceptions(organization, -1, -1);
	}

	@Override
	public List<PerceptionModel> getPerceptions(OrganizationModel organization, List<RequiredAction> requeridAction,
			boolean intoRequeridAction) {
		String queryName = "";
		if (intoRequeridAction) {
			queryName = "select i from PerceptionEntity i where i.organization.id = :organizationId and :requeridAction in elements(i.requeridAction) order by i.perceptionTypeCode ";
		} else {
			queryName = "select i from PerceptionEntity i where i.organization.id = :organizationId and :requeridAction not in elements(i.requeridAction) order by i.perceptionTypeCode ";

		}
		TypedQuery<PerceptionEntity> query = em.createQuery(queryName, PerceptionEntity.class);
		query.setParameter("organizationId", organization.getId());
		query.setParameter("requeridAction", requeridAction);
		List<PerceptionEntity> results = query.getResultList();
		List<PerceptionModel> perceptions = results.stream()
				.map(f -> new PerceptionAdapter(session, organization, em, f)).collect(Collectors.toList());
		return perceptions;
	}

	@Override
	public List<PerceptionModel> getPerceptions(OrganizationModel organization, Integer firstResult,
			Integer maxResults) {
		String queryName = "getAllPerceptionsByOrganization";

		TypedQuery<PerceptionEntity> query = em.createNamedQuery(queryName, PerceptionEntity.class);
		query.setParameter("organizationId", organization.getId());
		if (firstResult != -1) {
			query.setFirstResult(firstResult);
		}
		if (maxResults != -1) {
			query.setMaxResults(maxResults);
		}
		List<PerceptionEntity> results = query.getResultList();
		List<PerceptionModel> perceptions = results.stream()
				.map(f -> new PerceptionAdapter(session, organization, em, f)).collect(Collectors.toList());
		return perceptions;
	}

	@Override
	public List<PerceptionModel> searchForPerception(OrganizationModel organization, String filterText) {
		return searchForPerception(organization, filterText, -1, -1);
	}

	@Override
	public List<PerceptionModel> searchForPerception(OrganizationModel organization, String filterText,
			Integer firstResult, Integer maxResults) {
		TypedQuery<PerceptionEntity> query = em.createNamedQuery("searchForPerception", PerceptionEntity.class);
		query.setParameter("organizationId", organization.getId());
		query.setParameter("search", "%" + filterText.toLowerCase() + "%");
		if (firstResult != -1) {
			query.setFirstResult(firstResult);
		}
		if (maxResults != -1) {
			query.setMaxResults(maxResults);
		}
		List<PerceptionEntity> results = query.getResultList();
		List<PerceptionModel> perceptions = results.stream()
				.map(f -> new PerceptionAdapter(session, organization, em, f)).collect(Collectors.toList());
		return perceptions;
	}

	@Override
	public SearchResultsModel<PerceptionModel> searchForPerception(OrganizationModel organization,
			SearchCriteriaModel criteria) {
		criteria.addFilter("organization.id", organization.getId(), SearchCriteriaFilterOperator.eq);

		SearchResultsModel<PerceptionEntity> entityResult = find(criteria, PerceptionEntity.class);
		List<PerceptionEntity> entities = entityResult.getModels();

		SearchResultsModel<PerceptionModel> searchResult = new SearchResultsModel<>();
		List<PerceptionModel> models = searchResult.getModels();

		entities.forEach(f -> models.add(new PerceptionAdapter(session, organization, em, f)));
		searchResult.setTotalSize(entityResult.getTotalSize());
		return searchResult;
	}

	@Override
	public SearchResultsModel<PerceptionModel> searchForPerception(OrganizationModel organization,
			SearchCriteriaModel criteria, String filterText) {
		criteria.addFilter("organization.id", organization.getId(), SearchCriteriaFilterOperator.eq);

		SearchResultsModel<PerceptionEntity> entityResult = findFullText(criteria, PerceptionEntity.class, filterText,
				DOCUMENT_ID);
		List<PerceptionEntity> entities = entityResult.getModels();

		SearchResultsModel<PerceptionModel> searchResult = new SearchResultsModel<>();
		List<PerceptionModel> models = searchResult.getModels();

		entities.forEach(f -> models.add(new PerceptionAdapter(session, organization, em, f)));
		searchResult.setTotalSize(entityResult.getTotalSize());
		return searchResult;
	}

	@Override
	public ScrollModel<PerceptionModel> getPerceptionsScroll(OrganizationModel organization) {
		return getPerceptionsScroll(organization, true);
	}

	@Override
	public ScrollModel<PerceptionModel> getPerceptionsScroll(OrganizationModel organization, boolean asc) {
		return getPerceptionsScroll(organization, asc, -1);
	}

	@Override
	public ScrollModel<PerceptionModel> getPerceptionsScroll(OrganizationModel organization, boolean asc,
			int scrollSize) {
		return getPerceptionsScroll(organization, asc, scrollSize, -1);
	}

	@Override
	public ScrollModel<PerceptionModel> getPerceptionsScroll(OrganizationModel organization, boolean asc,
			int scrollSize, int fetchSize) {
		if (scrollSize == -1) {
			scrollSize = 5;
		}
		if (fetchSize == -1) {
			scrollSize = 1;
		}

		Criteria criteria = getSession().createCriteria(PerceptionEntity.class)
				.add(Restrictions.eq("organization.id", organization.getId()))
				.addOrder(asc ? Order.asc("createdTimestamp") : Order.desc("createdTimestamp"));

		JpaScrollAdapter<PerceptionModel, PerceptionEntity> result = new JpaScrollAdapter<>(criteria, scrollSize,
				f -> new PerceptionAdapter(session, organization, em, f));
		return result;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

}
