package org.openfact.pe.models;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface RetentionInformationModel {

    String getId();

    BigDecimal getSunatRetentionAmount();

    void setSunatRetentionAmount(BigDecimal sunatRetentionAmount);

    LocalDate getSunatRetentionDate();

    void setSunatRetentionDate(LocalDate sunatRetentionDate);

    BigDecimal getSunatNetTotalPaid();

    void setSunatNetTotalPaid(BigDecimal sunatNetTotalPaid);

    /*ExchangeRateModel getExchangeRate();

    ExchangeRateModel getExchangeRateAsNotNull();*/

}
