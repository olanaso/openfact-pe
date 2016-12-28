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
import org.openfact.models.enums.RequiredAction;
import org.openfact.models.jpa.JpaModel;
import org.openfact.pe.models.PerceptionLineModel;
import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.jpa.entities.PerceptionLineEntity;
import org.openfact.pe.models.jpa.entities.PerceptionEntity;
import org.openfact.pe.models.jpa.entities.PerceptionRequiredActionEntity;
import org.openfact.ubl.SendEventModel;

public class PerceptionAdapter implements PerceptionModel, JpaModel<PerceptionEntity> {
    protected static final Logger logger = Logger.getLogger(PerceptionAdapter.class);

    protected OrganizationModel organization;
    protected PerceptionEntity perception;
    protected EntityManager em;
    protected OpenfactSession session;

    public PerceptionAdapter(OpenfactSession session, OrganizationModel organization, EntityManager em,
                             PerceptionEntity perception) {
        this.organization = organization;
        this.session = session;
        this.em = em;
        this.perception = perception;
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
    public String getOrganizationId() {
        return perception.getOrganizationId();
    }

    @Override
    public String getUblVersionID() {
        return perception.getUblVersionId();
    }

    @Override
    public void setUblVersionID(String ublVersionID) {
        perception.setUblVersionId(ublVersionID);
    }

    @Override
    public String getCustomizationID() {
        return perception.getCustomizationId();
    }

    @Override
    public void setCustomizationID(String customizationID) {
        perception.setCustomizationId(customizationID);
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
    public void setDocumentId(String documentId) {
        perception.setDocumentId(documentId);
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
    public List<PerceptionLineModel> getPerceptionLines() {
        return perception.getPerceptionLines().stream()
                .map(f -> new PerceptionLineAdapter(session, em, f)).collect(Collectors.toList());
    }

    @Override
    public PerceptionLineModel addPerceptionLine() {
        List<PerceptionLineEntity> entities = perception.getPerceptionLines();
        PerceptionLineEntity entity = new PerceptionLineEntity();
        entities.add(entity);
        return new PerceptionLineAdapter(session, em, entity);
    }

    public static PerceptionEntity toEntity(PerceptionModel model, EntityManager em) {
        if (model instanceof PerceptionAdapter) {
            return ((PerceptionAdapter) model).getEntity();
        }
        return em.getReference(PerceptionEntity.class, model.getId());
    }

    @Override
    public byte[] getXmlDocument() {
        return perception.getXmlDocument();
    }

    @Override
    public void setXmlDocument(byte[] object) {
        perception.setXmlDocument(object);
    }

    @Override
    public Set<String> getRequiredActions() {
        Set<String> result = new HashSet<>();
        for (PerceptionRequiredActionEntity attr : perception.getRequiredActions()) {
            result.add(attr.getAction());
        }
        return result;
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
    	return perception.getSendEvents().stream().map(f -> new SunatSendEventAdapter(session, organization, em, f))
                .collect(Collectors.toList());
    }
}
