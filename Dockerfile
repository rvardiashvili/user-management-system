# Use a base image with Java 24 installed
# Eclipse Temurin is a popular choice for OpenJDK builds.
# 'jammy' refers to Ubuntu 22.04, a stable base.
FROM openjdk:24-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from your build directory into the container
# Replace 'your-application.jar' with the actual name of your JAR file
COPY build/libs/demo-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your Spring Boot application runs on (default is 8080)
EXPOSE 8080

# Command to run your application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]