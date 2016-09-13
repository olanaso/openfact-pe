package org.openfact.models.ubl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.openfact.models.ubl.type.*;

/**
 * A class to define a line in a StatementModel of account.
 * 
 * @author Erik
 * @version 2.0
 * @created 07-Set.-2016 9:17:49 a. m.
 */
public class StatementLineModel {

    /**
     * The balance amount on this statement line.
     */
    private BigDecimal balanceAmount;
    /**
     * An indication that this statement line contains an outstanding balance
     * from the previous bill(s) (true) or does not (false).
     */
    private boolean balanceBroughtForwardIndicator;
    /**
     * The amount credited on this statement line.
     */
    private BigDecimal creditLineAmount;
    /**
     * The amount debited on this statement line.
     */
    private BigDecimal debitLineAmount;
    /**
     * An identifier for this statement line.
     */
    private IdentifierModel ID;
    /**
     * Free-form text conveying information that is not contained explicitly in
     * other structures.
     */
    private TextModel note;
    /**
     * A code signifying the business purpose for this payment.
     */
    private CodeModel paymentPurposeCode;
    /**
     * A universally unique identifier for this statement line.
     */
    private IdentifierModel UUID;
    private List<AllowanceChargeModel> allowanceCharges = new ArrayList<>();
    private List<BillingReferenceModel> billingReferences = new ArrayList<>();
    private CustomerPartyModel buyerCustomerParty;
    private CustomerPartyModel originatorCustomerParty;
    private CustomerPartyModel accountingCustomerParty;
    private List<DocumentReferenceModel> documentReferences = new ArrayList<>();
    private List<ExchangeRateModel> exchangeRates = new ArrayList<>();
    private PartyModel payeeParty;
    private PaymentModel collectedPayment;
    private List<PaymentMeansModel> paymentMeanses = new ArrayList<>();
    private List<PaymentTermsModel> paymentTermses = new ArrayList<>();
    private PeriodModel invoicePeriod;
    private SupplierPartyModel accountingSupplierParty;
    private SupplierPartyModel sellerSupplierParty;

}
