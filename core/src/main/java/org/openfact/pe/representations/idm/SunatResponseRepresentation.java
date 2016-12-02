package org.openfact.pe.representations.idm;

public class SunatResponseRepresentation {
	protected String id;
	protected boolean result;
	protected byte[] documentResponse;
	protected String responseCode;
	protected String errorMessage;
	protected String ticket;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public byte[] getDocumentResponse() {
		return documentResponse;
	}

	public void setDocumentResponse(byte[] documentResponse) {
		this.documentResponse = documentResponse;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

}
