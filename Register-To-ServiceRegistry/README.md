Add the following dependency:
```
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```
Add this to the bottom
```
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>${spring-cloud.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

Add the following annotation to the main class:
```
@EnableFeignClients
```

Add the following properties to the application.properties file:
```
eureka.client.fetch-registry=true
eureka.client.register-with-eureka=true
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
eureka.instance.prefer-ip-address=true
```

Make sure the name of your microservice exists in the API GATEWAY application.properties file.
EG:
```
spring.cloud.gateway.routes[2].id=authenticationmicroservice
spring.cloud.gateway.routes[2].uri=lb://authenticationmicroservice
spring.cloud.gateway.routes[2].predicates[0]=Path=/auth/**
```