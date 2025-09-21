FROM eclipse-temurin:23-jre

WORKDIR /app

COPY target/*.jar app.jar

RUN src/main/java/LockInBackendApplication
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]