FROM jboss/keycloak-adapter-wildfly

ARG KEYCLOAK_VERSION=2.5.5.Final
ARG KEYCLOAK_ADMIN_USER=admin
ARG KEYCLOAK_ADMIN_PASWORD=admin

ARG JBOSS_HOME=/opt/jboss/wildfly

# JBOSS
USER jboss
RUN ${JBOSS_HOME}/bin/add-user.sh admin admin

# KEYCLOAK
ADD openfact-realm.json /${JBOSS_HOME}
ADD https://downloads.jboss.org/keycloak/2.5.5.Final/keycloak-overlay-2.5.5.Final.tar.gz /opt/jboss/wildfly/

USER root
RUN chown -R jboss ${JBOSS_HOME}
RUN chmod -R 700 ${JBOSS_HOME}

RUN tar -xvzf ${JBOSS_HOME}/keycloak-overlay-2.5.5.Final.tar.gz -C ${JBOSS_HOME} && rm -R ${JBOSS_HOME}/keycloak-overlay-2.5.5.Final.tar.gz
RUN ${JBOSS_HOME}/bin/jboss-cli.sh --file=${JBOSS_HOME}/bin/keycloak-install.cli
RUN ${JBOSS_HOME}/bin/add-user-keycloak.sh -u ${KEYCLOAK_ADMIN_USER} -p ${KEYCLOAK_ADMIN_PASWORD}


CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0", "-Dkeycloak.import=/opt/jboss/wildfly/openfact-realm.json"]