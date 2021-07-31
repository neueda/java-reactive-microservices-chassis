# To build and run:
#docker build -t chassis:openj9 .
#docker run --rm -p 8080:8080 --name chassis-openj9 -d chassis:openj9

ARG APP_NAME="reactive-microservice"
################ STAGE: BUILD ##################
FROM maven:3.8.1-adoptopenjdk-16-openj9 AS builder
ARG APP_NAME

WORKDIR build/$APP_NAME
COPY pom.xml ./
COPY src/main/layers.xml ./
COPY src src/
COPY db db/

RUN mvn dependency:resolve-plugins dependency:go-offline
# '-Dbuild.name' defines the name of the jar file to be generated, as well as,
# the directory name under /var/log/ where the app logs will be saved
RUN mvn package "-Dbuild.name=$APP_NAME"
RUN mkdir layers && \
    cd layers && \
    java -Djarmode=layertools -jar ../target/${APP_NAME}.jar extract

################ STAGE: DEPLOY ##################
FROM adoptopenjdk:16-jre-openj9
ARG APP_NAME

WORKDIR app/$APP_NAME
RUN mkdir -p /var/log/$APP_NAME

EXPOSE 8080/tcp

COPY --from=builder build/$APP_NAME/layers/dependencies/ ./
COPY --from=builder build/$APP_NAME/layers/spring-boot-loader/ ./
COPY --from=builder build/$APP_NAME/layers/snapshot-dependencies/ ./
COPY --from=builder build/$APP_NAME/layers/application/ ./
COPY --from=builder build/$APP_NAME/db db/
RUN chmod -R u+r db/

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
