package org.openfact.pe.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.openfact.models.CreditNoteModel;
import org.openfact.models.OpenfactSession;
import org.openfact.models.PartyModel;
import org.openfact.models.enums.RequiredAction;
import org.openfact.provider.ProviderEvent;
import org.openfact.ubl.SendEventModel;

public interface RetentionModel {

	String getId();

	String getDocumentId();

	void setDocumentId(String documentId);

	String getOrganizationId();

	String getUblVersionId();

	void setUblVersionId(String ublVersionId);

	String getCustomizationId();

	void setCustomizationId(String customizationId);

	String getDocumentCurrencyCode();

	void setDocumentCurrencyCode(String value);

	LocalDate getIssueDate();

	void setIssueDate(LocalDate issueDate);

	String getSunatRetentionSystemCode();

	void setSunatRetentionSystemCode(String code);

	BigDecimal getSunatRetentionPercent();

	void setSunatRetentionPercent(BigDecimal percent);

	BigDecimal getTotalInvoiceAmount();

	void setTotalInvoiceAmount(BigDecimal totalInvoiceAmount);

	BigDecimal getSunatTotalPaid();

	void setSunatTotalPaid(BigDecimal totalPaid);

	PartyModel getAgentParty();

	PartyModel getAgentPartyAsNotNull();

	PartyModel getReceiverParty();

	PartyModel getReceiverPartyAsNotNull();

	List<String> getNotes();

	void setNotes(List<String> notes);

	List<RetentionDocumentReferenceModel> getSunatRetentionDocumentReference();

	RetentionDocumentReferenceModel addSunatRetentionDocumentReference();

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
	interface RetentionCreationEvent extends ProviderEvent {
		RetentionModel getCreatedRetention();
	}

	interface RetentionPostCreateEvent extends ProviderEvent {
		RetentionModel getCreatedRetention();

		OpenfactSession getOpenfactSession();
	}

	interface RetentionRemovedEvent extends ProviderEvent {
		RetentionModel getRetention();

		OpenfactSession getOpenfactSession();
	}
}
