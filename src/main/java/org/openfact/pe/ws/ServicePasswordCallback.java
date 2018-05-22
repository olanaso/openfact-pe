package org.openfact.pe.ws;

import org.apache.wss4j.common.ext.WSPasswordCallback;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;
import java.util.Map;

public class ServicePasswordCallback implements CallbackHandler {

    public ServicePasswordCallback() {
    }

    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        WSPasswordCallback passwordCallback = (WSPasswordCallback) callbacks[0];
        Map<String, String> passThread = ServicePasswordThread.configThread.get();
        try {
            passwordCallback.setPassword(passThread.get("password"));
        } catch (Throwable ex) {
            passwordCallback.setPassword("moddatos");
        }
    }
}