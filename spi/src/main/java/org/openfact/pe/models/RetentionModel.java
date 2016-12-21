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

	String getOrganizationId();

	String getUblVersionID();

	void setUblVersionID(String ublVersionID);

	String getCustomizationID();

	void setCustomizationID(String customizationID);

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

	String getRetentionDocumentNumber();

	void setRetentionDocumentNumber(String retentionDocumentNumber);

	String getRetentionDocumentCurrency();

	void setRetentionDocumentCurrency(String retentionDocumentCurrency);

	BigDecimal getSunatRetentionPercent();

	void setSunatRetentionPercent(BigDecimal sunatRetentionPercent);

	String getNote();

	void setNote(String note);

	BigDecimal getTotalRetentionAmount();

	void setTotalRetentionAmount(BigDecimal totalRetentionAmount);

	BigDecimal getTotalCashed();

	void setTotalCashed(BigDecimal totalCashed);

	LocalDate getIssueDate();

	void setIssueDate(LocalDate issueDate);

	List<RetentionLineModel> getRetentionLines();

	RetentionLineModel addRetentionLine();
	

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
