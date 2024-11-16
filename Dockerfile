# Build stage
FROM container-registry.oracle.com/graalvm/jdk-community:17.0.9 AS builder

WORKDIR /app
COPY . .
RUN ./mvnw package

# Run stage
FROM container-registry.oracle.com/graalvm/jdk-community:17.0.9 AS runner

WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

CMD ["java", "-jar", "app.jar"]
