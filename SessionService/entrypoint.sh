#!/bin/sh
JVM_OPTIONS=$*

echo "[INFO] Run: java $JVM_OPTIONS -jar service.jar"
exec java $JVM_OPTIONS -jar /service.jar
