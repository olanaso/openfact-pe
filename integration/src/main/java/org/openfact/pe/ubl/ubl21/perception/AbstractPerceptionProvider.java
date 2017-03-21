package org.openfact.pe.ubl.ubl21.perception;

public abstract class AbstractPerceptionProvider {

    public static PerceptionType resolve(Object o) {
        PerceptionType type;
        if (o instanceof PerceptionType) {
            type = (PerceptionType) o;
        } else {
            throw new IllegalStateException("Object class " + o.getClass().getName() + " should be a children of " + PerceptionType.class.getName());
        }
        return type;
    }

}
