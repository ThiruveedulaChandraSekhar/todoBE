# Use OpenJDK 17 slim image
FROM openjdk:17-jdk-slim

# Set working directory inside container
WORKDIR /app

# Copy Maven wrapper and pom.xml first to cache dependencies
COPY mvnw mvnw
COPY .mvn .mvn
COPY pom.xml pom.xml

# Give execute permission to mvnw
RUN chmod +x mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build the jar
RUN ./mvnw clean package -DskipTests

# Copy the generated jar to a simple name
RUN cp target/*.jar app.jar

# Expose the port your Spring Boot app uses
EXPOSE 8081

# Run the Spring Boot jar
ENTRYPOINT ["java", "-jar", "app.jar"]
