package org.openfact.representations.idm.ubl.common;

import java.math.BigDecimal;
import java.util.List;

public class BillingReferenceLineRepresentation {
    private String ID;
    private BigDecimal amount;
    private List<AllowanceChargeRepresentation> allowanceCharge;
    private String id;

    public String getID() {
        return this.ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<AllowanceChargeRepresentation> getAllowanceCharge() {
        return this.allowanceCharge;
    }

    public void setAllowanceCharge(List<AllowanceChargeRepresentation> allowanceCharge) {
        this.allowanceCharge = allowanceCharge;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}