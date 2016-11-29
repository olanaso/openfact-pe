package org.openfact.models.jpa.pe.ubl;

import javax.persistence.EntityManager;

import org.jboss.logging.Logger;
import org.openfact.models.CreditNoteSendEventModel;
import org.openfact.models.DebitNoteSendEventModel;
import org.openfact.models.InvoiceSendEventModel;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.jpa.CreditNoteSendEventAdapter;
import org.openfact.models.jpa.DebitNoteSendEventAdapter;
import org.openfact.models.jpa.InvoiceSendEventAdapter;
import org.openfact.models.jpa.JpaModel;
import org.openfact.models.jpa.pe.entities.SunatResponseEntity;
import org.openfact.pe.models.PerceptionSendEventModel;
import org.openfact.pe.models.RetentionSendEventModel;
import org.openfact.pe.models.SummaryDocumentsSendEventModel;
import org.openfact.pe.models.SunatResponseModel;
import org.openfact.pe.models.VoidedDocumentsSendEventModel;

public class SunatResponseAdapter implements SunatResponseModel, JpaModel<SunatResponseEntity> {
	protected static final Logger logger = Logger.getLogger(SunatResponseAdapter.class);

	protected OrganizationModel organization;
	protected SunatResponseEntity sunatResponse;
	protected EntityManager em;
	protected OpenfactSession session;

	public SunatResponseAdapter(OpenfactSession session, OrganizationModel organization, EntityManager em,
			SunatResponseEntity sunatResponse) {
		this.organization = organization;
		this.session = session;
		this.em = em;
		this.sunatResponse = sunatResponse;
	}

	@Override
	public String getId() {
		return sunatResponse.getId();
	}

	@Override
	public SunatResponseEntity getEntity() {
		return sunatResponse;
	}

	@Override
	public InvoiceSendEventModel getInvoiceSendEvent() {
		if (sunatResponse.getInvoiceSendEvent() == null) {
			return null;
		}
		return new InvoiceSendEventAdapter(session, organization, em, sunatResponse.getInvoiceSendEvent());
	}

	@Override
	public CreditNoteSendEventModel getCreditNoteSendEvent() {
		if (sunatResponse.getCreditNoteSendEvent() == null) {
			return null;
		}
		return new CreditNoteSendEventAdapter(session, organization, em, sunatResponse.getCreditNoteSendEvent());
	}

	@Override
	public DebitNoteSendEventModel getDebitNoteSendEvent() {
		if (sunatResponse.getDebitNoteSendEvent() == null) {
			return null;
		}
		return new DebitNoteSendEventAdapter(session, organization, em, sunatResponse.getDebitNoteSendEvent());
	}

	@Override
	public PerceptionSendEventModel getPerceptionSendEvent() {
		if (sunatResponse.getPerceptionSendEvent() == null) {
			return null;
		}
		return new PerceptionSendEventAdapter(session, organization, em, sunatResponse.getPerceptionSendEvent());
	}

	@Override
	public RetentionSendEventModel getRetentionSendEvent() {
		if (sunatResponse.getRetentionSendEvent() == null) {
			return null;
		}
		return new RetentionSendEventAdapter(session, organization, em, sunatResponse.getRetentionSendEvent());
	}

	@Override
	public SummaryDocumentsSendEventModel getSummaryDocumentsSendEvent() {
		if (sunatResponse.getSummaryDocumentsSendEvent() == null) {
			return null;
		}
		return new SummaryDocumentSendEventAdapter(session, organization, em,
				sunatResponse.getSummaryDocumentsSendEvent());
	}

	@Override
	public VoidedDocumentsSendEventModel getVoidedDocumentsSendEvent() {
		if (sunatResponse.getVoidedDocumentsSendEvent() == null) {
			return null;
		}
		return new VoidedDocumentSendEventAdapter(session, organization, em,
				sunatResponse.getVoidedDocumentsSendEvent());
	}

	@Override
	public void setInvoiceSendEvent(InvoiceSendEventModel invoiceSendEvent) {
		sunatResponse.setInvoiceSendEvent(InvoiceSendEventAdapter.toEntity(invoiceSendEvent, em));
	}

	@Override
	public void setCreditNoteSendEvent(CreditNoteSendEventModel creditNoteSendEvent) {
		sunatResponse.setCreditNoteSendEvent(CreditNoteSendEventAdapter.toEntity(creditNoteSendEvent, em));
	}

	@Override
	public void setDebitNoteSendEvent(DebitNoteSendEventModel debitNoteSendEvent) {
		sunatResponse.setDebitNoteSendEvent(DebitNoteSendEventAdapter.toEntity(debitNoteSendEvent, em));
	}

	@Override
	public void setPerceptionSendEvent(PerceptionSendEventModel perceptionSendEvent) {
		sunatResponse.setPerceptionSendEvent(PerceptionSendEventAdapter.toEntity(perceptionSendEvent, em));
	}

	@Override
	public void setRetentionSendEvent(RetentionSendEventModel retentionSendEvent) {
		sunatResponse.setRetentionSendEvent(RetentionSendEventAdapter.toEntity(retentionSendEvent, em));
	}

	@Override
	public void setSummaryDocumentsSendEvent(SummaryDocumentsSendEventModel summaryDocumentsSendEvent) {
		sunatResponse
				.setSummaryDocumentsSendEvent(SummaryDocumentSendEventAdapter.toEntity(summaryDocumentsSendEvent, em));
	}

	@Override
	public void setVoidedDocumentsSendEvent(VoidedDocumentsSendEventModel voidedDocumentsSendEvent) {
		sunatResponse
				.setVoidedDocumentsSendEvent(VoidedDocumentSendEventAdapter.toEntity(voidedDocumentsSendEvent, em));
	}

	@Override
	public byte[] getDocumentResponse() {
		return sunatResponse.getDocumentResponse();
	}

	@Override
	public void setDocumentResponse(byte[] documentResponse) {
		sunatResponse.setDocumentResponse(documentResponse);
	}

	@Override
	public String getResponseCode() {
		return sunatResponse.getResponseCode();
	}

	@Override
	public void setResponseCode(String responseCode) {
		sunatResponse.setResponseCode(responseCode);
	}

	@Override
	public String getErrorMessage() {
		return sunatResponse.getErrorMessage();
	}

	@Override
	public void setErrorMessage(String errorMenssage) {
		sunatResponse.setErrorMessage(errorMenssage);
	}

	@Override
	public String getTicket() {
		return sunatResponse.getTicked();
	}

	@Override
	public void setTicket(String ticket) {
		sunatResponse.setTicked(ticket);
	}

	public static SunatResponseEntity toEntity(SunatResponseModel model, EntityManager em) {
		if (model instanceof SunatResponseAdapter) {
			return ((SunatResponseAdapter) model).getEntity();
		}
		return em.getReference(SunatResponseEntity.class, model.getId());
	}

	@Override
	public boolean getResult() {
		return sunatResponse.isResult();
	}

	@Override
	public void setResult(boolean result) {
		sunatResponse.setResult(result);
	}
}
