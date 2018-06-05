#!/usr/bin/env bash

mvn clean
mvn fabric8:deploy -Dfabric8.debug.enabled=true -Popenshift -DSSO_AUTH_SERVER_URL=$1 -DSSO_REALM=$2