FROM openjdk:17

WORKDIR /app

COPY target/online-0.0.1-SNAPSHOT.jar dark-online-back.jar

EXPOSE 8080

CMD ["java", "-jar" , "ozon-online-back.jar"]