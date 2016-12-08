package org.openfact.pe.models.jpa.ubl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.openfact.models.ModelDuplicateException;
import org.openfact.models.ModelException;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.ScrollModel;
import org.openfact.models.enums.RequiredAction;
import org.openfact.models.jpa.AbstractHibernateStorage;
import org.openfact.models.jpa.OrganizationAdapter;
import org.openfact.models.jpa.ScrollAdapter;
import org.openfact.models.search.SearchCriteriaFilterOperator;
import org.openfact.models.search.SearchCriteriaModel;
import org.openfact.models.search.SearchResultsModel;
import org.openfact.pe.models.SummaryDocumentModel;
import org.openfact.pe.models.SummaryDocumentProvider;
import org.openfact.pe.models.jpa.entities.SummaryDocumentsEntity;

public class JpaSummaryDocumentsProvider extends AbstractHibernateStorage implements SummaryDocumentProvider {

	private OpenfactSession session;
	protected EntityManager em;
	private static final String DOCUMENT_ID = "ID";

	public JpaSummaryDocumentsProvider(OpenfactSession session, EntityManager em) {
		this.session = session;
		this.em = em;
	}

	@Override
	public void close() {

	}

	private SummaryDocumentProvider getSummaryDocuments() {
		SummaryDocumentProvider cache = session.getProvider(SummaryDocumentProvider.class);
		if (cache != null) {
			return cache;
		} else {
			return session.getProvider(SummaryDocumentProvider.class);
		}
	}

	@Override
	public SummaryDocumentModel addSummaryDocument(OrganizationModel organization, String documentId) {
		if (documentId == null) {
			throw new ModelException("Invalid documentId, Null value");
		}

		if (getSummaryDocuments().getSummaryDocumentByID(organization, documentId) != null) {
			throw new ModelDuplicateException("SummaryDocument documentId existed");
		}

		SummaryDocumentsEntity summaryDocument = new SummaryDocumentsEntity();
		summaryDocument.setDocumentId(documentId);
		summaryDocument.setCreatedTimestamp(LocalDateTime.now());
		summaryDocument.setOrganization(OrganizationAdapter.toEntity(organization, em));
		em.persist(summaryDocument);
		em.flush();

		final SummaryDocumentModel adapter = new SummaryDocumentAdapter(session, organization, em, summaryDocument);
		session.getOpenfactSessionFactory().publish(new SummaryDocumentModel.SummaryDocumentCreationEvent() {
			@Override
			public SummaryDocumentModel getCreatedSummaryDocument() {
				return adapter;
			}
		});
		return adapter;
	}

	@Override
	public SummaryDocumentModel getSummaryDocumentById(OrganizationModel organization, String id) {
		TypedQuery<SummaryDocumentsEntity> query = em.createNamedQuery("getOrganizationSummaryDocumentById",
				SummaryDocumentsEntity.class);
		query.setParameter("id", id);
		query.setParameter("organizationId", organization.getId());
		List<SummaryDocumentsEntity> entities = query.getResultList();
		if (entities.size() == 0)
			return null;
		return new SummaryDocumentAdapter(session, organization, em, entities.get(0));
	}

	@Override
	public SummaryDocumentModel getSummaryDocumentByID(OrganizationModel organization, String ID) {
		TypedQuery<SummaryDocumentsEntity> query = em.createNamedQuery("getOrganizationSummaryDocumentByDocumentId",
				SummaryDocumentsEntity.class);
		query.setParameter("documentId", ID);
		query.setParameter("organizationId", organization.getId());
		List<SummaryDocumentsEntity> entities = query.getResultList();
		if (entities.size() == 0)
			return null;
		return new SummaryDocumentAdapter(session, organization, em, entities.get(0));
	}

	@Override
	public boolean removeSummaryDocument(OrganizationModel organization, String id) {
		return removeSummaryDocument(organization, getSummaryDocumentById(organization, id));
	}

	@Override
	public boolean removeSummaryDocument(OrganizationModel organization, SummaryDocumentModel summaryDocument) {
		SummaryDocumentsEntity summaryDocumentEntity = em.find(SummaryDocumentsEntity.class, summaryDocument.getId());
		if (summaryDocumentEntity == null)
			return false;
		removeSummaryDocument(summaryDocumentEntity);
		session.getOpenfactSessionFactory().publish(new SummaryDocumentModel.SummaryDocumentRemovedEvent() {

			@Override
			public SummaryDocumentModel getSummaryDocument() {
				return summaryDocument;
			}

			@Override
			public OpenfactSession getOpenfactSession() {
				return session;
			}
		});
		return true;
	}

	private void removeSummaryDocument(SummaryDocumentsEntity summaryDocument) {
		String id = summaryDocument.getId();
		summaryDocument = em.find(SummaryDocumentsEntity.class, id);
		if (summaryDocument != null) {
			em.remove(summaryDocument);
		}

		em.flush();
	}

	@Override
	public int getSummaryDocumentsCount(OrganizationModel organization) {
		Query query = em.createNamedQuery("getOrganizationSummaryDocumentCount");
		Long result = (Long) query.getSingleResult();
		return result.intValue();
	}

	@Override
	public List<SummaryDocumentModel> getSummaryDocuments(OrganizationModel organization) {
		return getSummaryDocuments(organization, -1, -1);
	}

	@Override
	public List<SummaryDocumentModel> getSummaryDocuments(OrganizationModel organization,
			List<RequiredAction> requeridAction, boolean intoRequeridAction) {
		String queryName = "";
		if (intoRequeridAction) {
			queryName = "select i from SummaryDocumentsEntity i where i.organization.id = :organizationId and :requeridAction in elements(i.requeridAction) order by i.summaryDocumentTypeCode ";
		} else {
			queryName = "select i from SummaryDocumentsEntity i where i.organization.id = :organizationId and :requeridAction not in elements(i.requeridAction) order by i.summaryDocumentTypeCode ";

		}
		TypedQuery<SummaryDocumentsEntity> query = em.createQuery(queryName, SummaryDocumentsEntity.class);
		query.setParameter("organizationId", organization.getId());
		query.setParameter("requeridAction", requeridAction);
		List<SummaryDocumentsEntity> results = query.getResultList();
		List<SummaryDocumentModel> summaryDocuments = results.stream()
				.map(f -> new SummaryDocumentAdapter(session, organization, em, f)).collect(Collectors.toList());
		return summaryDocuments;
	}

	@Override
	public List<SummaryDocumentModel> getSummaryDocuments(OrganizationModel organization, Integer firstResult,
			Integer maxResults) {
		String queryName = "getAllSummaryDocumentsByOrganization";

		TypedQuery<SummaryDocumentsEntity> query = em.createNamedQuery(queryName, SummaryDocumentsEntity.class);
		query.setParameter("organizationId", organization.getId());
		if (firstResult != -1) {
			query.setFirstResult(firstResult);
		}
		if (maxResults != -1) {
			query.setMaxResults(maxResults);
		}
		List<SummaryDocumentsEntity> results = query.getResultList();
		List<SummaryDocumentModel> summaryDocuments = results.stream()
				.map(f -> new SummaryDocumentAdapter(session, organization, em, f)).collect(Collectors.toList());
		return summaryDocuments;
	}

	@Override
	public List<SummaryDocumentModel> searchForSummaryDocument(OrganizationModel organization, String filterText) {
		return searchForSummaryDocument(organization, filterText, -1, -1);
	}

	@Override
	public List<SummaryDocumentModel> searchForSummaryDocument(OrganizationModel organization, String filterText,
			Integer firstResult, Integer maxResults) {
		TypedQuery<SummaryDocumentsEntity> query = em.createNamedQuery("searchForSummaryDocument",
				SummaryDocumentsEntity.class);
		query.setParameter("organizationId", organization.getId());
		query.setParameter("search", "%" + filterText.toLowerCase() + "%");
		if (firstResult != -1) {
			query.setFirstResult(firstResult);
		}
		if (maxResults != -1) {
			query.setMaxResults(maxResults);
		}
		List<SummaryDocumentsEntity> results = query.getResultList();
		List<SummaryDocumentModel> summaryDocuments = results.stream()
				.map(f -> new SummaryDocumentAdapter(session, organization, em, f)).collect(Collectors.toList());
		return summaryDocuments;
	}

	@Override
	public SearchResultsModel<SummaryDocumentModel> searchForSummaryDocument(OrganizationModel organization,
			SearchCriteriaModel criteria) {
		criteria.addFilter("organization.id", organization.getId(), SearchCriteriaFilterOperator.eq);

		SearchResultsModel<SummaryDocumentsEntity> entityResult = find(criteria, SummaryDocumentsEntity.class);
		List<SummaryDocumentsEntity> entities = entityResult.getModels();

		SearchResultsModel<SummaryDocumentModel> searchResult = new SearchResultsModel<>();
		List<SummaryDocumentModel> models = searchResult.getModels();

		entities.forEach(f -> models.add(new SummaryDocumentAdapter(session, organization, em, f)));
		searchResult.setTotalSize(entityResult.getTotalSize());
		return searchResult;
	}

	@Override
	public SearchResultsModel<SummaryDocumentModel> searchForSummaryDocument(OrganizationModel organization,
			SearchCriteriaModel criteria, String filterText) {
		criteria.addFilter("organization.id", organization.getId(), SearchCriteriaFilterOperator.eq);

		SearchResultsModel<SummaryDocumentsEntity> entityResult = findFullText(criteria, SummaryDocumentsEntity.class,
				filterText, DOCUMENT_ID);
		List<SummaryDocumentsEntity> entities = entityResult.getModels();

		SearchResultsModel<SummaryDocumentModel> searchResult = new SearchResultsModel<>();
		List<SummaryDocumentModel> models = searchResult.getModels();

		entities.forEach(f -> models.add(new SummaryDocumentAdapter(session, organization, em, f)));
		searchResult.setTotalSize(entityResult.getTotalSize());
		return searchResult;
	}

	@Override
	public ScrollModel<SummaryDocumentModel> getSummaryDocumentsScroll(OrganizationModel organization) {
		return getSummaryDocumentsScroll(organization, true);
	}

	@Override
	public ScrollModel<SummaryDocumentModel> getSummaryDocumentsScroll(OrganizationModel organization, boolean asc) {
		return getSummaryDocumentsScroll(organization, asc, -1);
	}

	@Override
	public ScrollModel<SummaryDocumentModel> getSummaryDocumentsScroll(OrganizationModel organization, boolean asc,
			int scrollSize) {
		if (scrollSize == -1) {
			scrollSize = 10;
		}

		TypedQuery<String> query = em.createNamedQuery("getAllSummaryDocumentsByOrganization", String.class);
		query.setParameter("organizationId", organization.getId());

		ScrollAdapter<SummaryDocumentModel, String> result = new ScrollAdapter<>(String.class, query, f -> {
			SummaryDocumentsEntity entity = em.find(SummaryDocumentsEntity.class, f);
			return new SummaryDocumentAdapter(session, organization, em, entity);
		});

		// Iterator<RetentionModel> iterator = result.iterator();
		// while (iterator.hasNext()) {
		// SummaryDocumentModel perceptionModel = iterator.next();
		// System.out.println("-------------------");
		// System.out.println(perceptionModel.getRequiredActions());
		// }

		return result;
	}
	

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

}
