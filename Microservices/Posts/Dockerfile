FROM maven:3.9.9-openjdk-21 as builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package

FROM openjdk:21-jdk-slim

# Set the author label
LABEL authors="seanc"

# Set the working directory inside the container
WORKDIR /app

# Copy the Spring Boot application JAR file into the container
COPY target/Posts-1.0.0.jar app.jar

# Expose the port your application runs on
EXPOSE 8084

# Set the entry point to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
