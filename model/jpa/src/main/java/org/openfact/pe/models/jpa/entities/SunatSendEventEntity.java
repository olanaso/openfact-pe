package org.openfact.pe.models.jpa.entities;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.openfact.models.jpa.entities.OrganizationEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "SUNAT_SEND_EVENT")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@NamedQueries({
        @NamedQuery(name = "getOrganizationSunatSendEventById", query = "select i from SunatSendEventEntity i where i.organizationId = :organizationId and i.id=:id order by i.createdTimestamp"),
        @NamedQuery(name = "getAllSunatSendEventsByOrganization", query = "select i from SunatSendEventEntity i where i.organizationId = :organizationId order by i.createdTimestamp")
})
public class SunatSendEventEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Access(AccessType.PROPERTY)
    private String id;

    @Column(name = "RESULT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean result;

    @Column(name = "DESCRIPTION", length = 2000)
    private String description;

    @Column(name = "SEND_TYPE")
    private String type;

    @Type(type = "org.hibernate.type.LocalDateTimeType")
    @Column(name = "CREATED_TIMESTAMP")
    private LocalDateTime createdTimestamp;

    @ElementCollection
    @MapKeyColumn(name = "NAME")
    @Column(name = "VALUE")
    @CollectionTable(name = "SUNAT_SEND_EVENT_DESTINY", joinColumns = {@JoinColumn(name = "SEND_EVENT_ID")})
    private Map<String, String> destiny = new HashMap<String, String>();

    @ElementCollection
    @MapKeyColumn(name = "NAME")
    @Column(name = "VALUE")
    @CollectionTable(name = "SUNAT_SEND_EVENT_REPONSE", joinColumns = {@JoinColumn(name = "SEND_EVENT_ID")})
    private Map<String, String> response = new HashMap<String, String>();

    @OneToMany(fetch = FetchType.LAZY)
    @ElementCollection
    @CollectionTable(name = "SUNAT_SEND_EVENT_FILES", joinColumns = @JoinColumn(name = "SEND_EVENT_ID"))
    @MapKeyColumn(name = "FILE_TYPE")
    private Map<String, SunatStorageFileEntity> fileAttatchments = new HashMap<>();

    @OneToMany(fetch = FetchType.LAZY)
    @ElementCollection
    @CollectionTable(name = "SUNAT_RESPONSE_EVENT_FILES", joinColumns = @JoinColumn(name = "SEND_EVENT_ID"))
    @MapKeyColumn(name = "FILE_TYPE")
    private Map<String, SunatStorageFileEntity> fileResponseAttatchments = new HashMap<>();

    @NotNull
    @Column(name = "ORGANIZATION_ID")
    private String organizationId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
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

    public Map<String, String> getDestiny() {
        return destiny;
    }

    public void setDestiny(Map<String, String> destiny) {
        this.destiny = destiny;
    }

    public Map<String, String> getResponse() {
        return response;
    }

    public void setResponse(Map<String, String> response) {
        this.response = response;
    }

    public Map<String, SunatStorageFileEntity> getFileAttatchments() {
        return fileAttatchments;
    }

    public void setFileAttatchments(Map<String, SunatStorageFileEntity> fileAttatchments) {
        this.fileAttatchments = fileAttatchments;
    }

    public Map<String, SunatStorageFileEntity> getFileResponseAttatchments() {
        return fileResponseAttatchments;
    }

    public void setFileResponseAttatchments(Map<String, SunatStorageFileEntity> fileResponseAttatchments) {
        this.fileResponseAttatchments = fileResponseAttatchments;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }
}