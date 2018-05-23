package org.openfact.truststore;

import org.jboss.logging.Logger;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.Optional;

@Startup
@Singleton(name = "FileTruststoreProvider")
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class FileTruststoreProvider implements TruststoreProvider {

    private static final Logger logger = Logger.getLogger(FileTruststoreProvider.class);

    private HostnameVerificationPolicy policy;
    private KeyStore truststore;

    @Inject
    @ConfigurationValue("org.openfact.truststore.enable")
    private Optional<Boolean> truststoreEnabled;

    @Inject
    @ConfigurationValue("org.openfact.truststore.file")
    private Optional<String> truststoreFile;

    @Inject
    @ConfigurationValue("org.openfact.truststore.password")
    private Optional<String> truststorePassword;

    @Inject
    @ConfigurationValue("org.openfact.truststore.hostname-verification-policy")
    private Optional<String> truststoreHostnameVerificationPolicy;

    @PostConstruct
    private void init() {
        if (!truststoreEnabled.orElse(Boolean.FALSE)) {
            return;
        }

        String storepath = truststoreFile.orElseThrow(() -> new IllegalStateException("invalid org.openfact.truststore.file"));
        String pass = truststorePassword.orElseThrow(() -> new IllegalStateException("invalid org.openfact.truststore.password"));
        String policy = truststoreHostnameVerificationPolicy.orElseThrow(() -> new IllegalStateException("invalid org.openfact.truststore.hostname-verification-policy"));
        Boolean disabled = truststoreEnabled.get();

        // if "truststore" . "file" is not configured then it is disabled
        if (storepath == null && pass == null && policy == null && disabled == null) {
            return;
        }

        // if explicitly disabled
        if (disabled != null && disabled) {
            return;
        }

        HostnameVerificationPolicy verificationPolicy = null;
        KeyStore truststore = null;

        if (storepath == null) {
            throw new RuntimeException("Attribute 'file' missing in 'truststore':'file' configuration");
        }
        if (pass == null) {
            throw new RuntimeException("Attribute 'password' missing in 'truststore':'file' configuration");
        }

        try {
            truststore = loadStore(storepath, pass == null ? null : pass.toCharArray());
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize TruststoreProviderFactory: " + new File(storepath).getAbsolutePath(), e);
        }
        if (policy == null) {
            verificationPolicy = HostnameVerificationPolicy.WILDCARD;
        } else {
            try {
                verificationPolicy = HostnameVerificationPolicy.valueOf(policy);
            } catch (Exception e) {
                throw new RuntimeException("Invalid value for 'hostname-verification-policy': " + policy + " (must be one of: ANY, WILDCARD, STRICT)");
            }
        }

        this.truststore = truststore;
        this.policy = verificationPolicy;
        logger.debug("File trustore provider initialized: " + new File(storepath).getAbsolutePath());
    }

    @Lock(LockType.READ)
    @Override
    public HostnameVerificationPolicy getPolicy() {
        return policy;
    }

    @Lock(LockType.READ)
    @Override
    public KeyStore getTruststore() {
        return truststore;
    }

    private KeyStore loadStore(String path, char[] password) throws Exception {
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        InputStream is = new FileInputStream(path);
        try {
            ks.load(is, password);
            return ks;
        } finally {
            try {
                is.close();
            } catch (IOException ignored) {
            }
        }
    }

}
