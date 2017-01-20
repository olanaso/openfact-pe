package org.openfact.pe.models;

import org.openfact.models.SendException;

public class SunatSendException extends SendException {

    public SunatSendException(Throwable cause) {
        super(cause);
    }

    public SunatSendException(String message) {
        super(message);
    }

    public SunatSendException(String message, Throwable cause) {
        super(message, cause);
    }

}
