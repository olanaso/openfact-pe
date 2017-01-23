package org.openfact.pe.models.jpa.ubl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.xml.transform.TransformerException;

import org.jboss.logging.Logger;
import org.json.JSONObject;
import org.json.XML;
import org.openfact.JSONObjectUtils;
import org.openfact.common.converts.DocumentUtils;
import org.openfact.file.FileModel;
import org.openfact.file.FileProvider;
import org.openfact.models.*;
import org.openfact.models.enums.DestinyType;
import org.openfact.models.enums.RequiredAction;
import org.openfact.models.enums.SendResultType;
import org.openfact.models.jpa.JpaModel;
import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.jpa.entities.*;
import org.openfact.pe.models.jpa.entities.PerceptionSendEventEntity;
import org.openfact.pe.models.types.perception.PerceptionType;
import org.openfact.pe.models.utils.SunatDocumentToType;
import org.w3c.dom.Document;

public class PerceptionAdapter implements PerceptionModel, JpaModel<PerceptionEntity> {
    protected static final Logger logger = Logger.getLogger(PerceptionAdapter.class);

    protected OrganizationModel organization;
    protected PerceptionEntity perception;
    protected EntityManager em;
    protected OpenfactSession session;

    protected FileModel xmlFile;
    protected PerceptionType perceptionType;
    protected Document document;
    protected JSONObject jsonObject;

    public PerceptionAdapter(OpenfactSession session, OrganizationModel organization, EntityManager em, PerceptionEntity perception) {
        this.organization = organization;
        this.session = session;
        this.em = em;
        this.perception = perception;
    }

    public static PerceptionEntity toEntity(PerceptionModel model, EntityManager em) {
        if (model instanceof PerceptionAdapter) {
            return ((PerceptionAdapter) model).getEntity();
        }
        return em.getReference(PerceptionEntity.class, model.getId());
    }

    @Override
    public PerceptionEntity getEntity() {
        return perception;
    }

    @Override
    public String getId() {
        return perception.getId();
    }

    @Override
    public String getSunatPerceptionSystemCode() {
        return perception.getSunatPerceptionSystemCode();
    }

    @Override
    public void setSunatPerceptionSystemCode(String sunatPerceptionSystemCode) {
        perception.setSunatPerceptionSystemCode(sunatPerceptionSystemCode);
    }

    @Override
    public String getEntityDocumentType() {
        return perception.getEntityDocumentType();
    }

    @Override
    public void setEntityDocumentType(String entityDocumentType) {
        perception.setEntityDocumentType(entityDocumentType);
    }

    @Override
    public String getEntityDocumentNuber() {
        return perception.getEntityDocumentNuber();
    }

    @Override
    public void setEntityDocumentNuber(String entityDocumentNuber) {
        perception.setEntityDocumentNuber(entityDocumentNuber);
    }

    @Override
    public String getEntityName() {
        return perception.getEntityName();
    }

    @Override
    public void setEntityName(String entityName) {
        perception.setEntityName(entityName);
    }

    @Override
    public String getEntityAddress() {
        return perception.getEntityAddress();
    }

    @Override
    public void setEntityAddress(String entityAddress) {
        perception.setEntityAddress(entityAddress);
    }

    @Override
    public String getEntityEmail() {
        return perception.getEntityEmail();
    }

    @Override
    public void setEntityEmail(String entityEmail) {
        perception.setEntityEmail(entityEmail);
    }

    @Override
    public String getDocumentId() {
        return perception.getDocumentId();
    }

    @Override
    public LocalDateTime getCreatedTimestamp() {
        return perception.getCreatedTimestamp();
    }

    @Override
    public OrganizationModel getOrganization() {
        return organization;
    }

    @Override
    public String getDocumentCurrencyCode() {
        return perception.getDocumentCurrencyCode();
    }

    @Override
    public void setDocumentCurrencyCode(String documentCurrencyCode) {
        perception.setDocumentCurrencyCode(documentCurrencyCode);
    }

    @Override
    public BigDecimal getSunatPerceptionPercent() {
        return perception.getSunatPerceptionPercent();
    }

    @Override
    public void setSunatPerceptionPercent(BigDecimal sunatPerceptionPercent) {
        perception.setSunatPerceptionPercent(sunatPerceptionPercent);
    }

    @Override
    public String getNote() {
        return perception.getNote();
    }

    @Override
    public void setNote(String note) {
        perception.setNote(note);
    }

    @Override
    public BigDecimal getTotalPerceptionAmount() {
        return perception.getTotalPerceptionAmount();
    }

    @Override
    public void setTotalPerceptionAmount(BigDecimal totalPerceptionAmount) {
        perception.setTotalPerceptionAmount(totalPerceptionAmount);
    }

    @Override
    public BigDecimal getTotalCashed() {
        return perception.getTotalCashed();
    }

    @Override
    public void setTotalCashed(BigDecimal totalCashed) {
        perception.setTotalCashed(totalCashed);
    }

    @Override
    public LocalDateTime getIssueDateTime() {
        return perception.getIssueDateTime();
    }

    @Override
    public void setIssueDateTime(LocalDateTime issueDateTime) {
        perception.setIssueDateTime(issueDateTime);
    }

    @Override
    public PerceptionType getPerceptionType() {
        if (perceptionType == null) {
            FileModel file = getXmlAsFile();
            if (file != null) {
                try {
                    perceptionType = SunatDocumentToType.toPerceptionType(DocumentUtils.byteToDocument(file.getFile()));
                } catch (Exception e) {
                    throw new ModelException("Error parsing xml file to Type", e);
                }
            }
        }
        return perceptionType;
    }

    @Override
    public FileModel getXmlAsFile() {
        if (xmlFile == null && perception.getXmlFileId() != null) {
            FileProvider provider = session.getProvider(FileProvider.class);
            xmlFile = provider.getFileById(organization, perception.getXmlFileId());
        }
        return xmlFile;
    }

    @Override
    public void attachXmlFile(FileModel file) {
        xmlFile = file;
        perception.setXmlFileId(xmlFile.getId());
    }

    @Override
    public Document getXmlAsDocument() {
        if (document == null) {
            FileModel file = getXmlAsFile();
            if (file != null) {
                try {
                    document = DocumentUtils.byteToDocument(file.getFile());
                } catch (Exception e) {
                    throw new ModelException("Error parsing xml file to Document", e);
                }
            }
        }
        return document;
    }

    @Override
    public JSONObject getXmlAsJSONObject() {
        if (jsonObject == null) {
            try {
                Document document = getXmlAsDocument();
                if (document != null) {
                    String documentString = DocumentUtils.getDocumentToString(document);
                    jsonObject = JSONObjectUtils.renameKey(XML.toJSONObject(documentString), ".*:", "");
                    jsonObject = JSONObjectUtils.getJSONObject(jsonObject, "Perception");
                }
            } catch (TransformerException e) {
                throw new ModelException("Error parsing xml file to JSON", e);
            }
        }
        return jsonObject;
    }

    /**
     * Send events
     */
    @Override
    public SendEventModel addSendEvent(DestinyType destinyType) {
        PerceptionSendEventEntity entity = new PerceptionSendEventEntity();
        entity.setCreatedTimestamp(LocalDateTime.now());
        entity.setResult(SendResultType.ON_PROCESS);
        entity.setDestinyType(destinyType);
        entity.setPerception(perception);
        em.persist(entity);
        //em.flush();

        return new SunatSendEventAdapter(session, em, organization, entity);
    }

    @Override
    public SendEventModel getSendEventById(String id) {
        SunatSendEventEntity entity = em.find(SunatSendEventEntity.class, id);
        if (entity != null) {
            return new SunatSendEventAdapter(session, em, organization, entity);
        }
        return null;
    }

    @Override
    public boolean removeSendEvent(String id) {
        SunatSendEventEntity entity = em.find(SunatSendEventEntity.class, id);
        if (entity == null)
            return false;

        em.remove(entity);
        em.flush();
        return true;
    }

    @Override
    public boolean removeSendEvent(SendEventModel sendEvent) {
        SunatSendEventEntity entity = em.find(SunatSendEventEntity.class, sendEvent.getId());
        if (entity == null)
            return false;

        em.remove(entity);
        em.flush();
        return true;
    }

    /**
     * Required actions
     */
    @Override
    public Set<String> getRequiredActions() {
        Set<String> result = new HashSet<>();
        for (PerceptionRequiredActionEntity attr : perception.getRequiredActions()) {
            result.add(attr.getAction());
        }
        return result;
    }

    @Override
    public void addRequiredAction(RequiredAction action) {
        String actionName = action.name();
        addRequiredAction(actionName);
    }

    @Override
    public void addRequiredAction(String actionName) {
        for (PerceptionRequiredActionEntity attr : perception.getRequiredActions()) {
            if (attr.getAction().equals(actionName)) {
                return;
            }
        }
        PerceptionRequiredActionEntity attr = new PerceptionRequiredActionEntity();
        attr.setAction(actionName);
        attr.setPerception(perception);
        em.persist(attr);
        perception.getRequiredActions().add(attr);

    }

    @Override
    public void removeRequiredAction(RequiredAction action) {
        String actionName = action.name();
        removeRequiredAction(actionName);
    }

    @Override
    public void removeRequiredAction(String actionName) {
        Iterator<PerceptionRequiredActionEntity> it = perception.getRequiredActions().iterator();
        while (it.hasNext()) {
            PerceptionRequiredActionEntity attr = it.next();
            if (attr.getAction().equals(actionName)) {
                it.remove();
                em.remove(attr);
            }
        }
    }


    @Override
    public List<SendEventModel> getSendEvents() {
        return getSendEvents(-1, -1);
    }

    @Override
    public List<SendEventModel> getSendEvents(Integer firstResult, Integer maxResults) {
        String queryName = "getAllSunatSendEventByPerceptionId";

        TypedQuery<SunatSendEventEntity> query = em.createNamedQuery(queryName, SunatSendEventEntity.class);
        query.setParameter("perceptionId", perception.getId());
        if (firstResult != -1) {
            query.setFirstResult(firstResult);
        }
        if (maxResults != -1) {
            query.setMaxResults(maxResults);
        }
        List<SunatSendEventEntity> results = query.getResultList();
        List<SendEventModel> sendEvents = results.stream().map(f -> new SunatSendEventAdapter(session, em, organization, f)).collect(Collectors.toList());
        return sendEvents;
    }

    @Override
    public List<SendEventModel> searchForSendEvent(Map<String, String> params) {
        return searchForSendEvent(params, -1, -1);
    }

    @Override
    public List<SendEventModel> searchForSendEvent(Map<String, String> attributes, int firstResult, int maxResults) {
        StringBuilder builder = new StringBuilder("select u from PerceptionSendEventEntity u where u.perception.id = :perceptionId");
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            String attribute = null;
            String parameterName = null;
            String operator = null;
            if (entry.getKey().equals(PerceptionModel.SEND_EVENT_DESTINY_TYPE)) {
                attribute = "u.destinyType";
                parameterName = JpaPerceptionProvider.SEND_EVENT_DESTINY_TYPE;
                operator = " = :";
            } else if (entry.getKey().equals(PerceptionModel.SEND_EVENT_TYPE)) {
                attribute = "lower(u.type)";
                parameterName = JpaPerceptionProvider.SEND_EVENT_TYPE;
                operator = " like :";
            } else if (entry.getKey().equals(PerceptionModel.SEND_EVENT_RESULT)) {
                attribute = "u.result";
                parameterName = JpaPerceptionProvider.SEND_EVENT_RESULT;
                operator = " = :";
            }
            if (attribute == null) continue;
            builder.append(" and ");
            builder.append(attribute).append(operator).append(parameterName);
        }
        builder.append(" order by u.createdTimestamp");
        String q = builder.toString();
        TypedQuery<PerceptionSendEventEntity> query = em.createQuery(q, PerceptionSendEventEntity.class);
        query.setParameter("perceptionId", perception.getId());
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            String parameterName = null;
            Object parameterValue = null;
            if (entry.getKey().equals(PerceptionModel.SEND_EVENT_DESTINY_TYPE)) {
                parameterName = JpaPerceptionProvider.SEND_EVENT_DESTINY_TYPE;
                parameterValue = DestinyType.valueOf(entry.getValue().toUpperCase());
            } else if (entry.getKey().equals(PerceptionModel.SEND_EVENT_TYPE)) {
                parameterName = JpaPerceptionProvider.SEND_EVENT_TYPE;
                parameterValue = "%" + entry.getValue().toLowerCase() + "%";
            } else if (entry.getKey().equals(PerceptionModel.SEND_EVENT_RESULT)) {
                parameterName = JpaPerceptionProvider.SEND_EVENT_RESULT;
                parameterValue = SendResultType.valueOf(entry.getValue().toUpperCase());
            }
            if (parameterName == null) continue;
            query.setParameter(parameterName, parameterValue);
        }
        if (firstResult != -1) {
            query.setFirstResult(firstResult);
        }
        if (maxResults != -1) {
            query.setMaxResults(maxResults);
        }
        List<PerceptionSendEventEntity> results = query.getResultList();
        return results.stream().map(f -> new SunatSendEventAdapter(session, em, organization, f)).collect(Collectors.toList());
    }

    @Override
    public int sendEventCount() {
        Object count = em.createNamedQuery("getPerceptionSunatSendEventCountByPerception")
                .setParameter("perceptionId", perception.getId())
                .getSingleResult();
        return ((Number) count).intValue();
    }

    @Override
    public int sendEventCount(Map<String, String> params) {
        return 0;
    }
}
