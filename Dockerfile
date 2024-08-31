# Use an official OpenJDK runtime as a parent image
from eclipse-temurin:22-jre

# Set the working directory inside the container
workdir /app

# Copy the application's jar file to the container
copy target/Channel-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your Spring Boot app runs on (optional)
expose 8080

# Run the jar file
entrypoint ["java", "-jar", "app.jar"]
