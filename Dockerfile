FROM adoptopenjdk/openjdk11:ubi
ARG JAR_FILE=/target/socialnetwork-0.0.1-SNAPSHOT.jar
WORKDIR /opt/buildagent/work
EXPOSE 8086
COPY ${JAR_FILE} socialnetwork.jar
CMD ["java","-jar", "socialnetwork.jar"]
