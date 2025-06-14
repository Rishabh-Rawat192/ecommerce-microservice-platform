FROM openjdk:17-jdk-slim AS build

WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests


FROM openjdk:17-jdk-slim

WORKDIR /app
COPY --from=build /app/target/order-service-0.0.1-SNAPSHOT.jar order-service.jar
EXPOSE 8086

CMD ["java", "-jar", "order-service.jar"]