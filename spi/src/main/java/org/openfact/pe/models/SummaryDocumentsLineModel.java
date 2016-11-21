package org.openfact.pe.models;

import java.math.BigDecimal;
import java.util.List;

import org.openfact.models.ubl.common.AllowanceChargeModel;
import org.openfact.models.ubl.common.BillingReferenceModel;
import org.openfact.models.ubl.common.CustomerPartyModel;
import org.openfact.models.ubl.common.PaymentModel;
import org.openfact.models.ubl.common.StatusModel;
import org.openfact.models.ubl.common.TaxTotalModel;

public interface SummaryDocumentsLineModel {

    String getId();

    String getDocumentId();

    String getLineId();

    void setLineId(String value);

    String getDocumentTypeCode();

    void setDocumentTypeCode(String value);

    String getDocumentSerialId();

    void setDocumentSerialId(String value);

    String getStartDocumentNumberId();

    void setStartDocumentNumberId(String value);

    String getEndDocumentNumberId();

    void setEndDocumentNumberId(String value);

    BigDecimal getTotalAmount();

    void setTotalAmount(BigDecimal value);

    CustomerPartyModel getAccountingCustomerParty();

    CustomerPartyModel getAccountingCustomerPartyAsNotNull();

    BillingReferenceModel getBillingReference();

    BillingReferenceModel getBillingReferenceAsNotNull();

    StatusModel getStatus();

    StatusModel getStatusAsNotNull();

    List<PaymentModel> getBillingPayment();

    PaymentModel addBillingPayment();

    List<AllowanceChargeModel> getAllowanceCharge();

    AllowanceChargeModel addAllowanceCharge();

    List<TaxTotalModel> getTaxTotal();

    TaxTotalModel addTaxTotal();

}
