//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.09.14 at 11:44:49 AM PET 
//

package org.openfact.models.ubl;

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
import org.openfact.models.ubl.common.AllowanceChargeType;
import org.openfact.models.ubl.common.BillingReferenceType;
import org.openfact.models.ubl.common.CopyIndicatorType;
import org.openfact.models.ubl.common.CreditNoteLineType;
import org.openfact.models.ubl.common.CustomerPartyType;
import org.openfact.models.ubl.common.CustomizationIDType;
import org.openfact.models.ubl.common.DocumentCurrencyCodeType;
import org.openfact.models.ubl.common.DocumentReferenceType;
import org.openfact.models.ubl.common.ExchangeRateType;
import org.openfact.models.ubl.common.IDType;
import org.openfact.models.ubl.common.IssueDateType;
import org.openfact.models.ubl.common.IssueTimeType;
import org.openfact.models.ubl.common.LineCountNumericType;
import org.openfact.models.ubl.common.MonetaryTotalType;
import org.openfact.models.ubl.common.NoteType;
import org.openfact.models.ubl.common.OrderReferenceType;
import org.openfact.models.ubl.common.PartyType;
import org.openfact.models.ubl.common.PaymentAlternativeCurrencyCodeType;
import org.openfact.models.ubl.common.PaymentCurrencyCodeType;
import org.openfact.models.ubl.common.PeriodType;
import org.openfact.models.ubl.common.PricingCurrencyCodeType;
import org.openfact.models.ubl.common.ProfileIDType;
import org.openfact.models.ubl.common.ResponseType;
import org.openfact.models.ubl.common.SignatureType;
import org.openfact.models.ubl.common.SupplierPartyType;
import org.openfact.models.ubl.common.TaxCurrencyCodeType;
import org.openfact.models.ubl.common.TaxPointDateType;
import org.openfact.models.ubl.common.TaxTotalType;
import org.openfact.models.ubl.common.UBLExtensionsType;
import org.openfact.models.ubl.common.UBLVersionIDType;
import org.openfact.models.ubl.common.UUIDType;

public class CreditNoteType {

    protected UBLExtensionsType ublExtensions;
    protected UBLVersionIDType ublVersionID;
    protected CustomizationIDType customizationID;
    protected ProfileIDType profileID;
    protected IDType ID;
    protected CopyIndicatorType copyIndicator;
    protected UUIDType uuid;
    protected IssueDateType issueDate;
    protected IssueTimeType issueTime;
    protected TaxPointDateType taxPointDate;
    protected List<NoteType> note;
    protected DocumentCurrencyCodeType documentCurrencyCode;
    protected TaxCurrencyCodeType taxCurrencyCode;
    protected PricingCurrencyCodeType pricingCurrencyCode;
    protected PaymentCurrencyCodeType paymentCurrencyCode;
    protected PaymentAlternativeCurrencyCodeType paymentAlternativeCurrencyCode;
    protected AccountingCostCodeType accountingCostCode;
    protected AccountingCostType accountingCost;
    protected LineCountNumericType lineCountNumeric;
    protected List<PeriodType> invoicePeriod;
    protected List<ResponseType> discrepancyResponse;
    protected OrderReferenceType orderReference;
    protected List<BillingReferenceType> billingReference;
    protected List<DocumentReferenceType> despatchDocumentReference;
    protected List<DocumentReferenceType> receiptDocumentReference;
    protected List<DocumentReferenceType> contractDocumentReference;
    protected List<DocumentReferenceType> additionalDocumentReference;
    protected List<SignatureType> signature;
    protected SupplierPartyType accountingSupplierParty;
    protected CustomerPartyType accountingCustomerParty;
    protected PartyType payeeParty;
    protected PartyType taxRepresentativeParty;
    protected ExchangeRateType taxExchangeRate;
    protected ExchangeRateType pricingExchangeRate;
    protected ExchangeRateType paymentExchangeRate;
    protected ExchangeRateType paymentAlternativeExchangeRate;
    protected List<AllowanceChargeType> allowanceCharge;
    protected List<TaxTotalType> taxTotal;
    protected MonetaryTotalType legalMonetaryTotal;
    protected List<CreditNoteLineType> creditNoteLine;
    protected String id;

    public UBLExtensionsType getUBLExtensions() {
        return ublExtensions;
    }

    public void setUBLExtensions(UBLExtensionsType value) {
        this.ublExtensions = value;
    }

    public UBLVersionIDType getUBLVersionID() {
        return ublVersionID;
    }

    public void setUBLVersionID(UBLVersionIDType value) {
        this.ublVersionID = value;
    }

    public CustomizationIDType getCustomizationID() {
        return customizationID;
    }

    public void setCustomizationID(CustomizationIDType value) {
        this.customizationID = value;
    }

    public ProfileIDType getProfileID() {
        return profileID;
    }

    public void setProfileID(ProfileIDType value) {
        this.profileID = value;
    }

    public IDType getID() {
        return ID;
    }

    public void setID(IDType value) {
        this.ID = value;
    }

    public CopyIndicatorType getCopyIndicator() {
        return copyIndicator;
    }

    public void setCopyIndicator(CopyIndicatorType value) {
        this.copyIndicator = value;
    }

    public UUIDType getUUID() {
        return uuid;
    }

    public void setUUID(UUIDType value) {
        this.uuid = value;
    }

    public IssueDateType getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(IssueDateType value) {
        this.issueDate = value;
    }

    public IssueTimeType getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(IssueTimeType value) {
        this.issueTime = value;
    }

    public TaxPointDateType getTaxPointDate() {
        return taxPointDate;
    }

    public void setTaxPointDate(TaxPointDateType value) {
        this.taxPointDate = value;
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

    public DocumentCurrencyCodeType getDocumentCurrencyCode() {
        return documentCurrencyCode;
    }

    public void setDocumentCurrencyCode(DocumentCurrencyCodeType value) {
        this.documentCurrencyCode = value;
    }

    public TaxCurrencyCodeType getTaxCurrencyCode() {
        return taxCurrencyCode;
    }

    public void setTaxCurrencyCode(TaxCurrencyCodeType value) {
        this.taxCurrencyCode = value;
    }

    public PricingCurrencyCodeType getPricingCurrencyCode() {
        return pricingCurrencyCode;
    }

    public void setPricingCurrencyCode(PricingCurrencyCodeType value) {
        this.pricingCurrencyCode = value;
    }

    public PaymentCurrencyCodeType getPaymentCurrencyCode() {
        return paymentCurrencyCode;
    }

    public void setPaymentCurrencyCode(PaymentCurrencyCodeType value) {
        this.paymentCurrencyCode = value;
    }

    public PaymentAlternativeCurrencyCodeType getPaymentAlternativeCurrencyCode() {
        return paymentAlternativeCurrencyCode;
    }

    public void setPaymentAlternativeCurrencyCode(PaymentAlternativeCurrencyCodeType value) {
        this.paymentAlternativeCurrencyCode = value;
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

    public LineCountNumericType getLineCountNumeric() {
        return lineCountNumeric;
    }

    public void setLineCountNumeric(LineCountNumericType value) {
        this.lineCountNumeric = value;
    }

    public List<PeriodType> getInvoicePeriod() {
        if (invoicePeriod == null) {
            invoicePeriod = new ArrayList<PeriodType>();
        }
        return this.invoicePeriod;
    }

    public void setInvoicePeriod(List<PeriodType> invoicePeriod) {
        this.invoicePeriod = invoicePeriod;
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

    public OrderReferenceType getOrderReference() {
        return orderReference;
    }

    public void setOrderReference(OrderReferenceType value) {
        this.orderReference = value;
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

    public List<DocumentReferenceType> getDespatchDocumentReference() {
        if (despatchDocumentReference == null) {
            despatchDocumentReference = new ArrayList<DocumentReferenceType>();
        }
        return this.despatchDocumentReference;
    }

    public void setDespatchDocumentReference(List<DocumentReferenceType> despatchDocumentReference) {
        this.despatchDocumentReference = despatchDocumentReference;
    }

    public List<DocumentReferenceType> getReceiptDocumentReference() {
        if (receiptDocumentReference == null) {
            receiptDocumentReference = new ArrayList<DocumentReferenceType>();
        }
        return this.receiptDocumentReference;
    }

    public void setReceiptDocumentReference(List<DocumentReferenceType> receiptDocumentReference) {
        this.receiptDocumentReference = receiptDocumentReference;
    }

    public List<DocumentReferenceType> getContractDocumentReference() {
        if (contractDocumentReference == null) {
            contractDocumentReference = new ArrayList<DocumentReferenceType>();
        }
        return this.contractDocumentReference;
    }

    public void setContractDocumentReference(List<DocumentReferenceType> contractDocumentReference) {
        this.contractDocumentReference = contractDocumentReference;
    }

    public List<DocumentReferenceType> getAdditionalDocumentReference() {
        if (additionalDocumentReference == null) {
            additionalDocumentReference = new ArrayList<DocumentReferenceType>();
        }
        return this.additionalDocumentReference;
    }

    public void setAdditionalDocumentReference(List<DocumentReferenceType> additionalDocumentReference) {
        this.additionalDocumentReference = additionalDocumentReference;
    }

    public List<SignatureType> getSignature() {
        if (signature == null) {
            signature = new ArrayList<SignatureType>();
        }
        return this.signature;
    }

    public void setSignature(List<SignatureType> signature) {
        this.signature = signature;
    }

    public SupplierPartyType getAccountingSupplierParty() {
        return accountingSupplierParty;
    }

    public void setAccountingSupplierParty(SupplierPartyType value) {
        this.accountingSupplierParty = value;
    }

    public CustomerPartyType getAccountingCustomerParty() {
        return accountingCustomerParty;
    }

    public void setAccountingCustomerParty(CustomerPartyType value) {
        this.accountingCustomerParty = value;
    }

    public PartyType getPayeeParty() {
        return payeeParty;
    }

    public void setPayeeParty(PartyType value) {
        this.payeeParty = value;
    }

    public PartyType getTaxRepresentativeParty() {
        return taxRepresentativeParty;
    }

    public void setTaxRepresentativeParty(PartyType value) {
        this.taxRepresentativeParty = value;
    }

    public ExchangeRateType getTaxExchangeRate() {
        return taxExchangeRate;
    }

    public void setTaxExchangeRate(ExchangeRateType value) {
        this.taxExchangeRate = value;
    }

    public ExchangeRateType getPricingExchangeRate() {
        return pricingExchangeRate;
    }

    public void setPricingExchangeRate(ExchangeRateType value) {
        this.pricingExchangeRate = value;
    }

    public ExchangeRateType getPaymentExchangeRate() {
        return paymentExchangeRate;
    }

    public void setPaymentExchangeRate(ExchangeRateType value) {
        this.paymentExchangeRate = value;
    }

    public ExchangeRateType getPaymentAlternativeExchangeRate() {
        return paymentAlternativeExchangeRate;
    }

    public void setPaymentAlternativeExchangeRate(ExchangeRateType value) {
        this.paymentAlternativeExchangeRate = value;
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

    public MonetaryTotalType getLegalMonetaryTotal() {
        return legalMonetaryTotal;
    }

    public void setLegalMonetaryTotal(MonetaryTotalType value) {
        this.legalMonetaryTotal = value;
    }

    public List<CreditNoteLineType> getCreditNoteLine() {
        if (creditNoteLine == null) {
            creditNoteLine = new ArrayList<CreditNoteLineType>();
        }
        return this.creditNoteLine;
    }

    public void setCreditNoteLine(List<CreditNoteLineType> creditNoteLine) {
        this.creditNoteLine = creditNoteLine;
    }

    public String getId() {
        return id;
    }

    public void setId(String value) {
        this.id = value;
    }

}
