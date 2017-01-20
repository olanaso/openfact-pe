package org.openfact.pe.models.jpa.ubl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.jboss.logging.Logger;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.PartyModel;
import org.openfact.models.SendEventModel;
import org.openfact.models.enums.RequiredAction;
import org.openfact.models.jpa.JpaModel;
import org.openfact.pe.models.RetentionLineModel;
import org.openfact.pe.models.RetentionModel;
import org.openfact.pe.models.jpa.entities.RetentionLineEntity;
import org.openfact.pe.models.jpa.entities.RetentionEntity;
import org.openfact.pe.models.jpa.entities.RetentionRequiredActionEntity;

public class RetentionAdapter implements RetentionModel, JpaModel<RetentionEntity> {
    protected static final Logger logger = Logger.getLogger(RetentionAdapter.class);

    protected OrganizationModel organization;
    protected RetentionEntity retention;
    protected EntityManager em;
    protected OpenfactSession session;

    public RetentionAdapter(OpenfactSession session, OrganizationModel organization, EntityManager em,
                            RetentionEntity retention) {
        this.organization = organization;
        this.session = session;
        this.em = em;
        this.retention = retention;
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
    public String getOrganizationId() {
        return retention.getOrganizationId();
    }

    @Override
    public String getUblVersionID() {
        return retention.getUblVersionId();
    }

    @Override
    public void setUblVersionID(String ublVersionID) {
        retention.setUblVersionId(ublVersionID);
    }

    @Override
    public String getCustomizationID() {
        return retention.getCustomizationId();
    }

    @Override
    public void setCustomizationID(String customizationID) {
        retention.setCustomizationId(customizationID);
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
    public void setDocumentId(String documentId) {
        retention.setDocumentId(documentId);
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
    public List<RetentionLineModel> getRetentionLines() {
        return retention.getRetentionLines().stream()
                .map(f -> new RetentionLineAdapter(session, em, f)).collect(Collectors.toList());
    }

    @Override
    public RetentionLineModel addRetentionLine() {
        List<RetentionLineEntity> entities = retention.getRetentionLines();
        RetentionLineEntity entity = new RetentionLineEntity();
        entities.add(entity);
        return new RetentionLineAdapter(session, em, entity);
    }

    public static RetentionEntity toEntity(RetentionModel model, EntityManager em) {
        if (model instanceof RetentionAdapter) {
            return ((RetentionAdapter) model).getEntity();
        }
        return em.getReference(RetentionEntity.class, model.getId());
    }

    @Override
    public byte[] getXmlDocument() {
        return retention.getXmlDocument();
    }

    @Override
    public void setXmlDocument(byte[] object) {
        retention.setXmlDocument(object);
    }

    @Override
    public Set<String> getRequiredActions() {
        Set<String> result = new HashSet<>();
        for (RetentionRequiredActionEntity attr : retention.getRequiredActions()) {
            result.add(attr.getAction());
        }
        return result;
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
    public void addRequiredAction(RequiredAction action) {
        String actionName = action.name();
        addRequiredAction(actionName);
    }

    @Override
    public void removeRequiredAction(RequiredAction action) {
        String actionName = action.name();
        removeRequiredAction(actionName);
    }

    @Override
    public List<SendEventModel> getSendEvents() {
        return retention.getSendEvents().stream().map(f -> new SunatSendEventAdapter(session, organization, em, f))
                .collect(Collectors.toList());
    }

}
