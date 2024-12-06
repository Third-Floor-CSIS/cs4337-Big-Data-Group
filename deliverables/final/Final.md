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

###  High-level design overview
![img.png](high_level_overview.png) 
In the diagram above, we can see the tools we planned on using. It included using tools such as MySQL, Docker, Kafka, communication with Google Server for Authentication. 


![img.png](service_communication.png)
In the diagram above, we can see the overall communication between the end-users' requests and the microservices;
#### Api Gateway + Eureka Server (Service Registry)
The **API Gateway**, acts as a proxy, where all requests can come through and distributes them to the appropriate microservices. Second responsibility of the **API Gateway** is being a **load balancer**. It tightly works with the Eureka Server to get information about 'living' services. 

**Service Registry** (_Eureka Server_) monitors the **health** of the microservices and provides information to the API Gateway. If it was implemented, it could also share information to other microservices about the existence of other services. Since API Gateway is also a load balancer, horizontally scaled applications would contact the Eureka Server about their upbringing and the Eureka would constantly do a **healthcheck** on them. Every service will **try and contact the Eureka about their existence**, and with that the Gateway will send the request. If a service is down Gateway will not forward the request and return a 503 error. 

#### Identity Service
Identity (also known as _authentication_) microservice is responsible for authentication. With our frontend

###  Architectural diagrams (if any) 

###  Technologies used 

###  Detailed explanation of components or modules 

###  Any assumptions or constraints considered during development 






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