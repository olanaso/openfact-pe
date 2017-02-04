package org.openfact.pe.models.enums;

import org.openfact.models.enums.DocumentType;

public enum SunatDocumentType {

    PERCEPTION, RETENTION, VOIDED_DOCUMENTS, SUMMARY;

    public static SunatDocumentType getFromString(String value) {
        SunatDocumentType[] elements = SunatDocumentType.values();
        for (int i = 0; i < elements.length; i++) {
            if(elements[i].toString().equals(value)) {
                return elements[i];
            }
        }
        return null;
    }

}
