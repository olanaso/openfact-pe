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


import org.openfact.models.ubl.common.AllowanceChargeType;
import org.openfact.models.ubl.common.AmountTypeCommBas;
import org.openfact.models.ubl.common.DocumentTypeCodeType;
import org.openfact.models.ubl.common.IdentifierType;
import org.openfact.models.ubl.common.LineIDType;
import org.openfact.models.ubl.common.PaymentType;
import org.openfact.models.ubl.common.TaxTotalType;

public class SummaryDocumentsLineType {

    protected LineIDType lineID;
    protected DocumentTypeCodeType documentTypeCode;
    protected IdentifierType documentSerialID;
    protected IdentifierType startDocumentNumberID;
    protected IdentifierType endDocumentNumberID;
    protected AmountTypeCommBas totalAmount;
    protected List<PaymentType> billingPayment;
    protected List<AllowanceChargeType> allowanceCharge;
    protected List<TaxTotalType> taxTotal;
    protected String id;

    public LineIDType getLineID() {
        return lineID;
    }

    public void setLineID(LineIDType value) {
        this.lineID = value;
    }

    public DocumentTypeCodeType getDocumentTypeCode() {
        return documentTypeCode;
    }

    public void setDocumentTypeCode(DocumentTypeCodeType value) {
        this.documentTypeCode = value;
    }

    public IdentifierType getDocumentSerialID() {
        return documentSerialID;
    }

    public void setDocumentSerialID(IdentifierType value) {
        this.documentSerialID = value;
    }

    public IdentifierType getStartDocumentNumberID() {
        return startDocumentNumberID;
    }

    public void setStartDocumentNumberID(IdentifierType value) {
        this.startDocumentNumberID = value;
    }

    public IdentifierType getEndDocumentNumberID() {
        return endDocumentNumberID;
    }

    public void setEndDocumentNumberID(IdentifierType value) {
        this.endDocumentNumberID = value;
    }

    public AmountTypeCommBas getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(AmountTypeCommBas value) {
        this.totalAmount = value;
    }

    public List<PaymentType> getBillingPayment() {
        if (billingPayment == null) {
            billingPayment = new ArrayList<PaymentType>();
        }
        return this.billingPayment;
    }

    public void setBillingPayment(List<PaymentType> billingPayment) {
        this.billingPayment = billingPayment;
    }

    public List<AllowanceChargeType> getAllowanceCharge() {
        if (allowanceCharge == null) {
            allowanceCharge = new ArrayList<AllowanceChargeType>();
        }
        return this.allowanceCharge;
    }

    public void setAllowanceCharge(List<AllowanceChargeType> allowanceCharge) {
        this.allowanceCharge = allowanceCharge;
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

    public String getId() {
        return id;
    }

    public void setId(String value) {
        this.id = value;
    }

}
