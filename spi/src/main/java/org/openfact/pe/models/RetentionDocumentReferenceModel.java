package org.openfact.pe.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface RetentionDocumentReferenceModel {

    String getId();

    String getDocumentId();

    LocalDateTime getIssueDate();

    void setIssueDateTime(LocalDate issueDate);

    BigDecimal getTotalInvoiceAmount();

    void setTotalInvoiceAmount(BigDecimal totalInvoiceAmount);

    /*PaymentModel getPayment();

    void setPayment(PaymentModel payment);*/

    RetentionInformationModel getSunatRetentionInformation();

    void setSunatRetentionInformation(RetentionInformationModel sunatRetentionInformation);

}
