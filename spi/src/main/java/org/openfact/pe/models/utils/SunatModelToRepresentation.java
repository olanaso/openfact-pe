package org.openfact.pe.models.utils;

import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.RetentionModel;
import org.openfact.pe.models.SummaryDocumentModel;
import org.openfact.pe.models.SunatResponseModel;
import org.openfact.pe.models.VoidedDocumentModel;
import org.openfact.pe.representations.idm.DocumentRepresentation;
import org.openfact.pe.representations.idm.SunatResponseRepresentation;

public class SunatModelToRepresentation {

	public static DocumentRepresentation toRepresentation(PerceptionModel model) {
		return null;
	}

	public static DocumentRepresentation toRepresentation(RetentionModel model) {
		return null;
	}

	public static DocumentRepresentation toRepresentation(SummaryDocumentModel model) {
		return null;
	}

	public static DocumentRepresentation toRepresentation(VoidedDocumentModel model) {
		return null;
	}

	public static SunatResponseRepresentation toRepresentation(SunatResponseModel model) {
		SunatResponseRepresentation rep = new SunatResponseRepresentation();
		if (model.getDocumentResponse() != null) {
			rep.setDocumentResponse(model.getDocumentResponse());
		}
		if (model.getErrorMessage() != null) {
			rep.setErrorMessage(model.getErrorMessage());
		}
		if (model.getId() != null) {
			rep.setId(model.getId());
		}
		if (model.getResponseCode() != null) {
			rep.setResponseCode(model.getResponseCode());
		}
		rep.setResult(model.getResult());
		if (model.getTicket() != null) {
			rep.setTicket(model.getTicket());
		}
		return rep;
	}

}
