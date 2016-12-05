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
import org.openfact.pe.models.SummaryDocumentModel;
import org.openfact.pe.models.SummaryDocumentProvider;

public class JpaSummaryDocumentsProvider extends AbstractHibernateStorage implements SummaryDocumentProvider{

	private OpenfactSession session;
	protected EntityManager em;

	public JpaSummaryDocumentsProvider(OpenfactSession session, EntityManager em) {
		this.session = session;
		this.em = em;
	}
	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SummaryDocumentModel addSummaryDocument(OrganizationModel organization, String ID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SummaryDocumentModel getSummaryDocumentById(OrganizationModel organization, String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SummaryDocumentModel getSummaryDocumentByID(OrganizationModel organization, String ID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeSummaryDocument(OrganizationModel organization, String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeSummaryDocument(OrganizationModel organization, SummaryDocumentModel summaryDocument) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getSummaryDocumentsCount(OrganizationModel organization) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<SummaryDocumentModel> getSummaryDocuments(OrganizationModel organization) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SummaryDocumentModel> getSummaryDocuments(OrganizationModel organization,
			List<RequiredAction> requeridAction, boolean intoRequeridAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SummaryDocumentModel> getSummaryDocuments(OrganizationModel organization, Integer firstResult,
			Integer maxResults) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SummaryDocumentModel> searchForSummaryDocument(OrganizationModel organization, String filterText) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SummaryDocumentModel> searchForSummaryDocument(OrganizationModel organization, String filterText,
			Integer firstResult, Integer maxResults) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchResultsModel<SummaryDocumentModel> searchForSummaryDocument(OrganizationModel organization,
			SearchCriteriaModel criteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchResultsModel<SummaryDocumentModel> searchForSummaryDocument(OrganizationModel organization,
			SearchCriteriaModel criteria, String filterText) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScrollModel<SummaryDocumentModel> getSummaryDocumentsScroll(OrganizationModel organization) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScrollModel<SummaryDocumentModel> getSummaryDocumentsScroll(OrganizationModel organization, boolean asc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScrollModel<SummaryDocumentModel> getSummaryDocumentsScroll(OrganizationModel organization, boolean asc,
			int scrollSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScrollModel<SummaryDocumentModel> getSummaryDocumentsScroll(OrganizationModel organization, boolean asc,
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
