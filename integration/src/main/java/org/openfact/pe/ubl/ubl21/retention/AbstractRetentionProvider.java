package org.openfact.pe.ubl.ubl21.retention;

public abstract class AbstractRetentionProvider {

    public static RetentionType resolve(Object o) {
        RetentionType type;
        if (o instanceof RetentionType) {
            type = (RetentionType) o;
        } else {
            throw new IllegalStateException("Object class " + o.getClass().getName() + " should be a children of " + RetentionType.class.getName());
        }
        return type;
    }

}
