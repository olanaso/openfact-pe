package org.openfact.models.ubl;

import java.util.ArrayList;
import java.util.List;

import org.openfact.models.ubl.type.*;

/**
 * A class to define the terms for awarding a contract.
 * 
 * @author Erik
 * @version 2.1
 * @created 07-Set.-2016 9:13:01 a. m.
 */
public class AwardingTermsModel {

    /**
     * Indicates if the decision is binding on the buyer (true) or not (false).
     */
    private boolean bindingOnBuyerIndicator;
    /**
     * Text describing terms under which the contract is to be awarded.
     */
    private TextModel description;
    /**
     * Indicates if any service contract following the contest will be awarded
     * to the winner or one of the winners of the contest (true) or not (false).
     */
    private boolean followupContractIndicator;
    /**
     * Text describing the exclusion criterion for abnormally low tenders.
     */
    private TextModel lowTendersDescription;
    /**
     * Details of payments to all participants.
     */
    private TextModel paymentDescription;
    /**
     * Number and value of the prizes to be awarded.
     */
    private TextModel prizeDescription;
    /**
     * Indicates whether a prize will be awarded (true) or not (false).
     */
    private boolean prizeIndicator;
    /**
     * Text describing the committee of experts evaluating the subjective
     * criteria for awarding the contract.
     */
    private TextModel technicalCommitteeDescription;
    /**
     * A code signifying the weighting algorithm for awarding criteria. When
     * multiple awarding criteria is used, different weighting and choices
     * management algorithms based upon scores and weights of all award criteria
     * can be used. An algorithm for weighting cri
     */
    private CodeModel weightingAlgorithmCode;
    private List<AwardingCriterionModel> awardingCriteria = new ArrayList<>();
    private PersonModel technicalCommitteePerson;

}
