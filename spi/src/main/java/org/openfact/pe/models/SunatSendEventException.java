package org.openfact.pe.models;

import org.openfact.models.SendEventException;

public class SunatSendEventException extends SendEventException {

    public SunatSendEventException(Throwable cause) {
        super(cause);
    }

    public SunatSendEventException(String message) {
        super(message);
    }

    public SunatSendEventException(String message, Throwable cause) {
        super(message, cause);
    }

}
