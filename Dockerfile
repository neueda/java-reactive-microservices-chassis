# To build and run:
#docker build -t chassis:openj9 .
#docker run --rm -p 8080:8080 --name chassis-openj9 -d chassis:openj9

################ STAGE: BUILD ##################
FROM maven:3.8.1-adoptopenjdk-16-openj9 AS builder

ENV APP_NAME="reactive-microservice"

WORKDIR /tmp/build/$APP_NAME
COPY pom.xml ./
COPY src ./src
COPY db ./db

RUN mvn package "-Dproject.artifactId=$APP_NAME" "-DbuildName=$APP_NAME"

################ STAGE: DEPLOY ##################
FROM adoptopenjdk:16-jre-openj9

WORKDIR /opt/app/reactive-microservice
RUN mkdir -p /var/log/reactive-microservice

EXPOSE 8080/tcp

COPY --from=builder /tmp/build/reactive-microservice/db db/
COPY --from=builder /tmp/build/reactive-microservice/target/reactive-microservice.jar ./

RUN chmod -R 777 db/

ENTRYPOINT ["java", "-jar", "reactive-microservice.jar"]
