FROM adoptopenjdk/openjdk11:ubi
ARG JAR_FILE=/target/notifications_microservice-0.0.1-SNAPSHOT.jar
WORKDIR /opt/buildagent/work
EXPOSE 8086
COPY ${JAR_FILE} notifications_microservice.jar
CMD ["java","-jar", "notifications_microservice.jar"]
