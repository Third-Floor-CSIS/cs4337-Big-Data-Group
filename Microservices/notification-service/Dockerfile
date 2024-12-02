#build
FROM maven:3.9.9-openjdk-21 as builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

#Run
FROM openjdk:21-jdk-slim
LABEL authors="killian"

WORKDIR /app

COPY target/notification-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "app.jar"]



