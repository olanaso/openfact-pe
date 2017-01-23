package org.openfact.pe.models.jpa.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.openfact.models.jpa.entities.InvoiceEntity;
import org.openfact.models.jpa.entities.SendEventEntity;

@Entity
@DiscriminatorValue(value = "PERCEPTION")
@NamedQueries(value = {
        @NamedQuery(name = "getAllSunatSendEventByPerceptionId", query = "select s from PerceptionSendEventEntity s where s.perception.id=:perceptionId"),
        @NamedQuery(name = "getPerceptionSunatSendEventCountByPerception", query="select count(s) from PerceptionSendEventEntity s where s.perception.id = :perceptionId"),
        @NamedQuery(name = "deletePerceptionSunatSendEventByOrganization", query = "delete from PerceptionSendEventEntity event where event.perception IN (select i from PerceptionEntity i where i.organizationId=:organizationId)")
})
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
