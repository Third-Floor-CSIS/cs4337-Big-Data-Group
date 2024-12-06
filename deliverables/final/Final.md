# Group 8

| Member         | ID       |
|----------------|----------|
| Milan Kovacs   | 21308128 |
| Fawad Shahzad  | 18265693 |
| Euan Bourke    | 21332142 |
| Sean Caplis    | 21342342 |
| Killian Carty  | 21332673 |
| Ben Hogan      | 21319561 |
| Brendan Golden | 12136891 |



## GitHub Link 

https://github.com/Third-Floor-CSIS/cs4337-Big-Data-Group

### Documentation references 

See folder deliverables within the repo for architecture, images, POC, reports.

See README.md for guidance provided during setting up of the project.

Other documentations:
- JwtAuthenticationGuideline --> Integrating JWT into each microservice 
- Register-To-ServiceRegistry --> Onboarding a microservice onto service registry 

## Product Documentation 
Description: Provide the product documentation, which includes details on the design, architecture, and any other relevant project information. If you are using a Google Doc or any other platform for documentation, make sure the document is shared with appropriate access permissions (Access without login).  
Note: The documentation should include:   

### High-level design overview
![overall_tools_and_architecture.png](high_level_overview.png) 
In the diagram above, we can see the tools we planned on using. It included using tools such as MySQL, Docker, Kafka, communication with Google Server for Authentication. 


![internal_communication.png](service_communication.png)
In the diagram above, we can see the overall communication between the end-users' requests and the microservices;
#### Api Gateway + Eureka Server (Service Registry)
The **API Gateway**, acts as a proxy, where all requests can come through and distributes them to the appropriate microservices. Second responsibility of the **API Gateway** is being a **load balancer**. It tightly works with the Eureka Server to get information about 'living' services. 
The **API Gateway** provides:

- Centralized routing and filtering for all microservices.
- Integration with the Service Registry for dynamic service discovery and fault tolerance.
- A JWT-based request filter embedded within the gateway to validate user authentication.

**Service Registry** (_Eureka Server_) monitors the **health** of the microservices and provides information to the API Gateway. If it was implemented, it could also share information to other microservices about the existence of other services. Since API Gateway is also a load balancer, horizontally scaled applications would contact the Eureka Server about their upbringing and the Eureka would constantly do a **healthcheck** on them. Every service will **try and contact the Eureka about their existence**, and with that the Gateway will send the request. If a service is down Gateway will not forward the request and return a 503 error. 

#### Identity Service & Stateless Microservices
Identity (also known as _authentication_) microservice is responsible for authentication. With our frontend there is a hyperlink that brings you to the Google authorization page and which will be then forward the request to the identity microservice (grantcode). The API will contact Google to get the access token and a refresh token. The refresh token is retained within our DB and the access token is given to the JWT service. If the user doesn't exists within the authentication DB then it will generate a JWT access and refresh token. The token is signed with the UserID as _'sub'_ and the Google resource access token as _'access_token'_. This ensures each microservice ends up being stateless and the "frontend" application should forward this request to each microservice. If a microservice receives a user ID that does not exist within said microservice, the microservice can ensure that user exists because of the signed JWT and create a user. 

There are two end points within the Identity Service that allows refresh for:
- Access Token
- Refresh Token

By default, JWT refresh token is valid for 7 days (25,200,000 milliseconds), and Google Refresh Token is valid for 14 days (as per the specified request) however the JWT and access token are both valid for only 1 Hour. The idea was that the frontend would detect when the JWT token would expire, then while the token is valid, contact the two endpoints and update it. Looking back, it is a bit redundant to have two end-points for the updating the two tokens separately. This is because the JWT token is issued when the access token is generated, which means they both will expire at the same time. However, if for whatever reason you wanted to update one of the two, you can. 

##### Filter chain
We have created a JWT filter chain which is embedded within each microservice. At the time of creation and we were not aware that you could expose only one port and looking back now we should have only embedded within the API gateway and just forward the request if it's valid. Each microservice contains a simplified version of the JWT Service to be able to retrieve user ID, access token and how to validate the JWT signature. The filter chain will check if the request is going to an exposed endpoint (such as health checks '/test') or secured, and it will try and validate the JWT token. If it's valid, it forwards the request otherwise reject it with 403 forbidden.

### Architectural diagrams (if any) 
As above the high-level overview of the architecture is shown. However in depth of each microservice follows the following:

#### Identity Microservice
![identity_architecture.png](identity_ms_arch.png)
Identity has a lot of components, tried to play around and make the most out of what spring boot provides. The user uses the frontend with the hyperlink which has a matching URI parameters as the service. 

Once the user has authenticated the API to have access to the resources the authority server will give a grant code to the API. The API will then process a grant code to retrieve the access token and refresh tokens. The access token will have a lifetime of one hour while the refresh token has two weeks. Then the API will check if this user exists within the database, if the user doesn't exist from the database it creates a user with an ID. It will then generate a JWT token alongside with a refresh token, they have one hour and one week lifetimes respectively.

#### API Gateway and Service Registry (Eureka Server)
![Gateway_and_eureka.png](gateway_and_eureka.png)
The API gateway behaves like a proxy, where all requests go through a single port, and also as a load balancer as far as we know. The Eureka server behaves as a health checker and monitors upcoming and down going servers. When a microservice comes up they will initially try and contact the service registry, on the defined port, and when the service registry receives the incoming requests, it will monitor the microservice going forward. It will then ping every couple of seconds whether the microservice is still alive, and if there is no response it will assume it to be down and the registration. As far as we know,if the microservice doesn’t receive health check, it will try and contact the Eureka. 

When an incoming request is looking for an endpoint with certain sub-route, there are sub-routes  mapped onto different microservices. The API gateway will fetch service registry for how many microservices exists for the certain route. The API gateway will then decide using round Robin to which instance to forward the request to. 

#### Profile Microservice
Allows users to customize their user profile. The user profile consists of their displayed username, profile picture, bio alongside with follower and followee count. 

#### Posts Microservice
Posts microservice allows for adding new posts, retrieving posts, liking and unliking posts. 

#### Kafka Cluster
![kafkaDiagram.png](kafkaDiagram.png)

Zookeeper acts as a cluster manager for the three kafka brokers. The three brokers store the partitions of the post-updates and profile-updates
kafka topics. The post and profile producers publish messages to their respective topics. The notification consumer then consumes the messages from each topic as they are produced.

#### Original Database
![Original_Database.png](Original_Database.png)

This is the entity relationship diagram for the original database containing all the tables for the project in the same database.
During the development of the project it was decided to split the database up into smaller databases for each microservice with decoupled tables.
Below are the entity relationship diagrams for each microservices database.
#### Identity Microservice Database
![Identity_Database.png](AuthDB.png)
#### Posts Microservice Database
![Posts_Database.png](postDB.png)
#### Profile Microservice Database
![Profile_Database.png](profileDB.png)
#### Notification Microservice Database
![Notification_Database.png](notificationDB.png)
### Technologies used

#### Github Actions
We used Github actions to build, test and deploy the project.  

##### Build
Our build pipeline is responsible for building and packaging each microservice.  

###### Matrix
Since all our microservices are based on Java SpringBoot it would be a waste of time/energy to create a unique pipeline for each one.  

Instead, we can make use of ``matrix`` which allows us to set an array of values such as:
```yaml
strategy:
  matrix:
    service:
      - "api-gateway"
      - "identity-microservice"
      - "notification-service"
      - "posts"
      - "profile"
      - "service-registry"
```

This will create ``n`` separate pipelines, one for each of the values in the matrix.  
The individual value can be accessed in these separate pipelines via ``${{ matrix.service }}``.  
This allows us to use this value in many different ways, as outlined in out build pipeline.

###### Building the images
Since we are using docker containers in our deployment it makes sense for us to build and archive it.  

We make use of the ``docker/login-action`` to log into Github's own container registry (ghcr.io).  
Following that we build and push the images using ``docker/build-push-action``.  
This makes our containers available for general use.

##### Deploy
Once the ``build`` workflow succeeds on the ``main`` branch it kicks off the ``deploy`` workflow.  
The goal of this workflow is simple: SSH into our deployment server, pull in the new images and restart the deployed services.

#### Host
We initially tried to host our service on Azure.  
Unfortunately we bounced off of that due to the high configuration difficulty.  

We ended up getting a ``cx32`` from Hetzner.  
Using Debian 12 as a base we installed: Docker, Git (LFS) and Caddy

##### Domain + DNS
We acquired our domain (``third-floor-csis.ie``) from Blacknight (https://www.blacknight.com/).
Our DNS is hosted by Cloudflare.  

It is best to not have the DNS in the same location as your domain host.

##### Docker Compose
We make use of Docker Compose extensively in this project.  
Using a base config (``base.docker-compose.yml``) which has the bulk of the config.  
Layered on top of that is ``dev.docker-compose.yml`` and ``prod.docker-compose.yml`` which are customised for development and deploying to production.

On the server we are able to pull in the created images from ghcr.io and redeploy using the new config

In hindsight we should have set up the ``dev.docker-compose.yml`` far earlier since this would have unified the build and testing process during development.

##### Caddy
Caddy is a reverse proxy that we settled on after experimenting with nginx.  
While nginx is an industry standard its configuration can be a tad tricky, especially if you want to have TLS termination.  

Caddy is another server with a very lightweight configuration.  
Below is the full config for the site as well as a subdomain for api requests.  
This also covers the https certs.

```caddyfile
third-floor-csis.ie {
    root * /cs4337/frontend
    encode gzip
    file_server
}
api.third-floor-csis.ie {
    reverse_proxy * 127.0.0.1:8080  
}
```

#### Kafka
Kafka was used to send messages between three microservices posts, profile and notification. Kafka topics (post-updates and profile-updates) allow producers (Post and Profile services) to publish events, which are then consumed by the Notification Service

#### Liquibase
Liquibase is a spring boot dependency, which allows us to manage database schemas and migrations. It allows to sync databases with schemas and retains history of the modifications, similar to git. You can see the `identity-microservice/src/resources/db/db.changelog-master.sql` file.
```sql
--liquibase formatted sql

-- changeset Milan:0
CREATE TABLE IF NOT EXISTS tokens (
  user_id BIGINT NOT NULL,
  refresh_token VARCHAR(500) NOT NULL,
    expiration_time_access_token DATETIME,
    expiration_time_refresh_token DATETIME,
    current_access_token VARCHAR(500) NOT NULL,
    PRIMARY KEY (user_id)
    );
```
#### JWT
We are leveraging authentication using JWT. The JWT token is signed with the user ID and Google access token. The JWT token is then passed to each microservice to validate the user. You can see in application.properties where we inject the secret and expiration times:
```properties
jwt.secret=${JWT_SECRET}
jwt.expiration=3600000
jwt.refresh.expiration=25200000
```

#### SpringBoot-DevTools
We used a dependency during development called Spring Boot Dev Tools which allows us to hot reload the application while developing. This is very useful as it saves time from having to restart the application every time a change is made.

#### Google Oauth 2.0
We are using Google Oauth 2.0 for third-party integration. Users must use Google authentication to access the application. See `Microservices/identity-micrservice/src/main/.../services/GoogleAuthService.java` for all the business logic. This service is not tested because it relies on third-party services, and we don't test third-party services (because they are expected to work a certain way).

#### Eureka
Eureka is used to monitor the health of the microservices and provide information to the API Gateway.
You can see how in the API Gateway `application.properties` we map the sub-route, the microservice name, etc:
```properties
# Route configuration for Auth Service
spring.cloud.gateway.routes[2].id=authenticationmicroservice
spring.cloud.gateway.routes[2].uri=lb://authenticationmicroservice
spring.cloud.gateway.routes[2].predicates[0]=Path=/auth/**
```
### Detailed explanation of components or modules

### Notification Micro Service
The Notification Microservice is responsible for managing notifications, including retrieving unread messages and will mark them as read. It is integrated with Kafka for asynchronous message processing and it also provides REST APIs for interacting with notifications. WIth Kafka this allows the system to handle a large volume of notifications without impacting the performance of other services.

The notification data is stored in a database with a well-structured schema.
- A unique notification ID to identify each notification.
- A receiver ID to associate the notification with a specific user.
- A status field to indicate whether the notification is unread or read.
- A timestamp to log the creation time of each notification.

For error handling, custom exceptions are implemented to provide meaningful error messages when issues occur, which makes it more user-friendly.

### Posts MicroService
The Posts microservice is a backend service built to manage user-created content, like posts and likes, within an application. Ensures security with JWT-based authentication to protect user data and restrict access to authorized users. It handles all the essential operations such as,

•	Posts creation
•	Liking posts
•	Un-liking Posts
•	Get posts by userId
•	Get posts by postId

The main logic lives in the service layer, while the database layer keeps everything organized and efficient. It’s designed to seamlessly handle interactions, like users liking posts, and makes sure data flows smoothly between the system’s components with the help of mappers and data transfer objects (DTOs).


#### Profile Service Unit Tests 
Unit tests were added to the Profile Service to ensure its functionality and reliability. 
##### Service Layer Tests:
The tests for the service layer focus on the logic of managing profiles. e.g they check that profiles can be created, retrieved, and updated correctly. These tests validate that the service handles the data as expected and respects the rules defined in the business logic
##### Controller Layer Tests:
These ensures that the API endpoints work well as they should. tests various scenerios like Handling valid and invalid requests, Ensuring proper HTTP status codes for success and failure cases.
##### Posts Service Unit Tests:
Includes unit and integration tests to ensure its functionality and reliability. Tests focus on critical components like the service layer and database interactions, verifying that the system behaves as expected under various scenarios.

### Kafka
The project integrates kafka as a messaging system across three microservices: Post, Profile and Notification. It is used to send notifications between the microservices. Kafka allows for real-time event handling,
scalability and fault tolerance in the project.

#### Configuration
Two Kafka topics were configured for the project: ``post-updates``used by the post service to publish post notifications and ``profile-updates`` used by the profile service to
publish profile notifications. The Kafka producers implemented utilize the KafkaTemplate for sending messages to topics. The consumer uses kafka listeners to consume messages from the two topics.

#### Microservice integration
Post Microservice: The ``KafkaProducer`` class in the post microservice publishes messages to the post updates topic. The ``PostService`` class produces messages after new events such as new post creation.

Profile Microservice: The ``KafkaProducer`` class in the profile microservice publishes messages to the profile updates topic. The ``ProfileService`` class produces messages after new events such as profile edited.

Notification Microservice: The ``KafkaConsumer`` class in the notifications microservice consumes messages from the profile-updates and post-updates topics. Currently the message is just printed to the terminal and stored, this could be developed further and for instance send the user 
an email containing their notifications.

#### Kafka Cluster Architecture
The Kafka cluster architecture consists of Zookeeper and three Kafka brokers. Zookeeper acts as the cluster manager ensuring the brokers are healthy and managing partitions. The three Kafka
brokers play the role of storing partitions of each kafka topic. Each partition has a leader broker who is responsible for handling requests while the other brokers act as followers keeping replicas.



### Any assumptions or constraints considered during development 

**DB stateless assumption**
To make the applications to be stateless is by passing the userID within the JWT. This allows the microservices to not have intercommunication with each other. This way each microservice will create a user related row in their table based on the UserID from the JWT. This can be ensured because if the JWT is valid, then it **must** have been created by the Identity Microservice. IE: The userID exists somewhere. Unfortunately we can't do complex JOINS on the table so we had to get creative with this.






## Deployed Link (Accessible POC Link)  

https://third-floor-csis.ie/

## Project Tracking Link 
https://trello.com/b/chFsPjoU/big-data-group-8
https://github.com/Third-Floor-CSIS/cs4337-Big-Data-Group/issues

## Project Breakdown (Ownership for Each Feature) 

| Feature                         | Owner        | Notes                                                                                                                        |
|---------------------------------|--------------|------------------------------------------------------------------------------------------------------------------------------|
| Microservice - API Gateway      | Fawad        | Implemented routing, filtering,& Service Registry integration                                                                |
| Microservice - Service Registry | Fawad        |                                                                                                                              |
| Microservice - Notification     | Euan & Fawad | Fawad created the base microservice, Euan implemented the logic                                                              |
| Microservice - Profile          | Brendan      |                                                                                                                              |
| Microservice - Posts            | Sean         |                                                                                                                              |
| Microservice - Identity         | Milan        | Auth, JWT, Google Oauth 2.0, User creation, Token generation, Token refresh                                                  | 
| Unit Tests - Profile            | Fawad        | Added and verified unit tests for service and controller layers                                                              |
| CI/CD - Linter                  | Euan         | Had to be removed due to time constraints                                                                                    |
| CI/CD - Build                   | Brendan      |                                                                                                                              |
| CI/CD - Deploy                  | Brendan      |                                                                                                                              |
| Deployment - Azure              | Brendan      | Initial testing of deploying to Azure                                                                                        |
| Deployment - Hetzner            | Brendan      | Fallback plan of deploying to VPS in Hetzner, with DNS in Cloudflare                                                         |
| Initial .env setup              | Euan         | Updated by whole team as project advanced                                                                                    |
| Architecture                    | Milan & Euan | Created high-level overview of the product                                                                                   |
| Code Style                      | Milan        | Created XML for standard code style                                                                                          |
| Template Microservice           | Milan        | Template to get the team upto speed                                                                                          |
| JWT Filter integration          | Milan        | Simpliefied JWT service & filterchain                                                                                        |
| Dockerization Fix               | Brendan      |                                                                                                                              |
| Setup Dev environment           | Brendan      | Initially a single docker compose file its now layered to share config with prod.                                            |
| Setup Prod environment          | Brendan      | Split off config from dev                                                                                                    |
| Identity Unit Test              | Milan        | Added unit tests for the Identity Microservice                                                                               |
| Profile Follow/Unfollow         | Milan        | Added follow/unfollow functionality to the Profile Microservice                                                              |
| Documentation - JWT             | Milan        | Guideline to integrate JWT into each microservice                                                                            |
| Kafka                           | Killian      |                                                                                                                              |
| Database                        | Killian      | Updated by whole team as the project developed                                                                               |
| Weekly Reports                  | Brendan      | Managed teh weekly reports                                                                                                   |
| Discord Webhook                 | Brendan      | Set up a webhook to link in with our Discord server, notified members of changes to repo, which lead to greater involvement. |


## Load testing/scaling test summary 

## Postman API Collection (Optional) 

Posts 
  1.	GET /api/posts/test:
        o	Purpose: Verifies the Posts service is running.
        o	Response: A simple confirmation message ‘Hello World’

  2.	POST /api/posts/root:
        o	Purpose: Creates a new post with data like userId.
        o	Response: Returns a 201 Created status with the new post’s details.

  3.	GET /api/posts/user/{userId}:
        o	Purpose: Retrieves all posts created by a specific user.
        o	Response: A list of posts in JSON format.

  4.	GET /api/posts/{postId}:
        o	Purpose: Fetches a specific post using its unique ID.
        o	Response: JSON details of the post or a 404 Not Found error.

Profile 

  1.	GET /profile/test:
        o	Purpose: Confirms the Profile service is operational.
        o	Response: A simple confirmation message ‘Hello World’

  2.	GET /profile/existing:
        o	Purpose: Retrieves the user’s profile based on their JWT token.
        o	Response: Profile data, including name, bio, and profile picture.

  3.	POST /profile/existing:
        o	Purpose: Updates the user’s profile with new data.
        o	Response: Confirms the update with a 200 OK or 204 No Content.

  4.	POST /profile/follow/{user_id}:
        o	Purpose: Allows the user to follow or unfollow another user.
        o	Response: Updated follower and following counts.

Auth 

  1.	GET /auth/test:
        o	Purpose: Verifies the Auth service is running.
        o	Response: A simple confirmation message, HttpStatus: OK

  2.	GET /auth/grantcode:
        o	Purpose: Processes OAuth grant codes for user authentication.
        o	Response: HttpStatus.OK
  3.	POST /auth/refresh-access-token:
        o	Purpose: Refreshes the access token using a valid refresh token.
        o	Response: Returns the new access token.

  4.	POST /auth/refresh-jwt-token:
        o	Purpose: Refreshes the entire JWT for session validity.
        o	Response: A new JWT token.


