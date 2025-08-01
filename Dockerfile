# Use an official Java image
FROM openjdk:17-jdk-slim

# Set working directory inside the container
WORKDIR /app

# Copy the built jar into the container
COPY target/personal-budget-planner-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your app runs on
EXPOSE 8090

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]