# To build and run:
#docker build -t chassis:openj9 .
#docker run --rm -p 8080:8080 --name chassis-openj9 -d chassis:openj9

ARG APP_NAME="reactive-microservice"

################ STAGE: BUILD ##################
FROM maven:3.8.1-adoptopenjdk-16-openj9 AS builder

WORKDIR /tmp/build/${APP_NAME}
COPY pom.xml ./
COPY src ./src
COPY db ./db

# '-Dlog.dir' defines the directory name under /var/log/ where app logs will be saved
# '-Dbuild.name' defines the name of the jar file to be generated
RUN mvn package "-Dlog.dir=${APP_NAME}" "-Dbuild.name=${APP_NAME}"

################ STAGE: DEPLOY ##################
FROM adoptopenjdk:16-jre-openj9

WORKDIR /opt/app/reactive-microservice
RUN mkdir -p /var/log/reactive-microservice

EXPOSE 8080/tcp

COPY --from=builder /tmp/build/reactive-microservice/db db/
COPY --from=builder /tmp/build/reactive-microservice/target/${APP_NAME}.jar ./

RUN chmod -R 777 db/

ENTRYPOINT ["java", "-jar", "${APP_NAME}.jar"]
