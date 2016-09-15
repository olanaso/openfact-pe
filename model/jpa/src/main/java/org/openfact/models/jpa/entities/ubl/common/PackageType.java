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
import org.openfact.models.ubl.common.DeliveryUnitType;
import org.openfact.models.ubl.common.DimensionType;
import org.openfact.models.ubl.common.GoodsItemType;
import org.openfact.models.ubl.common.IDType;
import org.openfact.models.ubl.common.PackageLevelCodeType;
import org.openfact.models.ubl.common.PackageType;
import org.openfact.models.ubl.common.PackagingTypeCodeTypeCommBas;
import org.openfact.models.ubl.common.PackingMaterialType;
import org.openfact.models.ubl.common.QuantityTypeCommBas;
import org.openfact.models.ubl.common.ReturnableMaterialIndicatorType;

@Entity(name = "PackageType")
@Table(name = "PACKAGETYPE")
@Inheritance(strategy = InheritanceType.JOINED)
public class PackageType {

    protected IDType ID;
    protected QuantityTypeCommBas quantity;
    protected ReturnableMaterialIndicatorType returnableMaterialIndicator;
    protected PackageLevelCodeType packageLevelCode;
    protected PackagingTypeCodeTypeCommBas packagingTypeCode;
    protected List<PackingMaterialType> packingMaterial;
    protected List<PackageType> containedPackage;
    protected List<GoodsItemType> goodsItem;
    protected List<DimensionType> measurementDimension;
    protected List<DeliveryUnitType> deliveryUnit;
    protected String id;

    @ManyToOne(targetEntity = IDType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "ID_PACKAGETYPE_OFID")
    public IDType getID() {
        return ID;
    }

    public void setID(IDType value) {
        this.ID = value;
    }

    @ManyToOne(targetEntity = QuantityTypeCommBas.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "QUANTITY_PACKAGETYPE_OFID")
    public QuantityTypeCommBas getQuantity() {
        return quantity;
    }

    public void setQuantity(QuantityTypeCommBas value) {
        this.quantity = value;
    }

    @ManyToOne(targetEntity = ReturnableMaterialIndicatorType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "RETURNABLEMATERIALINDICATOR__0")
    public ReturnableMaterialIndicatorType getReturnableMaterialIndicator() {
        return returnableMaterialIndicator;
    }

    public void setReturnableMaterialIndicator(ReturnableMaterialIndicatorType value) {
        this.returnableMaterialIndicator = value;
    }

    @ManyToOne(targetEntity = PackageLevelCodeType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "PACKAGELEVELCODE_PACKAGETYPE_0")
    public PackageLevelCodeType getPackageLevelCode() {
        return packageLevelCode;
    }

    public void setPackageLevelCode(PackageLevelCodeType value) {
        this.packageLevelCode = value;
    }

    @ManyToOne(targetEntity = PackagingTypeCodeTypeCommBas.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "PACKAGINGTYPECODE_PACKAGETYP_0")
    public PackagingTypeCodeTypeCommBas getPackagingTypeCode() {
        return packagingTypeCode;
    }

    public void setPackagingTypeCode(PackagingTypeCodeTypeCommBas value) {
        this.packagingTypeCode = value;
    }

    @OneToMany(targetEntity = PackingMaterialType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "PACKINGMATERIAL_PACKAGETYPE__0")
    public List<PackingMaterialType> getPackingMaterial() {
        if (packingMaterial == null) {
            packingMaterial = new ArrayList<PackingMaterialType>();
        }
        return this.packingMaterial;
    }

    public void setPackingMaterial(List<PackingMaterialType> packingMaterial) {
        this.packingMaterial = packingMaterial;
    }

    @OneToMany(targetEntity = PackageType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "CONTAINEDPACKAGE_PACKAGETYPE_0")
    public List<PackageType> getContainedPackage() {
        if (containedPackage == null) {
            containedPackage = new ArrayList<PackageType>();
        }
        return this.containedPackage;
    }

    public void setContainedPackage(List<PackageType> containedPackage) {
        this.containedPackage = containedPackage;
    }

    @OneToMany(targetEntity = GoodsItemType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "GOODSITEM_PACKAGETYPE_OFID")
    public List<GoodsItemType> getGoodsItem() {
        if (goodsItem == null) {
            goodsItem = new ArrayList<GoodsItemType>();
        }
        return this.goodsItem;
    }

    public void setGoodsItem(List<GoodsItemType> goodsItem) {
        this.goodsItem = goodsItem;
    }

    @OneToMany(targetEntity = DimensionType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "MEASUREMENTDIMENSION_PACKAGE_0")
    public List<DimensionType> getMeasurementDimension() {
        if (measurementDimension == null) {
            measurementDimension = new ArrayList<DimensionType>();
        }
        return this.measurementDimension;
    }

    public void setMeasurementDimension(List<DimensionType> measurementDimension) {
        this.measurementDimension = measurementDimension;
    }

    @OneToMany(targetEntity = DeliveryUnitType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "DELIVERYUNIT_PACKAGETYPE_OFID")
    public List<DeliveryUnitType> getDeliveryUnit() {
        if (deliveryUnit == null) {
            deliveryUnit = new ArrayList<DeliveryUnitType>();
        }
        return this.deliveryUnit;
    }

    public void setDeliveryUnit(List<DeliveryUnitType> deliveryUnit) {
        this.deliveryUnit = deliveryUnit;
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
