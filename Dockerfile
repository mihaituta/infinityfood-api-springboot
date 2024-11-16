# Use the OpenJDK 21 image as the base
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the packaged Spring Boot application JAR file into the container
COPY out/artifacts/Infinityfood_jar/Infinityfood.jar app.jar

# Expose port 8080
EXPOSE 8080

# Set the start command for the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
