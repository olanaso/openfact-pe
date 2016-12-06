package org.openfact.pe.models.jpa.ubl;

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
import org.openfact.models.search.SearchCriteriaFilterOperator;
import org.openfact.models.search.SearchCriteriaModel;
import org.openfact.models.search.SearchResultsModel;
import org.openfact.pe.models.VoidedDocumentModel;
import org.openfact.pe.models.VoidedDocumentProvider;
import org.openfact.pe.models.jpa.entities.VoidedDocumentsEntity;

public class JpaVoidedDocumentsProvider extends AbstractHibernateStorage implements VoidedDocumentProvider {

	private OpenfactSession session;
	protected EntityManager em;
	private static final String DOCUMENT_ID = "ID";

	public JpaVoidedDocumentsProvider(OpenfactSession session, EntityManager em) {
		this.session = session;
		this.em = em;
	}

	@Override
	public void close() {

	}

	private VoidedDocumentProvider getVoidedDocuments() {
		VoidedDocumentProvider cache = session.getProvider(VoidedDocumentProvider.class);
		if (cache != null) {
			return cache;
		} else {
			return session.getProvider(VoidedDocumentProvider.class);
		}
	}

	@Override
	public VoidedDocumentModel addVoidedDocument(OrganizationModel organization, String documentId) {
		if (documentId == null) {
			throw new ModelException("Invalid documentId, Null value");
		}

		if (getVoidedDocuments().getVoidedDocumentByID(organization, documentId) != null) {
			throw new ModelDuplicateException("VoidedDocument documentId existed");
		}

		VoidedDocumentsEntity retention = new VoidedDocumentsEntity();
		retention.setDocumentId(documentId);
		retention.setCreatedTimestamp(LocalDateTime.now());
		retention.setOrganization(OrganizationAdapter.toEntity(organization, em));
		em.persist(retention);
		em.flush();

		final VoidedDocumentModel adapter = new VoidedDocumentAdapter(session, organization, em, retention);
		session.getOpenfactSessionFactory().publish(new VoidedDocumentModel.VoidedDocumentCreationEvent() {
			@Override
			public VoidedDocumentModel getCreatedVoidedDocument() {
				return adapter;
			}
		});
		return adapter;
	}

	@Override
	public VoidedDocumentModel getVoidedDocumentById(OrganizationModel organization, String id) {
		TypedQuery<VoidedDocumentsEntity> query = em.createNamedQuery("getOrganizationVoidedDocumentById",
				VoidedDocumentsEntity.class);
		query.setParameter("id", id);
		query.setParameter("organizationId", organization.getId());
		List<VoidedDocumentsEntity> entities = query.getResultList();
		if (entities.size() == 0)
			return null;
		return new VoidedDocumentAdapter(session, organization, em, entities.get(0));
	}

	@Override
	public VoidedDocumentModel getVoidedDocumentByID(OrganizationModel organization, String ID) {
		TypedQuery<VoidedDocumentsEntity> query = em.createNamedQuery("getOrganizationVoidedDocumentByDocumentId",
				VoidedDocumentsEntity.class);
		query.setParameter("documentId", ID);
		query.setParameter("organizationId", organization.getId());
		List<VoidedDocumentsEntity> entities = query.getResultList();
		if (entities.size() == 0)
			return null;
		return new VoidedDocumentAdapter(session, organization, em, entities.get(0));
	}

	@Override
	public boolean removeVoidedDocument(OrganizationModel organization, String id) {
		return removeVoidedDocument(organization, getVoidedDocumentById(organization, id));
	}

	@Override
	public boolean removeVoidedDocument(OrganizationModel organization, VoidedDocumentModel retention) {
		VoidedDocumentsEntity retentionEntity = em.find(VoidedDocumentsEntity.class, retention.getId());
		if (retentionEntity == null)
			return false;
		removeVoidedDocument(retentionEntity);
		session.getOpenfactSessionFactory().publish(new VoidedDocumentModel.VoidedDocumentRemovedEvent() {

			@Override
			public VoidedDocumentModel getVoidedDocument() {
				return retention;
			}

			@Override
			public OpenfactSession getOpenfactSession() {
				return session;
			}
		});
		return true;
	}

	private void removeVoidedDocument(VoidedDocumentsEntity retention) {
		String id = retention.getId();
		retention = em.find(VoidedDocumentsEntity.class, id);
		if (retention != null) {
			em.remove(retention);
		}

		em.flush();
	}

	@Override
	public int getVoidedDocumentsCount(OrganizationModel organization) {
		Query query = em.createNamedQuery("getOrganizationVoidedDocumentCount");
		Long result = (Long) query.getSingleResult();
		return result.intValue();
	}

	@Override
	public List<VoidedDocumentModel> getVoidedDocuments(OrganizationModel organization) {
		return getVoidedDocuments(organization, -1, -1);
	}

	@Override
	public List<VoidedDocumentModel> getVoidedDocuments(OrganizationModel organization,
			List<RequiredAction> requeridAction, boolean intoRequeridAction) {
		String queryName = "";
		if (intoRequeridAction) {
			queryName = "select i from VoidedDocumentsEntity i where i.organization.id = :organizationId and :requeridAction in elements(i.requeridAction) order by i.retentionTypeCode ";
		} else {
			queryName = "select i from VoidedDocumentsEntity i where i.organization.id = :organizationId and :requeridAction not in elements(i.requeridAction) order by i.retentionTypeCode ";

		}
		TypedQuery<VoidedDocumentsEntity> query = em.createQuery(queryName, VoidedDocumentsEntity.class);
		query.setParameter("organizationId", organization.getId());
		query.setParameter("requeridAction", requeridAction);
		List<VoidedDocumentsEntity> results = query.getResultList();
		List<VoidedDocumentModel> retentions = results.stream()
				.map(f -> new VoidedDocumentAdapter(session, organization, em, f)).collect(Collectors.toList());
		return retentions;
	}

	@Override
	public List<VoidedDocumentModel> getVoidedDocuments(OrganizationModel organization, Integer firstResult,
			Integer maxResults) {
		String queryName = "getAllVoidedDocumentsByOrganization";

		TypedQuery<VoidedDocumentsEntity> query = em.createNamedQuery(queryName, VoidedDocumentsEntity.class);
		query.setParameter("organizationId", organization.getId());
		if (firstResult != -1) {
			query.setFirstResult(firstResult);
		}
		if (maxResults != -1) {
			query.setMaxResults(maxResults);
		}
		List<VoidedDocumentsEntity> results = query.getResultList();
		List<VoidedDocumentModel> retentions = results.stream()
				.map(f -> new VoidedDocumentAdapter(session, organization, em, f)).collect(Collectors.toList());
		return retentions;
	}

	@Override
	public List<VoidedDocumentModel> searchForVoidedDocument(OrganizationModel organization, String filterText) {
		return searchForVoidedDocument(organization, filterText, -1, -1);
	}

	@Override
	public List<VoidedDocumentModel> searchForVoidedDocument(OrganizationModel organization, String filterText,
			Integer firstResult, Integer maxResults) {
		TypedQuery<VoidedDocumentsEntity> query = em.createNamedQuery("searchForVoidedDocument",
				VoidedDocumentsEntity.class);
		query.setParameter("organizationId", organization.getId());
		query.setParameter("search", "%" + filterText.toLowerCase() + "%");
		if (firstResult != -1) {
			query.setFirstResult(firstResult);
		}
		if (maxResults != -1) {
			query.setMaxResults(maxResults);
		}
		List<VoidedDocumentsEntity> results = query.getResultList();
		List<VoidedDocumentModel> retentions = results.stream()
				.map(f -> new VoidedDocumentAdapter(session, organization, em, f)).collect(Collectors.toList());
		return retentions;
	}

	@Override
	public SearchResultsModel<VoidedDocumentModel> searchForVoidedDocument(OrganizationModel organization,
			SearchCriteriaModel criteria) {
		criteria.addFilter("organization.id", organization.getId(), SearchCriteriaFilterOperator.eq);

		SearchResultsModel<VoidedDocumentsEntity> entityResult = find(criteria, VoidedDocumentsEntity.class);
		List<VoidedDocumentsEntity> entities = entityResult.getModels();

		SearchResultsModel<VoidedDocumentModel> searchResult = new SearchResultsModel<>();
		List<VoidedDocumentModel> models = searchResult.getModels();

		entities.forEach(f -> models.add(new VoidedDocumentAdapter(session, organization, em, f)));
		searchResult.setTotalSize(entityResult.getTotalSize());
		return searchResult;
	}

	@Override
	public SearchResultsModel<VoidedDocumentModel> searchForVoidedDocument(OrganizationModel organization,
			SearchCriteriaModel criteria, String filterText) {
		criteria.addFilter("organization.id", organization.getId(), SearchCriteriaFilterOperator.eq);

		SearchResultsModel<VoidedDocumentsEntity> entityResult = findFullText(criteria, VoidedDocumentsEntity.class,
				filterText, DOCUMENT_ID);
		List<VoidedDocumentsEntity> entities = entityResult.getModels();

		SearchResultsModel<VoidedDocumentModel> searchResult = new SearchResultsModel<>();
		List<VoidedDocumentModel> models = searchResult.getModels();

		entities.forEach(f -> models.add(new VoidedDocumentAdapter(session, organization, em, f)));
		searchResult.setTotalSize(entityResult.getTotalSize());
		return searchResult;
	}

	@Override
	public ScrollModel<VoidedDocumentModel> getVoidedDocumentsScroll(OrganizationModel organization) {
		return getVoidedDocumentsScroll(organization, true);
	}

	@Override
	public ScrollModel<VoidedDocumentModel> getVoidedDocumentsScroll(OrganizationModel organization, boolean asc) {
		return getVoidedDocumentsScroll(organization, asc, -1);
	}

	@Override
	public ScrollModel<VoidedDocumentModel> getVoidedDocumentsScroll(OrganizationModel organization, boolean asc,
			int scrollSize) {
		return getVoidedDocumentsScroll(organization, asc, scrollSize, -1);
	}

	@Override
	public ScrollModel<VoidedDocumentModel> getVoidedDocumentsScroll(OrganizationModel organization, boolean asc,
			int scrollSize, int fetchSize) {
		if (scrollSize == -1) {
			scrollSize = 5;
		}
		if (fetchSize == -1) {
			scrollSize = 1;
		}

		Criteria criteria = getSession().createCriteria(VoidedDocumentsEntity.class)
				.add(Restrictions.eq("organization.id", organization.getId()))
				.addOrder(asc ? Order.asc("createdTimestamp") : Order.desc("createdTimestamp"));

		JpaScrollAdapter<VoidedDocumentModel, VoidedDocumentsEntity> result = new JpaScrollAdapter<>(criteria,
				scrollSize, f -> new VoidedDocumentAdapter(session, organization, em, f));
		return result;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

}
