package org.openfact.models.jpa.pe.ubl;

import java.util.List;

import javax.persistence.EntityManager;

import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.ScrollModel;
import org.openfact.models.enums.RequiredAction;
import org.openfact.models.jpa.AbstractHibernateStorage;
import org.openfact.models.search.SearchCriteriaModel;
import org.openfact.models.search.SearchResultsModel;
import org.openfact.pe.models.RetentionModel;
import org.openfact.pe.models.RetentionProvider;
import org.openfact.pe.models.RetentionProviderFactory;

public class JpaRetentionProvider extends AbstractHibernateStorage implements  RetentionProvider{

	private OpenfactSession session;
	protected EntityManager em;

	public JpaRetentionProvider(OpenfactSession session, EntityManager em) {
		this.session = session;
		this.em = em;
	}
	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public RetentionModel addRetention(OrganizationModel organization, String ID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RetentionModel getRetentionById(OrganizationModel organization, String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RetentionModel getRetentionByID(OrganizationModel organization, String ID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeRetention(OrganizationModel organization, String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeRetention(OrganizationModel organization, RetentionModel retention) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getRetentionsCount(OrganizationModel organization) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<RetentionModel> getRetentions(OrganizationModel organization) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RetentionModel> getRetentions(OrganizationModel organization, List<RequiredAction> requeridAction,
			boolean intoRequeridAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RetentionModel> getRetentions(OrganizationModel organization, Integer firstResult, Integer maxResults) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RetentionModel> searchForRetention(OrganizationModel organization, String filterText) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RetentionModel> searchForRetention(OrganizationModel organization, String filterText,
			Integer firstResult, Integer maxResults) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchResultsModel<RetentionModel> searchForRetention(OrganizationModel organization,
			SearchCriteriaModel criteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchResultsModel<RetentionModel> searchForRetention(OrganizationModel organization,
			SearchCriteriaModel criteria, String filterText) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScrollModel<RetentionModel> getRetentionsScroll(OrganizationModel organization) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScrollModel<RetentionModel> getRetentionsScroll(OrganizationModel organization, boolean asc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScrollModel<RetentionModel> getRetentionsScroll(OrganizationModel organization, boolean asc,
			int scrollSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScrollModel<RetentionModel> getRetentionsScroll(OrganizationModel organization, boolean asc, int scrollSize,
			int fetchSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected EntityManager getEntityManager() {
		// TODO Auto-generated method stub
		return null;
	}

}
