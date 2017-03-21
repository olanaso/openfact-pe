package org.openfact.pe.ubl.ubl21.voideddocument;

import org.openfact.pe.ubl.ubl21.voided.VoidedDocumentsType;

public abstract class AbstractVoidedDocumentProvider {

    public static VoidedDocumentsType resolve(Object o) {
        VoidedDocumentsType type;
        if (o instanceof VoidedDocumentsType) {
            type = (VoidedDocumentsType) o;
        } else {
            throw new IllegalStateException("Object class " + o.getClass().getName() + " should be a children of " + VoidedDocumentsType.class.getName());
        }
        return type;
    }

}
