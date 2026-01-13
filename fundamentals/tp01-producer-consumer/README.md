## TP1: Basic Producer - Understanding Message Sending

**Objective**: Learn how producers send messages to Kafka topics using pure Kafka API

**Tasks**:
1. Create a simple Java project with Kafka client dependency
2. Create a Producer class that sends String messages to a topic called `hello-topic`
3. Configure producer properties (bootstrap.servers, serializers)
4. Send 10 messages with different content
5. Use Kafka console consumer to verify messages arrived

**Key Concepts**:
- Topic creation
- Producer configuration (bootstrap servers, key/value serializers)
- KafkaProducer API
- ProducerRecord
- Synchronous vs Asynchronous sending

**Practice**:
- Try sending messages with and without keys
- Observe what happens when topic doesn't exist (auto-creation)
- Experiment with `send()` vs `send().get()` (async vs sync)
