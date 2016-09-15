package org.openfact.models.jpa.entities.ubl;import java.math.BigDecimal;import java.time.LocalDate;import java.time.LocalTime;
import java.util.List; 
import java.util.ArrayList; 
import org.openfact.models.jpa.entities.ublType.*;


/**
 * A class to describe a notification requirement.
 * @author Erik
 * @version 2.1
 * @created 07-Set.-2016 9:16:13 a. m.
 */
public class NotificationRequirement{

	/**
	 * A code signifying the type of notification (e.g., pickup status).
	 */
	private CodeType NotificationTypeCode;
	/**
	 * The length of time between the occurrence of a given event and the issuance of
	 * a notification.
	 */
	private MeasureType PostEventNotificationDurationMeasure;
	/**
	 * The length of time to elapse between the issuance of a notification and the
	 * occurrence of the event it relates to.
	 */
	private MeasureType PreEventNotificationDurationMeasure;
	private Location NotificationLocation;
	private Period NotificationPeriod;
	private Party NotifyParty;

}