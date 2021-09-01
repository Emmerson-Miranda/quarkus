#!/usr/bin/env bash
jaeger=${JAEGER_HOST:-jaeger}
echo "========================================================================="
echo "========================================================================="
echo "Starting microservice"
echo "========================================================================="
echo "========================================================================="

echo "Jaeger host to connect: $jaeger"
./application -Dquarkus.http.host=0.0.0.0 -Dquarkus.jaeger.agent-host-port=$jaeger