package org.openfact.pe.models.jpa.ubl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.enums.SendResultType;
import org.openfact.models.jpa.AbstractHibernateStorage;
import org.openfact.models.jpa.OrganizationAdapter;
import org.openfact.models.jpa.SendEventAdapter;
import org.openfact.pe.constants.CodigoTipoDocumento;
import org.openfact.pe.models.SunatResponseModel;
import org.openfact.pe.models.SunatResponseProvider;
import org.openfact.pe.models.jpa.entities.SunatResponseEntity;
import org.openfact.ubl.SendEventModel;

public class JpaSunatResponseProvider extends AbstractHibernateStorage implements SunatResponseProvider {

	private OpenfactSession session;
	protected EntityManager em;

	public JpaSunatResponseProvider(OpenfactSession session, EntityManager em) {
		this.session = session;
		this.em = em;
	}

	@Override
	public void close() {
	}

	@Override
	public SunatResponseModel addSunatResponse(OrganizationModel organization, SendResultType type,
			SendEventModel sendEvent) {
		SunatResponseEntity response = new SunatResponseEntity();
		buildResponse(response, organization, type);
		response.setSendEvent(SendEventAdapter.toEntity(sendEvent, em));
		em.persist(response);
		em.flush();
		return new SunatResponseAdapter(session, organization, em, response);
	}

	private void buildResponse(SunatResponseEntity response, OrganizationModel organization, SendResultType type) {
		response.setCreatedTimestamp(LocalDateTime.now());
		response.setResult(type.equals(SendResultType.SUCCESS));
		response.setOrganization(OrganizationAdapter.toEntity(organization, em));
	}

	@Override
	public SunatResponseModel getSunatResponseById(OrganizationModel organization, String id) {
		TypedQuery<SunatResponseEntity> query = em.createNamedQuery("getOrganizationSunatResponseById",
				SunatResponseEntity.class);
		query.setParameter("id", id);
		query.setParameter("organizationId", organization.getId());
		List<SunatResponseEntity> entities = query.getResultList();
		if (entities.size() == 0)
			return null;
		return new SunatResponseAdapter(session, organization, em, entities.get(0));
	}

	@Override
	public boolean removeSunatResponse(OrganizationModel organization, String id) {
		return removeSunatResponse(organization, getSunatResponseById(organization, id));
	}

	@Override
	public boolean removeSunatResponse(OrganizationModel organization, SunatResponseModel sunatResponse) {
		SunatResponseEntity sunatResponseEntity = em.find(SunatResponseEntity.class, sunatResponse.getId());
		if (sunatResponseEntity == null)
			return false;

		em.remove(sunatResponseEntity);
		em.flush();
		return true;
	}
	@Override
	public SunatResponseModel getSunatResponseByDocument(OrganizationModel organization,
														 CodigoTipoDocumento documentType, String id) {
		String queryName = "";
		switch (documentType) {
			case FACTURA:
				queryName = "getCDRInvoice";
				break;
			case NOTA_CREDITO:
				queryName = "getCDRCreditNote";
				break;
			case NOTA_DEBITO:
				queryName = "getCDRDebitNote";
				break;
			case PERCEPCION:
				queryName = "getCDRPerception";
				break;
			case RETENCION:
				queryName = "getCDRRetention";
				break;
			case RESUMEN_DIARIO:
				queryName = "getTicketSummary";
				break;
			case BAJA:
				queryName = "getTicketVoided";
				break;
			default:
				break;
		}
		TypedQuery<SunatResponseEntity> query = em.createNamedQuery(queryName, SunatResponseEntity.class);
		query.setParameter("Id", id);
		List<SunatResponseEntity> entities = query.getResultList();
		if (entities.size() == 0)
			return null;
		return new SunatResponseAdapter(session, organization, em, entities.get(0));
	}

	@Override
	public List<SunatResponseModel> getSunatResponsesByDocument(OrganizationModel organization,
																CodigoTipoDocumento documentType, String id) {
		String queryName = "";
		switch (documentType) {
			case FACTURA:
				queryName = "getAllSunatResponseEventsByInvoice";
				break;
			case NOTA_CREDITO:
				queryName = "getAllSunatResponseEventsByCreditNote";
				break;
			case NOTA_DEBITO:
				queryName = "getAllSunatResponseEventsByDebitNote";
				break;
			case PERCEPCION:
				queryName = "getAllSunatResponseEventsByPerception";
				break;
			case RETENCION:
				queryName = "getAllSunatResponseEventsByRetention";
				break;
			case RESUMEN_DIARIO:
				queryName = "getAllSunatResponseEventsBySummary";
				break;
			case BAJA:
				queryName = "getAllSunatResponseEventsByVoided";
				break;
			default:
				break;
		}
		TypedQuery<SunatResponseEntity> query = em.createNamedQuery(queryName, SunatResponseEntity.class);
		query.setParameter("Id", id);
		List<SunatResponseEntity> results = query.getResultList();
		List<SunatResponseModel> sunatResponses = results.stream()
				.map(f -> new SunatResponseAdapter(session, organization, em, f)).collect(Collectors.toList());
		return sunatResponses;
	}
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}	
}
