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


import org.openfact.models.ubl.common.AmountTypeCommBas;
import org.openfact.models.ubl.common.IDType;
import org.openfact.models.ubl.common.NoteType;
import org.openfact.models.ubl.common.PaymentMeansIDType;
import org.openfact.models.ubl.common.PenaltySurchargePercentType;
import org.openfact.models.ubl.common.PeriodType;
import org.openfact.models.ubl.common.PrepaidPaymentReferenceIDType;
import org.openfact.models.ubl.common.ReferenceEventCodeType;
import org.openfact.models.ubl.common.SettlementDiscountPercentType;

public class PaymentTermsType {

    protected IDType ID;
    protected PaymentMeansIDType paymentMeansID;
    protected PrepaidPaymentReferenceIDType prepaidPaymentReferenceID;
    protected List<NoteType> note;
    protected ReferenceEventCodeType referenceEventCode;
    protected SettlementDiscountPercentType settlementDiscountPercent;
    protected PenaltySurchargePercentType penaltySurchargePercent;
    protected AmountTypeCommBas amount;
    protected PeriodType settlementPeriod;
    protected PeriodType penaltyPeriod;
    protected String id;

    public IDType getID() {
        return ID;
    }

    public void setID(IDType value) {
        this.ID = value;
    }

    public PaymentMeansIDType getPaymentMeansID() {
        return paymentMeansID;
    }

    public void setPaymentMeansID(PaymentMeansIDType value) {
        this.paymentMeansID = value;
    }

    public PrepaidPaymentReferenceIDType getPrepaidPaymentReferenceID() {
        return prepaidPaymentReferenceID;
    }

    public void setPrepaidPaymentReferenceID(PrepaidPaymentReferenceIDType value) {
        this.prepaidPaymentReferenceID = value;
    }

    public List<NoteType> getNote() {
        if (note == null) {
            note = new ArrayList<NoteType>();
        }
        return this.note;
    }

    public void setNote(List<NoteType> note) {
        this.note = note;
    }

    public ReferenceEventCodeType getReferenceEventCode() {
        return referenceEventCode;
    }

    public void setReferenceEventCode(ReferenceEventCodeType value) {
        this.referenceEventCode = value;
    }

    public SettlementDiscountPercentType getSettlementDiscountPercent() {
        return settlementDiscountPercent;
    }

    public void setSettlementDiscountPercent(SettlementDiscountPercentType value) {
        this.settlementDiscountPercent = value;
    }

    public PenaltySurchargePercentType getPenaltySurchargePercent() {
        return penaltySurchargePercent;
    }

    public void setPenaltySurchargePercent(PenaltySurchargePercentType value) {
        this.penaltySurchargePercent = value;
    }

    public AmountTypeCommBas getAmount() {
        return amount;
    }

    public void setAmount(AmountTypeCommBas value) {
        this.amount = value;
    }

    public PeriodType getSettlementPeriod() {
        return settlementPeriod;
    }

    public void setSettlementPeriod(PeriodType value) {
        this.settlementPeriod = value;
    }

    public PeriodType getPenaltyPeriod() {
        return penaltyPeriod;
    }

    public void setPenaltyPeriod(PeriodType value) {
        this.penaltyPeriod = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String value) {
        this.id = value;
    }

}
