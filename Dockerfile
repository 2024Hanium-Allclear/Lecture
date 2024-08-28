# Dockerfile
FROM openjdk:17-jdk
ARG JAR_FILE=./build/libs/*-SNAPSHOT.jar

# Copy the keystore.p12 file to the container
COPY keystore.p12 /keystore.p12

# Copy the JAR file
COPY ${JAR_FILE} app.jar

# Run the application
ENTRYPOINT [ "java", "-jar", "/app.jar" ]
