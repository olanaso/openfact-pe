//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.09.14 at 11:44:49 AM PET 
//

package org.openfact.models.jpa.entities.ubl.common;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.openfact.models.ubl.common.DocumentHashType;
import org.openfact.models.ubl.common.ExpiryDateType;
import org.openfact.models.ubl.common.ExpiryTimeType;
import org.openfact.models.ubl.common.URIType;

@Entity(name = "ExternalReferenceType")
@Table(name = "EXTERNALREFERENCETYPE")
@Inheritance(strategy = InheritanceType.JOINED)
public class ExternalReferenceType {

    protected URIType uri;
    protected DocumentHashType documentHash;
    protected ExpiryDateType expiryDate;
    protected ExpiryTimeType expiryTime;
    protected String id;

    @ManyToOne(targetEntity = URIType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "URI_EXTERNALREFERENCETYPE_HJ_0")
    public URIType getURI() {
        return uri;
    }

    public void setURI(URIType value) {
        this.uri = value;
    }

    @ManyToOne(targetEntity = DocumentHashType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "DOCUMENTHASH_EXTERNALREFEREN_0")
    public DocumentHashType getDocumentHash() {
        return documentHash;
    }

    public void setDocumentHash(DocumentHashType value) {
        this.documentHash = value;
    }

    @ManyToOne(targetEntity = ExpiryDateType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "EXPIRYDATE_EXTERNALREFERENCE_0")
    public ExpiryDateType getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(ExpiryDateType value) {
        this.expiryDate = value;
    }

    @ManyToOne(targetEntity = ExpiryTimeType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "EXPIRYTIME_EXTERNALREFERENCE_0")
    public ExpiryTimeType getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(ExpiryTimeType value) {
        this.expiryTime = value;
    }

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Access(AccessType.PROPERTY)
    public String getId() {
        return id;
    }

    public void setId(String value) {
        this.id = value;
    }

}
