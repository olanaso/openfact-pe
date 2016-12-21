package org.openfact.pe.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface PerceptionLineModel {

    String getId();

    String getRelatedDocumentType();

    void setRelatedDocumentType(String relatedDocumentType);

    String getRelatedDocumentNumber();

    void setRelatedDocumentNumber(String relatedDocumentNumber);

    LocalDate getRelatedIssueDate();

    void setRelatedIssueDate(LocalDate relatedIssueDate);

    String getRelatedDocumentCurrency();

    void setRelatedDocumentCurrency(String relatedDocumentCurrency);

    BigDecimal getTotalDocumentRelated();

    void setTotalDocumentRelated(BigDecimal totalDocumentRelated);

    BigDecimal getTypeChange();

    void setTypeChange(BigDecimal typeChange);

    LocalDate getChangeIssueDate();

    void setChangeIssueDate(LocalDate changeIssueDate);

    BigDecimal getTotalPerceptionPayment();

    void setTotalPerceptionPayment(BigDecimal totalPerceptionPayment);

    String getPerceptionPaymentNumber();

    void setPerceptionPaymentNumber(String perceptionPaymentNumber);

    LocalDate getPerceptionIssueDate();

    void setPerceptionIssueDate(LocalDate perceptionIssueDate);

    BigDecimal getSunatNetPerceptionAmount();

    void setSunatNetPerceptionAmount(BigDecimal sunatNetPerceptionPayment);

    BigDecimal getSunatNetCashed();

    void setSunatNetCashed(BigDecimal netCashed);

}
