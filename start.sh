#!/bin/bash
set -e

# -----------------------------
# Configuration
# -----------------------------
SERVICES=("inventory" "order")
EUREKA_SERVICE="ServiceRegistry"

DB_CONTAINER="pg-colima"
DBS=("inventory_db" "orders_db")
DB_PORT=5434

ZOOKEEPER_CONTAINER="zookeeper-colima"
KAFKA_CONTAINER="kafka-colima"
KAFDROP_CONTAINER="kafdrop"

ZOOKEEPER_PORT=2181
KAFKA_PORT=9092
KAFDROP_PORT=9292

DOCKER_NETWORK="kafka-net"
MAVEN_CMD="(mvn clean compile && mvn spring-boot:run -Dspring-boot.run.profiles=dev)"

# -----------------------------
# Start Colima
# -----------------------------
echo "Starting Colima..."
colima start

# -----------------------------
# Create Docker network
# -----------------------------
if ! docker network inspect "$DOCKER_NETWORK" >/dev/null 2>&1; then
    echo "Creating Docker network: $DOCKER_NETWORK"
    docker network create "$DOCKER_NETWORK"
else
    echo "Docker network $DOCKER_NETWORK already exists."
fi

# -----------------------------
# Remove existing containers
# -----------------------------
for CONTAINER in "$ZOOKEEPER_CONTAINER" "$KAFKA_CONTAINER" "$KAFDROP_CONTAINER" "$DB_CONTAINER"; do
    if docker ps -aq -f name="^${CONTAINER}$" | grep -q .; then
        echo "Removing existing container $CONTAINER..."
        docker rm -f "$CONTAINER"
    fi
done

# -----------------------------
# Start PostgreSQL container
# -----------------------------
echo "Starting Postgres..."
docker run -d --name "$DB_CONTAINER" \
    -e POSTGRES_USER=postgres \
    -e POSTGRES_PASSWORD=password \
    -p "$DB_PORT:5432" \
    --network "$DOCKER_NETWORK" \
    postgres:16

# Wait for Postgres
echo "Waiting for Postgres..."
until docker exec "$DB_CONTAINER" pg_isready -U postgres >/dev/null 2>&1; do
    printf "."
    sleep 1
done
echo "Postgres is ready!"

# Create databases
for DB_NAME in "${DBS[@]}"; do
    echo "Ensuring database $DB_NAME exists..."
    docker exec "$DB_CONTAINER" psql -U postgres -tc \
        "SELECT 1 FROM pg_database WHERE datname='${DB_NAME}'" \
        | grep -q 1 || docker exec "$DB_CONTAINER" psql -U postgres -c \
        "CREATE DATABASE ${DB_NAME};"
done
echo "All databases ready!"

# -----------------------------
# Start Zookeeper
# -----------------------------
echo "Starting Zookeeper..."
docker run -d --name "$ZOOKEEPER_CONTAINER" \
    --network "$DOCKER_NETWORK" \
    -p "$ZOOKEEPER_PORT:2181" \
    -e ZOOKEEPER_CLIENT_PORT=2181 \
    confluentinc/cp-zookeeper:7.5.0

# -----------------------------
# Start Kafka
# -----------------------------
echo "Starting Kafka..."

docker run -d --name kafka-colima \
  --network kafka-net \
  -p 9092:9092 \
  -e KAFKA_BROKER_ID=1 \
  -e KAFKA_ZOOKEEPER_CONNECT=zookeeper-colima:2181 \
  -e KAFKA_LISTENER_SECURITY_PROTOCOL_MAP=INTERNAL:PLAINTEXT,EXTERNAL:PLAINTEXT \
  -e KAFKA_LISTENERS=INTERNAL://0.0.0.0:29092,EXTERNAL://0.0.0.0:9092 \
  -e KAFKA_ADVERTISED_LISTENERS=INTERNAL://kafka-colima:29092,EXTERNAL://localhost:9092 \
  -e KAFKA_INTER_BROKER_LISTENER_NAME=INTERNAL \
  -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 \
  confluentinc/cp-kafka:7.5.0

# Wait for Kafka
echo "Waiting for Kafka..."
until docker exec "$KAFKA_CONTAINER" kafka-topics \
    --bootstrap-server localhost:9092 --list >/dev/null 2>&1; do
    printf "."
    sleep 2
done
echo "Kafka is ready!"

# -----------------------------
# Start Kafdrop
# -----------------------------
echo "Starting Kafdrop..."
docker run -d --name "$KAFDROP_CONTAINER" \
    --network "$DOCKER_NETWORK" \
    -p "$KAFDROP_PORT:9000" \
  -e KAFKA_BROKERCONNECT="kafka-colima:29092" \
    -e JVM_OPTS="-Xms32M -Xmx64M" \
    obsidiandynamics/kafdrop

echo "Kafdrop available at http://localhost:$KAFDROP_PORT"


echo "Preparing Schema Registry..."

# Stop & remove existing container if present
if docker ps -aq -f name="^${SCHEMA_REGISTRY_CONTAINER}$" | grep -q .; then
    echo "Stopping existing Schema Registry..."
    docker rm -f "$SCHEMA_REGISTRY_CONTAINER"
fi

# -----------------------------
# Start Schema Registry
# -----------------------------
SCHEMA_REGISTRY_CONTAINER="schema-registry-colima"
SCHEMA_REGISTRY_PORT=8082

echo "Starting Schema Registry..."
docker run -d --name "$SCHEMA_REGISTRY_CONTAINER" \
  --network "$DOCKER_NETWORK" \
  -p "$SCHEMA_REGISTRY_PORT:8082" \
  -e SCHEMA_REGISTRY_KAFKASTORE_BOOTSTRAP_SERVERS=PLAINTEXT://kafka-colima:29092 \
  -e SCHEMA_REGISTRY_HOST_NAME=127.0.0.1 \
  -e SCHEMA_REGISTRY_LISTENERS=http://0.0.0.0:8082 \
  confluentinc/cp-schema-registry:7.5.0



# Wait for Schema Registry
echo "Waiting for Schema Registry..."
until curl -sf http://127.0.0.1:$SCHEMA_REGISTRY_PORT/subjects >/dev/null 2>&1; do
    printf "."
    sleep 2
done
echo "Schema Registry is ready!"


# -----------------------------
# Start Eureka Server
# -----------------------------
echo "Starting Eureka server: $EUREKA_SERVICE..."
(cd "$EUREKA_SERVICE" && mvn clean compile && mvn spring-boot:run -Dspring-boot.run.profiles=dev &)

echo "Waiting for Eureka..."
until curl -sf http://localhost:8761 >/dev/null; do
    printf "."
    sleep 2
done
echo "Eureka is ready!"

# -----------------------------
# Start other Spring Boot services
# -----------------------------
for SERVICE in "${SERVICES[@]}"; do
    echo "Starting service: $SERVICE..."
    (cd "$SERVICE" && mvn clean compile && mvn spring-boot:run -Dspring-boot.run.profiles=dev &)

done




echo "✅ All services started successfully!"


