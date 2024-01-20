FROM openjdk:17

WORKDIR /app

COPY /target/Avito-0.0.1-SNAPSHOT.jar avitoback.jar

EXPOSE 8080