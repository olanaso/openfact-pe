package org.openfact.models.jpa.entities.ubl;import java.math.BigDecimal;import java.time.LocalDate;import java.time.LocalTime;
import java.util.List; 
import java.util.ArrayList; 
import org.openfact.models.jpa.entities.ublType.*;


/**
 * A class to describe a price, expressed in a data structure containing multiple
 * properties (compare with UnstructuredPrice).
 * @author Erik
 * @version 2.0
 * @created 07-Set.-2016 9:16:52 a. m.
 */
public class Price{

	/**
	 * The quantity at which this price applies.
	 */
	private QuantityType BaseQuantity;
	/**
	 * The factor by which the base price unit can be converted to the orderable unit.
	 */
	private RateType OrderableUnitFactorRate;
	/**
	 * The amount of the price.
	 */
	private BigDecimal PriceAmount;
	/**
	 * A reason for a price change.
	 */
	private TextType PriceChangeReason;
	/**
	 * The type of price, expressed as text.
	 */
	private TextType PriceType;
	/**
	 * The type of price, expressed as a code.
	 */
	private CodeType PriceTypeCode;
	private List<AllowanceCharge> AllowanceCharges = new ArrayList<>();
	private ExchangeRate PricingExchangeRate;
	private Period ValidityPeriod;
	private List<PriceList> PriceLists = new ArrayList<>();

}