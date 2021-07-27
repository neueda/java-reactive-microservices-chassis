# To be build and run with:
#docker build --build-arg JAR=java-microservices-chassis-0.0.1-SNAPSHOT.jar -t chassis:openj9 .
#docker run --rm -p 8080:8080 --name chassis-openj9 -d chassis:openj9

FROM adoptopenjdk:16-jre-openj9

ARG JAR

WORKDIR /opt/app/java-microservices-chassis
RUN mkdir -p /var/log/java-microservices-chassis

EXPOSE 8080

COPY /db /opt/app/java-microservices-chassis/db/
COPY target/${JAR} /opt/app/java-microservices-chassis/java-microservices-chassis.jar


RUN chmod -R 777 /opt/app/java-microservices-chassis/db/

ENTRYPOINT ["java", "-jar", "/opt/app/java-microservices-chassis/java-microservices-chassis.jar"]
