package org.openfact.pe.models.jpa.ubl;

import javax.persistence.EntityManager;

import org.jboss.logging.Logger;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.jpa.JpaModel;
import org.openfact.models.jpa.OrganizationAdapter;
import org.openfact.models.jpa.SendEventAdapter;
import org.openfact.pe.models.SunatResponseModel;
import org.openfact.pe.models.jpa.entities.SunatResponseEntity;
import org.openfact.ubl.SendEventModel;

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
	public SendEventModel getSendEvent() {
		if (sunatResponse.getSendEvent() == null) {
			return null;
		}
		return new SendEventAdapter(session, organization, em, sunatResponse.getSendEvent());
	}

	@Override
	public void setSendEvent(SendEventModel sendEvent) {
		sunatResponse.setSendEvent(SendEventAdapter.toEntity(sendEvent, em));
	}

	@Override
	public OrganizationModel getOrganization() {
		if (sunatResponse.getOrganization() == null) {
			return null;
		}
		return new OrganizationAdapter(session, em, sunatResponse.getOrganization());
	}

	@Override
	public void setOrganization(OrganizationModel organization) {
		sunatResponse.setOrganization(OrganizationAdapter.toEntity(organization, em));
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
