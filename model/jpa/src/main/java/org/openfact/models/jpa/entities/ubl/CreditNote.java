package org.openfact.models.jpa.entities.ubl;


/**
 * A document used to specify credits due to the Debtor from the Creditor.
 * @author Erik
 * @version 2.0
 * @created 07-Set.-2016 9:20:25 a. m.
 */
public class CreditNote {

	/**
	 * The buyer's accounting code, applied to the Credit Note as a whole, expressed
	 * as text.
	 */
	private String AccountingCost;
	/**
	 * The buyer's accounting code, applied to the Credit Note as a whole.
	 */
	private String AccountingCostCode;
	/**
	 * A reference provided by the buyer used for internal routing of the document.
	 */
	private String BuyerReference;
	/**
	 * Indicates whether this document is a copy (true) or not (false).
	 */
	private boolean CopyIndicator;
	/**
	 * A code signifying the type of the Credit Note.
	 */
	private String CreditNoteTypeCode;
	/**
	 * Identifies a user-defined customization of UBL for a specific use.
	 */
	private String CustomizationID;
	/**
	 * A code signifying the default currency for this document.
	 */
	private Currency_ String DocumentCurrencyCode;
	/**
	 * An identifier for this document, assigned by the sender.
	 */
	private String ID;
	/**
	 * The date, assigned by the sender, on which this document was issued.
	 */
	private LocalDate IssueDate;
	/**
	 * The time, assigned by the sender, at which this document was issued.
	 */
	private LocalTime IssueTime;
	/**
	 * The number of Credit Note Lines in the document.
	 */
	private BigDecimal LineCountNumeric;
	/**
	 * Free-form text pertinent to this document, conveying information that is not
	 * contained explicitly in other structures.
	 */
	private String Note;
	/**
	 * A code signifying the alternative currency used for payment in the Credit Note.
	 */
	private Currency_ String PaymentAlternativeCurrencyCode;
	/**
	 * A code signifying the currency used for payment in the Credit Note.
	 */
	private Currency_ String PaymentCurrencyCode;
	/**
	 * A code signifying the currency used for prices in the Credit Note.
	 */
	private Currency_ String PricingCurrencyCode;
	/**
	 * Identifies an instance of executing a profile, to associate all transactions in
	 * a collaboration.
	 */
	private String ProfileExecutionID;
	/**
	 * Identifies a user-defined profile of the customization of UBL being used.
	 */
	private String ProfileID;
	/**
	 * A code signifying the currency used for tax amounts in the Credit Note.
	 */
	private Currency_ String TaxCurrencyCode;
	/**
	 * The date of the Credit Note, used to indicate the point at which tax becomes
	 * applicable.
	 */
	private LocalDate TaxPointDate;
	/**
	 * Identifies the earliest version of the UBL 2 schema for this document type that
	 * defines all of the elements that might be encountered in the current instance.
	 */
	private String UBLVersionID;
	/**
	 * A universally unique identifier for an instance of this document.
	 */
	private String UUID;
	private Allowance Charge m_Allowance Charge;
	private Billing Reference m_Billing Reference;
	private Credit Note Line m_Credit Note Line;
	private CustomerParty Accounting CustomerParty;
	private CustomerParty Buyer CustomerParty;
	private Delivery m_Delivery;
	private Delivery Terms m_Delivery Terms;
	private DocumentReference Statement DocumentReference;
	private DocumentReference Originator DocumentReference;
	private DocumentReference Contract DocumentReference;
	private DocumentReference Receipt DocumentReference;
	private DocumentReference Additional DocumentReference;
	private DocumentReference Despatch DocumentReference;
	private ExchangeRate Payment Alternative ExchangeRate;
	private ExchangeRate Payment ExchangeRate;
	private ExchangeRate Tax ExchangeRate;
	private ExchangeRate Pricing ExchangeRate;
	private Monetary Total Legal Monetary Total;
	private Order Reference m_Order Reference;
	private Party Tax Representative Party;
	private Party Payee Party;
	private Payment Means m_Payment Means;
	private PaymentTerms m_PaymentTerms;
	private Period Invoice Period;
	private Response Discrepancy Response;
	private Signature m_Signature;
	private Supplier Party Seller Supplier Party;
	private Supplier Party Accounting Supplier Party;
	private Tax Total m_Tax Total;

	public Credit Note(){

	}

	public void finalize() throws Throwable {

	}
	public CustomerParty getAccounting CustomerParty(){
		return Accounting CustomerParty;
	}

	public Supplier Party getAccounting Supplier Party(){
		return Accounting Supplier Party;
	}

	public String getAccountingCost(){
		return AccountingCost;
	}

	public String getAccountingCostCode(){
		return AccountingCostCode;
	}

	public DocumentReference getAdditional DocumentReference(){
		return Additional DocumentReference;
	}

	public Allowance Charge getAllowance Charge(){
		return m_Allowance Charge;
	}

	public Billing Reference getBilling Reference(){
		return m_Billing Reference;
	}

	public CustomerParty getBuyer CustomerParty(){
		return Buyer CustomerParty;
	}

	public String getBuyerReference(){
		return BuyerReference;
	}

	public DocumentReference getContract DocumentReference(){
		return Contract DocumentReference;
	}

	public boolean getCopyIndicator(){
		return CopyIndicator;
	}

	public Credit Note Line getCredit Note Line(){
		return m_Credit Note Line;
	}

	public String getCreditNoteTypeCode(){
		return CreditNoteTypeCode;
	}

	public String getCustomizationID(){
		return CustomizationID;
	}

	public Delivery getDelivery(){
		return m_Delivery;
	}

	public Delivery Terms getDelivery Terms(){
		return m_Delivery Terms;
	}

	public DocumentReference getDespatch DocumentReference(){
		return Despatch DocumentReference;
	}

	public Response getDiscrepancy Response(){
		return Discrepancy Response;
	}

	public Currency_ String getDocumentCurrencyCode(){
		return DocumentCurrencyCode;
	}

	public String getID(){
		return ID;
	}

	public Period getInvoice Period(){
		return Invoice Period;
	}

	public LocalDate getIssueDate(){
		return IssueDate;
	}

	public LocalTime getIssueTime(){
		return IssueTime;
	}

	public Monetary Total getLegal Monetary Total(){
		return Legal Monetary Total;
	}

	public BigDecimal getLineCountNumeric(){
		return LineCountNumeric;
	}

	public String getNote(){
		return Note;
	}

	public Order Reference getOrder Reference(){
		return m_Order Reference;
	}

	public DocumentReference getOriginator DocumentReference(){
		return Originator DocumentReference;
	}

	public Party getPayee Party(){
		return Payee Party;
	}

	public ExchangeRate getPayment Alternative ExchangeRate(){
		return Payment Alternative ExchangeRate;
	}

	public ExchangeRate getPayment ExchangeRate(){
		return Payment ExchangeRate;
	}

	public Payment Means getPayment Means(){
		return m_Payment Means;
	}

	public PaymentTerms getPaymentTerms(){
		return m_PaymentTerms;
	}

	public Currency_ String getPaymentAlternativeCurrencyCode(){
		return PaymentAlternativeCurrencyCode;
	}

	public Currency_ String getPaymentCurrencyCode(){
		return PaymentCurrencyCode;
	}

	public ExchangeRate getPricing ExchangeRate(){
		return Pricing ExchangeRate;
	}

	public Currency_ String getPricingCurrencyCode(){
		return PricingCurrencyCode;
	}

	public String getProfileExecutionID(){
		return ProfileExecutionID;
	}

	public String getProfileID(){
		return ProfileID;
	}

	public DocumentReference getReceipt DocumentReference(){
		return Receipt DocumentReference;
	}

	public Supplier Party getSeller Supplier Party(){
		return Seller Supplier Party;
	}

	public Signature getSignature(){
		return m_Signature;
	}

	public DocumentReference getStatement DocumentReference(){
		return Statement DocumentReference;
	}

	public ExchangeRate getTax ExchangeRate(){
		return Tax ExchangeRate;
	}

	public Party getTax Representative Party(){
		return Tax Representative Party;
	}

	public Tax Total getTax Total(){
		return m_Tax Total;
	}

	public Currency_ String getTaxCurrencyCode(){
		return TaxCurrencyCode;
	}

	public LocalDate getTaxPointDate(){
		return TaxPointDate;
	}

	public String getUBLVersionID(){
		return UBLVersionID;
	}

	public String getUUID(){
		return UUID;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setAccounting CustomerParty(CustomerParty newVal){
		Accounting CustomerParty = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setAccounting Supplier Party(Supplier Party newVal){
		Accounting Supplier Party = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setAccountingCost(String newVal){
		AccountingCost = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setAccountingCostCode(String newVal){
		AccountingCostCode = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setAdditional DocumentReference(DocumentReference newVal){
		Additional DocumentReference = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setAllowance Charge(Allowance Charge newVal){
		m_Allowance Charge = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setBilling Reference(Billing Reference newVal){
		m_Billing Reference = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setBuyer CustomerParty(CustomerParty newVal){
		Buyer CustomerParty = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setBuyerReference(String newVal){
		BuyerReference = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setContract DocumentReference(DocumentReference newVal){
		Contract DocumentReference = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setCopyIndicator(boolean newVal){
		CopyIndicator = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setCredit Note Line(Credit Note Line newVal){
		m_Credit Note Line = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setCreditNoteTypeCode(String newVal){
		CreditNoteTypeCode = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setCustomizationID(String newVal){
		CustomizationID = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setDelivery(Delivery newVal){
		m_Delivery = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setDelivery Terms(Delivery Terms newVal){
		m_Delivery Terms = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setDespatch DocumentReference(DocumentReference newVal){
		Despatch DocumentReference = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setDiscrepancy Response(Response newVal){
		Discrepancy Response = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setDocumentCurrencyCode(Currency_ String newVal){
		DocumentCurrencyCode = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setID(String newVal){
		ID = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setInvoice Period(Period newVal){
		Invoice Period = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setIssueDate(LocalDate newVal){
		IssueDate = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setIssueTime(LocalTime newVal){
		IssueTime = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setLegal Monetary Total(Monetary Total newVal){
		Legal Monetary Total = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setLineCountNumeric(BigDecimal newVal){
		LineCountNumeric = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setNote(String newVal){
		Note = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setOrder Reference(Order Reference newVal){
		m_Order Reference = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setOriginator DocumentReference(DocumentReference newVal){
		Originator DocumentReference = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setPayee Party(Party newVal){
		Payee Party = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setPayment Alternative ExchangeRate(ExchangeRate newVal){
		Payment Alternative ExchangeRate = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setPayment ExchangeRate(ExchangeRate newVal){
		Payment ExchangeRate = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setPayment Means(Payment Means newVal){
		m_Payment Means = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setPaymentTerms(PaymentTerms newVal){
		m_PaymentTerms = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setPaymentAlternativeCurrencyCode(Currency_ String newVal){
		PaymentAlternativeCurrencyCode = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setPaymentCurrencyCode(Currency_ String newVal){
		PaymentCurrencyCode = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setPricing ExchangeRate(ExchangeRate newVal){
		Pricing ExchangeRate = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setPricingCurrencyCode(Currency_ String newVal){
		PricingCurrencyCode = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setProfileExecutionID(String newVal){
		ProfileExecutionID = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setProfileID(String newVal){
		ProfileID = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setReceipt DocumentReference(DocumentReference newVal){
		Receipt DocumentReference = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setSeller Supplier Party(Supplier Party newVal){
		Seller Supplier Party = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setSignature(Signature newVal){
		m_Signature = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setStatement DocumentReference(DocumentReference newVal){
		Statement DocumentReference = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setTax ExchangeRate(ExchangeRate newVal){
		Tax ExchangeRate = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setTax Representative Party(Party newVal){
		Tax Representative Party = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setTax Total(Tax Total newVal){
		m_Tax Total = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setTaxCurrencyCode(Currency_ String newVal){
		TaxCurrencyCode = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setTaxPointDate(LocalDate newVal){
		TaxPointDate = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setUBLVersionID(String newVal){
		UBLVersionID = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setUUID(String newVal){
		UUID = newVal;
	}
}//end Credit Note