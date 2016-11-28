package org.openfact.pe.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.openfact.models.PartyModel;
import org.openfact.models.enums.RequiredAction;

public interface PerceptionModel {
     
	String getId();

    String getDocumentId();

    String getOrganizationId();

    void setDocumentId(String documentId);

    String getUblVersionID();

    void setUblVersionID(String ublVersionID);

    String getCustomizationID();

    void setCustomizationID(String customizationID);

    String getDocumentCurrencyCode();

    void setDocumentCurrencyCode(String value);

    LocalDate getIssueDate();

    void setIssueDate(LocalDate issueDate);

    String getSUNATPerceptionSystemCode();

    void setSUNATPerceptionSystemCode(String sUNATPerceptionSystemCode);

    BigDecimal getSUNATPerceptionPercent();

    void setSUNATPerceptionPercent(BigDecimal sUNATPerceptionPercent);

    BigDecimal getTotalInvoiceAmount();

    void setTotalInvoiceAmount(BigDecimal totalInvoiceAmount);

    BigDecimal getSUNATTotalCashed();

    void setSUNATTotalCashed(BigDecimal sUNATTotalCashed);

    PartyModel getAgentParty();

    PartyModel getAgentPartyAsNotNull();

    PartyModel getReceiverParty();

    PartyModel getReceiverPartyAsNotNull();

    List<String> getNotes();

    void setNotes(List<String> notes);

    List<PerceptionDocumentReferenceModel> getSunatPerceptionDocumentReference();

    PerceptionDocumentReferenceModel addSunatPerceptionDocumentReference();

    /**
     * Xml
     **/

    byte[] getXmlDocument();

    void setXmlDocument(byte[] object);

    /**
     * Required Actions
     */
    Set<String> getRequiredActions();

    void addRequiredAction(String action);

    void removeRequiredAction(String action);

    void addRequiredAction(RequiredAction action);

    void removeRequiredAction(RequiredAction action);

}
