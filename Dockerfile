# Image de base
FROM eclipse-temurin:21-jre

# Working directory
WORKDIR /app

# Copy the jar file
COPY /target/java-tcp-programming-1.0-SNAPSHOT.jar /app/java-tcp-programming-1.0-SNAPSHOT.jar

# Set the entrypoint
ENTRYPOINT ["java", "-jar", "java-tcp-programming-1.0-SNAPSHOT.jar"]

# set the default command
CMD ["--help"]
