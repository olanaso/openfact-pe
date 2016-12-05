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
import org.openfact.pe.models.VoidedDocumentModel;
import org.openfact.pe.models.VoidedDocumentProvider;

public class JpaVoidedDocumentsProvider extends AbstractHibernateStorage implements VoidedDocumentProvider {

	private OpenfactSession session;
	protected EntityManager em;

	public JpaVoidedDocumentsProvider(OpenfactSession session, EntityManager em) {
		this.session = session;
		this.em = em;
	}
	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public VoidedDocumentModel addVoidedDocument(OrganizationModel organization, String ID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VoidedDocumentModel getVoidedDocumentById(OrganizationModel organization, String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VoidedDocumentModel getVoidedDocumentByID(OrganizationModel organization, String ID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeVoidedDocument(OrganizationModel organization, String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeVoidedDocument(OrganizationModel organization, VoidedDocumentModel voidedDocument) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getVoidedDocumentsCount(OrganizationModel organization) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<VoidedDocumentModel> getVoidedDocuments(OrganizationModel organization) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VoidedDocumentModel> getVoidedDocuments(OrganizationModel organization,
			List<RequiredAction> requeridAction, boolean intoRequeridAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VoidedDocumentModel> getVoidedDocuments(OrganizationModel organization, Integer firstResult,
			Integer maxResults) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VoidedDocumentModel> searchForVoidedDocument(OrganizationModel organization, String filterText) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VoidedDocumentModel> searchForVoidedDocument(OrganizationModel organization, String filterText,
			Integer firstResult, Integer maxResults) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchResultsModel<VoidedDocumentModel> searchForVoidedDocument(OrganizationModel organization,
			SearchCriteriaModel criteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchResultsModel<VoidedDocumentModel> searchForVoidedDocument(OrganizationModel organization,
			SearchCriteriaModel criteria, String filterText) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScrollModel<VoidedDocumentModel> getVoidedDocumentsScroll(OrganizationModel organization) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScrollModel<VoidedDocumentModel> getVoidedDocumentsScroll(OrganizationModel organization, boolean asc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScrollModel<VoidedDocumentModel> getVoidedDocumentsScroll(OrganizationModel organization, boolean asc,
			int scrollSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScrollModel<VoidedDocumentModel> getVoidedDocumentsScroll(OrganizationModel organization, boolean asc,
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
