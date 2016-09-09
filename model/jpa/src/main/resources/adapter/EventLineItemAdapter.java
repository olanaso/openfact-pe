package org.openfact.models.jpa.entities.ubl;
import java.util.List; 
import java.util.ArrayList; 
import org.openfact.models.jpa.entities.ublType.*;


/**
 * A class to define a line item describing the expected impacts associated with a
 * retail event involving a specific product at a specific location.
 * @author Erik
 * @version 2.1
 * @created 07-Set.-2016 9:14:56 a. m.
 */
public class EventLineItemAdapter{

	/**
	 * The number of this event line item.
	 */
	private NumericType lineNumberNumeric; 
	private ItemAdapter supplyItem; 
	private LocationAdapter participatingLocationsLocation; 
	private List<RetailPlannedImpactAdapter> retailPlannedImpacts = new ArrayList<>(); 

}
