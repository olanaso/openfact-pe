package org.openfact.models.jpa.ubl.common;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.jboss.logging.Logger;
import org.openfact.models.OpenfactSession;
import org.openfact.models.jpa.JpaModel;
import org.openfact.models.jpa.entities.ubl.common.ContractEntity;
import org.openfact.models.jpa.entities.ubl.common.DocumentReferenceEntity;
import org.openfact.models.jpa.entities.ubl.common.TaxTotalEntity;
import org.openfact.models.ubl.common.ContractModel;
import org.openfact.models.ubl.common.DocumentReferenceModel;
import org.openfact.models.ubl.common.PeriodModel;
import org.openfact.models.ubl.common.TaxTotalModel;

public class ContractAdapter implements ContractModel, JpaModel<ContractEntity> {

	protected static final Logger logger = Logger.getLogger(ContractAdapter.class);
	protected ContractEntity contract;
	protected EntityManager em;
	protected OpenfactSession session;

	public ContractAdapter(OpenfactSession session, EntityManager em, ContractEntity contract) {
		this.session = session;
		this.em = em;
		this.contract = contract;
	}

	@Override
	public String getID() {
		return this.contract.getID();
	}

	@Override
	public void setID(String value) {
		this.contract.setID(value);
	}

	@Override
	public LocalDate getIssueDate() {
		return this.contract.getIssueDate();
	}

	@Override
	public void setIssueDate(LocalDate value) {
		this.contract.setIssueDate(value);
	}

	@Override
	public LocalTime getIssueTime() {
		return this.contract.getIssueTime();
	}

	@Override
	public void setIssueTime(LocalTime value) {
		this.contract.setIssueTime(value);
	}

	@Override
	public String getContractTypeCode() {
		return this.contract.getContractTypeCode();
	}

	@Override
	public void setContractTypeCode(String value) {
		this.contract.setContractTypeCode(value);
	}

	@Override
	public String getContractType() {
		return this.contract.getContractType();
	}

	@Override
	public void setContractType(String value) {
		this.contract.setContractType(value);
	}

	@Override
	public PeriodModel getValidityPeriod() {
		return new PeriodAdapter(session, em, this.contract.getValidityPeriod());
	}

	@Override
	public void setValidityPeriod(PeriodModel value) {
		this.contract.setValidityPeriod(PeriodAdapter.toEntity(value, em));
	}

	@Override
	public List<DocumentReferenceModel> getContractDocumentReference() {
		return this.contract.getContractDocumentReference().stream()
				.map(f -> new DocumentReferenceAdapter(session, em, f)).collect(Collectors.toList());
	}

	@Override
	public void setContractDocumentReference(List<DocumentReferenceModel> contractDocumentReference) {
		List<DocumentReferenceEntity> entities = contractDocumentReference.stream()
				.map(f -> DocumentReferenceAdapter.toEntity(f, em)).collect(Collectors.toList());
		this.contract.setContractDocumentReference(entities);
	}

	@Override
	public String getId() {
		return this.contract.getId();
	}

	public static ContractEntity toEntity(ContractModel model, EntityManager em) {
		if (model instanceof ContractAdapter) {
			return ((ContractAdapter) model).getEntity();
		}
		return em.getReference(ContractEntity.class, model.getId());
	}

	@Override
	public ContractEntity getEntity() {
		return contract;
	}

	@Override
	public DocumentReferenceModel addContractDocumentReference() {
		List<DocumentReferenceEntity> entities = contract.getContractDocumentReference();

		DocumentReferenceEntity entity = new DocumentReferenceEntity();
		entities.add(entity);
		return new DocumentReferenceAdapter(session, em, entity);
	}

}
