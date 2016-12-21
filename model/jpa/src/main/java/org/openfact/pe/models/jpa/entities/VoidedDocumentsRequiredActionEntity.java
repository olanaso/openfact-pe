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
		@NamedQuery(name = "deleteVoidedDocumentsRequiredActionsByOrganization", query = "delete from VoidedDocumentsRequiredActionEntity action where action.voidedDocuments IN (select u from VoidedDocumentsEntity u where u.organizationId=:organizationId)")
})
@Entity
@Table(name = "VOIDED_DOCUMENTS_REQUIRED_ACTION")
@IdClass(VoidedDocumentsRequiredActionEntity.Key.class)
public class VoidedDocumentsRequiredActionEntity {

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VOIDED_DOCUMENTS_ID")
	protected VoidedDocumentsEntity voidedDocuments;

	@Id
	@Column(name = "REQUIRED_ACTION")
	protected String action;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public VoidedDocumentsEntity getVoidedDocuments() {
		return voidedDocuments;
	}

	public void setVoidedDocuments(VoidedDocumentsEntity voidedDocuments) {
		this.voidedDocuments = voidedDocuments;
	}

	public static class Key implements Serializable {

		protected VoidedDocumentsEntity voidedDocuments;

		protected String action;

		public Key() {
		}

		public Key(VoidedDocumentsEntity voidedDocuments, String action) {
			this.voidedDocuments = voidedDocuments;
			this.action = action;
		}

		public VoidedDocumentsEntity getVoidedDocuments() {
			return voidedDocuments;
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
			if (voidedDocuments != null
					? !voidedDocuments.getId().equals(key.voidedDocuments != null ? key.voidedDocuments.getId() : null)
					: key.voidedDocuments != null)
				return false;

			return true;
		}

		@Override
		public int hashCode() {
			int result = voidedDocuments != null ? voidedDocuments.getId().hashCode() : 0;
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
		if (!(o instanceof VoidedDocumentsRequiredActionEntity))
			return false;

		VoidedDocumentsRequiredActionEntity key = (VoidedDocumentsRequiredActionEntity) o;

		if (action != key.action)
			return false;
		if (voidedDocuments != null
				? !voidedDocuments.getId().equals(key.voidedDocuments != null ? key.voidedDocuments.getId() : null)
				: key.voidedDocuments != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = voidedDocuments != null ? voidedDocuments.getId().hashCode() : 0;
		result = 31 * result + (action != null ? action.hashCode() : 0);
		return result;
	}
}
