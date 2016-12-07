package org.openfact.pe.models.jpa.ubl;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.EntityManager;

import org.jboss.logging.Logger;
import org.openfact.models.OpenfactSession;
import org.openfact.models.jpa.JpaModel;
import org.openfact.pe.models.RetentionInformationModel;
import org.openfact.pe.models.jpa.entities.RetentionInformationEntity;

public class RetentionInformationAdapter implements RetentionInformationModel, JpaModel<RetentionInformationEntity> {

	protected static final Logger logger = Logger.getLogger(RetentionInformationAdapter.class);

	protected RetentionInformationEntity retentionInformation;
	protected EntityManager em;
	protected OpenfactSession session;

	public RetentionInformationAdapter(OpenfactSession session, EntityManager em,
			RetentionInformationEntity retentionInformation) {
		this.session = session;
		this.em = em;
		this.retentionInformation = retentionInformation;
	}

	@Override
	public RetentionInformationEntity getEntity() {
		return retentionInformation;
	}

	@Override
	public String getId() {
		return retentionInformation.getId();
	}

	@Override
	public BigDecimal getSunatRetentionAmount() {
		return retentionInformation.getSunatRetentionAmount();
	}

	@Override
	public void setSunatRetentionAmount(BigDecimal sunatRetentionAmount) {
		retentionInformation.setSunatRetentionAmount(sunatRetentionAmount);
	}

	@Override
	public LocalDate getSunatRetentionDate() {
		return retentionInformation.getSunatRetentionDate();
	}

	@Override
	public void setSunatRetentionDate(LocalDate sunatRetentionDate) {
		retentionInformation.setSunatRetentionDate(sunatRetentionDate);
	}

	@Override
	public BigDecimal getSunatNetTotalPaid() {
		return retentionInformation.getSunatNetTotalPaid();
	}

	@Override
	public void setSunatNetTotalPaid(BigDecimal sunatNetTotalPaid) {
		retentionInformation.setSunatNetTotalPaid(sunatNetTotalPaid);
	}

	public static RetentionInformationEntity toEntity(RetentionInformationModel model, EntityManager em) {
		if (model instanceof RetentionInformationAdapter) {
			return ((RetentionInformationAdapter) model).getEntity();
		}
		return em.getReference(RetentionInformationEntity.class, model.getId());
	}
}
