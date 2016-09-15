//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.09.14 at 11:44:49 AM PET 
//

package org.openfact.models.ubl.common;

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


import org.openfact.models.ubl.common.AgencyIDType;
import org.openfact.models.ubl.common.AgencyNameType;
import org.openfact.models.ubl.common.ClassificationCategoryType;
import org.openfact.models.ubl.common.DescriptionType;
import org.openfact.models.ubl.common.IDType;
import org.openfact.models.ubl.common.LanguageIDType;
import org.openfact.models.ubl.common.LastRevisionDateType;
import org.openfact.models.ubl.common.LastRevisionTimeType;
import org.openfact.models.ubl.common.NameTypeCommBas;
import org.openfact.models.ubl.common.NoteType;
import org.openfact.models.ubl.common.SchemeURIType;
import org.openfact.models.ubl.common.URIType;
import org.openfact.models.ubl.common.UUIDType;
import org.openfact.models.ubl.common.VersionIDType;

public class ClassificationSchemeType {

    protected IDType ID;
    protected UUIDType uuid;
    protected LastRevisionDateType lastRevisionDate;
    protected LastRevisionTimeType lastRevisionTime;
    protected NoteType note;
    protected NameTypeCommBas name;
    protected List<DescriptionType> description;
    protected AgencyIDType agencyID;
    protected AgencyNameType agencyName;
    protected VersionIDType versionID;
    protected URIType uri;
    protected SchemeURIType schemeURI;
    protected LanguageIDType languageID;
    protected List<ClassificationCategoryType> classificationCategory;
    protected String id;

    public IDType getID() {
        return ID;
    }

    public void setID(IDType value) {
        this.ID = value;
    }

    public UUIDType getUUID() {
        return uuid;
    }

    public void setUUID(UUIDType value) {
        this.uuid = value;
    }

    public LastRevisionDateType getLastRevisionDate() {
        return lastRevisionDate;
    }

    public void setLastRevisionDate(LastRevisionDateType value) {
        this.lastRevisionDate = value;
    }

    public LastRevisionTimeType getLastRevisionTime() {
        return lastRevisionTime;
    }

    public void setLastRevisionTime(LastRevisionTimeType value) {
        this.lastRevisionTime = value;
    }

    public NoteType getNote() {
        return note;
    }

    public void setNote(NoteType value) {
        this.note = value;
    }

    public NameTypeCommBas getName() {
        return name;
    }

    public void setName(NameTypeCommBas value) {
        this.name = value;
    }

    public List<DescriptionType> getDescription() {
        if (description == null) {
            description = new ArrayList<DescriptionType>();
        }
        return this.description;
    }

    public void setDescription(List<DescriptionType> description) {
        this.description = description;
    }

    public AgencyIDType getAgencyID() {
        return agencyID;
    }

    public void setAgencyID(AgencyIDType value) {
        this.agencyID = value;
    }

    public AgencyNameType getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(AgencyNameType value) {
        this.agencyName = value;
    }

    public VersionIDType getVersionID() {
        return versionID;
    }

    public void setVersionID(VersionIDType value) {
        this.versionID = value;
    }

    public URIType getURI() {
        return uri;
    }

    public void setURI(URIType value) {
        this.uri = value;
    }

    public SchemeURIType getSchemeURI() {
        return schemeURI;
    }

    public void setSchemeURI(SchemeURIType value) {
        this.schemeURI = value;
    }

    public LanguageIDType getLanguageID() {
        return languageID;
    }

    public void setLanguageID(LanguageIDType value) {
        this.languageID = value;
    }

    public List<ClassificationCategoryType> getClassificationCategory() {
        if (classificationCategory == null) {
            classificationCategory = new ArrayList<ClassificationCategoryType>();
        }
        return this.classificationCategory;
    }

    public void setClassificationCategory(List<ClassificationCategoryType> classificationCategory) {
        this.classificationCategory = classificationCategory;
    }

    public String getId() {
        return id;
    }

    public void setId(String value) {
        this.id = value;
    }

}
