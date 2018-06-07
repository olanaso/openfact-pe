/*******************************************************************************
 * Copyright 2016 Sistcoop, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.09.14 at 11:44:49 AM PET 
//

package org.openfact.models.jpa.entities;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "DOCUMENT", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"ORGANIZATION_ID", "DOCUMENT_TYPE", "DOCUMENT_ID"})
})
@NamedQueries({
        @NamedQuery(name = "getAllDocumentsByOrganization", query = "select c from DocumentEntity c where c.organizationId = :organizationId order by c.createdTimestamp"),
        @NamedQuery(name = "getAllDocumentIdsByOrganization", query = "select c.id from DocumentEntity c where c.organizationId = :organizationId order by c.createdTimestamp"),
        @NamedQuery(name = "getOrganizationDocumentByDocumentPkId", query = "select i from DocumentEntity i where i.id = :documentPkId and i.organizationId = :organizationId"),
        @NamedQuery(name = "getOrganizationDocumentByDocumentTypeAndDocumentId", query = "select i from DocumentEntity i where i.documentType=:documentType and i.documentId=:documentId and i.organizationId = :organizationId"),
        @NamedQuery(name = "searchForDocument", query = "select i from DocumentEntity i where i.organizationId = :organizationId and lower(i.documentId) like :search order by i.createdTimestamp"),
        @NamedQuery(name = "getOrganizationDocumentCount", query = "select count(i) from DocumentEntity i where i.organizationId = :organizationId"),
        @NamedQuery(name = "deleteDocumentsByOrganization", query = "delete from DocumentEntity u where u.organizationId = :organizationId"),

        @NamedQuery(name = "selectLastDocumentChanged", query = "select d from DocumentEntity d where d.organizationId=:organizationId and d.documentType=:documentType and upper(d.documentId) like :firstLetter order by d.createdTimestamp"),

        @NamedQuery(name = "getAllClosedDocuments", query = "select distinct d from DocumentEntity d inner join d.requiredActions ra where d.closed=:closed and ra.action in :requiredActions and d.createdTimestamp <=:createdTimestamp order by d.createdTimestamp")
})
@NamedEntityGraphs({
        @NamedEntityGraph(name = "graph.BatchSendDocuments", attributeNodes = {
                @NamedAttributeNode(value = "organization", subgraph = "organization")
        }, subgraphs = {
                @NamedSubgraph(name = "organization", attributeNodes = {
                        @NamedAttributeNode(value = "id")
                })
        })
})
public class DocumentEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Access(AccessType.PROPERTY)
    private String id;

    @NotNull
    @Column(name = "DOCUMENT_ID")
    private String documentId;

    @NotNull
    @Column(name = "DOCUMENT_TYPE")
    private String documentType;

    @Column(name = "XML_FILE_ID")
    private String xmlFileId;

    @Column(name = "ORGANIZATION_ID", insertable = false, updatable = false)
    private String organizationId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORGANIZATION_ID", foreignKey = @ForeignKey)
    private OrganizationEntity organization;

    @Type(type = "org.hibernate.type.LocalDateTimeType")
    @Column(name = "CREATED_TIMESTAMP")
    private LocalDateTime createdTimestamp;

    @Column(name = "DOCUMENT_CURRENCY_CODE")
    private String documentCurrencyCode;

    @Column(name = "CUSTOMER_REGISTRATION_NAME")
    private String customerRegistrationName;

    @Column(name = "CUSTOMER_ASSIGNED_ACCOUNT_ID")
    private String customerAssignedAccountId;

    @Column(name = "CUSTOMER_ELECTRONIC_MAIL")
    private String customerElectronicMail;

    @Column(name = "ENABLED")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean enabled;

    @Column(name = "CLOSED")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean closed;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "document")
    private Collection<DocumentAttributeEntity> attributes = new ArrayList<>();

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "document")
    private Collection<DocumentRequiredActionEntity> requiredActions = new ArrayList<>();

    @Column(name = "CUSTOMER_SEND_EVENT_FAILURES")
    private int customerSendEventFailures;

    @Column(name = "THIRD_PARTY_SEND_EVENT_FAILURES")
    private int thirdPartySendEventFailures;

    @OneToMany(cascade = {CascadeType.REMOVE}, orphanRemoval = true, mappedBy = "document", fetch = FetchType.LAZY)
    private Collection<DocumentLineEntity> lines = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.REMOVE}, orphanRemoval = true, mappedBy = "document", fetch = FetchType.LAZY)
    private Collection<SendEventEntity> sendEvents = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.REMOVE}, orphanRemoval = true, mappedBy = "documentOrigin", fetch = FetchType.LAZY)
    private Collection<AttachedDocumentEntity> attachedDocumentsAsOrigin = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.REMOVE}, orphanRemoval = true, mappedBy = "documentDestiny", fetch = FetchType.LAZY)
    private Collection<AttachedDocumentEntity> attachedDocumentsAsDestiny = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getXmlFileId() {
        return xmlFileId;
    }

    public void setXmlFileId(String xmlFileId) {
        this.xmlFileId = xmlFileId;
    }

    public LocalDateTime getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(LocalDateTime createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getDocumentCurrencyCode() {
        return documentCurrencyCode;
    }

    public void setDocumentCurrencyCode(String documentCurrencyCode) {
        this.documentCurrencyCode = documentCurrencyCode;
    }

    public String getCustomerRegistrationName() {
        return customerRegistrationName;
    }

    public void setCustomerRegistrationName(String customerRegistrationName) {
        this.customerRegistrationName = customerRegistrationName;
    }

    public String getCustomerAssignedAccountId() {
        return customerAssignedAccountId;
    }

    public void setCustomerAssignedAccountId(String customerAssignedAccountId) {
        this.customerAssignedAccountId = customerAssignedAccountId;
    }

    public String getCustomerElectronicMail() {
        return customerElectronicMail;
    }

    public void setCustomerElectronicMail(String customerElectronicMail) {
        this.customerElectronicMail = customerElectronicMail;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Collection<DocumentAttributeEntity> getAttributes() {
        return attributes;
    }

    public void setAttributes(Collection<DocumentAttributeEntity> attributes) {
        this.attributes = attributes;
    }

    public Collection<DocumentRequiredActionEntity> getRequiredActions() {
        return requiredActions;
    }

    public void setRequiredActions(Collection<DocumentRequiredActionEntity> requiredActions) {
        this.requiredActions = requiredActions;
    }

    public Collection<SendEventEntity> getSendEvents() {
        return sendEvents;
    }

    public void setSendEvents(Collection<SendEventEntity> sendEvents) {
        this.sendEvents = sendEvents;
    }

    public Collection<AttachedDocumentEntity> getAttachedDocumentsAsOrigin() {
        return attachedDocumentsAsOrigin;
    }

    public void setAttachedDocumentsAsOrigin(Collection<AttachedDocumentEntity> attachedDocumentsAsOrigin) {
        this.attachedDocumentsAsOrigin = attachedDocumentsAsOrigin;
    }

    public Collection<AttachedDocumentEntity> getAttachedDocumentsAsDestiny() {
        return attachedDocumentsAsDestiny;
    }

    public void setAttachedDocumentsAsDestiny(Collection<AttachedDocumentEntity> attachedDocumentsAsDestiny) {
        this.attachedDocumentsAsDestiny = attachedDocumentsAsDestiny;
    }

    public int getCustomerSendEventFailures() {
        return customerSendEventFailures;
    }

    public void setCustomerSendEventFailures(int customerSendEventFailures) {
        this.customerSendEventFailures = customerSendEventFailures;
    }

    public int getThirdPartySendEventFailures() {
        return thirdPartySendEventFailures;
    }

    public void setThirdPartySendEventFailures(int thirdPartySendEventFailures) {
        this.thirdPartySendEventFailures = thirdPartySendEventFailures;
    }

    public Collection<DocumentLineEntity> getLines() {
        return lines;
    }

    public void setLines(Collection<DocumentLineEntity> lines) {
        this.lines = lines;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public OrganizationEntity getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationEntity organization) {
        this.organization = organization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentEntity that = (DocumentEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }
}
