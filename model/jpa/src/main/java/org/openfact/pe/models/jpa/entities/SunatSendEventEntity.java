package org.openfact.pe.models.jpa.entities;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.openfact.models.enums.DestinyType;
import org.openfact.models.enums.SendResultType;
import org.openfact.models.jpa.entities.OrganizationEntity;
import org.openfact.models.jpa.entities.SendEventDestinyAttributeEntity;
import org.openfact.models.jpa.entities.SendEventResponseAttributeEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "SUNAT_SEND_EVENT")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE")
public class SunatSendEventEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Access(AccessType.PROPERTY)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "DESTINY_TYPE")
    private DestinyType destinyType;

    @Enumerated(EnumType.STRING)
    @Column(name = "RESULT")
    private SendResultType result;

    @Column(name = "DESCRIPTION", length = 400)
    private String description;

    @Column(name = "SEND_TYPE")
    private String type;

    @Type(type = "org.hibernate.type.LocalDateTimeType")
    @Column(name = "CREATED_TIMESTAMP")
    private LocalDateTime createdTimestamp;

    @ElementCollection
    @Column(name = "VALUE")
    @CollectionTable(name = "SUNAT_SEND_EVENT_FILE_ATTATCHMENT", joinColumns = {@JoinColumn(name = "SEND_EVENT_ID")})
    private Set<String> fileAttatchmentIds = new HashSet<>();

    @ElementCollection
    @Column(name = "VALUE")
    @CollectionTable(name = "SUNAT_SEND_EVENT_FILE_RESPONSE_ATTATCHMENT", joinColumns = {@JoinColumn(name = "SEND_EVENT_ID")})
    private Set<String> fileResponseAttatchmentIds = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof SunatSendEventEntity)) return false;

        SunatSendEventEntity that = (SunatSendEventEntity) o;

        if (!getId().equals(that.getId())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DestinyType getDestinyType() {
        return destinyType;
    }

    public void setDestinyType(DestinyType destinyType) {
        this.destinyType = destinyType;
    }

    public SendResultType getResult() {
        return result;
    }

    public void setResult(SendResultType result) {
        this.result = result;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(LocalDateTime createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public Set<String> getFileAttatchmentIds() {
        return fileAttatchmentIds;
    }

    public void setFileAttatchmentIds(Set<String> fileAttatchmentIds) {
        this.fileAttatchmentIds = fileAttatchmentIds;
    }

    public Set<String> getFileResponseAttatchmentIds() {
        return fileResponseAttatchmentIds;
    }

    public void setFileResponseAttatchmentIds(Set<String> fileResponseAttatchmentIds) {
        this.fileResponseAttatchmentIds = fileResponseAttatchmentIds;
    }

}