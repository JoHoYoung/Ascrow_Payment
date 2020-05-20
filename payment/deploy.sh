#!/bin/bash
env=$1

if [${env} == "dev"]; then
    mvn package spring-boot:repackage -Dmaven.test.skip=true -Djava.net.preferIPv4Stack=true -P dev
    docker rm -f escrow
    docker image rm escrow
    docker build -t escrow .
elif [${env} == "prod"]; then
    mvn package spring-boot:repackage -Dmaven.test.skip=true -Djava.net.preferIPv4Stack=true -P prod
fi
