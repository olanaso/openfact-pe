package org.openfact.models;

public class ModelRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private Object[] parameters;

    public ModelRuntimeException() {
    }

    public ModelRuntimeException(String message) {
        super(message);
    }

    public ModelRuntimeException(String message, Object... parameters) {
        super(message);
        this.parameters = parameters;
    }

    public ModelRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModelRuntimeException(Throwable cause) {
        super(cause);
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }
}
