# Build stage
FROM docker.io/maven:3.9.4-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy pom.xml first for better layer caching
COPY pom.xml .

# Download dependencies (cached if pom.xml doesn't change)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build application
RUN mvn clean package -DskipTests -B

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

# Create app directory structure
RUN mkdir -p /app/logs

WORKDIR /app

# Configure volume for temporary files
VOLUME /tmp

# Copy JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Set production profile
ENV SPRING_PROFILES_ACTIVE=prod

# Expose ports (8080 for app, 8081 for actuator)
EXPOSE 8080 8084

# Run application
ENTRYPOINT ["java","-jar", "/app/app.jar"]