package org.openfact.representations.idm.ubl;

import java.util.ArrayList;
import java.util.List;

import org.openfact.representations.idm.ubl.type.CodeRepresentation;
import org.openfact.representations.idm.ubl.type.IdentifierRepresentation;
import org.openfact.representations.idm.ubl.type.QuantityRepresentation;
import org.openfact.representations.idm.ubl.type.TextRepresentation;

/**
 * A class to define a reference to an earlier consumption report (e.g., last
 * year's consumption).
 * 
 * @author Erik
 * @version 2.1
 * @created 07-Set.-2016 9:13:56 a. m.
 */
public class ConsumptionReportReferenceRepresentation {

    /**
     * An identifier for the referenced consumption report.
     */
    private IdentifierRepresentation consumptionReportID;
    /**
     * The reported consumption type, expressed as text.
     */
    private TextRepresentation consumptionType;
    /**
     * The reported consumption type, expressed as a code.
     */
    private CodeRepresentation consumptionTypeCode;
    /**
     * The total quantity consumed during the period of the referenced report.
     */
    private QuantityRepresentation totalConsumedQuantity;
    private List<PeriodRepresentation> periods = new ArrayList<>();

}
