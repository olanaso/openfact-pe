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
import org.openfact.models.ubl.common.DimensionType;
import org.openfact.models.ubl.common.ExtendedIDType;
import org.openfact.models.ubl.common.IDType;
import org.openfact.models.ubl.common.PartyType;
import org.openfact.models.ubl.common.PhysicalAttributeType;

@Entity(name = "ItemIdentificationType")
@Table(name = "ITEMIDENTIFICATIONTYPE")
@Inheritance(strategy = InheritanceType.JOINED)
public class ItemIdentificationType {

    protected IDType ID;
    protected ExtendedIDType extendedID;
    protected List<PhysicalAttributeType> physicalAttribute;
    protected List<DimensionType> measurementDimension;
    protected PartyType issuerParty;
    protected String id;

    @ManyToOne(targetEntity = IDType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "ID_ITEMIDENTIFICATIONTYPE_HJ_0")
    public IDType getID() {
        return ID;
    }

    public void setID(IDType value) {
        this.ID = value;
    }

    @ManyToOne(targetEntity = ExtendedIDType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "EXTENDEDID_ITEMIDENTIFICATIO_0")
    public ExtendedIDType getExtendedID() {
        return extendedID;
    }

    public void setExtendedID(ExtendedIDType value) {
        this.extendedID = value;
    }

    @OneToMany(targetEntity = PhysicalAttributeType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "PHYSICALATTRIBUTE_ITEMIDENTI_0")
    public List<PhysicalAttributeType> getPhysicalAttribute() {
        if (physicalAttribute == null) {
            physicalAttribute = new ArrayList<PhysicalAttributeType>();
        }
        return this.physicalAttribute;
    }

    public void setPhysicalAttribute(List<PhysicalAttributeType> physicalAttribute) {
        this.physicalAttribute = physicalAttribute;
    }

    @OneToMany(targetEntity = DimensionType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "MEASUREMENTDIMENSION_ITEMIDE_0")
    public List<DimensionType> getMeasurementDimension() {
        if (measurementDimension == null) {
            measurementDimension = new ArrayList<DimensionType>();
        }
        return this.measurementDimension;
    }

    public void setMeasurementDimension(List<DimensionType> measurementDimension) {
        this.measurementDimension = measurementDimension;
    }

    @ManyToOne(targetEntity = PartyType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "ISSUERPARTY_ITEMIDENTIFICATI_0")
    public PartyType getIssuerParty() {
        return issuerParty;
    }

    public void setIssuerParty(PartyType value) {
        this.issuerParty = value;
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
