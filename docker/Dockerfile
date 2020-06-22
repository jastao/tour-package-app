# Create the image for the tour-package application
FROM openjdk:11-jre-slim

# Expose the port
EXPOSE 9000

# Set the working directory
WORKDIR /home/tour

# Copy everything in the current directory to the /home/tour directory
COPY . /home/tour/

# Copy and rename the jar
ADD target/tour-package-0.0.1-SNAPSHOT.jar tour-package-0.0.1.jar

# Set the entrypoint to the application
ENTRYPOINT ["sh", "-c", "java -Dspring.profiles.active=prod -Dsecurity.jwt.token.secret-key=tour-secret -jar tour-package-0.0.1.jar"]
