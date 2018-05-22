package org.openfact.pe.ubl.ubl21.invoice;

import oasis.names.specification.ubl.schema.xsd.invoice_21.InvoiceType;

public abstract class AbstractInvoiceProvider {

    public static InvoiceType resolve(Object o) {
        InvoiceType type;
        if (o instanceof InvoiceType) {
            type = (InvoiceType) o;
        } else {
            throw new IllegalStateException("Object class " + o.getClass().getName() + " should be a children of " + InvoiceType.class.getName());
        }
        return type;
    }

}
