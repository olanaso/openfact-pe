package org.openfact.pe.models.utils;

import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.RetentionModel;
import org.openfact.pe.models.SummaryDocumentModel;
import org.openfact.pe.models.VoidedDocumentModel;
import org.openfact.pe.representations.idm.RetentionRepresentation;
import org.openfact.pe.representations.idm.SummaryRepresentation;
import org.openfact.pe.representations.idm.VoidedRepresentation;

public class SunatModelToRepresentation {

	public static RetentionRepresentation toRepresentation(PerceptionModel model) {
		RetentionRepresentation rep=new RetentionRepresentation();
		return rep;
	}

	public static RetentionRepresentation toRepresentation(RetentionModel model) {
		return null;
	}

	public static SummaryRepresentation toRepresentation(SummaryDocumentModel model) {
		return null;
	}

	public static VoidedRepresentation toRepresentation(VoidedDocumentModel model) {
		return null;
	}
	

}
