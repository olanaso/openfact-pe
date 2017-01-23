package org.openfact.pe.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;
import org.openfact.file.FileModel;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.PartyModel;
import org.openfact.models.SendEventModel;
import org.openfact.models.enums.DestinyType;
import org.openfact.models.enums.RequiredAction;
import org.openfact.pe.models.types.perception.PerceptionType;
import org.openfact.pe.representations.idm.DocumentoSunatLineRepresentation;
import org.openfact.provider.ProviderEvent;
import org.w3c.dom.Document;

public interface PerceptionModel {

    String DOCUMENT_ID = "documentId";
    String ENTITY_NAME = "entityName";

    String getId();
    String getDocumentId();
    LocalDateTime getCreatedTimestamp();

    /**
     * Organization*/
    OrganizationModel getOrganization();

    /**
     * Document information*/
    String getSunatPerceptionSystemCode();
    void setSunatPerceptionSystemCode(String sunatPerceptionSystemCode);

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

    BigDecimal getSunatPerceptionPercent();
    void setSunatPerceptionPercent(BigDecimal sunatPerceptionPercent);

    String getNote();
    void setNote(String note);

    BigDecimal getTotalPerceptionAmount();
    void setTotalPerceptionAmount(BigDecimal totalPerceptionAmount);

    BigDecimal getTotalCashed();
    void setTotalCashed(BigDecimal totalCashed);

    LocalDateTime getIssueDateTime();
    void setIssueDateTime(LocalDateTime issueDateTime);

    PerceptionType getPerceptionType();

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
    interface PerceptionCreationEvent extends ProviderEvent {
        PerceptionModel getCreatedPerception();
    }

    interface PerceptionPostCreateEvent extends ProviderEvent {
        PerceptionModel getCreatedPerception();

        OpenfactSession getOpenfactSession();
    }

    interface PerceptionRemovedEvent extends ProviderEvent {
        OrganizationModel getOrganization();

        PerceptionModel getPerception();

        OpenfactSession getOpenfactSession();
    }

}
