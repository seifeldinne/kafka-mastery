# 🚀 Kafka Hands-On Practice

> Exploring Apache Kafka through practical implementations and real-world architectural patterns

## 📂 Repository Structure

### Phase 1: Core Kafka Fundamentals (Pure Java)
```
fundamentals/
├── tp01-producer-consumer/      # Basic messaging with Kafka API
├── tp02-offsets-commits/        # Manual and automatic offset management
├── tp03-consumer-groups/        # Parallel processing and rebalancing
├── tp04-partitioning-keys/      # Data distribution and ordering guarantees
├── tp05-serialization/          # JSON and custom serializers
```

### Phase 2: Advanced Patterns (Spring Boot)
```
advanced/
├── tp06-error-handling/         # Retry logic and dead letter topics
├── tp07-transactions/           # Exactly-once semantics
├── tp08-multiple-topics/        # Service integration patterns
├── tp09-kafka-streams/          # Stream processing basics
├── tp10-monitoring/             # Consumer lag and operations
```

### Phase 3: Architectural Patterns
```
patterns/
├── tp11-event-sourcing/         # Event-driven state management
├── tp12-cqrs/                   # Command Query Responsibility Segregation
├── tp13-saga-pattern/           # Distributed transactions
```

### Phase 4: Real-World Projects
```
projects/
├── realtime-chat/               # Multi-user chat with Kafka
│   ├── chat-service/
│   ├── notification-service/
│   └── user-service/
│
├── banking-system/              # Event-sourced transactions
│   ├── account-service/
│   ├── transaction-service/
│   └── audit-service/
│
└── ecommerce-platform/          # Full microservices architecture
    ├── order-service/
    ├── payment-service/
    ├── inventory-service/
    └── notification-service/
```

## 🎯 Technical Coverage

### Messaging Fundamentals
- Producer/Consumer API
- Topics and partitions
- Message keys and routing
- Offset management (manual and automatic)
- Consumer groups and coordination
- Partition rebalancing

### Reliability & Performance
- At-least-once delivery
- At-most-once delivery
- Exactly-once semantics
- Idempotent producers
- Transactional messaging
- Batching and compression

### Data Handling
- String serialization
- JSON serialization
- Avro with Schema Registry
- Custom serializers/deserializers
- Schema evolution

### Error Handling & Resilience
- Retry mechanisms
- Dead letter topics
- Error handling strategies
- Circuit breaker patterns
- Backpressure management

### Stream Processing
- Kafka Streams API
- Stateless transformations
- Stateful operations
- Windowing
- Joins and aggregations

### Architectural Patterns
- Event sourcing
- CQRS (Command Query Responsibility Segregation)
- Saga pattern for distributed transactions
- Event-driven microservices
- Change Data Capture (CDC)

### Operations & Monitoring
- Consumer lag monitoring
- Offset management and reset
- Partition reassignment
- Performance tuning
- Health checks and metrics

## 🛠️ Technology Stack

**Core**
- Apache Kafka 3.x
- Java 17
- Maven

**Phase 1 (Fundamentals)**
- Pure Kafka Java Client API
- No frameworks - direct API usage

**Phase 2+ (Advanced & Projects)**
- Spring Boot 3.x
- Spring Kafka
- Docker & Docker Compose
- PostgreSQL (for projections)
- Redis (for caching)

## 🚀 Getting Started

### Prerequisites
```bash
# Install Kafka locally or use Docker
docker-compose up -d

# Verify Kafka is running
kafka-topics.sh --bootstrap-server localhost:9092 --list
```

### Running Examples

**Phase 1 - Pure Kafka Examples:**
```bash
cd fundamentals/tp01-producer-consumer
mvn compile exec:java -Dexec.mainClass="Producer"
mvn compile exec:java -Dexec.mainClass="Consumer"
```

**Phase 2+ - Spring Boot Examples:**
```bash
cd advanced/tp06-error-handling
./mvnw spring-boot:run
```

**Projects:**
```bash
cd projects/ecommerce-platform
docker-compose up -d
./start-all-services.sh
```

## 📖 Documentation Structure

Each module contains:
- **README.md** - Concept explanation and learning objectives
- **Architecture diagrams** - Visual representation of message flow
- **Running code** - Complete, executable examples
- **Setup instructions** - Step-by-step guide
- **Key learnings** - Important takeaways and gotchas

## 🎯 Key Projects

### 1. Real-time Chat Application
Multi-room chat system demonstrating:
- Topic-based message routing
- Consumer group scalability
- Real-time message delivery
- User presence tracking

**Tech:** Spring Boot, WebSocket, Kafka, Redis

### 2. Event-Sourced Banking System
Banking platform with full audit trail:
- Event store implementation
- Account state reconstruction
- Snapshot optimization
- Transaction history replay
- Temporal queries

**Tech:** Spring Boot, Kafka, PostgreSQL, Event Sourcing

### 3. E-commerce Microservices Platform
Distributed system with eventual consistency:
- Order processing pipeline
- Payment integration
- Inventory management
- Saga pattern for distributed transactions
- CQRS for read/write separation
- Event-driven notifications

**Tech:** Spring Boot, Kafka, PostgreSQL, Redis, Docker

## 📚 Resources & References

- [Apache Kafka Documentation](https://kafka.apache.org/documentation/)
- [Kafka: The Definitive Guide](https://www.confluent.io/resources/kafka-the-definitive-guide/)
- [Spring Kafka Reference](https://spring.io/projects/spring-kafka)
- [Confluent Kafka Tutorials](https://kafka-tutorials.confluent.io/)

## 📝 Learning Approach

1. **Start with fundamentals** - Pure Kafka API without frameworks
2. **Build incrementally** - Each TP builds on previous concepts
3. **Practice deliberately** - Code, break things, fix them, understand why
4. **Focus on patterns** - Learn architectural patterns through projects
5. **Document learnings** - Each module includes lessons learned

---

**Last Updated:** January 2025 | **Status:** 🟢 Active Development
