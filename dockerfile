FROM tomcat:11.0.0-M1-jdk17
COPY /target/ROOT.war /usr/local/tomcat/webapps
EXPOSE 8080
RUN apt-get update