package org.openfact.pe.models.jpa.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.openfact.models.jpa.entities.InvoiceEntity;
import org.openfact.models.jpa.entities.SendEventEntity;

@Entity
@DiscriminatorValue(value = "PERCEPTION")
public class PerceptionSendEventEntity extends SunatSendEventEntity {

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(foreignKey = @ForeignKey, name = "PERCEPTION_ID")
	private PerceptionEntity perception;

	public PerceptionEntity getPerception() {
		return perception;
	}

	public void setPerception(PerceptionEntity perception) {
		this.perception = perception;
	}
}
