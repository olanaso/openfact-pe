FROM jboss/keycloak-adapter-wildfly

ARG KEYCLOAK_VERSION=2.5.5.Final
ARG KEYCLOAK_ADMIN_USER=admin
ARG KEYCLOAK_ADMIN_PASWORD=admin


ADD https://downloads.jboss.org/keycloak/2.5.5.Final/keycloak-overlay-2.5.5.Final.tar.gz /opt/jboss/wildfly/keycloak-overlay-2.5.5.Final.tar.gz
RUN tar -xvzf /opt/jboss/wildfly/keycloak-overlay-2.5.5.Final.tar.gz


USER jboss


RUN /opt/jboss/wildfly/bin/add-user.sh admin admin
RUN /opt/jboss/wildfly/bin/jboss-cli.sh --file=keycloak-install.cli


RUN /opt/jboss/keycloak/bin/add-user-keycloak.sh -u ${KEYCLOAK_ADMIN_USER} -p ${KEYCLOAK_ADMIN_PASWORD}
ADD openfact-realm.json /opt/jboss/wildfly/


ENTRYPOINT [ "/opt/jboss/docker-entrypoint.sh" ]



CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0", "-Dkeycloak.import=/opt/jboss/wildfly/openfact-realm.json"]