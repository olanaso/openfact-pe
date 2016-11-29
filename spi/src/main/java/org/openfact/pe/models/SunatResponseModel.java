package org.openfact.pe.models;

import org.openfact.models.CreditNoteSendEventModel;
import org.openfact.models.DebitNoteSendEventModel;
import org.openfact.models.InvoiceSendEventModel;

public interface SunatResponseModel {

	String getId();

	boolean getResult();

	void setResult(boolean result);

	byte[] getDocumentResponse();

	void setDocumentResponse(byte[] documentResponse);

	String getResponseCode();

	void setResponseCode(String responseCode);

	String getErrorMessage();

	void setErrorMessage(String errorMenssage);

	String getTicket();

	void setTicket(String ticket);

	InvoiceSendEventModel getInvoiceSendEvent();

	CreditNoteSendEventModel getCreditNoteSendEvent();

	DebitNoteSendEventModel getDebitNoteSendEvent();

	PerceptionSendEventModel getPerceptionSendEvent();

	RetentionSendEventModel getRetentionSendEvent();

	SummaryDocumentsSendEventModel getSummaryDocumentsSendEvent();

	VoidedDocumentsSendEventModel getVoidedDocumentsSendEvent();

	void setInvoiceSendEvent(InvoiceSendEventModel invoiceSendEvent);

	void setCreditNoteSendEvent(CreditNoteSendEventModel creditNoteSendEvent);

	void setDebitNoteSendEvent(DebitNoteSendEventModel debitNoteSendEvent);

	void setPerceptionSendEvent(PerceptionSendEventModel perceptionSendEvent);

	void setRetentionSendEvent(RetentionSendEventModel retentionSendEvent);

	void setSummaryDocumentsSendEvent(SummaryDocumentsSendEventModel summaryDocumentsSendEvent);

	void setVoidedDocumentsSendEvent(VoidedDocumentsSendEventModel voidedDocumentsSendEvent);

}
