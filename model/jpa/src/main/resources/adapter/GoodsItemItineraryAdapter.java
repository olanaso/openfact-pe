package org.openfact.models.jpa.entities.ubl;
import java.util.List; 
import java.util.ArrayList; 
import org.openfact.models.jpa.entities.ublType.*;


/**
 * A document providing details relating to a transport service, such as transport
 * movement, identification of equipment and goods, subcontracted service
 * providers, etc.
 * @author Erik
 * @version 2.1
 * @created 07-Set.-2016 9:20:43 a. m.
 */
public class GoodsItemItineraryAdapter{

	/**
	 * Indicates whether this document is a copy (true) or not (false).
	 */
	private IndicatorType copyIndicator; 
	/**
	 * Identifies a user-defined customization of UBL for a specific use.
	 */
	private IdentifierType customizationID; 
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
	 * Free-form text pertinent to this document, conveying information that is not
	 * contained explicitly in other structures.
	 */
	private TextType note; 
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
	 * The TransportExecutionPlanAdapter associated with this GoodsItemItinerary.
	 */
	private IdentifierType transportExecutionPlanReferenceID; 
	/**
	 * Identifies the earliest version of the UBL 2 schema for this document type that
	 * defines all of the elements that might be encountered in the current instance.
	 */
	private IdentifierType UBLVersionID; 
	/**
	 * A universally unique identifier for an instance of this document.
	 */
	private IdentifierType UUID; 
	/**
	 * Identifies a version of a GoodsItemItineraryAdapter in order to distinguish updates.
	 */
	private IdentifierType versionID; 
	private ConsignmentAdapter referencedConsignment; 
	private GoodsItemAdapter referencedGoodsItem; 
	private PackageAdapter referencedPackage; 
	private PartyAdapter receiverParty; 
	private PartyAdapter senderParty; 
	private List<SignatureAdapter> signatures = new ArrayList<>(); 
	private TransportEquipmentAdapter referencedTransportEquipment; 
	private List<TransportationSegmentAdapter> transportationSegments = new ArrayList<>(); 

}
