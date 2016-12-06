package org.openfact.pe.models;

import org.openfact.models.OrganizationModel;
import org.openfact.ubl.SendEventModel;

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

	SendEventModel getSendEvent();

	void setSendEvent(SendEventModel sendEvent);

	OrganizationModel getOrganization();

	void setOrganization(OrganizationModel organization);

}
