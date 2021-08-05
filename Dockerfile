# To build and run:
#docker build -t chassis:openj9 .
#docker run --rm -p 8080:8080 --name chassis-openj9 -d chassis:openj9

ARG APP_NAME="reactive-microservice"
################ STAGE: BUILD ##################
FROM maven:3.8.1-adoptopenjdk-16-openj9 AS builder
ARG APP_NAME

WORKDIR build/$APP_NAME
COPY pom.xml ./
COPY src src/
COPY db db/

RUN mvn dependency:go-offline dependency:resolve-plugins
# '-Dbuild.name' defines the name of the jar file to be generated, as well as,
# the name of log file saved under /var/log/spring-boot
RUN mvn package "-Dbuild.name=$APP_NAME"
RUN mkdir layers && \
    cd layers && \
    java -Djarmode=layertools -jar ../target/${APP_NAME}.jar extract

################ STAGE: DEPLOY ##################
FROM adoptopenjdk:16-jre-openj9
ARG APP_NAME

WORKDIR /opt/app/$APP_NAME
RUN mkdir -p /var/log/spring-boot

EXPOSE 8080/tcp
EXPOSE 8081/tcp

COPY --from=builder build/$APP_NAME/layers/dependencies/ ./
COPY --from=builder build/$APP_NAME/layers/spring-boot-loader/ ./
COPY --from=builder build/$APP_NAME/layers/snapshot-dependencies/ ./
COPY --from=builder build/$APP_NAME/layers/application/ ./
COPY --from=builder build/$APP_NAME/db db/
RUN chmod -R u+r db/

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
