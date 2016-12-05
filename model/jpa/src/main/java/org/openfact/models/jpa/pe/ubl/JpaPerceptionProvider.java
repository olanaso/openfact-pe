package org.openfact.models.jpa.pe.ubl;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;

import org.openfact.models.InvoiceModel;
import org.openfact.models.InvoiceProvider;
import org.openfact.models.ModelDuplicateException;
import org.openfact.models.ModelException;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.ScrollModel;
import org.openfact.models.enums.RequiredAction;
import org.openfact.models.jpa.AbstractHibernateStorage;
import org.openfact.models.jpa.InvoiceAdapter;
import org.openfact.models.jpa.OrganizationAdapter;
import org.openfact.models.jpa.entities.InvoiceEntity;
import org.openfact.models.jpa.pe.entities.PerceptionEntity;
import org.openfact.models.search.SearchCriteriaModel;
import org.openfact.models.search.SearchResultsModel;
import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.PerceptionProvider;

public class JpaPerceptionProvider extends AbstractHibernateStorage implements PerceptionProvider {

	private OpenfactSession session;
	protected EntityManager em;

	public JpaPerceptionProvider(OpenfactSession session, EntityManager em) {
		this.session = session;
		this.em = em;
	}

	@Override
	public void close() {

	}

	private PerceptionProvider getPerceptions() {
		PerceptionProvider cache = session.getProvider(PerceptionProvider.class);
		if (cache != null) {
			return cache;
		} else {
			return session.getProvider(PerceptionProvider.class);
		}
	}

	@Override
	public PerceptionModel addPerception(OrganizationModel organization, String documentId) {
		if (documentId == null) {
			throw new ModelException("Invalid documentId, Null value");
		}

		if (getPerceptions().getPerceptionByID(organization, documentId) != null) {
			throw new ModelDuplicateException("Invoice documentId existed");
		}

		PerceptionEntity perception = new PerceptionEntity();
		perception.setDocumentId(documentId);
		perception.setOrganization(OrganizationAdapter.toEntity(organization, em));
		em.persist(perception);
		em.flush();

		final InvoiceModel adapter = new InvoiceAdapter(session, organization, em, perception);
		session.getOpenfactSessionFactory().publish(new InvoiceModel.InvoiceCreationEvent() {
			@Override
			public InvoiceModel getCreatedInvoice() {
				return adapter;
			}
		});

		return adapter;
	}

	@Override
	public PerceptionModel getPerceptionById(OrganizationModel organization, String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PerceptionModel getPerceptionByID(OrganizationModel organization, String ID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removePerception(OrganizationModel organization, String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removePerception(OrganizationModel organization, PerceptionModel perception) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getPerceptionsCount(OrganizationModel organization) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<PerceptionModel> getPerceptions(OrganizationModel organization) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PerceptionModel> getPerceptions(OrganizationModel organization, List<RequiredAction> requeridAction,
			boolean intoRequeridAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PerceptionModel> getPerceptions(OrganizationModel organization, Integer firstResult,
			Integer maxResults) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PerceptionModel> searchForPerception(OrganizationModel organization, String filterText) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PerceptionModel> searchForPerception(OrganizationModel organization, String filterText,
			Integer firstResult, Integer maxResults) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchResultsModel<PerceptionModel> searchForPerception(OrganizationModel organization,
			SearchCriteriaModel criteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchResultsModel<PerceptionModel> searchForPerception(OrganizationModel organization,
			SearchCriteriaModel criteria, String filterText) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScrollModel<PerceptionModel> getPerceptionsScroll(OrganizationModel organization) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScrollModel<PerceptionModel> getPerceptionsScroll(OrganizationModel organization, boolean asc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScrollModel<PerceptionModel> getPerceptionsScroll(OrganizationModel organization, boolean asc,
			int scrollSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScrollModel<PerceptionModel> getPerceptionsScroll(OrganizationModel organization, boolean asc,
			int scrollSize, int fetchSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected EntityManager getEntityManager() {
		// TODO Auto-generated method stub
		return null;
	}

}
