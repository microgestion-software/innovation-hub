# Spring Boot WebFlux API with Rate Limiting using Resilience4J

A Spring Boot WebFlux application that implements a rate-limited REST API for customer management using Resilience4j.

## Features

- CRUD operations for customer management
- Rate limiting with Resilience4j
- Reactive endpoints with Spring WebFlux
- PostgreSQL database with R2DBC
- OpenAPI 3.0 documentation
- Docker development environment

## Prerequisites

- Java 21
- Docker and Docker Compose
- Maven 3.9+
- Spring Boot 3.4.0
- PostgreSQL 16

## Quick Start

1. Start development environment:
```bash
docker-compose -f .devcontainer/docker-compose.yml up -d
```

2. Build and run:
```bash
./mvnw clean install
./mvnw spring-boot:run
```

3. Access API documentation:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI spec: http://localhost:8080/api-docs

## Configuration

### Application Properties

```yaml
# Configuring the database
spring:
  r2dbc:
    url: r2dbc:postgresql://localhost:5432/customerdb
    username: app_user
    password: app_password
    pool:
      initial-size: 5
      max-size: 10
    properties:
      schema: public

# Configuring the server
server:
  port: 8080


# Configuring the actuator
management:
  server:
    port: 9001
  endpoint:
    health:
      show-details: always
  endpoints:
    web:
      exposure:
        include: health
  health:
    ratelimiters:
      enabled: true

# Configuring the rate limiter
resilience4j.ratelimiter:
    configs:
        default:
            eventConsumerBufferSize: 100
            registerHealthIndicator: true
            limitForPeriod: 2
            limitRefreshPeriod: 60s
            timeoutDuration: 2s
    instances:
        customerWriteRateLimit:
            baseConfig: default
        customerServiceRateLimit:
            registerHealthIndicator: true
            limitForPeriod: 5
            limitRefreshPeriod: 60s
            timeoutDuration: 0s
```

### Rate Limiting

- **configs**: Defines the default configuration for rate limiters.

  - **default**: The default configuration settings.
    - **eventConsumerBufferSize**: The size of the event consumer buffer (100).
    - **registerHealthIndicator**: Registers a health indicator for the rate limiter.
    - **limitForPeriod**: The maximum number of calls allowed during a refresh period (2).
    - **limitRefreshPeriod**: The period after which the rate limiter's state is reset (60 sec).
    - **timeoutDuration**: The maximum time a thread waits for permission (2 sec).
- **instances**: Defines specific rate limiter instances.
  - **customerWriteRateLimit**: Uses the default configuration.
  - **customerServiceRateLimit**: Custom configuration for the customerServiceRateLimit instance.
    - **registerHealthIndicator**: Registers a health indicator for this instance.
    - **limitForPeriod**: The maximum number of calls allowed during a refresh period (5).
    - **limitRefreshPeriod**: The period after which the rate limiter's state is reset (60 sec).
    - **timeoutDuration**: The maximum time a thread waits for permission: (0 sec).

This configuration helps to control the rate of requests to the customerService and customerWrite operations, ensuring that the system is not overwhelmed by too many requests in a short period.

## API Endpoints

### Customers

```
GET    /api/customers         # List all customers
GET    /api/customers/{id}    # Get customer by ID
POST   /api/customers         # Create customer
PUT    /api/customers/{id}    # Update customer
DELETE /api/customers/{id}    # Delete customer
GET    /api/customers/search  # Search customers by name
```

## Development

### Running Tests

```bash
# Unit tests
./mvnw test

# Integration tests
./mvnw verify

# Coverage report
./mvnw verify jacoco:report
```

### Development Container

1. Open in VS Code
2. Install Remote - Containers extension
3. "Reopen in Container"

### Database Management

```bash
# Connect to database
psql -h localhost -p 15432 -U app_user -d customerdb

# Run migrations
./mvnw flyway:migrate
```

## Project Structure

```
├── src
│   ├── main
│   │   ├── java/com/microgestion/example/patterns/resilience/api
│   │   │   ├── config/       # Configuration classes
│   │   │   ├── controller/   # REST controllers
│   │   │   ├── exceptions/   # Exception handling
│   │   │   ├── model/        # Domain models
│   │   │   ├── repository/   # Data access
│   │   │   └── service/      # Business logic
│   │   └── resources/
│   │       ├── application-test.yml
│   │       └── application.yml
│   └── test/
├── .devcontainer/          # Development container config
├── docker-compose.yml      # Production deployment
└── pom.xml
```

## Error Handling

- 400: Bad Request - Invalid input
- 404: Not Found - Resource doesn't exist
- 429: Too Many Requests - Rate limit exceeded
- 500: Internal Server Error


## Production Deployment

1. Build Docker image:
```bash
docker build -t customer-api .
```

2. Deploy with Docker Compose:
```bash
docker-compose -f docker-compose.prod.yml up -d
```

## License

This project is licensed under the MIT License - see the [LICENSE](../../../LICENSE) file for details.