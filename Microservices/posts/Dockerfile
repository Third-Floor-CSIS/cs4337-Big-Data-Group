# Build
FROM maven:3.9.9-ibm-semeru-21-jammy AS build
ARG SERVICE
COPY ../pom.xml /home/
COPY ./ /home/app
WORKDIR /home/app/$SERVICE
RUN mvn clean package -DskipTests=true -DoutputDirectory=./target
#RUN ls -lah . && ls -lah ./posts && exit 1


# Run
FROM openjdk:21-jdk-slim
ARG SERVICE
ARG JAR_FILE=/home/app/$SERVICE/target/*.jar
COPY --from=build $JAR_FILE /app/runner.jar
ENTRYPOINT ["java", "-jar", "/app/runner.jar"]