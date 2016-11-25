package org.openfact.pe.models;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface PerceptionInformationModel {

    String getId();

    BigDecimal getSunatPerceptionAmount();

    void setSunatPerceptionAmount(BigDecimal sunatPerceptionAmount);

    LocalDate getSunatPerceptionDate();

    void setSunatPerceptionDate(LocalDate sunatPerceptionDate);

    BigDecimal getSunatNetTotalPaid();

    void setSunatNetTotalPaid(BigDecimal sunatNetTotalPaid);

    /*ExchangeRateModel getExchangeRate();

    ExchangeRateModel getExchangeRateAsNotNull();*/

}