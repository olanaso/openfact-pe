package org.openfact.models.jpa.pe.ubl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.jboss.logging.Logger;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.jpa.JpaModel;
import org.openfact.models.jpa.entities.ubl.CreditNoteSendEventEntity;
import org.openfact.models.jpa.pe.entities.PerceptionEntity;
import org.openfact.models.jpa.pe.entities.PerceptionSendEventEntity;
import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.PerceptionSendEventModel;

public class PerceptionSendEventAdapter implements PerceptionSendEventModel, JpaModel<PerceptionSendEventEntity> {
	protected static final Logger logger = Logger.getLogger(PerceptionSendEventAdapter.class);

	protected OrganizationModel organization;
	protected PerceptionSendEventEntity sendEvent;
	protected EntityManager em;
	protected OpenfactSession session;

	public PerceptionSendEventAdapter(OpenfactSession session, OrganizationModel organization, EntityManager em,
			PerceptionSendEventEntity sendEvent) {
		this.organization = organization;
		this.session = session;
		this.em = em;
		this.sendEvent = sendEvent;
	}

	@Override
	public PerceptionSendEventEntity getEntity() {
		return sendEvent;
	}

	@Override
	public String getId() {
		return sendEvent.getId();
	}

	@Override
	public String getDocumentId() {
		return sendEvent.getDocumentId();
	}

	@Override
	public boolean getAccepted() {
		return sendEvent.isAccepted();
	}

	@Override
	public String getDescription() {
		return sendEvent.getDescription();
	}

	@Override
	public String getNote() {
		return sendEvent.getNote();
	}

	@Override
	public String getResponseCode() {
		return sendEvent.getResponseCode();
	}

	@Override
	public String getErrorMessage() {
		return sendEvent.getErrorMessage();
	}

	@Override
	public String getDigestValue() {
		return sendEvent.getDigestValue();
	}

	@Override
	public String getBarCode() {
		return sendEvent.getBarCode();
	}

	@Override
	public byte[] getDocumentSubmitted() {
		return sendEvent.getDocumentSubmitted();
	}

	@Override
	public byte[] getDocumentResponse() {
		return sendEvent.getDocumentResponse();
	}

	@Override
	public byte[] getCustomerDocument() {
		return sendEvent.getCustomerDocument();
	}

	@Override
	public Map<String, String> getWarning() {
		return sendEvent.getWarning();
	}

	@Override
	public Map<String, String> getSuccess() {
		return sendEvent.getSuccess();
	}

	@Override
	public void setDocumentId(String documentId) {
		sendEvent.setDocumentId(documentId);
	}

	@Override
	public void setAccepted(boolean accepted) {
		sendEvent.setAccepted(accepted);
	}

	@Override
	public void setDescription(String description) {
		sendEvent.setDescription(description);
	}

	@Override
	public void setNote(String note) {
		sendEvent.setNote(note);
	}

	@Override
	public void setResponseCode(String responseCode) {
		sendEvent.setResponseCode(responseCode);
	}

	@Override
	public void setErrorMessage(String errorMessage) {
		sendEvent.setErrorMessage(errorMessage);
	}

	@Override
	public void setDigestValue(String digestValue) {
		sendEvent.setDigestValue(digestValue);
	}

	@Override
	public void setBarCode(String barCode) {
		sendEvent.setBarCode(barCode);
	}

	@Override
	public void setDocumentSubmitted(byte[] documentSubmitted) {
		sendEvent.setDocumentSubmitted(documentSubmitted);
	}

	@Override
	public void setDocumentResponse(byte[] documentResponse) {
		sendEvent.setDocumentResponse(documentResponse);
	}

	@Override
	public void setCustomerDocument(byte[] customerDocument) {
		sendEvent.setCustomerDocument(customerDocument);
	}

	@Override
	public void setWarning(Map<String, String> warning) {
		sendEvent.setWarning(warning);
	}

	@Override
	public void setSuccess(Map<String, String> success) {
		sendEvent.setSuccess(success);
	}

	@Override
	public List<PerceptionModel> getPerceptions() {
		return sendEvent.getPerceptions().stream().map(f -> new PerceptionAdapter(session, organization, em, f))
				.collect(Collectors.toList());
	}

	@Override
	public void setPerceptions(List<PerceptionModel> perceptions) {
		List<PerceptionEntity> entities = perceptions.stream().map(f -> PerceptionAdapter.toEntity(f, em))
				.collect(Collectors.toList());
		sendEvent.setPerceptions(entities);

	}

}
