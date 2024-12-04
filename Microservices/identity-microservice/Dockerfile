# Stage 1: Build
FROM maven:3.9.9-openjdk-21 as builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM openjdk:21-jdk-slim
LABEL authors="milan"

WORKDIR /app

COPY target/identity-microservice-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
