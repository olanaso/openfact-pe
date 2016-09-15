package org.openfact.models.ubl;

import java.math.BigDecimal;

import org.openfact.models.ubl.type.*;

/**
 * A class to describe the bond guarantee of a tenderer or bid submitter's
 * actual entry into a contract in the event that it is the successful bidder.
 * 
 * @author Erik
 * @version 2.1
 * @created 07-Set.-2016 9:15:13 a. m.
 */
public class FinancialGuaranteeModel {

    /**
     * The rate used to calculate the amount of liability in this financial
     * guarantee.
     */
    private RateModel amountRate;
    /**
     * Text describing this financial guarantee.
     */
    private TextModel description;
    /**
     * A code signifying the type of financial guarantee. For instance
     * "Provisional Guarantee" or "Final Guarantee"
     */
    private CodeModel guaranteeTypeCode;
    /**
     * The amount of liability in this financial guarantee.
     */
    private BigDecimal liabilityAmount;
    private PeriodModel constitutionPeriod;

}