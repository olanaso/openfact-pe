package org.openfact.models.ubl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.openfact.models.ubl.type.*;

/**
 * A document sent by a contracting party to an economic operator acknowledging
 * receipt of a Tender.
 * 
 * @author Erik
 * @version 2.1
 * @created 07-Set.-2016 9:21:30 a. m.
 */
public class TenderReceiptModel {

    /**
     * An identifier, assigned by the sender, for the process file (i.e.,
     * record) to which this document belongs.
     */
    private IdentifierModel contractFolderID;
    /**
     * Short title of a contract associated with this Tender.
     */
    private TextModel contractName;
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
     * Free-form text pertinent to this document, conveying information that is
     * not contained explicitly in other structures.
     */
    private TextModel note;
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
     * The date, assigned by the sender, on which the TenderReceiptModel was
     * created.
     */
    private LocalDate registeredDate;
    /**
     * The time, assigned by the sender, at which the TenderReceiptModel was
     * created.
     */
    private LocalTime registeredTime;
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
    private DocumentReferenceModel tenderDocumentReference;
    private PartyModel senderParty;
    private PartyModel receiverParty;
    private List<SignatureModel> signatures = new ArrayList<>();

}
