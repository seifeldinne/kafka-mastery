## TP2: Basic Consumer - Reading Messages

**Objective**: Learn how consumers read messages from Kafka using pure Kafka API

**Tasks**:
1. Create a Consumer class in the same or different Java project
2. Configure consumer properties (bootstrap.servers, group.id, deserializers)
3. Subscribe to `hello-topic` and poll for messages
4. Log each message received
5. Stop and restart the consumer - observe behavior

**Key Concepts**:
- Consumer configuration (group.id, auto-offset-reset)
- KafkaConsumer API
- poll() method and consumer loop
- Offset management
- Consumer groups

**Practice**:
- Change the `group.id` and see what happens
- Try `auto.offset.reset: earliest` vs `latest`
- Experiment with different `poll()` timeout values
