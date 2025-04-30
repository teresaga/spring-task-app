FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/spring-task-app-0.0.1-SNAPSHOT.jar task-app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]