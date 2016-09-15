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
import org.openfact.models.ubl.common.AttributeIDType;
import org.openfact.models.ubl.common.DescriptionType;
import org.openfact.models.ubl.common.MaximumMeasureType;
import org.openfact.models.ubl.common.MeasureTypeCommBas;
import org.openfact.models.ubl.common.MinimumMeasureType;

@Entity(name = "DimensionType")
@Table(name = "DIMENSIONTYPE")
@Inheritance(strategy = InheritanceType.JOINED)
public class DimensionType {

    protected AttributeIDType attributeID;
    protected MeasureTypeCommBas measure;
    protected List<DescriptionType> description;
    protected MinimumMeasureType minimumMeasure;
    protected MaximumMeasureType maximumMeasure;
    protected String id;

    @ManyToOne(targetEntity = AttributeIDType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "ATTRIBUTEID_DIMENSIONTYPE_HJ_0")
    public AttributeIDType getAttributeID() {
        return attributeID;
    }

    public void setAttributeID(AttributeIDType value) {
        this.attributeID = value;
    }

    @ManyToOne(targetEntity = MeasureTypeCommBas.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "MEASURE_DIMENSIONTYPE_OFID")
    public MeasureTypeCommBas getMeasure() {
        return measure;
    }

    public void setMeasure(MeasureTypeCommBas value) {
        this.measure = value;
    }

    @OneToMany(targetEntity = DescriptionType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "DESCRIPTION_DIMENSIONTYPE_HJ_0")
    public List<DescriptionType> getDescription() {
        if (description == null) {
            description = new ArrayList<DescriptionType>();
        }
        return this.description;
    }

    public void setDescription(List<DescriptionType> description) {
        this.description = description;
    }

    @ManyToOne(targetEntity = MinimumMeasureType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "MINIMUMMEASURE_DIMENSIONTYPE_0")
    public MinimumMeasureType getMinimumMeasure() {
        return minimumMeasure;
    }

    public void setMinimumMeasure(MinimumMeasureType value) {
        this.minimumMeasure = value;
    }

    @ManyToOne(targetEntity = MaximumMeasureType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "MAXIMUMMEASURE_DIMENSIONTYPE_0")
    public MaximumMeasureType getMaximumMeasure() {
        return maximumMeasure;
    }

    public void setMaximumMeasure(MaximumMeasureType value) {
        this.maximumMeasure = value;
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
