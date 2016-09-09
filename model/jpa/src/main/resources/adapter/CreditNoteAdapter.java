package org.openfact.models.jpa.entities.ubl;
import java.util.List; 
import java.util.ArrayList; 
import org.openfact.models.jpa.entities.ublType.*;


/**
 * A document used to specify credits due to the Debtor from the Creditor.
 * @author Erik
 * @version 2.0
 * @created 07-Set.-2016 9:20:25 a. m.
 */
public class CreditNoteAdapter{

	/**
	 * The buyer's accounting code, applied to the CreditNoteAdapter as a whole, expressed
	 * as text.
	 */
	private TextType accountingCost; 
	/**
	 * The buyer's accounting code, applied to the CreditNoteAdapter as a whole.
	 */
	private CodeType accountingCostCode; 
	/**
	 * A reference provided by the buyer used for internal routing of the document.
	 */
	private TextType buyerReference; 
	/**
	 * Indicates whether this document is a copy (true) or not (false).
	 */
	private IndicatorType copyIndicator; 
	/**
	 * A code signifying the type of the CreditNote.
	 */
	private CodeType creditNoteTypeCode; 
	/**
	 * Identifies a user-defined customization of UBL for a specific use.
	 */
	private IdentifierType customizationID; 
	/**
	 * A code signifying the default currency for this document.
	 */
	private CurrencyCodeType codeTypeDocumentCurrencyCode; 
	/**
	 * An identifier for this document, assigned by the sender.
	 */
	private IdentifierType ID; 
	/**
	 * The date, assigned by the sender, on which this document was issued.
	 */
	private DateType issueDate; 
	/**
	 * The time, assigned by the sender, at which this document was issued.
	 */
	private TimeType issueTime; 
	/**
	 * The number of creditNoteLines in the document.
	 */
	private NumericType lineCountNumeric; 
	/**
	 * Free-form text pertinent to this document, conveying information that is not
	 * contained explicitly in other structures.
	 */
	private TextType note; 
	/**
	 * A code signifying the alternative currency used for payment in the CreditNote.
	 */
	private CurrencyCodeType codeTypePaymentAlternativeCurrencyCode; 
	/**
	 * A code signifying the currency used for payment in the CreditNote.
	 */
	private CurrencyCodeType codeTypePaymentCurrencyCode; 
	/**
	 * A code signifying the currency used for prices in the CreditNote.
	 */
	private CurrencyCodeType codeTypePricingCurrencyCode; 
	/**
	 * Identifies an instance of executing a profile, to associate all transactions in
	 * a collaboration.
	 */
	private IdentifierType profileExecutionID; 
	/**
	 * Identifies a user-defined profile of the customization of UBL being used.
	 */
	private IdentifierType profileID; 
	/**
	 * A code signifying the currency used for tax amounts in the CreditNote.
	 */
	private CurrencyCodeType codeTypeTaxCurrencyCode; 
	/**
	 * The date of the CreditNote, used to indicate the point at which tax becomes
	 * applicable.
	 */
	private DateType taxPointDate; 
	/**
	 * Identifies the earliest version of the UBL 2 schema for this document type that
	 * defines all of the elements that might be encountered in the current instance.
	 */
	private IdentifierType UBLVersionID; 
	/**
	 * A universally unique identifier for an instance of this document.
	 */
	private IdentifierType UUID; 
	private List<AllowanceChargeAdapter> allowanceCharges = new ArrayList<>(); 
	private List<BillingReferenceAdapter> billingReferences = new ArrayList<>(); 
	private List<CreditNoteLineAdapter> creditNoteLines = new ArrayList<>(); 
	private CustomerPartyAdapter accountingCustomerParty; 
	private CustomerPartyAdapter buyerCustomerParty; 
	private List<DeliveryAdapter> deliveries = new ArrayList<>(); 
	private List<DeliveryTermsAdapter> deliveriesTerms = new ArrayList<>(); 
	private DocumentReferenceAdapter statementDocumentReference; 
	private DocumentReferenceAdapter originatorDocumentReference; 
	private DocumentReferenceAdapter contractDocumentReference; 
	private DocumentReferenceAdapter receiptDocumentReference; 
	private DocumentReferenceAdapter additionalDocumentReference; 
	private DocumentReferenceAdapter despatchDocumentReference; 
	private ExchangeRateAdapter paymentAlternativeExchangeRate; 
	private ExchangeRateAdapter paymentExchangeRate; 
	private ExchangeRateAdapter taxExchangeRate; 
	private ExchangeRateAdapter pricingExchangeRate; 
	private MonetaryTotalAdapter legalMonetaryTotal; 
	private List<OrderReferenceAdapter> orderReferences = new ArrayList<>(); 
	private PartyAdapter taxRepresentativeParty; 
	private PartyAdapter payeeParty; 
	private List<PaymentMeansAdapter> paymentMeanses = new ArrayList<>(); 
	private List<PaymentTermsAdapter> paymentTermses = new ArrayList<>(); 
	private PeriodAdapter invoicePeriod; 
	private ResponseAdapter discrepancyResponse; 
	private List<SignatureAdapter> signatures = new ArrayList<>(); 
	private SupplierPartyAdapter sellerSupplierParty; 
	private SupplierPartyAdapter accountingSupplierParty; 
	private List<TaxTotalAdapter> taxTotals = new ArrayList<>(); 

}
