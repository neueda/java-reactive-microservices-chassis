# To build and run:
#docker build -t chassis:openj9 .
#docker run --rm -p 8080:8080 -p 8081:8081 --name chassis-openj9 -d chassis:openj9

ARG APP_NAME="reactive-microservice"
################ STAGE: BUILD ##################
FROM gradle:7.2-jdk16-openj9 AS builder
ARG APP_NAME

WORKDIR /build/$APP_NAME
COPY gradle.properties settings.gradle build.gradle ./
COPY .git .git/
RUN gradle -i dependencies

COPY config config/
COPY src src/
# '-PjarBaseName' defines the name of the jar file to be generated, as well as,
# the name of log file that will be created under /var/log/spring-boot
RUN gradle -i build "-PjarBaseName=$APP_NAME" -x intTest
RUN mkdir jar-layers && cd jar-layers && \
    java -Djarmode=layertools -jar ../build/libs/${APP_NAME}.jar extract

################ STAGE: DEPLOY ##################
FROM adoptopenjdk:16-jre-openj9
ARG APP_NAME

WORKDIR /opt/app/$APP_NAME
RUN mkdir -p /var/log/spring-boot

EXPOSE 8080/tcp
EXPOSE 8081/tcp

COPY --from=builder /build/$APP_NAME/jar-layers/dependencies/ ./
COPY --from=builder /build/$APP_NAME/jar-layers/spring-boot-loader/ ./
COPY --from=builder /build/$APP_NAME/jar-layers/snapshot-dependencies/ ./
COPY --from=builder /build/$APP_NAME/jar-layers/application/ ./
COPY --from=builder /build/$APP_NAME/config config/
RUN chmod -R u+r config/

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
