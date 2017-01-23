package org.openfact.pe.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;
import org.openfact.file.FileModel;
import org.openfact.models.*;
import org.openfact.models.enums.DestinyType;
import org.openfact.models.enums.RequiredAction;
import org.openfact.pe.models.types.retention.RetentionType;
import org.openfact.provider.ProviderEvent;
import org.w3c.dom.Document;

public interface RetentionModel {

    String DOCUMENT_ID = "documentId";
    String ENTITY_NAME = "entityName";

    String getId();
    String getDocumentId();
    LocalDateTime getCreatedTimestamp();

    OrganizationModel getOrganization();

    String getSunatRetentionSystemCode();
    void setSunatRetentionSystemCode(String sunatRetentionSystemCode);

    String getEntityDocumentType();
    void setEntityDocumentType(String entityDocumentType);

    String getEntityDocumentNuber();
    void setEntityDocumentNuber(String numberEntityDocument);

    String getEntityName();
    void setEntityName(String entityName);

    String getEntityAddress();
    void setEntityAddress(String entityAddress);

    String getEntityEmail();
    void setEntityEmail(String entityEmail);

    String getDocumentCurrencyCode();
    void setDocumentCurrencyCode(String documentCurrencyCode);

    BigDecimal getSunatRetentionPercent();
    void setSunatRetentionPercent(BigDecimal sunatRetentionPercent);

    String getNote();
    void setNote(String note);

    BigDecimal getTotalRetentionAmount();
    void setTotalRetentionAmount(BigDecimal totalRetentionAmount);

    BigDecimal getTotalCashed();
    void setTotalCashed(BigDecimal totalCashed);

    LocalDateTime getIssueDateTime();
    void setIssueDateTime(LocalDateTime issueDateTime);

    RetentionType getRetentionType();

    /**
     * Xml
     **/
    FileModel getXmlAsFile();
    void attachXmlFile(FileModel file);

    Document getXmlAsDocument();
    JSONObject getXmlAsJSONObject();

    /**
     * Required Actions
     */
    Set<String> getRequiredActions();
    void addRequiredAction(String action);
    void removeRequiredAction(String action);
    void addRequiredAction(RequiredAction action);
    void removeRequiredAction(RequiredAction action);

    /**
     * Send events
     */
    String SEND_EVENT_DESTINY_TYPE = "destinyType";
    String SEND_EVENT_TYPE = "type";
    String SEND_EVENT_RESULT = "result";

    SendEventModel addSendEvent(DestinyType destinyType);
    SendEventModel  getSendEventById(String id);
    boolean removeSendEvent(String id);
    boolean removeSendEvent(SendEventModel sendEvent);
    List<SendEventModel > getSendEvents();
    List<SendEventModel > getSendEvents(Integer firstResult, Integer maxResults);
    List<SendEventModel > searchForSendEvent(Map<String, String> params);
    List<SendEventModel > searchForSendEvent(Map<String, String> params, int firstResult, int maxResults);
    int sendEventCount();
    int sendEventCount(Map<String, String> params);

    /**
     * Events interfaces
     */
    interface RetentionCreationEvent extends ProviderEvent {
        RetentionModel getCreatedRetention();
    }

    interface RetentionPostCreateEvent extends ProviderEvent {
        RetentionModel getCreatedRetention();

        OpenfactSession getOpenfactSession();
    }

    interface RetentionRemovedEvent extends ProviderEvent {
        OrganizationModel getOrganization();

        RetentionModel getRetention();

        OpenfactSession getOpenfactSession();
    }
}
