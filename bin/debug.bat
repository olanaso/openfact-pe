@echo off
echo BUILDING OPENFACT - PE
echo ------------------------------------------------------

cd..
mvn clean package -DskipTests && java -jar -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005 target/openfactv2-1.0.0-SNAPSHOT-swarm.jar -Dsso.realm=ahren -Dsso.auth.server.url=http://keycloak-keycloak-sso-development.apps.console.sistcoop.org/auth

PAUSE
