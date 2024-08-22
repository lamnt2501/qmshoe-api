FROM maven:3.9.9-sapmachine-21 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:24-jdk-slim

WORKDIR /app

COPY --from=build /app/target/qmshoe-0.0.1-SNAPSHOT.jar /app-service/qmshoe-0.0.1-SNAPSHOT.jar

WORKDIR /app-service

EXPOSE 8080

ENTRYPOINT ["java","-jar","qmshoe-0.0.1-SNAPSHOT.jar"]