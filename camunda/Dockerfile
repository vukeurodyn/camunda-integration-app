FROM camunda/camunda-bpm-platform:run-latest

USER root

# Use version 1.6.0 for connectors (compatible with Camunda 7.21+)
ADD https://repo1.maven.org/maven2/org/camunda/connect/camunda-connect-core/1.6.0/camunda-connect-core-1.6.0.jar \
    /camunda/configuration/userlib/
ADD https://repo1.maven.org/maven2/org/camunda/connect/camunda-connect-http-client/1.6.0/camunda-connect-http-client-1.6.0.jar \
    /camunda/configuration/userlib/
ADD https://repo1.maven.org/maven2/org/camunda/bpm/camunda-engine-plugin-connect/7.22.0/camunda-engine-plugin-connect-7.22.0.jar \
    /camunda/configuration/userlib/
ADD https://repo1.maven.org/maven2/org/apache/httpcomponents/httpclient/4.5.14/httpclient-4.5.14.jar \
    /camunda/configuration/userlib/
ADD https://repo1.maven.org/maven2/org/apache/httpcomponents/httpcore/4.4.16/httpcore-4.4.16.jar \
    /camunda/configuration/userlib/
ADD https://repo1.maven.org/maven2/commons-logging/commons-logging/1.2/commons-logging-1.2.jar \
    /camunda/configuration/userlib/

RUN chown -R camunda:camunda /camunda/configuration/userlib/ && \
    chmod 644 /camunda/configuration/userlib/*.jar

USER camunda