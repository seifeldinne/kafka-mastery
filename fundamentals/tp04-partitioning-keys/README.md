## TP4: Message Keys and Partitioning

**Objective**: Learn how Kafka distributes messages across partitions

**Tasks**:
1. Create a topic `user-events` with 3 partitions
2. Send messages with specific keys (e.g., userId) using ProducerRecord constructor
3. Add logging to show which partition each message goes to (use RecordMetadata)
4. Send multiple messages with the same key
5. Observe that same key always goes to same partition

**Key Concepts**:
- Partitioning strategy
- Key-based routing
- Default partitioner behavior
- Ordering guarantees (within a partition)
- RecordMetadata

**Practice**:
- Send messages with no key - observe round-robin distribution
- Send 10 messages with key "user-123" - all go to same partition
- Implement a custom Partitioner class
