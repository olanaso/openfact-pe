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
import org.openfact.models.search.SearchCriteriaFilterOperator;
import org.openfact.models.search.SearchCriteriaModel;
import org.openfact.models.search.SearchResultsModel;
import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.PerceptionProvider;
import org.openfact.pe.models.jpa.entities.PerceptionEntity;

public class JpaPerceptionProvider extends AbstractHibernateStorage implements PerceptionProvider {

    protected OpenfactSession session;
    protected EntityManager em;

    protected PerceptionProvider cache;

    protected static final String DOCUMENT_ID = "documentId";
    protected static final String ENTITY_NAME = "entityName";

    protected static final String SEND_EVENT_DESTINY_TYPE = "destinyType";
    protected static final String SEND_EVENT_TYPE = "type";
    protected static final String SEND_EVENT_RESULT = "result";

    public JpaPerceptionProvider(OpenfactSession session, EntityManager em) {
        this.session = session;
        this.em = em;
    }

    private PerceptionProvider perceptions() {
        if (cache == null) {
            cache = session.getProvider(PerceptionProvider.class);
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
    public PerceptionModel addPerception(OrganizationModel organization, String documentId) {
        if (perceptions().getPerceptionByDocumentId(organization, documentId) != null) {
            throw new ModelDuplicateException("Perception documentId existed");
        }

        PerceptionEntity perception = new PerceptionEntity();
        perception.setDocumentId(documentId);
        perception.setCreatedTimestamp(LocalDateTime.now());
        perception.setOrganizationId(organization.getId());
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
        TypedQuery<PerceptionEntity> query = em.createNamedQuery("getOrganizationPerceptionById", PerceptionEntity.class);
        query.setParameter("id", id);
        query.setParameter("organizationId", organization.getId());
        List<PerceptionEntity> entities = query.getResultList();
        if (entities.size() == 0)
            return null;
        return new PerceptionAdapter(session, organization, em, entities.get(0));
    }

    @Override
    public PerceptionModel getPerceptionByDocumentId(OrganizationModel organization, String ID) {
        TypedQuery<PerceptionEntity> query = em.createNamedQuery("getOrganizationPerceptionByDocumentId", PerceptionEntity.class);
        query.setParameter("documentId", ID);
        query.setParameter("organizationId", organization.getId());
        List<PerceptionEntity> entities = query.getResultList();
        if (entities.size() == 0)
            return null;
        return new PerceptionAdapter(session, organization, em, entities.get(0));
    }

    @Override
    public void preRemove(OrganizationModel organization) {

    }

    @Override
    public boolean removePerception(OrganizationModel organization, String id) {
        return removePerception(organization, getPerceptionById(organization, id));
    }

    @Override
    public boolean removePerception(OrganizationModel organization, PerceptionModel perception) {
        PerceptionEntity perceptionEntity = em.find(PerceptionEntity.class, perception.getId());
        if (perceptionEntity == null) return false;
        removePerception(perceptionEntity);
        return true;
    }

    private void removePerception(PerceptionEntity perception) {
        String id = perception.getId();
        em.flush();
        em.clear();

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
    public List<PerceptionModel> getPerceptions(OrganizationModel organization, List<RequiredAction> requeridAction, boolean intoRequeridAction) {
        String queryName = "";
        if (intoRequeridAction) {
            queryName = "select i from PerceptionEntity i where i.organizationId = :organizationId and :requeridAction in elements(i.requeridAction) order by i.perceptionTypeCode ";
        } else {
            queryName = "select i from PerceptionEntity i where i.organizationId = :organizationId and :requeridAction not in elements(i.requeridAction) order by i.perceptionTypeCode ";

        }
        TypedQuery<PerceptionEntity> query = em.createQuery(queryName, PerceptionEntity.class);
        query.setParameter("organizationId", organization.getId());
        query.setParameter("requeridAction", requeridAction);
        List<PerceptionEntity> results = query.getResultList();
        List<PerceptionModel> perceptions = results.stream().map(f -> new PerceptionAdapter(session, organization, em, f)).collect(Collectors.toList());
        return perceptions;
    }

    @Override
    public List<PerceptionModel> getPerceptions(OrganizationModel organization, Integer firstResult, Integer maxResults) {
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
        List<PerceptionModel> perceptions = results.stream().map(f -> new PerceptionAdapter(session, organization, em, f)).collect(Collectors.toList());
        return perceptions;
    }

    @Override
    public List<PerceptionModel> searchForPerception(OrganizationModel organization, String filterText) {
        return searchForPerception(organization, filterText, -1, -1);
    }

    @Override
    public List<PerceptionModel> searchForPerception(OrganizationModel organization, String filterText, Integer firstResult, Integer maxResults) {
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
        List<PerceptionModel> perceptions = results.stream().map(f -> new PerceptionAdapter(session, organization, em, f)).collect(Collectors.toList());
        return perceptions;
    }

    @Override
    public SearchResultsModel<PerceptionModel> searchForPerception(OrganizationModel organization, SearchCriteriaModel criteria) {
        criteria.addFilter("organizationId", organization.getId(), SearchCriteriaFilterOperator.eq);

        SearchResultsModel<PerceptionEntity> entityResult = find(criteria, PerceptionEntity.class);
        List<PerceptionEntity> entities = entityResult.getModels();

        SearchResultsModel<PerceptionModel> searchResult = new SearchResultsModel<>();
        List<PerceptionModel> models = searchResult.getModels();

        entities.forEach(f -> models.add(new PerceptionAdapter(session, organization, em, f)));
        searchResult.setTotalSize(entityResult.getTotalSize());
        return searchResult;
    }

    @Override
    public SearchResultsModel<PerceptionModel> searchForPerception(OrganizationModel organization, SearchCriteriaModel criteria, String filterText) {
        criteria.addFilter("organizationId", organization.getId(), SearchCriteriaFilterOperator.eq);

        SearchResultsModel<PerceptionEntity> entityResult = findFullText(criteria, PerceptionEntity.class, filterText, DOCUMENT_ID, ENTITY_NAME);
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
    public ScrollModel<PerceptionModel> getPerceptionsScroll(OrganizationModel organization, boolean asc, int scrollSize) {
        if (scrollSize == -1) {
            scrollSize = 10;
        }
        String queryName = null;
        if (asc) {
            queryName = "getAllPerceptionsByOrganization";
        } else {
            queryName = "getAllPerceptionsByOrganizationDesc";
        }

        TypedQuery<PerceptionEntity> query = em.createNamedQuery(queryName, PerceptionEntity.class);
        query.setParameter("organizationId", organization.getId());

        ScrollAdapter<PerceptionModel, PerceptionEntity> result = new ScrollAdapter<>(PerceptionEntity.class, query, f -> {
            return new PerceptionAdapter(session, organization, em, f);
        });

        return result;
    }

    @Override
    public ScrollModel<List<PerceptionModel>> getPerceptionsScroll(OrganizationModel organization, int scrollSize, String... requiredAction) {
        if (scrollSize == -1) {
            scrollSize = 10;
        }

        TypedQuery<PerceptionEntity> query = em.createNamedQuery("getAllPerceptionsByRequiredActionAndOrganization", PerceptionEntity.class);
        query.setParameter("organizationId", organization.getId());
        query.setParameter("requiredAction", new ArrayList<>(Arrays.asList(requiredAction)));

        ScrollModel<List<PerceptionModel>> result = new ScrollPagingAdapter<>(PerceptionEntity.class, query, f -> {
            return f.stream().map(m -> new PerceptionAdapter(session, organization, em, m)).collect(Collectors.toList());
        });
        return result;
    }

    @Override
    public List<PerceptionModel> searchForPerception(Map<String, String> params, OrganizationModel organization) {
        return searchForPerception(params, organization, -1, -1);
    }

    @Override
    public List<PerceptionModel> searchForPerception(Map<String, String> attributes, OrganizationModel organization, Integer firstResult, Integer maxResults) {
        StringBuilder builder = new StringBuilder("select u from PerceptionEntity u where u.organizationId = :organizationId");
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            String attribute = null;
            String parameterName = null;
            if (entry.getKey().equals(PerceptionModel.DOCUMENT_ID)) {
                attribute = "lower(u.documentId)";
                parameterName = JpaPerceptionProvider.DOCUMENT_ID;
            } else if (entry.getKey().equals(PerceptionModel.ENTITY_NAME)) {
                attribute = "lower(u.entityName)";
                parameterName = JpaPerceptionProvider.ENTITY_NAME;
            }
            if (attribute == null) continue;
            builder.append(" and ");
            builder.append(attribute).append(" like :").append(parameterName);
        }
        builder.append(" order by u.createdTimestamp");
        String q = builder.toString();
        TypedQuery<PerceptionEntity> query = em.createQuery(q, PerceptionEntity.class);
        query.setParameter("organizationId", organization.getId());
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            String parameterName = null;
            if (entry.getKey().equals(PerceptionModel.DOCUMENT_ID)) {
                parameterName = JpaPerceptionProvider.DOCUMENT_ID;
            } else if (entry.getKey().equals(PerceptionModel.ENTITY_NAME)) {
                parameterName = JpaPerceptionProvider.ENTITY_NAME;
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
        List<PerceptionEntity> results = query.getResultList();
        List<PerceptionModel> perceptions = results.stream().map(f -> new PerceptionAdapter(session, organization, em, f)).collect(Collectors.toList());
        return perceptions;
    }

}
