#!/bin/sh

if [ -z "$PORT" ]; then
    PORT="8080"
fi

if [ -z "$AWS_REGION" ]; then
    HIBERNATE="-hibernate.persistenceunit=com.tray.service.orders.db.DbSettingsInMemory"
    JAVA_TOOL_OPTIONS="-Dlogback.configurationFile=resources/logback.xml"
else
    HIBERNATE="-hibernate.persistenceunit=com.tray.service.orders.db.DbSettingsProd"
    JAVA_TOOL_OPTIONS="-XX:InitialRAMPercentage=25.0 -XX:MaxRAMPercentage=85.0 -Dlogback.configurationFile=resources/logback.cloud.xml"
fi

export JAVA_TOOL_OPTIONS

exec java -cp @/app/jib-classpath-file @/app/jib-main-class-file -https.over.http=false -http.port=:$PORT -https.port= $HIBERNATE -hibernate.loadclassmeta=true
