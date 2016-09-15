package org.openfact.models.ubl;

import java.util.ArrayList;
import java.util.List;

import org.openfact.models.ubl.type.*;

/**
 * A class to describe the consumption of a utility.
 * 
 * @author Erik
 * @version 2.1
 * @created 07-Set.-2016 9:13:45 a. m.
 */
public class ConsumptionModel {

    /**
     * A code identifying the type of the UtilityStatementModel required for
     * this consumption. Explains the kind of utility the statement is about,
     * e.g.. "gas", "electricity", "telephone"
     */
    private CodeModel utilityStatementTypeCode;
    private List<AllowanceChargeModel> allowanceCharges = new ArrayList<>();
    private List<EnergyWaterSupplyModel> energyWaterSupplies = new ArrayList<>();
    private MonetaryTotalModel legalMonetaryTotal;
    private PeriodModel mainPeriod;
    private List<TaxTotalModel> taxTotals = new ArrayList<>();
    private List<TelecommunicationsSupplyModel> telecommunicationsSupplies = new ArrayList<>();

}