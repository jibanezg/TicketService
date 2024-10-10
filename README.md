# My Spring Boot Microservice

## Overview
This is a Spring Boot application that facilitates the discovery, temporary hold, and final reservation of seats within a high-demand performance
venueLevel. It is built using Maven and can be run entirely from the command line.

## Prerequisites
Before you begin, ensure you have met the following requirements:

- **Java 21** or higher installed on your machine.
- **Maven 3.8.7** installed on your machine.

You can verify your installations by running the following commands:

```bash
java -version
mvn -version
```

## Running the Application

Before the application can be run, dependencies need to be installed using the following command:

```
mvn clean install
```

To run the application, use the following command:

```
mvn spring-boot:run
```

The application will start running on http://localhost:8080 by default.

## Running the Tests

To execute the tests, run the following command:

```
mvn test
```

## API Testing (Optional)

The available RESTful endpoints have been documented using swagger. If required, endpoints can be accessed through a convenient UI by accessing: 

http://localhost:8080/swagger-ui/index.html