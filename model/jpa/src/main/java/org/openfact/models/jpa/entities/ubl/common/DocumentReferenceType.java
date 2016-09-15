//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.09.14 at 11:44:49 AM PET 
//

package org.openfact.models.jpa.entities.ubl.common;

import java.util.ArrayList;
import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.openfact.models.ubl.common.AttachmentType;
import org.openfact.models.ubl.common.CopyIndicatorType;
import org.openfact.models.ubl.common.DocumentTypeCodeType;
import org.openfact.models.ubl.common.DocumentTypeType;
import org.openfact.models.ubl.common.IDType;
import org.openfact.models.ubl.common.IssueDateType;
import org.openfact.models.ubl.common.UUIDType;
import org.openfact.models.ubl.common.XPathType;

@Entity(name = "DocumentReferenceType")
@Table(name = "DOCUMENTREFERENCETYPE")
@Inheritance(strategy = InheritanceType.JOINED)
public class DocumentReferenceType {

    protected IDType ID;
    protected CopyIndicatorType copyIndicator;
    protected UUIDType uuid;
    protected IssueDateType issueDate;
    protected DocumentTypeCodeType documentTypeCode;
    protected DocumentTypeType documentType;
    protected List<XPathType> xPath;
    protected AttachmentType attachment;
    protected String id;

    @ManyToOne(targetEntity = IDType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "ID_DOCUMENTREFERENCETYPE_OFID")
    public IDType getID() {
        return ID;
    }

    public void setID(IDType value) {
        this.ID = value;
    }

    @ManyToOne(targetEntity = CopyIndicatorType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "COPYINDICATOR_DOCUMENTREFERE_0")
    public CopyIndicatorType getCopyIndicator() {
        return copyIndicator;
    }

    public void setCopyIndicator(CopyIndicatorType value) {
        this.copyIndicator = value;
    }

    @ManyToOne(targetEntity = UUIDType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "UUID_DOCUMENTREFERENCETYPE_H_0")
    public UUIDType getUUID() {
        return uuid;
    }

    public void setUUID(UUIDType value) {
        this.uuid = value;
    }

    @ManyToOne(targetEntity = IssueDateType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "ISSUEDATE_DOCUMENTREFERENCET_0")
    public IssueDateType getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(IssueDateType value) {
        this.issueDate = value;
    }

    @ManyToOne(targetEntity = DocumentTypeCodeType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "DOCUMENTTYPECODE_DOCUMENTREF_0")
    public DocumentTypeCodeType getDocumentTypeCode() {
        return documentTypeCode;
    }

    public void setDocumentTypeCode(DocumentTypeCodeType value) {
        this.documentTypeCode = value;
    }

    @ManyToOne(targetEntity = DocumentTypeType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "DOCUMENTTYPE_DOCUMENTREFEREN_0")
    public DocumentTypeType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentTypeType value) {
        this.documentType = value;
    }

    @OneToMany(targetEntity = XPathType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "XPATH_DOCUMENTREFERENCETYPE__0")
    public List<XPathType> getXPath() {
        if (xPath == null) {
            xPath = new ArrayList<XPathType>();
        }
        return this.xPath;
    }

    public void setXPath(List<XPathType> xPath) {
        this.xPath = xPath;
    }

    @ManyToOne(targetEntity = AttachmentType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "ATTACHMENT_DOCUMENTREFERENCE_0")
    public AttachmentType getAttachment() {
        return attachment;
    }

    public void setAttachment(AttachmentType value) {
        this.attachment = value;
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
