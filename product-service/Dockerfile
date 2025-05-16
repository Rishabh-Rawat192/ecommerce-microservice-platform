FROM openjdk:17-jdk-slim AS build

# Set the working directory
WORKDIR /app

# Copy the source code
COPY . /app

# Build the application
RUN ./mvnw clean package -DskipTests

# Use a smaller base image for the runtime
FROM openjdk:17-jdk-slim
# Set the working directory
WORKDIR /app
# Copy the built application from the build stage
COPY --from=build /app/target/product-service-0.0.1-SNAPSHOT.jar /app/product-service.jar

# Expose the application port
EXPOSE 8082

# Command to run the jar file
ENTRYPOINT ["java", "-jar", "/app/product-service.jar"]