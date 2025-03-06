# Docker Build Maven Stage
# Stage 1: Build stage (uses Maven)
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src/ ./src/
RUN mvn clean package -DskipTests

# Stage 2: Runtime stage (uses lightweight JDK)
FROM amazoncorretto:17-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Set memory limits (optional)
ENV JAVA_OPTS="-Xms512m -Xmx1024m"

EXPOSE 8000
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

