package org.openfact.events.log;

import org.jboss.logging.Logger;
import org.openfact.events.EventListenerProvider;
import org.openfact.events.EventListenerType;
import org.openfact.events.admin.AdminEvent;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.Optional;

@Stateless
public class JBossLoggingEventListenerProvider implements EventListenerProvider {

    private static final Logger logger = Logger.getLogger("org.openfact.events");
    public static final String NAME = "jboss-logging";

    private Logger.Level successLevel;
    private Logger.Level errorLevel;

    @Inject
    @ConfigurationValue("org.openfact.logging.success-level")
    private Optional<String> loggingSuccessLevel;

    @Inject
    @ConfigurationValue("org.openfact.logging.error-level")
    private Optional<String> loggingErrorLevel;

    @PostConstruct
    public void init() {
        successLevel = Logger.Level.valueOf(loggingSuccessLevel.orElse("debug").toUpperCase());
        errorLevel = Logger.Level.valueOf(loggingErrorLevel.orElse("warn").toUpperCase());
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void onEvent(@Observes @EventListenerType(value = NAME) AdminEvent adminEvent) {
        Logger.Level level = adminEvent.getError() != null ? errorLevel : successLevel;

        if (logger.isEnabled(level)) {
            StringBuilder sb = new StringBuilder();

            sb.append("operationType=");
            sb.append(adminEvent.getOperationType());
            sb.append(", organizationId=");
            sb.append(adminEvent.getAuthDetails().getOrganizationId());
            sb.append(", userId=");
            sb.append(adminEvent.getAuthDetails().getUserId());
            sb.append(", ipAddress=");
            sb.append(adminEvent.getAuthDetails().getIpAddress());
            sb.append(", resourcePath=");
            sb.append(adminEvent.getResourcePath());

            if (adminEvent.getError() != null) {
                sb.append(", error=");
                sb.append(adminEvent.getError());
            }

            logger.log(logger.isTraceEnabled() ? Logger.Level.TRACE : level, sb.toString());
        }
    }

}
