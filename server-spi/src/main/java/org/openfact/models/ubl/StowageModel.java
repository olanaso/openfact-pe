package org.openfact.models.ubl;

import org.openfact.models.ubl.type.*;

/**
 * A class to describe a location on board a means of transport where specified
 * goods or transport equipment have been stowed or are to be stowed.
 * 
 * @author Erik
 * @version 2.0
 * @created 07-Set.-2016 9:17:54 a. m.
 */
public class StowageModel {

    /**
     * Text describing the location.
     */
    private TextModel location;
    /**
     * An identifier for the location.
     */
    private IdentifierModel locationID;
    private DimensionModel measurementDimension;

}
