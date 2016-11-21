package org.openfact.pe.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.openfact.models.ubl.common.PaymentModel;

public interface RetentionDocumentReferenceModel {

    String getId();

    String getDocumentId();

    LocalDateTime getIssueDate();

    void setIssueDateTime(LocalDate issueDate);

    BigDecimal getTotalInvoiceAmount();

    void setTotalInvoiceAmount(BigDecimal totalInvoiceAmount);

    PaymentModel getPayment();

    void setPayment(PaymentModel payment);

    RetentionInformationModel getSunatRetentionInformation();

    void setSunatRetentionInformation(RetentionInformationModel sunatRetentionInformation);

}
