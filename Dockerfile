# ====== Build stage ======
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Leverage layer caching
COPY pom.xml .
RUN mvn -q -e -DskipTests dependency:go-offline

# Build
COPY src ./src
RUN mvn -q -DskipTests package

# ====== Runtime stage ======
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy fat jar
COPY --from=build /app/target/*.jar app.jar

# Use a non-root user for safety (optional)
RUN useradd -r -u 10001 appuser
USER appuser

# Spring defaults to 8080. We still EXPOSE for clarity.
EXPOSE 8080

# JVM flags tuned for containers
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=70"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
