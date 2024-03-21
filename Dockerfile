FROM openjdk:17

WORKDIR /app

COPY target/online-0.0.1.jar online-back.jar

EXPOSE 8080

CMD ["java", "-jar" , "online-back.jar"]



#FROM openjdk:17
#
##WORKDIR /app
#
#ADD ./pom.xml ./pom.xml
#
#RUN ["mvn", "verify", "clean", "--fail-never"]
#
#ADD ./src ./src
#
#RUN ["mvn", "package"]
#
#COPY target/online-0.0.1-SNAPSHOT.jar online-back.jar
#
#EXPOSE 8080
#
#CMD ["java", "-jar" , "online-back.jar"]