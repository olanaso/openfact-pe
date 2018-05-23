#!/usr/bin/env bash

mvn clean
mvn fabric8:deploy -Popenshift -DSSO_AUTH_SERVER_URL=$1