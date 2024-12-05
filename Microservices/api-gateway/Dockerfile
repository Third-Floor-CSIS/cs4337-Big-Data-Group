# Build
FROM maven:3.9.9-ibm-semeru-21-jammy AS build


COPY ../pom.xml /home/
COPY ./ /home/app
WORKDIR /home/app
RUN mvn clean package -DskipTests=true -DoutputDirectory=./target

# Run
FROM openjdk:21-jdk-slim
ARG SERVICE
ARG JAR_FILE=/home/app/$SERVICE/target/*.jar
COPY --from=build $JAR_FILE /app/runner.jar
ENTRYPOINT ["java", "-jar", "/app/runner.jar"]