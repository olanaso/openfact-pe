package org.openfact.pe.models.jpa.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.openfact.models.jpa.entities.SendEventEntity;

@Entity
@DiscriminatorValue(value = "RETENTION")
@NamedQueries(value = {
		@NamedQuery(name = "getAllSunatSendEventByRetentionId", query = "select s from RetentionSendEventEntity s where s.retention.id=:retentionId"),
		@NamedQuery(name = "getRetentionSunatSendEventCountByRetention", query="select count(s) from RetentionSendEventEntity s where s.retention.id = :retentionId"),
		@NamedQuery(name = "deleteRetentionSunatSendEventByOrganization", query = "delete from RetentionSendEventEntity event where event.retention IN (select i from RetentionEntity i where i.organizationId=:organizationId)")
})
public class RetentionSendEventEntity extends SunatSendEventEntity{

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(foreignKey = @ForeignKey, name = "RETENTION_ID")
	private RetentionEntity retention;

	public RetentionEntity getRetention() {
		return retention;
	}

	public void setRetention(RetentionEntity retention) {
		this.retention = retention;
	}

}
