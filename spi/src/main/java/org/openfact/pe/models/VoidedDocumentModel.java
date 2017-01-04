package org.openfact.pe.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.openfact.models.OpenfactSession;
import org.openfact.models.enums.RequiredAction;
import org.openfact.provider.ProviderEvent;
import org.openfact.ubl.SendEventModel;

public interface VoidedDocumentModel {

    String getId();

    String getDocumentId();

    void setDocumentId(String documentId);

    String getOrganizationId();

    String getUblVersionID();

    void setUblVersionID(String ublVersionID);

    String getCustomizationID();

    void setCustomizationID(String customizationID);

    LocalDateTime getIssueDateTime();

    void setIssueDateTime(LocalDateTime issueDateTime);

   List<VoidedDocumentLineModel> getVoidedDocumentLines();

    VoidedDocumentLineModel addVoidedDocumentLines();

    /**
     * Xml
     */
    byte[] getXmlDocument();

    void setXmlDocument(byte[] bytes);

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
    interface VoidedDocumentCreationEvent extends ProviderEvent {
        VoidedDocumentModel getCreatedVoidedDocument();
    }

    interface VoidedDocumentPostCreateEvent extends ProviderEvent {
        VoidedDocumentModel getCreatedVoidedDocument();

        OpenfactSession getOpenfactSession();
    }

    interface VoidedDocumentRemovedEvent extends ProviderEvent {
        VoidedDocumentModel getVoidedDocument();

        OpenfactSession getOpenfactSession();
    }
}
