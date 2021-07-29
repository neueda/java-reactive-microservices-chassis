# To build and run:
#docker build -t chassis:openj9 .
#docker run --rm -p 8080:8080 --name chassis-openj9 -d chassis:openj9
ARG APP_NAME="reactive-microservice"

################ STAGE: BUILD ##################
FROM maven:3.8.1-adoptopenjdk-16-openj9 AS builder
ARG APP_NAME

WORKDIR /tmp/build/$APP_NAME
COPY pom.xml ./
COPY src ./src
COPY db ./db

# '-Dbuild.name' defines the name of the jar file to be generated, as well as,
# the directory name under /var/log/ where the app logs will be saved
RUN mvn package "-Dbuild.name=$APP_NAME" "-Dmaven.test.skip=true"

################ STAGE: DEPLOY ##################
FROM adoptopenjdk:16-jre-openj9
ARG APP_NAME

WORKDIR /opt/app/$APP_NAME
RUN mkdir -p /var/log/$APP_NAME

EXPOSE 8080/tcp

COPY --from=builder /tmp/build/$APP_NAME/db db/
COPY --from=builder /tmp/build/$APP_NAME/target/${APP_NAME}.jar ./

RUN chmod -R 777 db/

ENV APP_JAR ${APP_NAME}.jar
ENTRYPOINT ["/bin/bash", "-c", "java -jar $APP_JAR"]
