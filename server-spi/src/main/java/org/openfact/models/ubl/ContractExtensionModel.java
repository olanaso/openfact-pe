package org.openfact.models.ubl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.openfact.models.ubl.type.*;

/**
 * A class to describe possible extensions to a contract.
 * 
 * @author Erik
 * @version 2.1
 * @created 07-Set.-2016 9:14:03 a. m.
 */
public class ContractExtensionModel {

    /**
     * The maximum allowed number of contract extensions.
     */
    private BigDecimal maximumNumberNumeric;
    /**
     * The fixed minimum number of contract extensions or renewals.
     */
    private BigDecimal minimumNumberNumeric;
    /**
     * A description for the possible options that can be carried out during the
     * execution of the contract.
     */
    private TextModel optionsDescription;
    private PeriodModel optionValidityPeriod;
    private List<RenewalModel> renewals = new ArrayList<>();

}
