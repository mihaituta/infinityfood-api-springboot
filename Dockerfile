# Stage 1: Build the application
FROM maven:3.9.4-eclipse-temurin-21 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project files into the container
COPY pom.xml .
COPY src ./src

# Package the application without running tests
RUN mvn clean package -DskipTests

# Stage 2: Create the final runtime image
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080 for the Spring Boot application
EXPOSE 8080

# Set the start command for the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
