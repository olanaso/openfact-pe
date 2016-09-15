//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.09.14 at 11:44:49 AM PET 
//

package org.openfact.models.ubl.common;

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


import org.openfact.models.ubl.common.BaseUnitMeasureType;
import org.openfact.models.ubl.common.IDType;
import org.openfact.models.ubl.common.NameTypeCommBas;
import org.openfact.models.ubl.common.PerUnitAmountType;
import org.openfact.models.ubl.common.PercentType;
import org.openfact.models.ubl.common.TaxExemptionReasonCodeType;
import org.openfact.models.ubl.common.TaxExemptionReasonType;
import org.openfact.models.ubl.common.TaxSchemeType;
import org.openfact.models.ubl.common.TierRangeType;
import org.openfact.models.ubl.common.TierRatePercentType;

public class TaxCategoryType {

    protected IDType ID;
    protected NameTypeCommBas name;
    protected PercentType percent;
    protected BaseUnitMeasureType baseUnitMeasure;
    protected PerUnitAmountType perUnitAmount;
    protected TaxExemptionReasonCodeType taxExemptionReasonCode;
    protected TaxExemptionReasonType taxExemptionReason;
    protected TierRangeType tierRange;
    protected TierRatePercentType tierRatePercent;
    protected TaxSchemeType taxScheme;
    protected String id;

    public IDType getID() {
        return ID;
    }

    public void setID(IDType value) {
        this.ID = value;
    }

    public NameTypeCommBas getName() {
        return name;
    }

    public void setName(NameTypeCommBas value) {
        this.name = value;
    }

    public PercentType getPercent() {
        return percent;
    }

    public void setPercent(PercentType value) {
        this.percent = value;
    }

    public BaseUnitMeasureType getBaseUnitMeasure() {
        return baseUnitMeasure;
    }

    public void setBaseUnitMeasure(BaseUnitMeasureType value) {
        this.baseUnitMeasure = value;
    }

    public PerUnitAmountType getPerUnitAmount() {
        return perUnitAmount;
    }

    public void setPerUnitAmount(PerUnitAmountType value) {
        this.perUnitAmount = value;
    }

    public TaxExemptionReasonCodeType getTaxExemptionReasonCode() {
        return taxExemptionReasonCode;
    }

    public void setTaxExemptionReasonCode(TaxExemptionReasonCodeType value) {
        this.taxExemptionReasonCode = value;
    }

    public TaxExemptionReasonType getTaxExemptionReason() {
        return taxExemptionReason;
    }

    public void setTaxExemptionReason(TaxExemptionReasonType value) {
        this.taxExemptionReason = value;
    }

    public TierRangeType getTierRange() {
        return tierRange;
    }

    public void setTierRange(TierRangeType value) {
        this.tierRange = value;
    }

    public TierRatePercentType getTierRatePercent() {
        return tierRatePercent;
    }

    public void setTierRatePercent(TierRatePercentType value) {
        this.tierRatePercent = value;
    }

    public TaxSchemeType getTaxScheme() {
        return taxScheme;
    }

    public void setTaxScheme(TaxSchemeType value) {
        this.taxScheme = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String value) {
        this.id = value;
    }

}
