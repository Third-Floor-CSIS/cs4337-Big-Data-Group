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

JwtAuthenticationGuideline à Integrating JWT into each microservice 

Register-To-ServiceRegistry à Onboarding a microservice onto service registry 

## Product Documentation 
Description: Provide the product documentation, which includes details on the design, architecture, and any other relevant project information. If you are using a Google Doc or any other platform for documentation, make sure the document is shared with appropriate access permissions (Access without login).  
Note: The documentation should include:   

### High-level design overview

### Architectural diagrams (if any) 

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


{Diagram of the arch}  
In the diagram above, we can see the tools we planned on using. It included using dependencies and tools such as MySQL, Docker, Kafka,



A diagram of a company
Description automatically generated
Figure 2 Overall communication of the application



## Deployed Link (Accessible POC Link)  

https://third-floor-csis.ie/

## Project Tracking Link 

## Project Breakdown (Ownership for Each Feature) 

| Feature                | Owner   | Notes |
|------------------------|---------|-------|
|                        |         |       |
|                        |         |       |
| Microservice - Profile | Brendan |       |
|                        |         |       |
|                        |         |       |
| CI/CD - Build          | Brendan |       |
| CI/CD - Deploy         | Brendan |       |
|                        |         |       |


## Load testing/scaling test summary 

## Postman API Collection (Optional) 