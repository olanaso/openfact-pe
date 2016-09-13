package org.openfact.models.ubl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.openfact.models.ubl.type.*;

/**
 * A class to describe one segment or leg in a transportation service.
 * 
 * @author Erik
 * @version 2.1
 * @created 07-Set.-2016 9:18:51 a. m.
 */
public class TransportationSegmentModel {

    /**
     * A number indicating the order of this segment in the sequence of
     * transportation segments making up a transportation service.
     */
    private BigDecimal sequenceNumeric;
    /**
     * An identifier for the transport execution plan governing this
     * transportation segment.
     */
    private IdentifierModel transportExecutionPlanReferenceID;
    private ConsignmentModel referencedConsignment;
    private PartyModel transportServiceProviderParty;
    private List<ShipmentStageModel> shipmentStages = new ArrayList<>();
    private List<TransportationServiceModel> transportationServices = new ArrayList<>();

}
