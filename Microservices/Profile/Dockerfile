FROM eclipse-temurin:21-jdk

# this is teh folder that the microservice is in
# Required since the context is set to teh level above so we can pull in the parent pom
ARG SERVICE_PATH

# paremt pom
COPY ../pom.xml ./

# this is the workdir for the app to run in
WORKDIR /app

COPY ./${SERVICE_PATH}/.mvn/ .mvn
COPY ./${SERVICE_PATH}/mvnw ./${SERVICE_PATH}/pom.xml ./

# grab all dependencies
RUN ./mvnw dependency:go-offline -e

COPY ./${SERVICE_PATH}/src ./src

CMD ["./mvnw", "spring-boot:run"]