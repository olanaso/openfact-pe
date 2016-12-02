package org.openfact.models.jpa.pe.ubl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openfact.models.CreditNoteModel;
import org.openfact.models.CreditNoteSendEventModel;
import org.openfact.models.DebitNoteModel;
import org.openfact.models.DebitNoteSendEventModel;
import org.openfact.models.InvoiceModel;
import org.openfact.models.InvoiceSendEventModel;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.ScrollModel;
import org.openfact.models.enums.SendResultType;
import org.openfact.models.jpa.AbstractHibernateStorage;
import org.openfact.models.jpa.CreditNoteSendEventAdapter;
import org.openfact.models.jpa.DebitNoteSendEventAdapter;
import org.openfact.models.jpa.InvoiceSendEventAdapter;
import org.openfact.models.jpa.JpaScrollAdapter;
import org.openfact.models.jpa.pe.entities.SunatResponseEntity;
import org.openfact.models.search.SearchCriteriaFilterOperator;
import org.openfact.models.search.SearchCriteriaModel;
import org.openfact.models.search.SearchResultsModel;
import org.openfact.pe.constants.CodigoTipoDocumento;
import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.PerceptionSendEventModel;
import org.openfact.pe.models.RetentionModel;
import org.openfact.pe.models.RetentionSendEventModel;
import org.openfact.pe.models.SummaryDocumentModel;
import org.openfact.pe.models.SummaryDocumentsSendEventModel;
import org.openfact.pe.models.SunatResponseModel;
import org.openfact.pe.models.SunatResponseProvider;
import org.openfact.pe.models.VoidedDocumentModel;
import org.openfact.pe.models.VoidedDocumentsSendEventModel;

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
			InvoiceSendEventModel invoiceSendEvent) {
		SunatResponseEntity response = new SunatResponseEntity();
		buildResponse(response, organization, type);
		response.setInvoiceSendEvent(InvoiceSendEventAdapter.toEntity(invoiceSendEvent, em));
		em.persist(response);
		em.flush();
		return new SunatResponseAdapter(session, organization, em, response);
	}

	@Override
	public SunatResponseModel addSunatResponse(OrganizationModel organization, SendResultType type,
			CreditNoteSendEventModel creditNoteSendEvent) {
		SunatResponseEntity response = new SunatResponseEntity();
		buildResponse(response, organization, type);
		response.setCreditNoteSendEvent(CreditNoteSendEventAdapter.toEntity(creditNoteSendEvent, em));
		em.persist(response);
		em.flush();
		return new SunatResponseAdapter(session, organization, em, response);
	}

	@Override
	public SunatResponseModel addSunatResponse(OrganizationModel organization, SendResultType type,
			DebitNoteSendEventModel debitNoteSendEvent) {
		SunatResponseEntity response = new SunatResponseEntity();
		buildResponse(response, organization, type);
		response.setDebitNoteSendEvent(DebitNoteSendEventAdapter.toEntity(debitNoteSendEvent, em));
		em.persist(response);
		em.flush();
		return new SunatResponseAdapter(session, organization, em, response);
	}

	@Override
	public SunatResponseModel addSunatResponse(OrganizationModel organization, SendResultType type,
			PerceptionSendEventModel perceptionSendEvent) {
		SunatResponseEntity response = new SunatResponseEntity();
		buildResponse(response, organization, type);
		response.setPerceptionSendEvent(PerceptionSendEventAdapter.toEntity(perceptionSendEvent, em));
		em.persist(response);
		em.flush();
		return new SunatResponseAdapter(session, organization, em, response);
	}

	@Override
	public SunatResponseModel addSunatResponse(OrganizationModel organization, SendResultType type,
			RetentionSendEventModel retentionSendEvent) {
		SunatResponseEntity response = new SunatResponseEntity();
		buildResponse(response, organization, type);
		response.setRetentionSendEvent(RetentionSendEventAdapter.toEntity(retentionSendEvent, em));
		em.persist(response);
		em.flush();
		return new SunatResponseAdapter(session, organization, em, response);
	}

	@Override
	public SunatResponseModel addSunatResponse(OrganizationModel organization, SendResultType type,
			SummaryDocumentsSendEventModel summaryDocumentsSendEvent) {
		SunatResponseEntity response = new SunatResponseEntity();
		buildResponse(response, organization, type);
		response.setSummaryDocumentsSendEvent(SummaryDocumentSendEventAdapter.toEntity(summaryDocumentsSendEvent, em));
		em.persist(response);
		em.flush();
		return new SunatResponseAdapter(session, organization, em, response);
	}

	@Override
	public SunatResponseModel addSunatResponse(OrganizationModel organization, SendResultType type,
			VoidedDocumentsSendEventModel voidedDocumentsSendEvent) {
		SunatResponseEntity response = new SunatResponseEntity();
		buildResponse(response, organization, type);
		response.setVoidedDocumentsSendEvent(VoidedDocumentSendEventAdapter.toEntity(voidedDocumentsSendEvent, em));
		em.persist(response);
		em.flush();
		return new SunatResponseAdapter(session, organization, em, response);
	}

	private void buildResponse(SunatResponseEntity response, OrganizationModel organization, SendResultType type) {
		response.setCreatedTimestamp(LocalDateTime.now());
		response.setResult(type.equals(SendResultType.SUCCESS));
		// response.setOrganization(OrganizationAdapter.toEntity(organization,
		// em));
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
	protected EntityManager getEntityManager() {
		return em;
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

}
