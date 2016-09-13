package org.openfact.models.ubl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.openfact.models.ubl.type.*;

/**
 * A class to define a line in a CatalogueModel describing a purchasable item.
 * 
 * @author Erik
 * @version 2.0
 * @created 07-Set.-2016 9:13:19 a. m.
 */
public class CatalogueLineModel {

    /**
     * A code signifying the action required to synchronize this catalogue line.
     * Recommend codes (delete, update, add)
     */
    private CodeModel actionCode;
    /**
     * The numeric quantity of the ordering unit (and units of measure) of the
     * catalogue line.
     */
    private QuantityModel contentUnitQuantity;
    /**
     * A subdivision of a contract or tender covering this catalogue line.
     */
    private TextModel contractSubdivision;
    /**
     * An identifier for the line in the catalogue.
     */
    private IdentifierModel ID;
    /**
     * A code signifying the life cycle status of this catalogue line. Examples
     * are pre-order, end of production
     */
    private CodeModel lifeCycleStatusCode;
    /**
     * The maximum amount of the item described in this catalogue line that can
     * be ordered.
     */
    private QuantityModel maximumOrderQuantity;
    /**
     * The minimum amount of the item described in this catalogue line that can
     * be ordered.
     */
    private QuantityModel minimumOrderQuantity;
    /**
     * Free-form text conveying information that is not contained explicitly in
     * other structures.
     */
    private TextModel note;
    /**
     * An indicator that this catalogue line describes an orderable item (true)
     * or is included for reference purposes only (false).
     */
    private boolean orderableIndicator;
    /**
     * A textual description of the units in which the item described in this
     * catalogue line can be ordered.
     */
    private TextModel orderableUnit;
    /**
     * The number of items that can set the order quantity increments.
     */
    private BigDecimal orderQuantityIncrementNumeric;
    /**
     * A mutually agreed code signifying the level of packaging associated with
     * the item described in this catalogue line.
     */
    private CodeModel packLevelCode;
    /**
     * Text about a warranty (provided by WarrantyParty) for the good or service
     * described in this catalogue line.
     */
    private TextModel warrantyInformation;
    private CustomerPartyModel contractorCustomerParty;
    private List<DocumentReferenceModel> callForTendersDocumentReference = new ArrayList<>();
    private List<DocumentReferenceModel> documentReferences = new ArrayList<>();
    private List<ItemModel> items = new ArrayList<>();
    private List<ItemComparisonModel> itemsComparison = new ArrayList<>();
    private ItemLocationQuantityModel requiredItemLocationQuantity;
    private ItemPropertyModel keywordItemProperty;
    private LineReferenceModel callForTendersLineReference;
    private PartyModel warrantyParty;
    private PeriodModel warrantyValidityPeriod;
    private PeriodModel lineValidityPeriod;
    private RelatedItemModel componentRelatedItem;
    private RelatedItemModel replacedRelatedItem;
    private RelatedItemModel complementaryRelatedItem;
    private RelatedItemModel replacementRelatedItem;
    private RelatedItemModel accessoryRelatedItem;
    private RelatedItemModel requiredRelatedItem;
    private SupplierPartyModel sellerSupplierParty;

}
