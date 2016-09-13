package org.openfact.models.ubl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.openfact.models.ubl.type.*;

/**
 * A document used to request a QuotationModel for goods and services from a
 * Seller.
 * 
 * @author Erik
 * @version 2.0
 * @created 07-Set.-2016 9:21:19 a. m.
 */
public class RequestForQuotationModel {

    /**
     * Indicates whether this document is a copy (true) or not (false).
     */
    private boolean copyIndicator;
    /**
     * Identifies a user-defined customization of UBL for a specific use.
     */
    private IdentifierModel customizationID;
    /**
     * An identifier for this document, assigned by the sender.
     */
    private IdentifierModel ID;
    /**
     * The date, assigned by the sender, on which this document was issued.
     */
    private LocalDate issueDate;
    /**
     * The time, assigned by the sender, at which this document was issued.
     */
    private LocalTime issueTime;
    /**
     * The number of requestForQuotationLines in this document.
     */
    private BigDecimal lineCountNumeric;
    /**
     * Free-form text pertinent to this document, conveying information that is
     * not contained explicitly in other structures.
     */
    private TextModel note;
    /**
     * The currency that the Seller should use to price the Quotation.
     */
    private CurrencyCodeModel codeTypePricingCurrencyCode;
    /**
     * Identifies an instance of executing a profile, to associate all
     * transactions in a collaboration.
     */
    private IdentifierModel profileExecutionID;
    /**
     * Identifies a user-defined profile of the customization of UBL being used.
     */
    private IdentifierModel profileID;
    /**
     * The due date for submission of the Quotation.
     */
    private LocalDate submissionDueDate;
    /**
     * Identifies the earliest version of the UBL 2 schema for this document
     * type that defines all of the elements that might be encountered in the
     * current instance.
     */
    private IdentifierModel UBLVersionID;
    /**
     * A universally unique identifier for an instance of this document.
     */
    private IdentifierModel UUID;
    private List<ContractModel> contracts = new ArrayList<>();
    private CountryModel destinationCountry;
    private CustomerPartyModel buyerCustomerParty;
    private CustomerPartyModel originatorCustomerParty;
    private List<DeliveryModel> deliveries = new ArrayList<>();
    private List<DeliveryTermsModel> deliveriesTerms = new ArrayList<>();
    private DocumentReferenceModel additionalDocumentReference;
    private DocumentReferenceModel catalogueDocumentReference;
    private PeriodModel requestedValidityPeriod;
    private List<RequestForQuotationLineModel> requestForQuotationLines = new ArrayList<>();
    private List<SignatureModel> signatures = new ArrayList<>();
    private SupplierPartyModel sellerSupplierParty;

}
