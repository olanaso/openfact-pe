package org.openfact.models.ubl;

import org.openfact.models.ubl.type.*;

/**
 * A class to provide information about the preselection of a short list of
 * economic operators for consideration as possible candidates in a tendering
 * process.
 * 
 * @author Erik
 * @version 2.1
 * @created 07-Set.-2016 9:14:41 a. m.
 */
public class EconomicOperatorShortListModel {

    /**
     * The number of economic operators expected to be on the short list.
     */
    private QuantityModel expectedQuantity;
    /**
     * Text describing the criteria used to restrict the number of candidates.
     */
    private TextModel limitationDescription;
    /**
     * The maximum number of economic operators on the short list.
     */
    private QuantityModel maximumQuantity;
    /**
     * The minimum number of economic operators on the short list.
     */
    private QuantityModel minimumQuantity;
    private PartyModel preSelectedParty;

}