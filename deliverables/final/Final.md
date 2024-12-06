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
As above the high-level overview of the architecture is shown. However in depth the base of each microservice follows the following:
![img.png]


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

### Detailed explanation of components or modules 

### Any assumptions or constraints considered during development 






## Deployed Link (Accessible POC Link)  

https://third-floor-csis.ie/

## Project Tracking Link 

## Project Breakdown (Ownership for Each Feature) 

| Feature                     | Owner        | Notes                                                           |
|-----------------------------|--------------|-----------------------------------------------------------------|
|                             |              |                                                                 |
| Microservice - Notification | Euan & Fawad | Fawad created the base microservice, Euan implemented the logic |
| Microservice - Profile      | Brendan      |                                                                 |
|                             |              |                                                                 |
| CI/CD - Linter              | Euan         | Had to be removed due to time constraints                       |
| CI/CD - Build               | Brendan      |                                                                 |
| CI/CD - Deploy              | Brendan      |                                                                 |
| Initial .env setup          | Euan         | Updated by whole team as project advanced                       |


## Load testing/scaling test summary 

## Postman API Collection (Optional) 