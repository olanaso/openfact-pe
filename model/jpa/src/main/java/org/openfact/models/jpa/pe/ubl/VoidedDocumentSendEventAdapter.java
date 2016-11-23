package org.openfact.models.jpa.pe.ubl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.jboss.logging.Logger;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.jpa.JpaModel;
import org.openfact.models.jpa.pe.entities.PerceptionSendEventEntity;
import org.openfact.models.jpa.pe.entities.VoidedDocumentsSendEventEntity;
import org.openfact.models.jpa.ubl.CreditNoteAdapter;
import org.openfact.pe.models.PerceptionSendEventModel;
import org.openfact.pe.models.VoidedDocumentModel;
import org.openfact.pe.models.VoidedDocumentProvider;
import org.openfact.pe.models.VoidedDocumentsSendEventModel;

public class VoidedDocumentSendEventAdapter
		implements VoidedDocumentsSendEventModel, JpaModel<VoidedDocumentsSendEventEntity> {
	protected static final Logger logger = Logger.getLogger(VoidedDocumentSendEventAdapter.class);

	protected OrganizationModel organization;
	protected VoidedDocumentsSendEventEntity sendEvent;
	protected EntityManager em;
	protected OpenfactSession session;

	public VoidedDocumentSendEventAdapter(OpenfactSession session, OrganizationModel organization, EntityManager em,
			VoidedDocumentsSendEventEntity sendEvent) {
		this.organization = organization;
		this.session = session;
		this.em = em;
		this.sendEvent = sendEvent;
	}

	@Override
	public String getId() {
		return sendEvent.getId();
	}

	@Override
	public void setId(String id) {
		sendEvent.setId(id);
	}

	@Override
	public String getID() {
		return sendEvent.getID();
	}

	@Override
	public void setID(String ID) {
		sendEvent.setID(ID);
	}

	@Override
	public boolean isAccepted() {
		return sendEvent.isAccepted();
	}

	@Override
	public void setAccepted(boolean accepted) {
		sendEvent.setAccepted(accepted);
	}

	@Override
	public String getDescription() {
		return sendEvent.getDescription();
	}

	@Override
	public void setDescription(String description) {
		sendEvent.setDescription(description);
	}

	@Override
	public String getNote() {
		return sendEvent.getNote();
	}

	@Override
	public void setNote(String note) {
		sendEvent.setNote(note);
	}

	@Override
	public String getResponseCode() {
		return sendEvent.getResponseCode();
	}

	@Override
	public void setResponseCode(String responseCode) {
		sendEvent.setResponseCode(responseCode);
	}

	@Override
	public String getError() {
		return sendEvent.getError();
	}

	@Override
	public void setError(String error) {
		sendEvent.setError(error);
	}

	@Override
	public String getDigestValue() {
		return sendEvent.getDigestValue();
	}

	@Override
	public void setDigestValue(String digestValue) {
		sendEvent.setDigestValue(digestValue);
	}

	@Override
	public String getbarCode() {
		return sendEvent.getBarCode();
	}

	@Override
	public void setbarCode(String barCode) {
		sendEvent.setBarCode(barCode);
	}

	@Override
	public byte[] getXmlDoument() {
		return sendEvent.getXmlDoument();
	}

	@Override
	public void setXmlDocument(byte[] xmlDocument) {
		sendEvent.setXmlDoument(xmlDocument);
	}

	@Override
	public byte[] getDocumentResponse() {
		return sendEvent.getDocumentResponse();
	}

	@Override
	public void setDocumentResponse(byte[] documentResponse) {
		sendEvent.setDocumentResponse(documentResponse);
	}

	@Override
	public byte[] getCustomerDoument() {
		return sendEvent.getCustomerDoument();
	}

	@Override
	public void setCustomerDoument(byte[] customerDoument) {
		sendEvent.setCustomerDoument(customerDoument);
	}

	@Override
	public Map<String, String> getSendWarning() {
		return sendEvent.getSendWarning();
	}

	@Override
	public void setSendWarning(Map<String, String> sendWarning) {
		sendEvent.setSendWarning(sendWarning);
	}

	@Override
	public VoidedDocumentsSendEventEntity getEntity() {
		return sendEvent;
	}

	@Override
	public List<VoidedDocumentModel> getSummaryDocuments() {
		return null;
	}

	@Override
	public void setSummaryDocuments(List<VoidedDocumentModel> summaryDocuments) {
		// TODO Auto-generated method stub

	}
}
