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


import org.openfact.models.ubl.common.AccountingCostCodeType;
import org.openfact.models.ubl.common.AccountingCostType;
import org.openfact.models.ubl.common.BillingReferenceType;
import org.openfact.models.ubl.common.DebitedQuantityType;
import org.openfact.models.ubl.common.DeliveryType;
import org.openfact.models.ubl.common.DocumentReferenceType;
import org.openfact.models.ubl.common.IDType;
import org.openfact.models.ubl.common.ItemType;
import org.openfact.models.ubl.common.LineExtensionAmountType;
import org.openfact.models.ubl.common.LineReferenceType;
import org.openfact.models.ubl.common.NoteType;
import org.openfact.models.ubl.common.PriceType;
import org.openfact.models.ubl.common.PricingReferenceType;
import org.openfact.models.ubl.common.ResponseType;
import org.openfact.models.ubl.common.TaxPointDateType;
import org.openfact.models.ubl.common.TaxTotalType;
import org.openfact.models.ubl.common.UUIDType;

public class DebitNoteLineType {

    protected IDType ID;
    protected UUIDType uuid;
    protected NoteType note;
    protected DebitedQuantityType debitedQuantity;
    protected LineExtensionAmountType lineExtensionAmount;
    protected TaxPointDateType taxPointDate;
    protected AccountingCostCodeType accountingCostCode;
    protected AccountingCostType accountingCost;
    protected List<ResponseType> discrepancyResponse;
    protected List<LineReferenceType> despatchLineReference;
    protected List<LineReferenceType> receiptLineReference;
    protected List<BillingReferenceType> billingReference;
    protected List<DocumentReferenceType> documentReference;
    protected PricingReferenceType pricingReference;
    protected List<DeliveryType> delivery;
    protected List<TaxTotalType> taxTotal;
    protected ItemType item;
    protected PriceType price;
    protected String id;

    public IDType getID() {
        return ID;
    }

    public void setID(IDType value) {
        this.ID = value;
    }

    public UUIDType getUUID() {
        return uuid;
    }

    public void setUUID(UUIDType value) {
        this.uuid = value;
    }

    public NoteType getNote() {
        return note;
    }

    public void setNote(NoteType value) {
        this.note = value;
    }

    public DebitedQuantityType getDebitedQuantity() {
        return debitedQuantity;
    }

    public void setDebitedQuantity(DebitedQuantityType value) {
        this.debitedQuantity = value;
    }

    public LineExtensionAmountType getLineExtensionAmount() {
        return lineExtensionAmount;
    }

    public void setLineExtensionAmount(LineExtensionAmountType value) {
        this.lineExtensionAmount = value;
    }

    public TaxPointDateType getTaxPointDate() {
        return taxPointDate;
    }

    public void setTaxPointDate(TaxPointDateType value) {
        this.taxPointDate = value;
    }

    public AccountingCostCodeType getAccountingCostCode() {
        return accountingCostCode;
    }

    public void setAccountingCostCode(AccountingCostCodeType value) {
        this.accountingCostCode = value;
    }

    public AccountingCostType getAccountingCost() {
        return accountingCost;
    }

    public void setAccountingCost(AccountingCostType value) {
        this.accountingCost = value;
    }

    public List<ResponseType> getDiscrepancyResponse() {
        if (discrepancyResponse == null) {
            discrepancyResponse = new ArrayList<ResponseType>();
        }
        return this.discrepancyResponse;
    }

    public void setDiscrepancyResponse(List<ResponseType> discrepancyResponse) {
        this.discrepancyResponse = discrepancyResponse;
    }

    public List<LineReferenceType> getDespatchLineReference() {
        if (despatchLineReference == null) {
            despatchLineReference = new ArrayList<LineReferenceType>();
        }
        return this.despatchLineReference;
    }

    public void setDespatchLineReference(List<LineReferenceType> despatchLineReference) {
        this.despatchLineReference = despatchLineReference;
    }

    public List<LineReferenceType> getReceiptLineReference() {
        if (receiptLineReference == null) {
            receiptLineReference = new ArrayList<LineReferenceType>();
        }
        return this.receiptLineReference;
    }

    public void setReceiptLineReference(List<LineReferenceType> receiptLineReference) {
        this.receiptLineReference = receiptLineReference;
    }

    public List<BillingReferenceType> getBillingReference() {
        if (billingReference == null) {
            billingReference = new ArrayList<BillingReferenceType>();
        }
        return this.billingReference;
    }

    public void setBillingReference(List<BillingReferenceType> billingReference) {
        this.billingReference = billingReference;
    }

    public List<DocumentReferenceType> getDocumentReference() {
        if (documentReference == null) {
            documentReference = new ArrayList<DocumentReferenceType>();
        }
        return this.documentReference;
    }

    public void setDocumentReference(List<DocumentReferenceType> documentReference) {
        this.documentReference = documentReference;
    }

    public PricingReferenceType getPricingReference() {
        return pricingReference;
    }

    public void setPricingReference(PricingReferenceType value) {
        this.pricingReference = value;
    }

    public List<DeliveryType> getDelivery() {
        if (delivery == null) {
            delivery = new ArrayList<DeliveryType>();
        }
        return this.delivery;
    }

    public void setDelivery(List<DeliveryType> delivery) {
        this.delivery = delivery;
    }

    public List<TaxTotalType> getTaxTotal() {
        if (taxTotal == null) {
            taxTotal = new ArrayList<TaxTotalType>();
        }
        return this.taxTotal;
    }

    public void setTaxTotal(List<TaxTotalType> taxTotal) {
        this.taxTotal = taxTotal;
    }

    public ItemType getItem() {
        return item;
    }

    public void setItem(ItemType value) {
        this.item = value;
    }

    public PriceType getPrice() {
        return price;
    }

    public void setPrice(PriceType value) {
        this.price = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String value) {
        this.id = value;
    }

}
