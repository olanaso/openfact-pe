package org.openfact.pe.ubl.ubl21.creditnote;

import oasis.names.specification.ubl.schema.xsd.creditnote_21.CreditNoteType;

public abstract class AbstractCreditNoteProvider {

    public static CreditNoteType resolve(Object o) {
        CreditNoteType type;
        if (o instanceof CreditNoteType) {
            type = (CreditNoteType) o;
        } else {
            throw new IllegalStateException("Object class " + o.getClass().getName() + " should be a children of " + CreditNoteType.class.getName());
        }
        return type;
    }

}
