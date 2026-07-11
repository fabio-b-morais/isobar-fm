# ---- Build stage: compile & test with Maven on JDK 11 ----
FROM maven:3.9-eclipse-temurin-11 AS build
WORKDIR /build
# Cache dependencies first (only re-downloads when pom.xml changes)
COPY pom.xml .
RUN mvn -B -q dependency:go-offline
COPY src ./src
RUN mvn -B -q clean package

# ---- Runtime stage: slim JRE 11 with just the fat jar ----
FROM eclipse-temurin:11-jre
WORKDIR /app
# Run as a non-root user
RUN useradd -r -u 1001 appuser
COPY --from=build /build/target/isobar-fm.jar app.jar
USER appuser
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
