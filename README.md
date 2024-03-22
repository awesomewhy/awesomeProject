# Technologies
Java 17

Spring / Boot / Data JPA / Sequrity

Hibernate

Kafka

Liquibase

Postgres

Docker

JUnit

JWT

Swagger

REST API

Maven


# How to start:

1. Clone
```sh
git clone https://github.com/awesomewhy/awesomeProject.git
```
2. Build
```sh
mvn install package -DskipTests
```

3. Start in docker
```sh
docker-compose up --build -d
```
4. Add data in postgres db
```sh
1. docker exec -it postgres bash
2. psql -U postgres
3. create database test 
4. \c test
5. insert into roles (id, name) values (1, 'ROLE_USER'), (2, 'ROLE_ADMIN');
```