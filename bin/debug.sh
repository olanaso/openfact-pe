#!/usr/bin/env bash

mvn clean package -DskipTests
java -jar -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005 target/*-swarm.jar \
-Dsso.realm='ahren' \
-Dsso.auth.server.url='http://keycloak-keycloak-sso-development.apps.console.sistcoop.org/auth'
