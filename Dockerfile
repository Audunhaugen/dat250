FROM ubuntu:latest
LABEL authors="alejandro"

ENTRYPOINT ["top", "-b"]


#
# BUILD STAGE
#
FROM maven:3-openjdk-17 AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package

#
# PACKAGE STAGE
#
FROM openjdk:17-jdk
COPY --from=build /usr/src/app/target/jpa-tutorial-0.0.1-SNAPSHOT.jar /usr/app/jpa-tutorial-0.0.1-SNAPSHOT.jar
EXPOSE 8080
CMD ["java","-jar","/usr/app/jpa-tutorial-0.0.1-SNAPSHOT.jar"]