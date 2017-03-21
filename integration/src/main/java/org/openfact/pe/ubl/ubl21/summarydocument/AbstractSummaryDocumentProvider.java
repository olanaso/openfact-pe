package org.openfact.pe.ubl.ubl21.summarydocument;

import org.openfact.pe.ubl.ubl21.summary.SummaryDocumentsType;

public abstract class AbstractSummaryDocumentProvider {

    public static SummaryDocumentsType resolve(Object o) {
        SummaryDocumentsType type;
        if (o instanceof SummaryDocumentsType) {
            type = (SummaryDocumentsType) o;
        } else {
            throw new IllegalStateException("Object class " + o.getClass().getName() + " should be a children of " + SummaryDocumentsType.class.getName());
        }
        return type;
    }

}
