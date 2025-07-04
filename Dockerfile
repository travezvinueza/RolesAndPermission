FROM openjdk:17-slim
COPY ./target/*.jar /app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app.jar"]