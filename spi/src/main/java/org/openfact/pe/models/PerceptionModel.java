package org.openfact.pe.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.openfact.models.OpenfactSession;
import org.openfact.models.PartyModel;
import org.openfact.models.enums.RequiredAction;
import org.openfact.pe.representations.idm.DocumentoSunatLineRepresentation;
import org.openfact.provider.ProviderEvent;
import org.openfact.ubl.SendEventModel;

public interface PerceptionModel {

    String getId();

    String getOrganizationId();

    String getDocumentId();

    void setDocumentId(String documentId);

    String getUblVersionID();

    void setUblVersionID(String ublVersionID);

    String getCustomizationID();

    void setCustomizationID(String customizationID);

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

    List<PerceptionLineModel> getPerceptionLines();

    PerceptionLineModel addPerceptionLine();


    /**
     * Xml
     **/

    byte[] getXmlDocument();

    void setXmlDocument(byte[] object);

    /**
     * Send events
     */
    List<SendEventModel> getSendEvents();

    /**
     * Required Actions
     */
    Set<String> getRequiredActions();

    void addRequiredAction(String action);

    void removeRequiredAction(String action);

    void addRequiredAction(RequiredAction action);

    void removeRequiredAction(RequiredAction action);

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
        PerceptionModel getPerception();

        OpenfactSession getOpenfactSession();
    }

}
