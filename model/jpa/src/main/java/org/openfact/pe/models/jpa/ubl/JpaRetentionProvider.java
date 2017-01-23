package org.openfact.pe.models.jpa.ubl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.openfact.models.*;
import org.openfact.models.enums.RequiredAction;
import org.openfact.models.jpa.*;
import org.openfact.models.jpa.entities.DebitNoteEntity;
import org.openfact.models.search.SearchCriteriaFilterOperator;
import org.openfact.models.search.SearchCriteriaModel;
import org.openfact.models.search.SearchResultsModel;
import org.openfact.pe.models.RetentionModel;
import org.openfact.pe.models.RetentionProvider;
import org.openfact.pe.models.RetentionModel;
import org.openfact.pe.models.RetentionProvider;
import org.openfact.pe.models.jpa.entities.RetentionEntity;
import org.openfact.pe.models.jpa.entities.RetentionEntity;

public class JpaRetentionProvider extends AbstractHibernateStorage implements RetentionProvider {

	private OpenfactSession session;
	protected EntityManager em;

	protected RetentionProvider cache;

	protected static final String DOCUMENT_ID = "documentId";
	protected static final String ENTITY_NAME = "entityName";

	protected static final String SEND_EVENT_DESTINY_TYPE = "destinyType";
	protected static final String SEND_EVENT_TYPE = "type";
	protected static final String SEND_EVENT_RESULT = "result";

	public JpaRetentionProvider(OpenfactSession session, EntityManager em) {
		this.session = session;
		this.em = em;
	}

	private RetentionProvider retentions() {
		if (cache == null) {
			cache = session.getProvider(RetentionProvider.class);
		}
		return cache;
	}

	@Override
	public void close() {
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public RetentionModel addRetention(OrganizationModel organization, String documentId) {
		if (retentions().getRetentionByDocumentId(organization, documentId) != null) {
			throw new ModelDuplicateException("Retention documentId existed");
		}

		RetentionEntity retention = new RetentionEntity();
		retention.setDocumentId(documentId);
		retention.setCreatedTimestamp(LocalDateTime.now());
		retention.setOrganizationId(organization.getId());
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
		TypedQuery<RetentionEntity> query = em.createNamedQuery("getOrganizationRetentionById", RetentionEntity.class);
		query.setParameter("id", id);
		query.setParameter("organizationId", organization.getId());
		List<RetentionEntity> entities = query.getResultList();
		if (entities.size() == 0)
			return null;
		return new RetentionAdapter(session, organization, em, entities.get(0));
	}

	@Override
	public RetentionModel getRetentionByDocumentId(OrganizationModel organization, String ID) {
		TypedQuery<RetentionEntity> query = em.createNamedQuery("getOrganizationRetentionByDocumentId", RetentionEntity.class);
		query.setParameter("documentId", ID);
		query.setParameter("organizationId", organization.getId());
		List<RetentionEntity> entities = query.getResultList();
		if (entities.size() == 0)
			return null;
		return new RetentionAdapter(session, organization, em, entities.get(0));
	}

	@Override
	public void preRemove(OrganizationModel organization) {

	}

	@Override
	public boolean removeRetention(OrganizationModel organization, String id) {
		return removeRetention(organization, getRetentionById(organization, id));
	}

	@Override
	public boolean removeRetention(OrganizationModel organization, RetentionModel retention) {
		RetentionEntity retentionEntity = em.find(RetentionEntity.class, retention.getId());
		if (retentionEntity == null) return false;
		removeRetention(retentionEntity);
		return true;
	}

	private void removeRetention(RetentionEntity retention) {
		String id = retention.getId();
		em.flush();
		em.clear();

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
	public List<RetentionModel> getRetentions(OrganizationModel organization, List<RequiredAction> requeridAction, boolean intoRequeridAction) {
		String queryName = "";
		if (intoRequeridAction) {
			queryName = "select i from RetentionEntity i where i.organizationId = :organizationId and :requeridAction in elements(i.requeridAction) order by i.retentionTypeCode ";
		} else {
			queryName = "select i from RetentionEntity i where i.organizationId = :organizationId and :requeridAction not in elements(i.requeridAction) order by i.retentionTypeCode ";

		}
		TypedQuery<RetentionEntity> query = em.createQuery(queryName, RetentionEntity.class);
		query.setParameter("organizationId", organization.getId());
		query.setParameter("requeridAction", requeridAction);
		List<RetentionEntity> results = query.getResultList();
		List<RetentionModel> retentions = results.stream().map(f -> new RetentionAdapter(session, organization, em, f)).collect(Collectors.toList());
		return retentions;
	}

	@Override
	public List<RetentionModel> getRetentions(OrganizationModel organization, Integer firstResult, Integer maxResults) {
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
		List<RetentionModel> retentions = results.stream().map(f -> new RetentionAdapter(session, organization, em, f)).collect(Collectors.toList());
		return retentions;
	}

	@Override
	public List<RetentionModel> searchForRetention(OrganizationModel organization, String filterText) {
		return searchForRetention(organization, filterText, -1, -1);
	}

	@Override
	public List<RetentionModel> searchForRetention(OrganizationModel organization, String filterText, Integer firstResult, Integer maxResults) {
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
		List<RetentionModel> retentions = results.stream().map(f -> new RetentionAdapter(session, organization, em, f)).collect(Collectors.toList());
		return retentions;
	}

	@Override
	public SearchResultsModel<RetentionModel> searchForRetention(OrganizationModel organization, SearchCriteriaModel criteria) {
		criteria.addFilter("organizationId", organization.getId(), SearchCriteriaFilterOperator.eq);

		SearchResultsModel<RetentionEntity> entityResult = find(criteria, RetentionEntity.class);
		List<RetentionEntity> entities = entityResult.getModels();

		SearchResultsModel<RetentionModel> searchResult = new SearchResultsModel<>();
		List<RetentionModel> models = searchResult.getModels();

		entities.forEach(f -> models.add(new RetentionAdapter(session, organization, em, f)));
		searchResult.setTotalSize(entityResult.getTotalSize());
		return searchResult;
	}

	@Override
	public SearchResultsModel<RetentionModel> searchForRetention(OrganizationModel organization, SearchCriteriaModel criteria, String filterText) {
		criteria.addFilter("organizationId", organization.getId(), SearchCriteriaFilterOperator.eq);

		SearchResultsModel<RetentionEntity> entityResult = findFullText(criteria, RetentionEntity.class, filterText, DOCUMENT_ID, ENTITY_NAME);
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
	public ScrollModel<RetentionModel> getRetentionsScroll(OrganizationModel organization, boolean asc, int scrollSize) {
		if (scrollSize == -1) {
			scrollSize = 10;
		}
		String queryName = null;
		if (asc) {
			queryName = "getAllRetentionsByOrganization";
		} else {
			queryName = "getAllRetentionsByOrganizationDesc";
		}

		TypedQuery<RetentionEntity> query = em.createNamedQuery(queryName, RetentionEntity.class);
		query.setParameter("organizationId", organization.getId());

		ScrollAdapter<RetentionModel, RetentionEntity> result = new ScrollAdapter<>(RetentionEntity.class, query, f -> {
			return new RetentionAdapter(session, organization, em, f);
		});

		return result;
	}

	@Override
	public ScrollModel<List<RetentionModel>> getRetentionsScroll(OrganizationModel organization, int scrollSize, String... requiredAction) {
		if (scrollSize == -1) {
			scrollSize = 10;
		}

		TypedQuery<RetentionEntity> query = em.createNamedQuery("getAllRetentionsByRequiredActionAndOrganization", RetentionEntity.class);
		query.setParameter("organizationId", organization.getId());
		query.setParameter("requiredAction", new ArrayList<>(Arrays.asList(requiredAction)));

		ScrollModel<List<RetentionModel>> result = new ScrollPagingAdapter<>(RetentionEntity.class, query, f -> {
			return f.stream().map(m -> new RetentionAdapter(session, organization, em, m)).collect(Collectors.toList());
		});
		return result;
	}

	@Override
	public List<RetentionModel> searchForRetention(Map<String, String> params, OrganizationModel organization) {
		return searchForRetention(params, organization, -1, -1);
	}

	@Override
	public List<RetentionModel> searchForRetention(Map<String, String> attributes, OrganizationModel organization, Integer firstResult, Integer maxResults) {
		StringBuilder builder = new StringBuilder("select u from RetentionEntity u where u.organizationId = :organizationId");
		for (Map.Entry<String, String> entry : attributes.entrySet()) {
			String attribute = null;
			String parameterName = null;
			if (entry.getKey().equals(RetentionModel.DOCUMENT_ID)) {
				attribute = "lower(u.documentId)";
				parameterName = JpaRetentionProvider.DOCUMENT_ID;
			} else if (entry.getKey().equals(RetentionModel.ENTITY_NAME)) {
				attribute = "lower(u.entityName)";
				parameterName = JpaRetentionProvider.ENTITY_NAME;
			}
			if (attribute == null) continue;
			builder.append(" and ");
			builder.append(attribute).append(" like :").append(parameterName);
		}
		builder.append(" order by u.createdTimestamp");
		String q = builder.toString();
		TypedQuery<RetentionEntity> query = em.createQuery(q, RetentionEntity.class);
		query.setParameter("organizationId", organization.getId());
		for (Map.Entry<String, String> entry : attributes.entrySet()) {
			String parameterName = null;
			if (entry.getKey().equals(RetentionModel.DOCUMENT_ID)) {
				parameterName = JpaRetentionProvider.DOCUMENT_ID;
			} else if (entry.getKey().equals(RetentionModel.ENTITY_NAME)) {
				parameterName = JpaRetentionProvider.ENTITY_NAME;
			}
			if (parameterName == null) continue;
			query.setParameter(parameterName, "%" + entry.getValue().toLowerCase() + "%");
		}
		if (firstResult != -1) {
			query.setFirstResult(firstResult);
		}
		if (maxResults != -1) {
			query.setMaxResults(maxResults);
		}
		List<RetentionEntity> results = query.getResultList();
		List<RetentionModel> retentions = results.stream().map(f -> new RetentionAdapter(session, organization, em, f)).collect(Collectors.toList());
		return retentions;
	}

}
