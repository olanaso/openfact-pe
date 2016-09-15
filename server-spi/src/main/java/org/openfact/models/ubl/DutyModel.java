package org.openfact.models.ubl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.openfact.models.ubl.type.*;

/**
 * The charging rate used for both call charging and time dependent charging
 * 
 * @author Erik
 * @version 2.1
 * @created 07-Set.-2016 9:14:38 a. m.
 */
public class DutyModel {

    /**
     * The amount of this duty.
     */
    private BigDecimal amount;
    /**
     * Text describing this duty.
     */
    private TextModel duty;
    /**
     * The type of this charge rate, expressed as a code.
     */
    private CodeModel dutyCode;
    private List<TaxCategoryModel> taxCategories = new ArrayList<>();

}