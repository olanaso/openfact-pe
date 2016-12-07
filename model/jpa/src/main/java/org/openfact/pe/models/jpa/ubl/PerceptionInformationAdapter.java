package org.openfact.pe.models.jpa.ubl;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.EntityManager;

import org.jboss.logging.Logger;
import org.openfact.models.OpenfactSession;
import org.openfact.models.jpa.JpaModel;
import org.openfact.pe.models.PerceptionInformationModel;
import org.openfact.pe.models.jpa.entities.PerceptionInformationEntity;

public class PerceptionInformationAdapter implements PerceptionInformationModel, JpaModel<PerceptionInformationEntity> {

	protected static final Logger logger = Logger.getLogger(PerceptionInformationAdapter.class);

	protected PerceptionInformationEntity perceptionInformation;
	protected EntityManager em;
	protected OpenfactSession session;

	public PerceptionInformationAdapter(OpenfactSession session, EntityManager em,
			PerceptionInformationEntity perceptionInformation) {
		this.session = session;
		this.em = em;
		this.perceptionInformation = perceptionInformation;
	}

	@Override
	public PerceptionInformationEntity getEntity() {
		return perceptionInformation;
	}

	@Override
	public String getId() {
		return perceptionInformation.getId();
	}

	@Override
	public BigDecimal getSunatPerceptionAmount() {
		return perceptionInformation.getSunatPerceptionAmount();
	}

	@Override
	public void setSunatPerceptionAmount(BigDecimal sunatPerceptionAmount) {
		perceptionInformation.setSunatPerceptionAmount(sunatPerceptionAmount);
	}

	@Override
	public BigDecimal getSunatNetTotalCashed() {
		return perceptionInformation.getSunatNetTotalCashed();
	}

	@Override
	public void setSunatNetTotalCashed(BigDecimal sunatNetTotalCashed) {
		perceptionInformation.setSunatNetTotalCashed(sunatNetTotalCashed);
	}

	public static PerceptionInformationEntity toEntity(PerceptionInformationModel model, EntityManager em) {
		if (model instanceof PerceptionInformationAdapter) {
			return ((PerceptionInformationAdapter) model).getEntity();
		}
		return em.getReference(PerceptionInformationEntity.class, model.getId());
	}

	@Override
	public LocalDate getSunatPerceptionDate() {
		return perceptionInformation.getSunatPerceptionDate();
	}

	@Override
	public void setSunatPerceptionDate(LocalDate sunatPerceptionDate) {
		perceptionInformation.setSunatPerceptionDate(sunatPerceptionDate);
	}

}