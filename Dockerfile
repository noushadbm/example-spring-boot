# Build stage
FROM container-registry.oracle.com/graalvm/jdk-community:17.0.9 AS builder

WORKDIR /app
COPY . .
RUN ./mvnw package

# Run stage
FROM container-registry.oracle.com/graalvm/jdk-community:17.0.9 AS runner

WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

# Install Tailscale
RUN apt-get update && apt-get install -y iproute2 curl unzip && \
    curl -fsSL https://tailscale.com/install.sh | sh

# Start Tailscale in the background before running Spring Boot
CMD (tailscaled --tun=userspace-networking &) && \
    sleep 2 && \
    tailscale up --authkey=$TS_AUTHKEY --hostname=koyeb-tailscale && \
    java -jar app.jar
