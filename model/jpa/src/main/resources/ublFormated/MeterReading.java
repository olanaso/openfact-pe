package org.openfact.models.jpa.entities.ubl;import java.math.BigDecimal;import java.time.LocalDate;import java.time.LocalTime;
import java.util.List; 
import java.util.ArrayList; 
import org.openfact.models.jpa.entities.ublType.*;


/**
 * A class to describe a meter reading.
 * @author Erik
 * @version 2.1
 * @created 07-Set.-2016 9:16:09 a. m.
 */
public class MeterReading{

	/**
	 * Consumption in the period from PreviousMeterReadingDate to
	 * LatestMeterReadingDate.
	 */
	private QuantityType DeliveredQuantity;
	/**
	 * An identifier for this meter reading.
	 */
	private IdentifierType ID;
	/**
	 * The quantity of the latest meter reading.
	 */
	private QuantityType LatestMeterQuantity;
	/**
	 * The date of the latest meter reading.
	 */
	private LocalDate LatestMeterReadingDate;
	/**
	 * The method used for the latest meter reading, expressed as text.
	 */
	private TextType LatestMeterReadingMethod;
	/**
	 * The method used for the latest meter reading, expressed as a code.
	 */
	private CodeType LatestMeterReadingMethodCode;
	/**
	 * Text containing comments on this meter reading.
	 */
	private TextType MeterReadingComments;
	/**
	 * The type of this meter reading, expressed as text.
	 */
	private TextType MeterReadingType;
	/**
	 * The type of this meter reading, expressed as a code.
	 */
	private CodeType MeterReadingTypeCode;
	/**
	 * The quantity of the previous meter reading.
	 */
	private QuantityType PreviousMeterQuantity;
	/**
	 * The date of the previous meter reading.
	 */
	private LocalDate PreviousMeterReadingDate;
	/**
	 * The method used for the previous meter reading, expressed as text.
	 */
	private TextType PreviousMeterReadingMethod;
	/**
	 * The method used for the previous meter reading, expressed as a code.
	 */
	private CodeType PreviousMeterReadingMethodCode;

}