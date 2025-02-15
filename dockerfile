# Docker Build Maven Stage
FROM maven:3-openjdk-17 AS build
ARG JAR_FILE=target/product-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} products-service.jar
ENV JAVA_OPTS="-Xms512m -Xmx1024m"
ENTRYPOINT ["java","-jar","/products-service.jar"]
EXPOSE 8000

