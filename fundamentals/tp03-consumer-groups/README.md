## TP3: Consumer Groups - Parallel Processing

**Objective**: Understand how multiple consumers share the load

**Tasks**:
1. Create a topic with 4 partitions: `orders-topic`
2. Start 2 consumer instances with the SAME group.id listening to this topic
3. Send 20 messages from a producer
4. Observe which consumer receives which messages
5. Start a 3rd consumer with the same group.id
6. Watch the rebalancing happen in the logs

**Key Concepts**:
- Partitions and parallelism
- Consumer group coordination
- Rebalancing process
- One partition → one consumer in a group
- ConsumerRebalanceListener

**Practice**:
- Try 5 consumers but only 4 partitions - what happens to the 5th?
- Create a consumer with a DIFFERENT group.id - does it get all messages?
- Implement ConsumerRebalanceListener to see rebalancing events
