package org.openfact.models.ubl;

import java.util.ArrayList;
import java.util.List;

import org.openfact.models.ubl.type.*;

/**
 * A class for information about pricing structure, lead time, and location
 * associated with an item.
 * 
 * @author Erik
 * @version 2.0
 * @created 07-Set.-2016 9:15:47 a. m.
 */
public class ItemLocationQuantityModel {

    /**
     * An indication that the transported item, as delivered, in the stated
     * quantity to the stated location, is subject to an international
     * regulation concerning the carriage of dangerous goods (true) or not
     * (false).
     */
    private boolean hazardousRiskIndicator;
    /**
     * The lead time, i.e., the time taken from the time at which an item is
     * ordered to the time of its delivery.
     */
    private MeasureModel leadTimeMeasure;
    /**
     * The maximum quantity that can be ordered to qualify for a specific price.
     */
    private QuantityModel maximumQuantity;
    /**
     * The minimum quantity that can be ordered to qualify for a specific price.
     */
    private QuantityModel minimumQuantity;
    /**
     * Text describing trade restrictions on the quantity of this item or on the
     * item itself.
     */
    private TextModel tradingRestrictions;
    private AddressModel applicableTerritoryAddress;
    private List<AllowanceChargeModel> allowanceCharges = new ArrayList<>();
    private List<DeliveryUnitModel> deliveryUnits = new ArrayList<>();
    private List<DependentPriceReferenceModel> dependentPriceReferences = new ArrayList<>();
    private List<PackageModel> packages = new ArrayList<>();
    private List<PriceModel> prices = new ArrayList<>();
    private TaxCategoryModel applicableTaxCategory;

}
