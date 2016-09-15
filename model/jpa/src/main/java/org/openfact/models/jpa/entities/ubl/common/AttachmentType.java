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
import org.openfact.models.ubl.common.EmbeddedDocumentBinaryObjectType;
import org.openfact.models.ubl.common.ExternalReferenceType;

@Entity(name = "AttachmentType")
@Table(name = "ATTACHMENTTYPE")
@Inheritance(strategy = InheritanceType.JOINED)
public class AttachmentType {

    protected EmbeddedDocumentBinaryObjectType embeddedDocumentBinaryObject;
    protected ExternalReferenceType externalReference;
    protected String id;

    @ManyToOne(targetEntity = EmbeddedDocumentBinaryObjectType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "EMBEDDEDDOCUMENTBINARYOBJECT_0")
    public EmbeddedDocumentBinaryObjectType getEmbeddedDocumentBinaryObject() {
        return embeddedDocumentBinaryObject;
    }

    public void setEmbeddedDocumentBinaryObject(EmbeddedDocumentBinaryObjectType value) {
        this.embeddedDocumentBinaryObject = value;
    }

    @ManyToOne(targetEntity = ExternalReferenceType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "EXTERNALREFERENCE_ATTACHMENT_0")
    public ExternalReferenceType getExternalReference() {
        return externalReference;
    }

    public void setExternalReference(ExternalReferenceType value) {
        this.externalReference = value;
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
