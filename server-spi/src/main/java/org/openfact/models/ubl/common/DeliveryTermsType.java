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


import org.openfact.models.ubl.common.AllowanceChargeType;
import org.openfact.models.ubl.common.IDType;
import org.openfact.models.ubl.common.LocationTypeCommAgg;
import org.openfact.models.ubl.common.LossRiskResponsibilityCodeType;
import org.openfact.models.ubl.common.LossRiskType;
import org.openfact.models.ubl.common.SpecialTermsType;

public class DeliveryTermsType {

    protected IDType ID;
    protected SpecialTermsType specialTerms;
    protected LossRiskResponsibilityCodeType lossRiskResponsibilityCode;
    protected LossRiskType lossRisk;
    protected LocationTypeCommAgg deliveryLocation;
    protected AllowanceChargeType allowanceCharge;
    protected String id;

    public IDType getID() {
        return ID;
    }

    public void setID(IDType value) {
        this.ID = value;
    }

    public SpecialTermsType getSpecialTerms() {
        return specialTerms;
    }

    public void setSpecialTerms(SpecialTermsType value) {
        this.specialTerms = value;
    }

    public LossRiskResponsibilityCodeType getLossRiskResponsibilityCode() {
        return lossRiskResponsibilityCode;
    }

    public void setLossRiskResponsibilityCode(LossRiskResponsibilityCodeType value) {
        this.lossRiskResponsibilityCode = value;
    }

    public LossRiskType getLossRisk() {
        return lossRisk;
    }

    public void setLossRisk(LossRiskType value) {
        this.lossRisk = value;
    }

    public LocationTypeCommAgg getDeliveryLocation() {
        return deliveryLocation;
    }

    public void setDeliveryLocation(LocationTypeCommAgg value) {
        this.deliveryLocation = value;
    }

    public AllowanceChargeType getAllowanceCharge() {
        return allowanceCharge;
    }

    public void setAllowanceCharge(AllowanceChargeType value) {
        this.allowanceCharge = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String value) {
        this.id = value;
    }

}
