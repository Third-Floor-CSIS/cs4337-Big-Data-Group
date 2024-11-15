FROM openjdk:17-jdk-alpine
LABEL authors="milan"

WORKDIR /app

COPY target/IdentityMicroservice-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
