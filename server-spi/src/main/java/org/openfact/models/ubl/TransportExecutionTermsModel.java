package org.openfact.models.ubl;

import java.util.ArrayList;
import java.util.List;

import org.openfact.models.ubl.type.*;

/**
 * A class to describe terms applying to a transport execution plan.
 * 
 * @author Erik
 * @version 2.1
 * @created 07-Set.-2016 9:18:44 a. m.
 */
public class TransportExecutionTermsModel {

    /**
     * Text describing conditions applying to a change of these transport
     * execution terms.
     */
    private TextModel changeConditions;
    /**
     * Text describing special terms specified by the transport service
     * provider.
     */
    private TextModel transportServiceProviderSpecialTerms;
    /**
     * Text describing special terms specified by the transport user.
     */
    private TextModel transportUserSpecialTerms;
    private List<DeliveryTermsModel> deliveryTermses = new ArrayList<>();
    private List<EnvironmentalEmissionModel> environmentalEmissions = new ArrayList<>();
    private List<NotificationRequirementModel> notificationRequirements = new ArrayList<>();
    private List<PaymentTermsModel> bonusPaymentTerms = new ArrayList<>();
    private List<PaymentTermsModel> commissionPaymentTerms = new ArrayList<>();
    private List<PaymentTermsModel> penaltyPaymentTerms = new ArrayList<>();
    private List<PaymentTermsModel> serviceChargePaymentTerms = new ArrayList<>();
    private List<PaymentTermsModel> paymentTermses = new ArrayList<>();

}
