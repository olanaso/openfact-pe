package org.openfact.models.ubl;

import java.math.BigDecimal;

import org.openfact.models.ubl.type.*;

/**
 * A class to define a budget account.
 * 
 * @author Erik
 * @version 2.1
 * @created 07-Set.-2016 9:13:12 a. m.
 */
public class BudgetAccountModel {

    /**
     * The number of the year for this budget account, e.g. 2012
     */
    private BigDecimal budgetYearNumeric;
    /**
     * An identifier for the budget account, typically an internal accounting
     * reference.
     */
    private IdentifierModel ID;
    private ClassificationSchemeModel requiredClassificationScheme;

}
