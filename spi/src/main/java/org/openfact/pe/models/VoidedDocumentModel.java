package org.openfact.pe.models;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.openfact.models.OpenfactSession;
import org.openfact.models.SupplierPartyModel;
import org.openfact.models.enums.RequiredAction;
import org.openfact.provider.ProviderEvent;

public interface VoidedDocumentModel {

    String getId();

    String getDocumentId();

    String getOrganizationId();

    String getDocumentCurrencyCode();

    void setDocumentCurrencyCode(String value);

    String getUblVersionId();

    void setUblVersionId(String ublVersionId);

    String getCustomizationId();

    void setCustomizationId(String customizationId);

    LocalDate getReferenceDate();

    void setReferenceDate(LocalDate referenceDate);

    LocalDate getIssueDate();

    void setIssueDate(LocalDate issueDate);

    SupplierPartyModel getAccountingSupplierParty();

    SupplierPartyModel addAccountingSupplierParty();

    List<String> getNotes();

    void setNote(List<String> notes);

    List<VoidedDocumentsLineModel> getVoidedDocumentsLine();

    VoidedDocumentsLineModel addVoidedDocumentsLine();

    /**
     * Xml
     */
    byte[] getXmlDocument();

    void setXmlDocument(byte[] bytes);

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
