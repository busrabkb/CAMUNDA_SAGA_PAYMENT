# syntax=docker/dockerfile:1
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# IPv6 timeouts can make Maven look frozen inside Docker Desktop.
ENV MAVEN_OPTS="-Djava.net.preferIPv4Stack=true"

COPY pom.xml .
COPY .mvn .mvn
COPY src src
# Cache ~/.m2 across builds so Ctrl+C / src changes do not re-download Camunda+Spring.
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -s .mvn/settings.xml -DskipTests -Dmaven.test.skip=true package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/order-saga-demo-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
