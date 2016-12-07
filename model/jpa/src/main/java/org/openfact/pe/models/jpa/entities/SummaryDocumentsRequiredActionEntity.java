package org.openfact.pe.models.jpa.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "deleteSummaryDocumentsRequiredActionsByOrganization", query = "delete from SummaryDocumentsRequiredActionEntity action where action.summaryDocuments IN (select u from SummaryDocumentsEntity u where u.organization.id=:organizationId)") })
@Entity
@Table(name = "SUMMARY_DOCUMENTS_REQUIRED_ACTION")
@IdClass(SummaryDocumentsRequiredActionEntity.Key.class)
public class SummaryDocumentsRequiredActionEntity {

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUMMARY_DOCUMENTS_ID")
	protected SummaryDocumentsEntity summaryDocuments;

	@Id
	@Column(name = "REQUIRED_ACTION")
	protected String action;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public SummaryDocumentsEntity getSummaryDocuments() {
		return summaryDocuments;
	}

	public void setSummaryDocuments(SummaryDocumentsEntity summaryDocuments) {
		this.summaryDocuments = summaryDocuments;
	}

	public static class Key implements Serializable {

		protected SummaryDocumentsEntity summaryDocuments;

		protected String action;

		public Key() {
		}

		public Key(SummaryDocumentsEntity summaryDocuments, String action) {
			this.summaryDocuments = summaryDocuments;
			this.action = action;
		}

		public SummaryDocumentsEntity getSummaryDocuments() {
			return summaryDocuments;
		}

		public String getAction() {
			return action;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;

			Key key = (Key) o;

			if (action != key.action)
				return false;
			if (summaryDocuments != null
					? !summaryDocuments.getId()
							.equals(key.summaryDocuments != null ? key.summaryDocuments.getId() : null)
					: key.summaryDocuments != null)
				return false;

			return true;
		}

		@Override
		public int hashCode() {
			int result = summaryDocuments != null ? summaryDocuments.getId().hashCode() : 0;
			result = 31 * result + (action != null ? action.hashCode() : 0);
			return result;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (!(o instanceof SummaryDocumentsRequiredActionEntity))
			return false;

		SummaryDocumentsRequiredActionEntity key = (SummaryDocumentsRequiredActionEntity) o;

		if (action != key.action)
			return false;
		if (summaryDocuments != null
				? !summaryDocuments.getId().equals(key.summaryDocuments != null ? key.summaryDocuments.getId() : null)
				: key.summaryDocuments != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = summaryDocuments != null ? summaryDocuments.getId().hashCode() : 0;
		result = 31 * result + (action != null ? action.hashCode() : 0);
		return result;
	}
}
