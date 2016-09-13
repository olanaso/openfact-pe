package org.openfact.representations.idm.ubl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.openfact.representations.idm.ubl.type.CodeRepresentation;
import org.openfact.representations.idm.ubl.type.IdentifierRepresentation;
import org.openfact.representations.idm.ubl.type.NameRepresentation;
import org.openfact.representations.idm.ubl.type.TextRepresentation;

/**
 * A document to circulate reports of transportation status or changes in status
 * (events) among a group of participants.
 * 
 * @author Erik
 * @version 2.0
 * @created 07-Set.-2016 9:21:46 a. m.
 */
public class TransportationStatusRepresentation {

    /**
     * A reference number assigned by a carrier or its agent to identify a
     * specific shipment, such as a booking reference number when cargo space is
     * reserved prior to loading.
     */
    private IdentifierRepresentation carrierAssignedID;
    /**
     * Identifies a user-defined customization of UBL for a specific use.
     */
    private IdentifierRepresentation customizationID;
    /**
     * A textual description of transportation status.
     */
    private TextRepresentation description;
    /**
     * An identifier for this document, assigned by the sender.
     */
    private IdentifierRepresentation ID;
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
    private NameRepresentation name;
    /**
     * Free-form text pertinent to this document, conveying information that is
     * not contained explicitly in other structures.
     */
    private TextRepresentation note;
    /**
     * An instruction regarding this message.
     */
    private TextRepresentation otherInstruction;
    /**
     * Identifies an instance of executing a profile, to associate all
     * transactions in a collaboration.
     */
    private IdentifierRepresentation profileExecutionID;
    /**
     * Identifies a user-defined profile of the customization of UBL being used.
     */
    private IdentifierRepresentation profileID;
    /**
     * A reference number for a shipping order.
     */
    private IdentifierRepresentation shippingOrderID;
    /**
     * A code signifying the type of status provided in a TransportationStatus
     * document.
     */
    private CodeRepresentation transportationStatusTypeCode;
    /**
     * A code signifying the overall status of transport service execution.
     */
    private CodeRepresentation transportExecutionStatusCode;
    /**
     * Identifies the earliest version of the UBL 2 schema for this document
     * type that defines all of the elements that might be encountered in the
     * current instance.
     */
    private IdentifierRepresentation UBLVersionID;
    /**
     * A universally unique identifier for an instance of this document.
     */
    private IdentifierRepresentation UUID;
    private List<ConsignmentRepresentation> consignments = new ArrayList<>();
    private List<DocumentReferenceRepresentation> documentReferences = new ArrayList<>();
    private List<DocumentReferenceRepresentation> transportationStatusRequestDocumentReference = new ArrayList<>();
    private List<DocumentReferenceRepresentation> transportExecutionPlanDocumentReference = new ArrayList<>();
    private LocationRepresentation statusLocation;
    private PartyRepresentation receiverParty;
    private PartyRepresentation senderParty;
    private PeriodRepresentation statusPeriod;
    private List<SignatureRepresentation> signatures = new ArrayList<>();
    private List<TransportEventRepresentation> transportEvents = new ArrayList<>();
    private List<TransportEventRepresentation> updatedPickupTransportEvent = new ArrayList<>();
    private List<TransportEventRepresentation> updatedDeliveryTransportEvent = new ArrayList<>();

}
