package org.openfact.pe.ubl.ubl21.debitnote;

import oasis.names.specification.ubl.schema.xsd.debitnote_21.DebitNoteType;

public abstract class AbstractDebitNoteProvider {

    public static DebitNoteType resolve(Object o) {
        DebitNoteType type;
        if (o instanceof DebitNoteType) {
            type = (DebitNoteType) o;
        } else {
            throw new IllegalStateException("Object class " + o.getClass().getName() + " should be a children of " + DebitNoteType.class.getName());
        }
        return type;
    }

}
