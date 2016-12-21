package org.openfact.pe.models;

import java.math.BigDecimal;
import java.util.List;

import org.openfact.models.AllowanceChargeModel;
import org.openfact.models.CustomerPartyModel;
import org.openfact.models.TaxTotalModel;

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


    List<AllowanceChargeModel> getAllowanceCharge();

    AllowanceChargeModel addAllowanceCharge();

    List<TaxTotalModel> getTaxTotal();

    TaxTotalModel addTaxTotal();

}
