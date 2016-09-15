package org.openfact.models.ubl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.openfact.models.ubl.type.*;

/**
 * A transport document describing a shipment It is issued by the party who
 * undertakes to provide transportation services, or undertakes to arrange for
 * their provision, to the party who gives instructions for the transportation
 * services (shipper, consignor,
 * 
 * @author Erik
 * @version 2.0
 * @created 07-Set.-2016 9:21:52 a. m.
 */
public class WaybillModel {

    /**
     * A term used in commerce in reference to certain duties, called ad valorem
     * duties, which are levied on commodities at certain rates per centum on
     * their value.
     */
    private boolean adValoremIndicator;
    /**
     * An identifier (in the form of a reference number) assigned by a carrier
     * or its agent to identify a specific shipment.
     */
    private IdentifierModel carrierAssignedID;
    /**
     * Identifies a user-defined customization of UBL for a specific use.
     */
    private IdentifierModel customizationID;
    /**
     * Value declared by the shipper or his agent solely for the purpose of
     * varying the carrier's level of liability from that provided in the
     * contract of carriage in case of loss or damage to goods or delayed
     * delivery.
     */
    private BigDecimal declaredCarriageValueAmount;
    /**
     * Text describing the contents of the Waybill.
     */
    private TextModel description;
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
     * Text, assigned by the sender, that identifies this document to business
     * users.
     */
    private NameModel name;
    /**
     * Free-form text pertinent to this document, conveying information that is
     * not contained explicitly in other structures.
     */
    private TextModel note;
    /**
     * Other free-text instructions related to the shipment to the forwarders or
     * carriers. This should only be used where such information cannot be
     * represented in other structured information entities within the document.
     */
    private TextModel otherInstruction;
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
     * An identifier (in the form of a reference number) of the Shipping
     * OrderModel or Forwarding Instruction associated with this shipment.
     */
    private IdentifierModel shippingOrderID;
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
    private List<DocumentDistributionModel> documentDistributions = new ArrayList<>();
    private List<DocumentReferenceModel> documentReferences = new ArrayList<>();
    private List<ExchangeRateModel> exchangeRates = new ArrayList<>();
    private PartyModel freightForwarderParty;
    private PartyModel carrierParty;
    private PartyModel consignorParty;
    private List<ShipmentModel> shipments = new ArrayList<>();
    private List<SignatureModel> signatures = new ArrayList<>();

}