package org.openfact.pe.models;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.openfact.models.OpenfactSession;
import org.openfact.models.SupplierPartyModel;
import org.openfact.models.enums.RequiredAction;
import org.openfact.provider.ProviderEvent;

public interface SummaryDocumentModel {

    String getId();

    String getDocumentId();

    String getOrganizationId();

    String getDocumentCurrencyCode();

    void setDocumentCurrencyCode(String value);

    String getUblVersionId();

    void setUblVersionId(String ublVersionId);

    String getCustomizationI();

    void setCustomizationId(String customizationId);

    LocalDate getReferenceDate();

    void setReferenceDate(LocalDate referenceDate);

    LocalDate getIssueDate();

    void setIssueDateTime(LocalDate issueDate);

    SupplierPartyModel getAccountingSupplierParty();

    SupplierPartyModel getAccountingSupplierPartyAsNotNull();

    List<SummaryDocumentsLineModel> getSummaryDocumentsLines();

    SummaryDocumentsLineModel addSummaryDocumentsLines();

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
    interface SummaryDocumentCreationEvent extends ProviderEvent {
        SummaryDocumentModel getCreatedSummaryDocument();
    }

    interface SummaryDocumentPostCreateEvent extends ProviderEvent {
        SummaryDocumentModel getCreatedSummaryDocument();

        OpenfactSession getOpenfactSession();
    }

    interface SummaryDocumentRemovedEvent extends ProviderEvent {
        SummaryDocumentModel getSummaryDocument();

        OpenfactSession getOpenfactSession();
    }
}
