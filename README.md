# Movie Catalog Service: A Microservices-based Movie Information System

The Movie Catalog Service is a Spring Boot-based microservices application that provides a centralized movie catalog system with service discovery capabilities. It aggregates movie information and user ratings from multiple microservices to deliver personalized movie recommendations and ratings.

The system is built using Spring Cloud Netflix Eureka for service discovery and implements a microservices architecture with three core services: movie-catalog-service, movie-info-service, and ratings-data-service. Each service is independently deployable and communicates via REST APIs, making the system highly scalable and maintainable.

## Repository Structure
```
.
├── discovery-server/               # Eureka Server for service discovery
│   ├── src/                       # Source files for discovery server
│   └── pom.xml                    # Maven configuration for discovery server
├── movie-catalog-service/         # Main service for aggregating movie data
│   ├── src/
│   │   └── main/
│   │       └── java/
│   │           └── resource/      # REST controllers and business logic
│   │           └── model/         # Data models for movie catalog
│   └── pom.xml                    # Maven configuration for catalog service
├── movie-info-service/           # Service for movie information
│   ├── src/
│   │   └── main/
│   │       └── java/
│   │           └── resource/     # Movie information REST endpoints
│   └── pom.xml                   # Maven configuration for movie info service
└── ratings-data-service/         # Service for user ratings
    ├── src/
    │   └── main/
    │       └── java/
    │           └── resource/     # Rating data REST endpoints
    └── pom.xml                   # Maven configuration for ratings service
```

## Usage Instructions
### Prerequisites
- Java Development Kit (JDK) 17 or later
- Maven 3.6.3 or later
- Spring Boot 3.1.1
- Spring Cloud 2022.0.3

### Installation

1. Clone the repository:
```bash
git clone <repository-url>
cd movie-catalog-service
```

2. Start the Discovery Server:
```bash
cd discovery-server
./mvnw spring-boot:run
```

3. Start the Movie Info Service:
```bash
cd ../movie-info-service
./mvnw spring-boot:run
```

4. Start the Ratings Data Service:
```bash
cd ../ratings-data-service
./mvnw spring-boot:run
```

5. Start the Movie Catalog Service:
```bash
cd ../movie-catalog-service
./mvnw spring-boot:run
```

### Quick Start
1. Verify the Eureka Server is running by accessing:
```
http://localhost:8761
```

2. Access the movie catalog for a user:
```
GET http://localhost:8081/catalog/{userId}
```

### More Detailed Examples
1. Get movie information:
```
GET http://localhost:8082/movies/{movieId}
```

2. Get user ratings:
```
GET http://localhost:8083/ratingsdata/user/{userId}
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
  - Movie Catalog Service: 8081
  - Movie Info Service: 8082
  - Ratings Data Service: 8083

## Data Flow
The system implements a microservices architecture where the movie-catalog-service orchestrates data from movie-info-service and ratings-data-service.

```ascii
                                    ┌──────────────────┐
                                    │  Eureka Server   │
                                    │    (Port 8761)   │
                                    └────────┬─────────┘
                                            │
                    ┌────────────────────────────────────────┐
                    │                                        │
            ┌───────┴──────────┐                    ┌───────┴──────────┐
            │  Movie Info      │                    │   Ratings Data   │
            │  Service (8082)  │                    │  Service (8083)  │
            └───────┬──────────┘                    └───────┬──────────┘
                    │                                       │
                    └───────────────┐           ┌──────────┘
                                   │           │
                            ┌──────┴───────────┴───────┐
                            │   Movie Catalog Service   │
                            │        (Port 8081)        │
                            └──────────────────────────┘
```

Component interactions:
1. Movie Catalog Service receives user request for movie recommendations
2. Retrieves user ratings from Ratings Data Service
3. For each rated movie, fetches movie details from Movie Info Service
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