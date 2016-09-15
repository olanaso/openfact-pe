//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.09.14 at 11:44:49 AM PET 
//

package org.openfact.models.jpa.entities.ubl;

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
import org.openfact.models.ubl.common.AccountingCostCodeType;
import org.openfact.models.ubl.common.AccountingCostType;
import org.openfact.models.ubl.common.BillingReferenceType;
import org.openfact.models.ubl.common.CopyIndicatorType;
import org.openfact.models.ubl.common.CustomerPartyType;
import org.openfact.models.ubl.common.CustomizationIDType;
import org.openfact.models.ubl.common.DebitNoteLineType;
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
import org.openfact.models.ubl.common.PaymentType;
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

@Entity(name = "DebitNoteType")
@Table(name = "DEBITNOTETYPE")
@Inheritance(strategy = InheritanceType.JOINED)
public class DebitNoteType {

    protected UBLExtensionsType ublExtensions;
    protected UBLVersionIDType ublVersionID;
    protected CustomizationIDType customizationID;
    protected ProfileIDType profileID;
    protected IDType ID;
    protected CopyIndicatorType copyIndicator;
    protected UUIDType uuid;
    protected IssueDateType issueDate;
    protected IssueTimeType issueTime;
    protected List<NoteType> note;
    protected TaxPointDateType taxPointDate;
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
    protected List<PaymentType> prepaidPayment;
    protected ExchangeRateType taxExchangeRate;
    protected ExchangeRateType pricingExchangeRate;
    protected ExchangeRateType paymentExchangeRate;
    protected ExchangeRateType paymentAlternativeExchangeRate;
    protected List<TaxTotalType> taxTotal;
    protected MonetaryTotalType requestedMonetaryTotal;
    protected List<DebitNoteLineType> debitNoteLine;
    protected String id;

    @ManyToOne(targetEntity = UBLExtensionsType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "UBLEXTENSIONS_DEBITNOTETYPE__0")
    public UBLExtensionsType getUBLExtensions() {
        return ublExtensions;
    }

    public void setUBLExtensions(UBLExtensionsType value) {
        this.ublExtensions = value;
    }

    @ManyToOne(targetEntity = UBLVersionIDType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "UBLVERSIONID_DEBITNOTETYPE_H_0")
    public UBLVersionIDType getUBLVersionID() {
        return ublVersionID;
    }

    public void setUBLVersionID(UBLVersionIDType value) {
        this.ublVersionID = value;
    }

    @ManyToOne(targetEntity = CustomizationIDType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "CUSTOMIZATIONID_DEBITNOTETYP_0")
    public CustomizationIDType getCustomizationID() {
        return customizationID;
    }

    public void setCustomizationID(CustomizationIDType value) {
        this.customizationID = value;
    }

    @ManyToOne(targetEntity = ProfileIDType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "PROFILEID_DEBITNOTETYPE_OFID")
    public ProfileIDType getProfileID() {
        return profileID;
    }

    public void setProfileID(ProfileIDType value) {
        this.profileID = value;
    }

    @ManyToOne(targetEntity = IDType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "ID_DEBITNOTETYPE_OFID")
    public IDType getID() {
        return ID;
    }

    public void setID(IDType value) {
        this.ID = value;
    }

    @ManyToOne(targetEntity = CopyIndicatorType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "COPYINDICATOR_DEBITNOTETYPE__0")
    public CopyIndicatorType getCopyIndicator() {
        return copyIndicator;
    }

    public void setCopyIndicator(CopyIndicatorType value) {
        this.copyIndicator = value;
    }

    @ManyToOne(targetEntity = UUIDType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "UUID_DEBITNOTETYPE_OFID")
    public UUIDType getUUID() {
        return uuid;
    }

    public void setUUID(UUIDType value) {
        this.uuid = value;
    }

    @ManyToOne(targetEntity = IssueDateType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "ISSUEDATE_DEBITNOTETYPE_OFID")
    public IssueDateType getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(IssueDateType value) {
        this.issueDate = value;
    }

    @ManyToOne(targetEntity = IssueTimeType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "ISSUETIME_DEBITNOTETYPE_OFID")
    public IssueTimeType getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(IssueTimeType value) {
        this.issueTime = value;
    }

    @OneToMany(targetEntity = NoteType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "NOTE_DEBITNOTETYPE_OFID")
    public List<NoteType> getNote() {
        if (note == null) {
            note = new ArrayList<NoteType>();
        }
        return this.note;
    }

    public void setNote(List<NoteType> note) {
        this.note = note;
    }

    @ManyToOne(targetEntity = TaxPointDateType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "TAXPOINTDATE_DEBITNOTETYPE_H_0")
    public TaxPointDateType getTaxPointDate() {
        return taxPointDate;
    }

    public void setTaxPointDate(TaxPointDateType value) {
        this.taxPointDate = value;
    }

    @ManyToOne(targetEntity = DocumentCurrencyCodeType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "DOCUMENTCURRENCYCODE_DEBITNO_0")
    public DocumentCurrencyCodeType getDocumentCurrencyCode() {
        return documentCurrencyCode;
    }

    public void setDocumentCurrencyCode(DocumentCurrencyCodeType value) {
        this.documentCurrencyCode = value;
    }

    @ManyToOne(targetEntity = TaxCurrencyCodeType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "TAXCURRENCYCODE_DEBITNOTETYP_0")
    public TaxCurrencyCodeType getTaxCurrencyCode() {
        return taxCurrencyCode;
    }

    public void setTaxCurrencyCode(TaxCurrencyCodeType value) {
        this.taxCurrencyCode = value;
    }

    @ManyToOne(targetEntity = PricingCurrencyCodeType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "PRICINGCURRENCYCODE_DEBITNOT_0")
    public PricingCurrencyCodeType getPricingCurrencyCode() {
        return pricingCurrencyCode;
    }

    public void setPricingCurrencyCode(PricingCurrencyCodeType value) {
        this.pricingCurrencyCode = value;
    }

    @ManyToOne(targetEntity = PaymentCurrencyCodeType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "PAYMENTCURRENCYCODE_DEBITNOT_0")
    public PaymentCurrencyCodeType getPaymentCurrencyCode() {
        return paymentCurrencyCode;
    }

    public void setPaymentCurrencyCode(PaymentCurrencyCodeType value) {
        this.paymentCurrencyCode = value;
    }

    @ManyToOne(targetEntity = PaymentAlternativeCurrencyCodeType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "PAYMENTALTERNATIVECURRENCYCO_4")
    public PaymentAlternativeCurrencyCodeType getPaymentAlternativeCurrencyCode() {
        return paymentAlternativeCurrencyCode;
    }

    public void setPaymentAlternativeCurrencyCode(PaymentAlternativeCurrencyCodeType value) {
        this.paymentAlternativeCurrencyCode = value;
    }

    @ManyToOne(targetEntity = AccountingCostCodeType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "ACCOUNTINGCOSTCODE_DEBITNOTE_0")
    public AccountingCostCodeType getAccountingCostCode() {
        return accountingCostCode;
    }

    public void setAccountingCostCode(AccountingCostCodeType value) {
        this.accountingCostCode = value;
    }

    @ManyToOne(targetEntity = AccountingCostType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "ACCOUNTINGCOST_DEBITNOTETYPE_0")
    public AccountingCostType getAccountingCost() {
        return accountingCost;
    }

    public void setAccountingCost(AccountingCostType value) {
        this.accountingCost = value;
    }

    @ManyToOne(targetEntity = LineCountNumericType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "LINECOUNTNUMERIC_DEBITNOTETY_0")
    public LineCountNumericType getLineCountNumeric() {
        return lineCountNumeric;
    }

    public void setLineCountNumeric(LineCountNumericType value) {
        this.lineCountNumeric = value;
    }

    @OneToMany(targetEntity = PeriodType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "INVOICEPERIOD_DEBITNOTETYPE__0")
    public List<PeriodType> getInvoicePeriod() {
        if (invoicePeriod == null) {
            invoicePeriod = new ArrayList<PeriodType>();
        }
        return this.invoicePeriod;
    }

    public void setInvoicePeriod(List<PeriodType> invoicePeriod) {
        this.invoicePeriod = invoicePeriod;
    }

    @OneToMany(targetEntity = ResponseType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "DISCREPANCYRESPONSE_DEBITNOT_0")
    public List<ResponseType> getDiscrepancyResponse() {
        if (discrepancyResponse == null) {
            discrepancyResponse = new ArrayList<ResponseType>();
        }
        return this.discrepancyResponse;
    }

    public void setDiscrepancyResponse(List<ResponseType> discrepancyResponse) {
        this.discrepancyResponse = discrepancyResponse;
    }

    @ManyToOne(targetEntity = OrderReferenceType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "ORDERREFERENCE_DEBITNOTETYPE_0")
    public OrderReferenceType getOrderReference() {
        return orderReference;
    }

    public void setOrderReference(OrderReferenceType value) {
        this.orderReference = value;
    }

    @OneToMany(targetEntity = BillingReferenceType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "BILLINGREFERENCE_DEBITNOTETY_0")
    public List<BillingReferenceType> getBillingReference() {
        if (billingReference == null) {
            billingReference = new ArrayList<BillingReferenceType>();
        }
        return this.billingReference;
    }

    public void setBillingReference(List<BillingReferenceType> billingReference) {
        this.billingReference = billingReference;
    }

    @OneToMany(targetEntity = DocumentReferenceType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "DESPATCHDOCUMENTREFERENCE_DE_0")
    public List<DocumentReferenceType> getDespatchDocumentReference() {
        if (despatchDocumentReference == null) {
            despatchDocumentReference = new ArrayList<DocumentReferenceType>();
        }
        return this.despatchDocumentReference;
    }

    public void setDespatchDocumentReference(List<DocumentReferenceType> despatchDocumentReference) {
        this.despatchDocumentReference = despatchDocumentReference;
    }

    @OneToMany(targetEntity = DocumentReferenceType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "RECEIPTDOCUMENTREFERENCE_DEB_0")
    public List<DocumentReferenceType> getReceiptDocumentReference() {
        if (receiptDocumentReference == null) {
            receiptDocumentReference = new ArrayList<DocumentReferenceType>();
        }
        return this.receiptDocumentReference;
    }

    public void setReceiptDocumentReference(List<DocumentReferenceType> receiptDocumentReference) {
        this.receiptDocumentReference = receiptDocumentReference;
    }

    @OneToMany(targetEntity = DocumentReferenceType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "CONTRACTDOCUMENTREFERENCE_DE_0")
    public List<DocumentReferenceType> getContractDocumentReference() {
        if (contractDocumentReference == null) {
            contractDocumentReference = new ArrayList<DocumentReferenceType>();
        }
        return this.contractDocumentReference;
    }

    public void setContractDocumentReference(List<DocumentReferenceType> contractDocumentReference) {
        this.contractDocumentReference = contractDocumentReference;
    }

    @OneToMany(targetEntity = DocumentReferenceType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "ADDITIONALDOCUMENTREFERENCE__3")
    public List<DocumentReferenceType> getAdditionalDocumentReference() {
        if (additionalDocumentReference == null) {
            additionalDocumentReference = new ArrayList<DocumentReferenceType>();
        }
        return this.additionalDocumentReference;
    }

    public void setAdditionalDocumentReference(List<DocumentReferenceType> additionalDocumentReference) {
        this.additionalDocumentReference = additionalDocumentReference;
    }

    @OneToMany(targetEntity = SignatureType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "SIGNATURE_DEBITNOTETYPE_OFID")
    public List<SignatureType> getSignature() {
        if (signature == null) {
            signature = new ArrayList<SignatureType>();
        }
        return this.signature;
    }

    public void setSignature(List<SignatureType> signature) {
        this.signature = signature;
    }

    @ManyToOne(targetEntity = SupplierPartyType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "ACCOUNTINGSUPPLIERPARTY_DEBI_0")
    public SupplierPartyType getAccountingSupplierParty() {
        return accountingSupplierParty;
    }

    public void setAccountingSupplierParty(SupplierPartyType value) {
        this.accountingSupplierParty = value;
    }

    @ManyToOne(targetEntity = CustomerPartyType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "ACCOUNTINGCUSTOMERPARTY_DEBI_0")
    public CustomerPartyType getAccountingCustomerParty() {
        return accountingCustomerParty;
    }

    public void setAccountingCustomerParty(CustomerPartyType value) {
        this.accountingCustomerParty = value;
    }

    @ManyToOne(targetEntity = PartyType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "PAYEEPARTY_DEBITNOTETYPE_OFID")
    public PartyType getPayeeParty() {
        return payeeParty;
    }

    public void setPayeeParty(PartyType value) {
        this.payeeParty = value;
    }

    @ManyToOne(targetEntity = PartyType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "TAXREPRESENTATIVEPARTY_DEBIT_0")
    public PartyType getTaxRepresentativeParty() {
        return taxRepresentativeParty;
    }

    public void setTaxRepresentativeParty(PartyType value) {
        this.taxRepresentativeParty = value;
    }

    @OneToMany(targetEntity = PaymentType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "PREPAIDPAYMENT_DEBITNOTETYPE_0")
    public List<PaymentType> getPrepaidPayment() {
        if (prepaidPayment == null) {
            prepaidPayment = new ArrayList<PaymentType>();
        }
        return this.prepaidPayment;
    }

    public void setPrepaidPayment(List<PaymentType> prepaidPayment) {
        this.prepaidPayment = prepaidPayment;
    }

    @ManyToOne(targetEntity = ExchangeRateType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "TAXEXCHANGERATE_DEBITNOTETYP_0")
    public ExchangeRateType getTaxExchangeRate() {
        return taxExchangeRate;
    }

    public void setTaxExchangeRate(ExchangeRateType value) {
        this.taxExchangeRate = value;
    }

    @ManyToOne(targetEntity = ExchangeRateType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "PRICINGEXCHANGERATE_DEBITNOT_0")
    public ExchangeRateType getPricingExchangeRate() {
        return pricingExchangeRate;
    }

    public void setPricingExchangeRate(ExchangeRateType value) {
        this.pricingExchangeRate = value;
    }

    @ManyToOne(targetEntity = ExchangeRateType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "PAYMENTEXCHANGERATE_DEBITNOT_0")
    public ExchangeRateType getPaymentExchangeRate() {
        return paymentExchangeRate;
    }

    public void setPaymentExchangeRate(ExchangeRateType value) {
        this.paymentExchangeRate = value;
    }

    @ManyToOne(targetEntity = ExchangeRateType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "PAYMENTALTERNATIVEEXCHANGERA_3")
    public ExchangeRateType getPaymentAlternativeExchangeRate() {
        return paymentAlternativeExchangeRate;
    }

    public void setPaymentAlternativeExchangeRate(ExchangeRateType value) {
        this.paymentAlternativeExchangeRate = value;
    }

    @OneToMany(targetEntity = TaxTotalType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "TAXTOTAL_DEBITNOTETYPE_OFID")
    public List<TaxTotalType> getTaxTotal() {
        if (taxTotal == null) {
            taxTotal = new ArrayList<TaxTotalType>();
        }
        return this.taxTotal;
    }

    public void setTaxTotal(List<TaxTotalType> taxTotal) {
        this.taxTotal = taxTotal;
    }

    @ManyToOne(targetEntity = MonetaryTotalType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "REQUESTEDMONETARYTOTAL_DEBIT_0")
    public MonetaryTotalType getRequestedMonetaryTotal() {
        return requestedMonetaryTotal;
    }

    public void setRequestedMonetaryTotal(MonetaryTotalType value) {
        this.requestedMonetaryTotal = value;
    }

    @OneToMany(targetEntity = DebitNoteLineType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "DEBITNOTELINE_DEBITNOTETYPE__0")
    public List<DebitNoteLineType> getDebitNoteLine() {
        if (debitNoteLine == null) {
            debitNoteLine = new ArrayList<DebitNoteLineType>();
        }
        return this.debitNoteLine;
    }

    public void setDebitNoteLine(List<DebitNoteLineType> debitNoteLine) {
        this.debitNoteLine = debitNoteLine;
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
