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
import org.openfact.models.jpa.pe.entities.RetentionEntity;
import org.openfact.models.search.SearchCriteriaFilterOperator;
import org.openfact.models.search.SearchCriteriaModel;
import org.openfact.models.search.SearchResultsModel;
import org.openfact.pe.models.RetentionModel;
import org.openfact.pe.models.RetentionProvider;

public class JpaRetentionProvider extends AbstractHibernateStorage implements  RetentionProvider{

	private OpenfactSession session;
	protected EntityManager em;
	private static final String DOCUMENT_ID = "ID";

	public JpaRetentionProvider(OpenfactSession session, EntityManager em) {
		this.session = session;
		this.em = em;
	}
	@Override
	public void close() {

	}

	private RetentionProvider getRetentions() {
		RetentionProvider cache = session.getProvider(RetentionProvider.class);
		if (cache != null) {
			return cache;
		} else {
			return session.getProvider(RetentionProvider.class);
		}
	}

	@Override
	public RetentionModel addRetention(OrganizationModel organization, String documentId) {
		if (documentId == null) {
			throw new ModelException("Invalid documentId, Null value");
		}

		if (getRetentions().getRetentionByID(organization, documentId) != null) {
			throw new ModelDuplicateException("Retention documentId existed");
		}

		RetentionEntity retention = new RetentionEntity();
		retention.setDocumentId(documentId);
		retention.setCreatedTimestamp(LocalDateTime.now());
		retention.setOrganization(OrganizationAdapter.toEntity(organization, em));
		em.persist(retention);
		em.flush();

		final RetentionModel adapter = new RetentionAdapter(session, organization, em, retention);
		session.getOpenfactSessionFactory().publish(new RetentionModel.RetentionCreationEvent() {
			@Override
			public RetentionModel getCreatedRetention() {
				return adapter;
			}
		});
		return adapter;
	}

	@Override
	public RetentionModel getRetentionById(OrganizationModel organization, String id) {
		TypedQuery<RetentionEntity> query = em.createNamedQuery("getOrganizationRetentionById",
				RetentionEntity.class);
		query.setParameter("id", id);
		query.setParameter("organizationId", organization.getId());
		List<RetentionEntity> entities = query.getResultList();
		if (entities.size() == 0)
			return null;
		return new RetentionAdapter(session, organization, em, entities.get(0));
	}

	@Override
	public RetentionModel getRetentionByID(OrganizationModel organization, String ID) {
		TypedQuery<RetentionEntity> query = em.createNamedQuery("getOrganizationRetentionByDocumentId",
				RetentionEntity.class);
		query.setParameter("documentId", ID);
		query.setParameter("organizationId", organization.getId());
		List<RetentionEntity> entities = query.getResultList();
		if (entities.size() == 0)
			return null;
		return new RetentionAdapter(session, organization, em, entities.get(0));
	}

	@Override
	public boolean removeRetention(OrganizationModel organization, String id) {
		return removeRetention(organization, getRetentionById(organization, id));
	}

	@Override
	public boolean removeRetention(OrganizationModel organization, RetentionModel retention) {
		RetentionEntity retentionEntity = em.find(RetentionEntity.class, retention.getId());
		if (retentionEntity == null)
			return false;
		removeRetention(retentionEntity);
		session.getOpenfactSessionFactory().publish(new RetentionModel.RetentionRemovedEvent() {

			@Override
			public RetentionModel getRetention() {
				return retention;
			}

			@Override
			public OpenfactSession getOpenfactSession() {
				return session;
			}
		});
		return true;
	}

	private void removeRetention(RetentionEntity retention) {
		String id = retention.getId();
		retention = em.find(RetentionEntity.class, id);
		if (retention != null) {
			em.remove(retention);
		}

		em.flush();
	}

	@Override
	public int getRetentionsCount(OrganizationModel organization) {
		Query query = em.createNamedQuery("getOrganizationRetentionCount");
		Long result = (Long) query.getSingleResult();
		return result.intValue();
	}

	@Override
	public List<RetentionModel> getRetentions(OrganizationModel organization) {
		return getRetentions(organization, -1, -1);
	}

	@Override
	public List<RetentionModel> getRetentions(OrganizationModel organization, List<RequiredAction> requeridAction,
			boolean intoRequeridAction) {
		String queryName = "";
		if (intoRequeridAction) {
			queryName = "select i from RetentionEntity i where i.organization.id = :organizationId and :requeridAction in elements(i.requeridAction) order by i.retentionTypeCode ";
		} else {
			queryName = "select i from RetentionEntity i where i.organization.id = :organizationId and :requeridAction not in elements(i.requeridAction) order by i.retentionTypeCode ";

		}
		TypedQuery<RetentionEntity> query = em.createQuery(queryName, RetentionEntity.class);
		query.setParameter("organizationId", organization.getId());
		query.setParameter("requeridAction", requeridAction);
		List<RetentionEntity> results = query.getResultList();
		List<RetentionModel> retentions = results.stream()
				.map(f -> new RetentionAdapter(session, organization, em, f)).collect(Collectors.toList());
		return retentions;
	}

	@Override
	public List<RetentionModel> getRetentions(OrganizationModel organization, Integer firstResult,
			Integer maxResults) {
		String queryName = "getAllRetentionsByOrganization";

		TypedQuery<RetentionEntity> query = em.createNamedQuery(queryName, RetentionEntity.class);
		query.setParameter("organizationId", organization.getId());
		if (firstResult != -1) {
			query.setFirstResult(firstResult);
		}
		if (maxResults != -1) {
			query.setMaxResults(maxResults);
		}
		List<RetentionEntity> results = query.getResultList();
		List<RetentionModel> retentions = results.stream()
				.map(f -> new RetentionAdapter(session, organization, em, f)).collect(Collectors.toList());
		return retentions;
	}

	@Override
	public List<RetentionModel> searchForRetention(OrganizationModel organization, String filterText) {
		return searchForRetention(organization, filterText, -1, -1);
	}

	@Override
	public List<RetentionModel> searchForRetention(OrganizationModel organization, String filterText,
			Integer firstResult, Integer maxResults) {
		TypedQuery<RetentionEntity> query = em.createNamedQuery("searchForRetention", RetentionEntity.class);
		query.setParameter("organizationId", organization.getId());
		query.setParameter("search", "%" + filterText.toLowerCase() + "%");
		if (firstResult != -1) {
			query.setFirstResult(firstResult);
		}
		if (maxResults != -1) {
			query.setMaxResults(maxResults);
		}
		List<RetentionEntity> results = query.getResultList();
		List<RetentionModel> retentions = results.stream()
				.map(f -> new RetentionAdapter(session, organization, em, f)).collect(Collectors.toList());
		return retentions;
	}

	@Override
	public SearchResultsModel<RetentionModel> searchForRetention(OrganizationModel organization,
			SearchCriteriaModel criteria) {
		criteria.addFilter("organization.id", organization.getId(), SearchCriteriaFilterOperator.eq);

		SearchResultsModel<RetentionEntity> entityResult = find(criteria, RetentionEntity.class);
		List<RetentionEntity> entities = entityResult.getModels();

		SearchResultsModel<RetentionModel> searchResult = new SearchResultsModel<>();
		List<RetentionModel> models = searchResult.getModels();

		entities.forEach(f -> models.add(new RetentionAdapter(session, organization, em, f)));
		searchResult.setTotalSize(entityResult.getTotalSize());
		return searchResult;
	}

	@Override
	public SearchResultsModel<RetentionModel> searchForRetention(OrganizationModel organization,
			SearchCriteriaModel criteria, String filterText) {
		criteria.addFilter("organization.id", organization.getId(), SearchCriteriaFilterOperator.eq);

		SearchResultsModel<RetentionEntity> entityResult = findFullText(criteria, RetentionEntity.class, filterText,
				DOCUMENT_ID);
		List<RetentionEntity> entities = entityResult.getModels();

		SearchResultsModel<RetentionModel> searchResult = new SearchResultsModel<>();
		List<RetentionModel> models = searchResult.getModels();

		entities.forEach(f -> models.add(new RetentionAdapter(session, organization, em, f)));
		searchResult.setTotalSize(entityResult.getTotalSize());
		return searchResult;
	}

	@Override
	public ScrollModel<RetentionModel> getRetentionsScroll(OrganizationModel organization) {
		return getRetentionsScroll(organization, true);
	}

	@Override
	public ScrollModel<RetentionModel> getRetentionsScroll(OrganizationModel organization, boolean asc) {
		return getRetentionsScroll(organization, asc, -1);
	}

	@Override
	public ScrollModel<RetentionModel> getRetentionsScroll(OrganizationModel organization, boolean asc,
			int scrollSize) {
		return getRetentionsScroll(organization, asc, scrollSize, -1);
	}

	@Override
	public ScrollModel<RetentionModel> getRetentionsScroll(OrganizationModel organization, boolean asc,
			int scrollSize, int fetchSize) {
		if (scrollSize == -1) {
			scrollSize = 5;
		}
		if (fetchSize == -1) {
			scrollSize = 1;
		}

		Criteria criteria = getSession().createCriteria(RetentionEntity.class)
				.add(Restrictions.eq("organization.id", organization.getId()))
				.addOrder(asc ? Order.asc("createdTimestamp") : Order.desc("createdTimestamp"));

		JpaScrollAdapter<RetentionModel, RetentionEntity> result = new JpaScrollAdapter<>(criteria, scrollSize,
				f -> new RetentionAdapter(session, organization, em, f));
		return result;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

}
