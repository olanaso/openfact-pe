package org.openfact.pe.models.utils;

import org.openfact.pe.representations.idm.DocumentRepresentation;
import org.openfact.pe.types.PerceptionType;
import org.openfact.pe.types.RetentionType;
import org.openfact.pe.types.SummaryDocumentsType;
import org.openfact.pe.types.VoidedDocumentsType;

public class RepresentationToType_PE {

    public static PerceptionType toPerceptionType(DocumentRepresentation rep) {
        PerceptionType type = new PerceptionType();

        if (rep.getDescuentoGlobal() != null) {

        }

        return type;
    }

    public static RetentionType toRetentionType(DocumentRepresentation rep) {
        return null;
    }

    public static SummaryDocumentsType toSummaryDocumentType(DocumentRepresentation rep) {
        return null;
    }

    public static VoidedDocumentsType toVoidedDocumentType(DocumentRepresentation rep) {
        return null;
    }

}
