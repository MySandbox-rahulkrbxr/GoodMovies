# GoodMovies: A Microservices-based Movie Information System

GoodMovies is a Spring Boot-based microservices application that provides a centralized movie catalog system with service discovery capabilities. It aggregates movie information and user ratings from multiple microservices to deliver personalized movie recommendations and ratings.

The system is built using Spring Cloud Netflix Eureka for service discovery and implements a microservices architecture with three core services: good-movies-catalog-service, good-movies-info-service, and good-movies-ratings-service. Each service is independently deployable and communicates via REST APIs, making the system highly scalable and maintainable.

## Features

- **Service Discovery**: Centralized Eureka server for automatic service registration and discovery
- **Load Balancing**: Client-side load balancing with Spring Cloud LoadBalancer
- **Resilient Communication**: Services communicate via REST with fallback capabilities
- **Independent Deployment**: Each microservice can be deployed and scaled independently

## Repository Structure
```
.
├── discovery-server/               # Eureka Server for service discovery
│   ├── src/                       # Source files for discovery server
│   └── pom.xml                    # Maven configuration for discovery server
├── goodmovies-catalog-service/    # Main service for aggregating movie data
│   ├── src/
│   │   └── main/
│   │       └── java/
│   │           └── resource/      # REST controllers and business logic
│   │           └── model/         # Data models for movie catalog
│   └── pom.xml                    # Maven configuration for catalog service
├── goodmovies-info-service/      # Service for movie information
│   ├── src/
│   │   └── main/
│   │       └── java/
│   │           └── resource/     # Movie information REST endpoints
│   └── pom.xml                   # Maven configuration for movie info service
└── goodmovies-ratings-service/   # Service for user ratings
    ├── src/
    │   └── main/
    │       └── java/
    │           └── resource/     # Rating data REST endpoints
    └── pom.xml                   # Maven configuration for ratings service
```

## Usage Instructions
### Prerequisites
- Java Development Kit (JDK) 21
- Maven 3.6.3 or later
- Spring Boot 3.1.1
- Spring Cloud 2022.0.3

### Installation

1. Clone the repository:
```bash
git clone <repository-url>
cd GoodMovies
```

2. Start the Discovery Server (MUST be started first):
```bash
cd discovery-server
./mvnw spring-boot:run
```

   You can also run it from IntelliJ:
   - Open the project in IntelliJ IDEA
   - Navigate to `discovery-server/src/main/java/com/nova/discovery_server/DiscoveryServerApplication.java`
   - Right-click and select "Run DiscoveryServerApplication"

3. Start the Movie Info Service:
```bash
cd ../goodmovies-info-service
./mvnw spring-boot:run
```

4. Start the Ratings Data Service:
```bash
cd ../goodmovies-ratings-service
./mvnw spring-boot:run
```

5. Start the Movie Catalog Service:
```bash
cd ../goodmovies-catalog-service
./mvnw spring-boot:run
```

### Quick Start
1. Verify the Eureka Server is running by accessing:
```
http://localhost:8761
```
   You should see the Eureka dashboard with all registered services.

2. Access the movie catalog for a user:
```
GET http://localhost:8094/catalog/{userId}
```

### More Detailed Examples
1. Get movie information:
```
GET http://localhost:8095/movies/{movieId}
```

2. Get user ratings:
```
GET http://localhost:8096/ratingsdata/user/{userId}
```

### Troubleshooting
Common issues and solutions:

1. Service Registration Issues
- Error: Services not showing up in Eureka
- Solution: Verify application.properties has correct eureka client configuration
```properties
spring.application.name=service-name
eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka/
```

2. Connection Refused
- Error: Connection refused between services
- Solution: Ensure all services are running and ports are correctly configured
- Check ports in application.properties:
  - Discovery Server: 8761
  - GoodMovies Catalog Service: 8094
  - GoodMovies Info Service: 8095
  - GoodMovies Ratings Service: 8096

## Data Flow
The system implements a microservices architecture where the goodmovies-catalog-service orchestrates data from goodmovies-info-service and goodmovies-ratings-service.

```ascii
                                    ┌──────────────────┐
                                    │  Eureka Server   │
                                    │    (Port 8761)   │
                                    └────────┬─────────┘
                                            │
                    ┌────────────────────────────────────────┐
                    │                                        │
            ┌───────┴──────────┐                    ┌───────┴──────────┐
            │  GoodMovies Info │                    │ GoodMovies Ratings│
            │  Service (8095)  │                    │  Service (8096)  │
            └───────┬──────────┘                    └───────┬──────────┘
                    │                                       │
                    └───────────────┐           ┌──────────┘
                                   │           │
                            ┌──────┴───────────┴───────┐
                            │ GoodMovies Catalog Service│
                            │        (Port 8094)        │
                            └──────────────────────────┘
```

Component interactions:
1. GoodMovies Catalog Service receives user request for movie recommendations
2. Retrieves user ratings from GoodMovies Ratings Service
3. For each rated movie, fetches movie details from GoodMovies Info Service
4. Aggregates the data and returns a complete catalog response
5. All services register with Eureka Server for service discovery
6. Load balanced requests using Spring Cloud LoadBalancer
7. RESTful communication between services using Spring WebClient/RestTemplate

## Deployment
### Prerequisites
- JDK 17 installed on deployment server
- Network access between services
- Sufficient memory (minimum 256MB per service)

### Environment Configurations
Configure the following properties for each service:
```properties
server.port=<port>
spring.application.name=<service-name>
eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka/
```