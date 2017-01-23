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
import org.openfact.pe.models.RetentionModel;
import org.openfact.pe.models.jpa.entities.*;
import org.openfact.pe.models.types.retention.RetentionType;
import org.openfact.pe.models.utils.SunatDocumentToType;
import org.w3c.dom.Document;

public class RetentionAdapter implements RetentionModel, JpaModel<RetentionEntity> {
    protected static final Logger logger = Logger.getLogger(RetentionAdapter.class);

    protected OrganizationModel organization;
    protected RetentionEntity retention;
    protected EntityManager em;
    protected OpenfactSession session;

    protected FileModel xmlFile;
    protected RetentionType retentionType;
    protected Document document;
    protected JSONObject jsonObject;

    public RetentionAdapter(OpenfactSession session, OrganizationModel organization, EntityManager em, RetentionEntity retention) {
        this.organization = organization;
        this.session = session;
        this.em = em;
        this.retention = retention;
    }

    public static RetentionEntity toEntity(RetentionModel model, EntityManager em) {
        if (model instanceof RetentionAdapter) {
            return ((RetentionAdapter) model).getEntity();
        }
        return em.getReference(RetentionEntity.class, model.getId());
    }

    @Override
    public RetentionEntity getEntity() {
        return retention;
    }

    @Override
    public String getId() {
        return retention.getId();
    }

    @Override
    public String getSunatRetentionSystemCode() {
        return retention.getSunatRetentionSystemCode();
    }

    @Override
    public void setSunatRetentionSystemCode(String sunatRetentionSystemCode) {
        retention.setSunatRetentionSystemCode(sunatRetentionSystemCode);
    }

    @Override
    public String getEntityDocumentType() {
        return retention.getEntityDocumentType();
    }

    @Override
    public void setEntityDocumentType(String entityDocumentType) {
        retention.setEntityDocumentType(entityDocumentType);
    }

    @Override
    public String getEntityDocumentNuber() {
        return retention.getEntityDocumentNuber();
    }

    @Override
    public void setEntityDocumentNuber(String entityDocumentNuber) {
        retention.setEntityDocumentNuber(entityDocumentNuber);
    }

    @Override
    public String getEntityName() {
        return retention.getEntityName();
    }

    @Override
    public void setEntityName(String entityName) {
        retention.setEntityName(entityName);
    }

    @Override
    public String getEntityAddress() {
        return retention.getEntityAddress();
    }

    @Override
    public void setEntityAddress(String entityAddress) {
        retention.setEntityAddress(entityAddress);
    }

    @Override
    public String getEntityEmail() {
        return retention.getEntityEmail();
    }

    @Override
    public void setEntityEmail(String entityEmail) {
        retention.setEntityEmail(entityEmail);
    }

    @Override
    public String getDocumentId() {
        return retention.getDocumentId();
    }

    @Override
    public LocalDateTime getCreatedTimestamp() {
        return retention.getCreatedTimestamp();
    }

    @Override
    public OrganizationModel getOrganization() {
        return organization;
    }

    @Override
    public String getDocumentCurrencyCode() {
        return retention.getDocumentCurrencyCode();
    }

    @Override
    public void setDocumentCurrencyCode(String documentCurrencyCode) {
        retention.setDocumentCurrencyCode(documentCurrencyCode);
    }

    @Override
    public BigDecimal getSunatRetentionPercent() {
        return retention.getSunatRetentionPercent();
    }

    @Override
    public void setSunatRetentionPercent(BigDecimal sunatRetentionPercent) {
        retention.setSunatRetentionPercent(sunatRetentionPercent);
    }

    @Override
    public String getNote() {
        return retention.getNote();
    }

    @Override
    public void setNote(String note) {
        retention.setNote(note);
    }

    @Override
    public BigDecimal getTotalRetentionAmount() {
        return retention.getTotalRetentionAmount();
    }

    @Override
    public void setTotalRetentionAmount(BigDecimal totalRetentionAmount) {
        retention.setTotalRetentionAmount(totalRetentionAmount);
    }

    @Override
    public BigDecimal getTotalCashed() {
        return retention.getTotalCashed();
    }

    @Override
    public void setTotalCashed(BigDecimal totalCashed) {
        retention.setTotalCashed(totalCashed);
    }

    @Override
    public LocalDateTime getIssueDateTime() {
        return retention.getIssueDateTime();
    }

    @Override
    public void setIssueDateTime(LocalDateTime issueDateTime) {
        retention.setIssueDateTime(issueDateTime);
    }

    @Override
    public RetentionType getRetentionType() {
        if (retentionType == null) {
            FileModel file = getXmlAsFile();
            if (file != null) {
                try {
                    retentionType = SunatDocumentToType.toRetentionType(DocumentUtils.byteToDocument(file.getFile()));
                } catch (Exception e) {
                    throw new ModelException("Error parsing xml file to Type", e);
                }
            }
        }
        return retentionType;
    }

    @Override
    public FileModel getXmlAsFile() {
        if (xmlFile == null && retention.getXmlFileId() != null) {
            FileProvider provider = session.getProvider(FileProvider.class);
            xmlFile = provider.getFileById(organization, retention.getXmlFileId());
        }
        return xmlFile;
    }

    @Override
    public void attachXmlFile(FileModel file) {
        xmlFile = file;
        retention.setXmlFileId(xmlFile.getId());
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
                    jsonObject = JSONObjectUtils.getJSONObject(jsonObject, "Retention");
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
        RetentionSendEventEntity entity = new RetentionSendEventEntity();
        entity.setCreatedTimestamp(LocalDateTime.now());
        entity.setResult(SendResultType.ON_PROCESS);
        entity.setDestinyType(destinyType);
        entity.setRetention(retention);
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
        for (RetentionRequiredActionEntity attr : retention.getRequiredActions()) {
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
        for (RetentionRequiredActionEntity attr : retention.getRequiredActions()) {
            if (attr.getAction().equals(actionName)) {
                return;
            }
        }
        RetentionRequiredActionEntity attr = new RetentionRequiredActionEntity();
        attr.setAction(actionName);
        attr.setRetention(retention);
        em.persist(attr);
        retention.getRequiredActions().add(attr);

    }

    @Override
    public void removeRequiredAction(RequiredAction action) {
        String actionName = action.name();
        removeRequiredAction(actionName);
    }

    @Override
    public void removeRequiredAction(String actionName) {
        Iterator<RetentionRequiredActionEntity> it = retention.getRequiredActions().iterator();
        while (it.hasNext()) {
            RetentionRequiredActionEntity attr = it.next();
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
        String queryName = "getAllSunatSendEventByRetentionId";

        TypedQuery<SunatSendEventEntity> query = em.createNamedQuery(queryName, SunatSendEventEntity.class);
        query.setParameter("retentionId", retention.getId());
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
        StringBuilder builder = new StringBuilder("select u from RetentionSendEventEntity u where u.retention.id = :retentionId");
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            String attribute = null;
            String parameterName = null;
            String operator = null;
            if (entry.getKey().equals(RetentionModel.SEND_EVENT_DESTINY_TYPE)) {
                attribute = "u.destinyType";
                parameterName = JpaRetentionProvider.SEND_EVENT_DESTINY_TYPE;
                operator = " = :";
            } else if (entry.getKey().equals(RetentionModel.SEND_EVENT_TYPE)) {
                attribute = "lower(u.type)";
                parameterName = JpaRetentionProvider.SEND_EVENT_TYPE;
                operator = " like :";
            } else if (entry.getKey().equals(RetentionModel.SEND_EVENT_RESULT)) {
                attribute = "u.result";
                parameterName = JpaRetentionProvider.SEND_EVENT_RESULT;
                operator = " = :";
            }
            if (attribute == null) continue;
            builder.append(" and ");
            builder.append(attribute).append(operator).append(parameterName);
        }
        builder.append(" order by u.createdTimestamp");
        String q = builder.toString();
        TypedQuery<RetentionSendEventEntity> query = em.createQuery(q, RetentionSendEventEntity.class);
        query.setParameter("retentionId", retention.getId());
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            String parameterName = null;
            Object parameterValue = null;
            if (entry.getKey().equals(RetentionModel.SEND_EVENT_DESTINY_TYPE)) {
                parameterName = JpaRetentionProvider.SEND_EVENT_DESTINY_TYPE;
                parameterValue = DestinyType.valueOf(entry.getValue().toUpperCase());
            } else if (entry.getKey().equals(RetentionModel.SEND_EVENT_TYPE)) {
                parameterName = JpaRetentionProvider.SEND_EVENT_TYPE;
                parameterValue = "%" + entry.getValue().toLowerCase() + "%";
            } else if (entry.getKey().equals(RetentionModel.SEND_EVENT_RESULT)) {
                parameterName = JpaRetentionProvider.SEND_EVENT_RESULT;
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
        List<RetentionSendEventEntity> results = query.getResultList();
        return results.stream().map(f -> new SunatSendEventAdapter(session, em, organization, f)).collect(Collectors.toList());
    }

    @Override
    public int sendEventCount() {
        Object count = em.createNamedQuery("getRetentionSunatSendEventCountByRetention")
                .setParameter("retentionId", retention.getId())
                .getSingleResult();
        return ((Number) count).intValue();
    }

    @Override
    public int sendEventCount(Map<String, String> params) {
        return 0;
    }

}
