FROM docker.io/folioci/eclipse-temurin:25-alpine
WORKDIR /app
ADD --checksum=sha256:1279040261ee47b834bd21488f42e6dc765d4bd95442a66f586bec238a081293 \
    https://repo1.maven.org/maven2/io/prometheus/jmx/jmx_prometheus_javaagent/0.17.2/jmx_prometheus_javaagent-0.17.2.jar \
    jmx_exporter/
COPY ./prometheus-jmx-config.yaml jmx_exporter/
COPY target/mod-oai-pmh-fat.jar app.jar
EXPOSE 8081 9991
CMD ["java", "-jar", "app.jar"]
