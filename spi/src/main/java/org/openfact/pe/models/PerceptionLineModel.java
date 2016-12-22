package org.openfact.pe.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface PerceptionLineModel {

    String getId();

    String getRelatedDocumentType();

    void setRelatedDocumentType(String relatedDocumentType);

    String getRelatedDocumentNumber();

    void setRelatedDocumentNumber(String relatedDocumentNumber);

    LocalDateTime getRelatedIssueDateTime();

    void setRelatedIssueDateTime(LocalDateTime relatedIssueDateTime);

    String getRelatedDocumentCurrency();

    void setRelatedDocumentCurrency(String relatedDocumentCurrency);

    BigDecimal getTotalDocumentRelated();

    void setTotalDocumentRelated(BigDecimal totalDocumentRelated);

    BigDecimal getTypeChange();

    void setTypeChange(BigDecimal typeChange);

    LocalDateTime getChangeIssueDateTime();

    void setChangeIssueDateTime(LocalDateTime changeIssueDateTime);

    BigDecimal getTotalPerceptionPayment();

    void setTotalPerceptionPayment(BigDecimal totalPerceptionPayment);

    String getPerceptionPaymentNumber();

    void setPerceptionPaymentNumber(String perceptionPaymentNumber);

    LocalDateTime getPerceptionIssueDateTime();

    void setPerceptionIssueDateTime(LocalDateTime perceptionIssueDateTime);

    BigDecimal getSunatNetPerceptionAmount();

    void setSunatNetPerceptionAmount(BigDecimal sunatNetPerceptionPayment);

    BigDecimal getSunatNetCashed();

    void setSunatNetCashed(BigDecimal netCashed);

}
