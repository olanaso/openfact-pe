package org.openfact.representations.idm.ubl.common;

public class DeliveryTermsRepresentation {
    private String idUbl;
    private String specialTerms;
    private String lossRiskResponsibilityCode;
    private String lossRisk;
    private LocationCommAggRepresentation deliveryLocation;
    private AllowanceChargeRepresentation allowanceCharge;
    private String id;

    public String getIdUbl() {
        return this.idUbl;
    }

    public void setIdUbl(String idUbl) {
        this.idUbl = idUbl;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLossRiskResponsibilityCode() {
        return this.lossRiskResponsibilityCode;
    }

    public void setLossRiskResponsibilityCode(String lossRiskResponsibilityCode) {
        this.lossRiskResponsibilityCode = lossRiskResponsibilityCode;
    }

    public String getSpecialTerms() {
        return this.specialTerms;
    }

    public void setSpecialTerms(String specialTerms) {
        this.specialTerms = specialTerms;
    }

    public String getLossRisk() {
        return this.lossRisk;
    }

    public void setLossRisk(String lossRisk) {
        this.lossRisk = lossRisk;
    }

    public AllowanceChargeRepresentation getAllowanceCharge() {
        return this.allowanceCharge;
    }

    public void setAllowanceCharge(AllowanceChargeRepresentation allowanceCharge) {
        this.allowanceCharge = allowanceCharge;
    }

    public LocationCommAggRepresentation getDeliveryLocation() {
        return this.deliveryLocation;
    }

    public void setDeliveryLocation(LocationCommAggRepresentation deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }
}