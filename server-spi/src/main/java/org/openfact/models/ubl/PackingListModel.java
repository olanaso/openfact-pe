package org.openfact.models.ubl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.openfact.models.ubl.type.*;

/**
 * A document describing how goods are packed.
 * 
 * @author Erik
 * @version 2.0
 * @created 07-Set.-2016 9:21:05 a. m.
 */
public class PackingListModel {

    /**
     * Identifies a user-defined customization of UBL for a specific use.
     */
    private IdentifierModel customizationID;
    /**
     * Textual description of the document instance.
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
     * Contains other free-text-based instructions related to the shipment to
     * the forwarders or carriers. This should only be used where such
     * information cannot be represented in other structured information
     * entities within the document.
     */
    private TextModel otherInstruction;
    /**
     * Identifies an instance of executing a profile, to associate all
     * transactions in a collaboration.
     */
    private IdentifierModel profileExecutionID;
    /**
     * Identifies a user-defined profile of the subset of UBL being used.
     */
    private IdentifierModel profileID;
    /**
     * Identifies the earliest version of the UBL 2 schema for this document
     * type that defines all of the elements that might be encountered in the
     * current instance.
     */
    private IdentifierModel UBLVersionID;
    /**
     * A universally unique identifier for an instance of this document..
     */
    private IdentifierModel UUID;
    /**
     * Version identifier of a PackingList.
     */
    private IdentifierModel versionID;
    private List<DocumentDistributionModel> documentDistributions = new ArrayList<>();
    private List<DocumentReferenceModel> documentReferences = new ArrayList<>();
    private PartyModel consignorParty;
    private PartyModel carrierParty;
    private PartyModel freightForwarderParty;
    private List<ShipmentModel> shipments = new ArrayList<>();
    private List<SignatureModel> signatures = new ArrayList<>();

}