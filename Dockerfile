# Use the official Java 22 image
FROM openjdk:22-jdk-slim

# Install netcat-openbsd for debugging
RUN apt-get update && apt-get install -y netcat-openbsd
# Install telnet for debugging
RUN apt-get update && apt-get install -y telnet

# Set the working directory
WORKDIR /app

# Copy the jar file
COPY target/kafkaStudy-0.0.1-SNAPSHOT.jar .

# Command to run the application
CMD ["java", "-jar", "kafkaStudy-0.0.1-SNAPSHOT.jar"]
