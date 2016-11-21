package org.openfact.pe.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.openfact.models.ubl.common.PaymentModel;

public interface PerceptionDocumentReferenceModel {

    String getId();

    String getDocumentId();

    LocalDate getIssueDate();

    void setIssueDate(LocalDateTime issueDate);

    BigDecimal getTotalInvoiceAmount();

    void setTotalInvoiceAmount(BigDecimal totalInvoiceAmount);

    PaymentModel getPayment();

    PaymentModel getPaymentAsNotNull();

    PerceptionInformationModel getSunatPerceptionInformation();

    PerceptionInformationModel getSunatPerceptionInformationAsNotNull();

}
