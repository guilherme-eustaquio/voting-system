# Voting System

Voting system was developed in Java 8 with Spring Boot, Spring Data Reactive MongoDB, Spring WebFlux, JUNIT 5, Docker, Docker-compose, and Rabbit MQ.

# About the System

The system allows creating an agenda for a limited time session for simultaneous voting and publishing its result after the provided session time.

## Technologies Used

- Spring Boot 4.4
- Spring WebFlux
- Spring Data Reactive with MongoDB
- Docker
- Docker-compose
- Rabbit MQ

## Database Diagram

[![diagrama-banco-de-dados-votingsystem](https://i.ibb.co/6FjHzR4/diagrama-banco-de-dados-votingsystem.png)](https://ibb.co/7KsnPJG)

## Execution

To run the project, you need [gradle 6.4.1](https://docs.gradle.org/6.4.1/release-notes.html) or similar and [Docker](https://docs.docker.com/engine/install/ubuntu/).

To build the project, at its root, execute in the terminal:

```
gradle clean build
```

To run it:

```
docker-compose up --build
```

To access the application documentation, simply go to http://localhost:8080/swagger.html

To access RabbitMQ, go to http://localhost:15672

Username: guest, Password: guest

## Architecture

WebFlux was used to ensure that client requests are asynchronous. Spring Data Reactive accesses the MongoDB database with the same purpose: to ensure that there are no blocking actions in the system.

Taken from the website https://subscription.packtpub.com/book/application_development/9781788995979/5/ch05lvl1sec40/spring-mvc-versus-webflux
![Taken from the website https://subscription.packtpub.com/book/application_development/9781788995979/5/ch05lvl1sec40/spring-mvc-versus-webflux](https://static.packt-cdn.com/products/9781788995979/graphics/d2af6e5b-5d26-448d-b54c-64b42d307736.png)

However, the system modeling itself is basically MVC, that is, we have the repository, service, and controller layers. There is also the validation layer intended to perform input validations, key violations, etc.

## Rabbit MQ

RabbitMQ was used when creating a session on the backend. When creating a new session (/sessions of type POST), a thread is created and suspended according to the time provided in the session creation. As soon as the time is reached, the thread is parallelly activated, and the message is sent via RabbitMQ.
