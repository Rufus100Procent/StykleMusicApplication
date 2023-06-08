#FROM tomcat:11.0.0-M1-jdk17
#COPY /target/ROOT.war /usr/local/tomcat/webapps
#EXPOSE 8080
#RUN apt-get update


FROM maven:3-amazoncorretto-17 AS build

WORKDIR /opt/myapp
COPY pom.xml .
COPY ./src ./src
RUN mvn package

FROM amazoncorretto:17-alpine
WORKDIR /opt/myapp
COPY --from=build /opt/myapp/target/ROOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
