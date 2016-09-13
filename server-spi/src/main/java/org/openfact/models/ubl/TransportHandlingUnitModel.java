package org.openfact.models.ubl;

import java.util.ArrayList;
import java.util.List;

import org.openfact.models.ubl.type.*;

/**
 * A class to describe a uniquely identifiable unit consisting of one or more
 * packages, goods items, or pieces of transport equipment.
 * 
 * @author Erik
 * @version 2.0
 * @created 07-Set.-2016 9:18:46 a. m.
 */
public class TransportHandlingUnitModel {

    /**
     * Text describing damage associated with this transport handling unit.
     */
    private TextModel damageRemarks;
    /**
     * The handling required for this transport handling unit, expressed as a
     * code.
     */
    private CodeModel handlingCode;
    /**
     * The handling required for this transport handling unit, expressed as
     * text.
     */
    private TextModel handlingInstructions;
    /**
     * An indicator that the materials contained in this transport handling unit
     * are subject to an international regulation concerning the carriage of
     * dangerous goods (true) or not (false).
     */
    private boolean hazardousRiskIndicator;
    /**
     * An identifier for this transport handling unit.
     */
    private IdentifierModel ID;
    /**
     * Text describing the marks and numbers on this transport handling unit.
     */
    private TextModel shippingMarks;
    /**
     * The total number of goods items in this transport handling unit.
     */
    private QuantityModel totalGoodsItemQuantity;
    /**
     * The total number of packages in this transport handling unit.
     */
    private QuantityModel totalPackageQuantity;
    /**
     * An identifier for use in tracing this transport handling unit, such as
     * the EPC number used in RFID.
     */
    private IdentifierModel traceID;
    /**
     * A code signifying the type of this transport handling unit.
     */
    private CodeModel transportHandlingUnitTypeCode;
    private List<CustomsDeclarationModel> customsDeclarations = new ArrayList<>();
    private DespatchLineModel handlingUnitDespatchLine;
    private DimensionModel floorSpaceMeasurementDimension;
    private DimensionModel palletSpaceMeasurementDimension;
    private DimensionModel measurementDimension;
    private DocumentReferenceModel shipmentDocumentReference;
    private List<GoodsItemModel> goodsItems = new ArrayList<>();
    private List<HazardousGoodsTransitModel> hazardousGoodsTransits = new ArrayList<>();
    private List<PackageModel> packages = new ArrayList<>();
    private List<PackageModel> actualPackage = new ArrayList<>();
    private ReceiptLineModel receivedHandlingUnitReceiptLine;
    private ShipmentModel referencedShipment;
    private List<StatusModel> statuses = new ArrayList<>();
    private TemperatureModel minimumTemperature;
    private TemperatureModel maximumTemperature;
    private List<TransportEquipmentModel> transportEquipments = new ArrayList<>();
    private List<TransportMeansModel> transportMeanses = new ArrayList<>();

}
