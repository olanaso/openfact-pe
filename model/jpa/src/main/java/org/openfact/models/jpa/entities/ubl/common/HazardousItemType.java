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

@Entity(name = "HazardousItemType")
@Table(name = "HAZARDOUSITEMTYPE")
@Inheritance(strategy = InheritanceType.JOINED)
public class HazardousItemType {

    protected IDType ID;
    protected PlacardNotationType placardNotation;
    protected PlacardEndorsementType placardEndorsement;
    protected AdditionalInformationType additionalInformation;
    protected UNDGCodeType undgCode;
    protected EmergencyProceduresCodeType emergencyProceduresCode;
    protected MedicalFirstAidGuideCodeType medicalFirstAidGuideCode;
    protected TechnicalNameType technicalName;
    protected CategoryNameType categoryName;
    protected HazardousCategoryCodeType hazardousCategoryCode;
    protected UpperOrangeHazardPlacardIDType upperOrangeHazardPlacardID;
    protected LowerOrangeHazardPlacardIDType lowerOrangeHazardPlacardID;
    protected MarkingIDType markingID;
    protected HazardClassIDType hazardClassID;
    protected NetWeightMeasureType netWeightMeasure;
    protected NetVolumeMeasureType netVolumeMeasure;
    protected QuantityTypeCommBas quantity;
    protected PartyType contactParty;
    protected List<SecondaryHazardType> secondaryHazard;
    protected List<HazardousGoodsTransitType> hazardousGoodsTransit;
    protected TemperatureType emergencyTemperature;
    protected TemperatureType flashpointTemperature;
    protected List<TemperatureType> additionalTemperature;
    protected String id;

    @ManyToOne(targetEntity = IDType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "ID_HAZARDOUSITEMTYPE_OFID")
    public IDType getID() {
        return ID;
    }

    public void setID(IDType value) {
        this.ID = value;
    }

    @ManyToOne(targetEntity = PlacardNotationType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "PLACARDNOTATION_HAZARDOUSITE_0")
    public PlacardNotationType getPlacardNotation() {
        return placardNotation;
    }

    public void setPlacardNotation(PlacardNotationType value) {
        this.placardNotation = value;
    }

    @ManyToOne(targetEntity = PlacardEndorsementType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "PLACARDENDORSEMENT_HAZARDOUS_0")
    public PlacardEndorsementType getPlacardEndorsement() {
        return placardEndorsement;
    }

    public void setPlacardEndorsement(PlacardEndorsementType value) {
        this.placardEndorsement = value;
    }

    @ManyToOne(targetEntity = AdditionalInformationType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "ADDITIONALINFORMATION_HAZARD_0")
    public AdditionalInformationType getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(AdditionalInformationType value) {
        this.additionalInformation = value;
    }

    @ManyToOne(targetEntity = UNDGCodeType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "UNDGCODE_HAZARDOUSITEMTYPE_H_0")
    public UNDGCodeType getUNDGCode() {
        return undgCode;
    }

    public void setUNDGCode(UNDGCodeType value) {
        this.undgCode = value;
    }

    @ManyToOne(targetEntity = EmergencyProceduresCodeType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "EMERGENCYPROCEDURESCODE_HAZA_0")
    public EmergencyProceduresCodeType getEmergencyProceduresCode() {
        return emergencyProceduresCode;
    }

    public void setEmergencyProceduresCode(EmergencyProceduresCodeType value) {
        this.emergencyProceduresCode = value;
    }

    @ManyToOne(targetEntity = MedicalFirstAidGuideCodeType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "MEDICALFIRSTAIDGUIDECODE_HAZ_0")
    public MedicalFirstAidGuideCodeType getMedicalFirstAidGuideCode() {
        return medicalFirstAidGuideCode;
    }

    public void setMedicalFirstAidGuideCode(MedicalFirstAidGuideCodeType value) {
        this.medicalFirstAidGuideCode = value;
    }

    @ManyToOne(targetEntity = TechnicalNameType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "TECHNICALNAME_HAZARDOUSITEMT_0")
    public TechnicalNameType getTechnicalName() {
        return technicalName;
    }

    public void setTechnicalName(TechnicalNameType value) {
        this.technicalName = value;
    }

    @ManyToOne(targetEntity = CategoryNameType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "CATEGORYNAME_HAZARDOUSITEMTY_0")
    public CategoryNameType getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(CategoryNameType value) {
        this.categoryName = value;
    }

    @ManyToOne(targetEntity = HazardousCategoryCodeType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "HAZARDOUSCATEGORYCODE_HAZARD_0")
    public HazardousCategoryCodeType getHazardousCategoryCode() {
        return hazardousCategoryCode;
    }

    public void setHazardousCategoryCode(HazardousCategoryCodeType value) {
        this.hazardousCategoryCode = value;
    }

    @ManyToOne(targetEntity = UpperOrangeHazardPlacardIDType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "UPPERORANGEHAZARDPLACARDID_H_0")
    public UpperOrangeHazardPlacardIDType getUpperOrangeHazardPlacardID() {
        return upperOrangeHazardPlacardID;
    }

    public void setUpperOrangeHazardPlacardID(UpperOrangeHazardPlacardIDType value) {
        this.upperOrangeHazardPlacardID = value;
    }

    @ManyToOne(targetEntity = LowerOrangeHazardPlacardIDType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "LOWERORANGEHAZARDPLACARDID_H_0")
    public LowerOrangeHazardPlacardIDType getLowerOrangeHazardPlacardID() {
        return lowerOrangeHazardPlacardID;
    }

    public void setLowerOrangeHazardPlacardID(LowerOrangeHazardPlacardIDType value) {
        this.lowerOrangeHazardPlacardID = value;
    }

    @ManyToOne(targetEntity = MarkingIDType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "MARKINGID_HAZARDOUSITEMTYPE__0")
    public MarkingIDType getMarkingID() {
        return markingID;
    }

    public void setMarkingID(MarkingIDType value) {
        this.markingID = value;
    }

    @ManyToOne(targetEntity = HazardClassIDType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "HAZARDCLASSID_HAZARDOUSITEMT_0")
    public HazardClassIDType getHazardClassID() {
        return hazardClassID;
    }

    public void setHazardClassID(HazardClassIDType value) {
        this.hazardClassID = value;
    }

    @ManyToOne(targetEntity = NetWeightMeasureType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "NETWEIGHTMEASURE_HAZARDOUSIT_0")
    public NetWeightMeasureType getNetWeightMeasure() {
        return netWeightMeasure;
    }

    public void setNetWeightMeasure(NetWeightMeasureType value) {
        this.netWeightMeasure = value;
    }

    @ManyToOne(targetEntity = NetVolumeMeasureType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "NETVOLUMEMEASURE_HAZARDOUSIT_0")
    public NetVolumeMeasureType getNetVolumeMeasure() {
        return netVolumeMeasure;
    }

    public void setNetVolumeMeasure(NetVolumeMeasureType value) {
        this.netVolumeMeasure = value;
    }

    @ManyToOne(targetEntity = QuantityTypeCommBas.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "QUANTITY_HAZARDOUSITEMTYPE_H_0")
    public QuantityTypeCommBas getQuantity() {
        return quantity;
    }

    public void setQuantity(QuantityTypeCommBas value) {
        this.quantity = value;
    }

    @ManyToOne(targetEntity = PartyType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "CONTACTPARTY_HAZARDOUSITEMTY_0")
    public PartyType getContactParty() {
        return contactParty;
    }

    public void setContactParty(PartyType value) {
        this.contactParty = value;
    }

    @OneToMany(targetEntity = SecondaryHazardType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "SECONDARYHAZARD_HAZARDOUSITE_0")
    public List<SecondaryHazardType> getSecondaryHazard() {
        if (secondaryHazard == null) {
            secondaryHazard = new ArrayList<SecondaryHazardType>();
        }
        return this.secondaryHazard;
    }

    public void setSecondaryHazard(List<SecondaryHazardType> secondaryHazard) {
        this.secondaryHazard = secondaryHazard;
    }

    @OneToMany(targetEntity = HazardousGoodsTransitType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "HAZARDOUSGOODSTRANSIT_HAZARD_0")
    public List<HazardousGoodsTransitType> getHazardousGoodsTransit() {
        if (hazardousGoodsTransit == null) {
            hazardousGoodsTransit = new ArrayList<HazardousGoodsTransitType>();
        }
        return this.hazardousGoodsTransit;
    }

    public void setHazardousGoodsTransit(List<HazardousGoodsTransitType> hazardousGoodsTransit) {
        this.hazardousGoodsTransit = hazardousGoodsTransit;
    }

    @ManyToOne(targetEntity = TemperatureType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "EMERGENCYTEMPERATURE_HAZARDO_0")
    public TemperatureType getEmergencyTemperature() {
        return emergencyTemperature;
    }

    public void setEmergencyTemperature(TemperatureType value) {
        this.emergencyTemperature = value;
    }

    @ManyToOne(targetEntity = TemperatureType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "FLASHPOINTTEMPERATURE_HAZARD_0")
    public TemperatureType getFlashpointTemperature() {
        return flashpointTemperature;
    }

    public void setFlashpointTemperature(TemperatureType value) {
        this.flashpointTemperature = value;
    }

    @OneToMany(targetEntity = TemperatureType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "ADDITIONALTEMPERATURE_HAZARD_0")
    public List<TemperatureType> getAdditionalTemperature() {
        if (additionalTemperature == null) {
            additionalTemperature = new ArrayList<TemperatureType>();
        }
        return this.additionalTemperature;
    }

    public void setAdditionalTemperature(List<TemperatureType> additionalTemperature) {
        this.additionalTemperature = additionalTemperature;
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