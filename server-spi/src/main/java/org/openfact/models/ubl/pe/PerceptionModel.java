package org.openfact.models.ubl.pe;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.enums.RequeridActionDocument;
import org.openfact.models.ubl.common.InvoiceLineModel;
import org.openfact.models.ubl.common.PartyModel;
import org.openfact.models.ubl.common.SignatureModel;
import org.openfact.models.ubl.common.TaxTotalModel;
import org.openfact.models.ubl.common.UBLExtensionsModel;
import org.openfact.models.ubl.common.pe.PerceptionDocumentReferenceModel;
import org.openfact.provider.ProviderEvent;
import org.openfact.representations.idm.ubl.common.pe.PerceptionDocumentReferenceRepresentation;

public interface PerceptionModel {
	/**
	 * attributes
	 */
	void setSingleAttribute(String name, String value);

	void setAttribute(String name, List<String> values);

	void removeAttribute(String name);

	String getFirstAttribute(String name);

	List<String> getAttribute(String name);

	Map<String, List<String>> getAttributes();

	String getId();

	OrganizationModel getOrganization();

	void setOrganization(OrganizationModel organization);

	UBLExtensionsModel getUblExtensions();

	void setUblExtensions(UBLExtensionsModel ublExtensions);

	String getUblVersionID();

	void setUblVersionID(String ublVersionID);

	String getCustomizationID();

	void setCustomizationID(String customizationID);

	List<SignatureModel> getSignature();

	void setSignature(List<SignatureModel> signature);

	String getID();

	void setID(String iD);

	LocalDateTime getIssueDateTime();

	void setIssueDateTime(LocalDateTime issueDateTime);

	List<String> getNote();

	void setNote(List<String> note);

	String getSUNATPerceptionSystemCode();

	void setSUNATPerceptionSystemCode(String sUNATPerceptionSystemCode);

	BigDecimal getSUNATPerceptionPercent();

	void setSUNATPerceptionPercent(BigDecimal sUNATPerceptionPercent);

	PartyModel getAgentParty();

	void setAgentParty(PartyModel agentParty);

	PartyModel getReceiverParty();

	void setReceiverParty(PartyModel receiverParty);

	BigDecimal getTotalInvoiceAmount();

	void setTotalInvoiceAmount(BigDecimal totalInvoiceAmount);

	BigDecimal getSUNATTotalCashed();

	void setSUNATTotalCashed(BigDecimal sUNATTotalCashed);

	List<PerceptionDocumentReferenceModel> getSUNATPerceptionDocumentReference();

	void setSUNATPerceptionDocumentReference(List<PerceptionDocumentReferenceModel> sUNATPerceptionDocumentReference);

	Byte[] getXmlDocument();

	void setXmlDocument(Byte[] xmlDocument);

	List<RequeridActionDocument> getRequeridAction();

	void setRequeridAction(List<RequeridActionDocument> requeridAction);

	/**
	 * Other methods
	 */
	PerceptionDocumentReferenceModel addPerceptionDocumentReference();

	SignatureModel addSignature();

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
