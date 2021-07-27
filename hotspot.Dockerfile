FROM adoptopenjdk:16-jre-hotspot

ARG JAR

WORKDIR /opt/app/java-microservices-chassis
RUN mkdir -p /var/log/java-microservices-chassis

EXPOSE 8080

COPY /db /opt/app/java-microservices-chassis/db/
COPY target/${JAR} /opt/app/java-microservices-chassis/java-microservices-chassis.jar


RUN chmod -R 777 /opt/app/java-microservices-chassis/db/

ENTRYPOINT ["java", "-jar", "/opt/app/java-microservices-chassis/java-microservices-chassis.jar"]
