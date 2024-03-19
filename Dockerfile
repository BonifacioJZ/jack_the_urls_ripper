FROM openjdk:17-jdk-bullseye
LABEL authors="bonifacio"
COPY target/urls_ripper-0.0.1-SNAPSHOT.jar java-app.jar
ENTRYPOINT ["java", "-jar","java-app.jar"]