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
import org.openfact.models.ubl.common.DamageRemarksType;
import org.openfact.models.ubl.common.DespatchLineType;
import org.openfact.models.ubl.common.DimensionType;
import org.openfact.models.ubl.common.HandlingCodeType;
import org.openfact.models.ubl.common.HandlingInstructionsType;
import org.openfact.models.ubl.common.HazardousGoodsTransitType;
import org.openfact.models.ubl.common.HazardousRiskIndicatorType;
import org.openfact.models.ubl.common.IDType;
import org.openfact.models.ubl.common.PackageType;
import org.openfact.models.ubl.common.ReceiptLineType;
import org.openfact.models.ubl.common.ShippingMarksType;
import org.openfact.models.ubl.common.TemperatureType;
import org.openfact.models.ubl.common.TotalGoodsItemQuantityType;
import org.openfact.models.ubl.common.TotalPackageQuantityType;
import org.openfact.models.ubl.common.TransportEquipmentType;
import org.openfact.models.ubl.common.TransportHandlingUnitTypeCodeType;

@Entity(name = "TransportHandlingUnitType")
@Table(name = "TRANSPORTHANDLINGUNITTYPE")
@Inheritance(strategy = InheritanceType.JOINED)
public class TransportHandlingUnitType {

    protected IDType ID;
    protected TransportHandlingUnitTypeCodeType transportHandlingUnitTypeCode;
    protected HandlingCodeType handlingCode;
    protected HandlingInstructionsType handlingInstructions;
    protected HazardousRiskIndicatorType hazardousRiskIndicator;
    protected TotalGoodsItemQuantityType totalGoodsItemQuantity;
    protected TotalPackageQuantityType totalPackageQuantity;
    protected List<DamageRemarksType> damageRemarks;
    protected List<ShippingMarksType> shippingMarks;
    protected List<DespatchLineType> handlingUnitDespatchLine;
    protected List<PackageType> actualPackage;
    protected List<ReceiptLineType> receivedHandlingUnitReceiptLine;
    protected List<TransportEquipmentType> transportEquipment;
    protected List<HazardousGoodsTransitType> hazardousGoodsTransit;
    protected List<DimensionType> measurementDimension;
    protected TemperatureType minimumTemperature;
    protected TemperatureType maximumTemperature;
    protected String id;

    @ManyToOne(targetEntity = IDType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "ID_TRANSPORTHANDLINGUNITTYPE_0")
    public IDType getID() {
        return ID;
    }

    public void setID(IDType value) {
        this.ID = value;
    }

    @ManyToOne(targetEntity = TransportHandlingUnitTypeCodeType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "TRANSPORTHANDLINGUNITTYPECOD_1")
    public TransportHandlingUnitTypeCodeType getTransportHandlingUnitTypeCode() {
        return transportHandlingUnitTypeCode;
    }

    public void setTransportHandlingUnitTypeCode(TransportHandlingUnitTypeCodeType value) {
        this.transportHandlingUnitTypeCode = value;
    }

    @ManyToOne(targetEntity = HandlingCodeType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "HANDLINGCODE_TRANSPORTHANDLI_0")
    public HandlingCodeType getHandlingCode() {
        return handlingCode;
    }

    public void setHandlingCode(HandlingCodeType value) {
        this.handlingCode = value;
    }

    @ManyToOne(targetEntity = HandlingInstructionsType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "HANDLINGINSTRUCTIONS_TRANSPO_0")
    public HandlingInstructionsType getHandlingInstructions() {
        return handlingInstructions;
    }

    public void setHandlingInstructions(HandlingInstructionsType value) {
        this.handlingInstructions = value;
    }

    @ManyToOne(targetEntity = HazardousRiskIndicatorType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "HAZARDOUSRISKINDICATOR_TRANS_0")
    public HazardousRiskIndicatorType getHazardousRiskIndicator() {
        return hazardousRiskIndicator;
    }

    public void setHazardousRiskIndicator(HazardousRiskIndicatorType value) {
        this.hazardousRiskIndicator = value;
    }

    @ManyToOne(targetEntity = TotalGoodsItemQuantityType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "TOTALGOODSITEMQUANTITY_TRANS_0")
    public TotalGoodsItemQuantityType getTotalGoodsItemQuantity() {
        return totalGoodsItemQuantity;
    }

    public void setTotalGoodsItemQuantity(TotalGoodsItemQuantityType value) {
        this.totalGoodsItemQuantity = value;
    }

    @ManyToOne(targetEntity = TotalPackageQuantityType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "TOTALPACKAGEQUANTITY_TRANSPO_0")
    public TotalPackageQuantityType getTotalPackageQuantity() {
        return totalPackageQuantity;
    }

    public void setTotalPackageQuantity(TotalPackageQuantityType value) {
        this.totalPackageQuantity = value;
    }

    @OneToMany(targetEntity = DamageRemarksType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "DAMAGEREMARKS_TRANSPORTHANDL_0")
    public List<DamageRemarksType> getDamageRemarks() {
        if (damageRemarks == null) {
            damageRemarks = new ArrayList<DamageRemarksType>();
        }
        return this.damageRemarks;
    }

    public void setDamageRemarks(List<DamageRemarksType> damageRemarks) {
        this.damageRemarks = damageRemarks;
    }

    @OneToMany(targetEntity = ShippingMarksType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "SHIPPINGMARKS_TRANSPORTHANDL_0")
    public List<ShippingMarksType> getShippingMarks() {
        if (shippingMarks == null) {
            shippingMarks = new ArrayList<ShippingMarksType>();
        }
        return this.shippingMarks;
    }

    public void setShippingMarks(List<ShippingMarksType> shippingMarks) {
        this.shippingMarks = shippingMarks;
    }

    @OneToMany(targetEntity = DespatchLineType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "HANDLINGUNITDESPATCHLINE_TRA_0")
    public List<DespatchLineType> getHandlingUnitDespatchLine() {
        if (handlingUnitDespatchLine == null) {
            handlingUnitDespatchLine = new ArrayList<DespatchLineType>();
        }
        return this.handlingUnitDespatchLine;
    }

    public void setHandlingUnitDespatchLine(List<DespatchLineType> handlingUnitDespatchLine) {
        this.handlingUnitDespatchLine = handlingUnitDespatchLine;
    }

    @OneToMany(targetEntity = PackageType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "ACTUALPACKAGE_TRANSPORTHANDL_0")
    public List<PackageType> getActualPackage() {
        if (actualPackage == null) {
            actualPackage = new ArrayList<PackageType>();
        }
        return this.actualPackage;
    }

    public void setActualPackage(List<PackageType> actualPackage) {
        this.actualPackage = actualPackage;
    }

    @OneToMany(targetEntity = ReceiptLineType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "RECEIVEDHANDLINGUNITRECEIPTL_1")
    public List<ReceiptLineType> getReceivedHandlingUnitReceiptLine() {
        if (receivedHandlingUnitReceiptLine == null) {
            receivedHandlingUnitReceiptLine = new ArrayList<ReceiptLineType>();
        }
        return this.receivedHandlingUnitReceiptLine;
    }

    public void setReceivedHandlingUnitReceiptLine(List<ReceiptLineType> receivedHandlingUnitReceiptLine) {
        this.receivedHandlingUnitReceiptLine = receivedHandlingUnitReceiptLine;
    }

    @OneToMany(targetEntity = TransportEquipmentType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "TRANSPORTEQUIPMENT_TRANSPORT_0")
    public List<TransportEquipmentType> getTransportEquipment() {
        if (transportEquipment == null) {
            transportEquipment = new ArrayList<TransportEquipmentType>();
        }
        return this.transportEquipment;
    }

    public void setTransportEquipment(List<TransportEquipmentType> transportEquipment) {
        this.transportEquipment = transportEquipment;
    }

    @OneToMany(targetEntity = HazardousGoodsTransitType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "HAZARDOUSGOODSTRANSIT_TRANSP_0")
    public List<HazardousGoodsTransitType> getHazardousGoodsTransit() {
        if (hazardousGoodsTransit == null) {
            hazardousGoodsTransit = new ArrayList<HazardousGoodsTransitType>();
        }
        return this.hazardousGoodsTransit;
    }

    public void setHazardousGoodsTransit(List<HazardousGoodsTransitType> hazardousGoodsTransit) {
        this.hazardousGoodsTransit = hazardousGoodsTransit;
    }

    @OneToMany(targetEntity = DimensionType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "MEASUREMENTDIMENSION_TRANSPO_0")
    public List<DimensionType> getMeasurementDimension() {
        if (measurementDimension == null) {
            measurementDimension = new ArrayList<DimensionType>();
        }
        return this.measurementDimension;
    }

    public void setMeasurementDimension(List<DimensionType> measurementDimension) {
        this.measurementDimension = measurementDimension;
    }

    @ManyToOne(targetEntity = TemperatureType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "MINIMUMTEMPERATURE_TRANSPORT_0")
    public TemperatureType getMinimumTemperature() {
        return minimumTemperature;
    }

    public void setMinimumTemperature(TemperatureType value) {
        this.minimumTemperature = value;
    }

    @ManyToOne(targetEntity = TemperatureType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "MAXIMUMTEMPERATURE_TRANSPORT_0")
    public TemperatureType getMaximumTemperature() {
        return maximumTemperature;
    }

    public void setMaximumTemperature(TemperatureType value) {
        this.maximumTemperature = value;
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
