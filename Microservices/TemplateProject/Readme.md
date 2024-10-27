# Template Microservice
Rough instructions to set up another microservice using this template.

1. Copy this folder inside  ``Microservices``
   * For example ``Microservices/example_service``
2. Inside that new folder
   * ``cd Microservices/example_service``
3. Config file options may have to be modified (port etc)
   * ***Do not put passwords in here***
   * ``./src/main/resources/application.properties``
4. Now build and run the program
   *  ``./mvnw spring-boot:run``