# build stage
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /taskapp
COPY . .
RUN mvn clean package -DskipTests

# run stage
FROM openjdk:17-jdk-slim
WORKDIR /taskapp
COPY --from=build /taskapp/target/spring-task-app-0.0.1-SNAPSHOT.jar task-app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]