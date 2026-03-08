# 📚 Kafka — Course Notes & Key Concepts

> One file to review everything. Updated after each TP.

---

## 📋 Table of Contents

- [TP1 — Basic Producer: Understanding Message Sending](#tp1--basic-producer-understanding-message-sending)
- [TP2 — Basic Consumer: Reading Messages](#tp2--basic-consumer-reading-messages)
- [TP3 — Consumer Groups & Parallel Processing](#tp3--consumer-groups--parallel-processing)
- [TP4 — ...](#tp4--)
- [TP5 — ...](#tp5--)

---

## TP1 — Basic Producer: Understanding Message Sending

### What is a Topic?
- A **topic** is a named channel where producers send messages and consumers read from
- Topics are split into **partitions** for parallelism
- If `auto.create.topics.enable=true` on the broker, Kafka creates the topic automatically when a producer sends to it — otherwise it throws an error

### Producer Configuration

```java
Properties props = new Properties();
props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // broker address
props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

KafkaProducer<String, String> producer = new KafkaProducer<>(props);
```

### ProducerRecord — Sending a Message

```java
// With key and value
ProducerRecord<String, String> record = new ProducerRecord<>("hello-topic", "key1", "Hello Kafka!");

// Without key (Kafka assigns partition using round-robin)
ProducerRecord<String, String> record = new ProducerRecord<>("hello-topic", "Hello Kafka!");
```

### Synchronous vs Asynchronous Sending

| | Async `send()` | Sync `send().get()` |
|---|---|---|
| Waits for broker confirmation | ❌ No | ✅ Yes |
| Performance | ✅ Faster | ❌ Slower |
| Error handling | Callback needed | Exception thrown directly |
| Use case | High throughput | When you need guaranteed delivery |

```java
// Async — fire and forget (fast, no guarantee)
producer.send(record);

// Async with callback — fast + error handling
producer.send(record, (metadata, exception) -> {
    if (exception != null) System.out.println("Error: " + exception);
    else System.out.println("Sent to partition " + metadata.partition());
});

// Sync — wait for confirmation (slow but safe)
RecordMetadata metadata = producer.send(record).get();
System.out.println("Sent to partition: " + metadata.partition() + " offset: " + metadata.offset());
```

### Sending with vs without Keys

| | With Key | Without Key |
|---|---|---|
| Partition assignment | Same key → always same partition | Round-robin across partitions |
| Use case | Order matters (e.g. same user events together) | Load balancing |

```java
// With key — "user-1" always goes to same partition
new ProducerRecord<>("hello-topic", "user-1", "clicked button");

// Without key — distributed across partitions
new ProducerRecord<>("hello-topic", "just a message");
```

### Useful Docker Commands

```bash
# Enter the Kafka container
docker exec -it kafka bash

# Export path so you don't need to type full path
export PATH=$PATH:/usr/bin

# Read messages from a topic (from beginning)
kafka-console-consumer --bootstrap-server localhost:9092 --topic hello-topic --from-beginning

# Read with full details (key, partition, offset)
kafka-console-consumer --bootstrap-server localhost:9092 --topic hello-topic \
  --from-beginning \
  --property print.key=true \
  --property print.partition=true \
  --property print.offset=true
```

### TP1 — Key Rules to Remember

1. **Always close the producer** — `producer.close()` flushes remaining messages and releases resources
2. **Key = same partition** — messages with the same key always go to the same partition
3. **No key = round-robin** — Kafka distributes messages evenly across partitions
4. **`send().get()`** = synchronous (waits) — **`send()`** = asynchronous (fire and forget)
5. **Topic auto-creation** depends on broker config — don't rely on it in production

---

## TP2 — Basic Consumer: Reading Messages

### Consumer Configuration

```java
Properties props = new Properties();
props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
props.put(ConsumerConfig.GROUP_ID_CONFIG, "my-group");              // consumer group
props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");     // start from beginning
props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");        // auto-commit offsets

KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
consumer.subscribe(Collections.singleton("hello-topic"));
```

### The Poll Loop

```java
while (true) {
    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

    for (ConsumerRecord<String, String> record : records) {
        System.out.printf("partition=%d offset=%d key=%s value=%s%n",
            record.partition(), record.offset(), record.key(), record.value());
    }
}
```

> `poll()` is not just fetching — it also sends heartbeats to the broker to signal the consumer is alive.

### What is an Offset?
- An **offset** is a unique ID for each message within a partition
- Kafka tracks which offset each consumer group has read up to
- When you restart a consumer, it resumes from the **last committed offset**

```
Partition-0:  [msg0] [msg1] [msg2] [msg3] [msg4]
               ↑offset=0                   ↑offset=4
                              ↑ consumer committed here → resumes from msg3
```

### auto.offset.reset — What Happens on First Start?

| Value | Behaviour |
|---|---|
| `earliest` | Read **all messages from the beginning** of the topic |
| `latest` | Read only **new messages** arriving after the consumer started |

> This only applies when there is **no committed offset** yet for this `group.id` (first time, or new group).

### Stop & Restart Behaviour

| Situation | Result |
|---|---|
| Stop and restart, **same `group.id`** | Resumes from where it left off (committed offset) |
| Restart with a **new `group.id`** | Starts fresh — reads from `earliest` or `latest` depending on config |
| Change `auto.offset.reset` to `latest`, new group | Only reads new messages — skips old ones |

### poll() Timeout — What Does it Affect?

```java
consumer.poll(Duration.ofMillis(1000));  // waits up to 1000ms for messages
consumer.poll(Duration.ofMillis(100));   // returns faster, more CPU usage
consumer.poll(Duration.ofMillis(5000));  // waits longer, less CPU usage
```

- Higher timeout = less CPU, slightly more latency
- Lower timeout = more responsive, more CPU usage
- Does **not** affect how many messages you receive — just how long to wait if none are available

### TP2 — Key Rules to Remember

1. **Offset = bookmark** — Kafka remembers where each group stopped reading
2. **`earliest`** = read everything from the start (good for testing)
3. **`latest`** = read only new messages (good for production)
4. **Same `group.id` after restart** = continues from last committed offset
5. **New `group.id`** = treated as a brand new consumer, starts fresh
6. **`poll()` keeps the consumer alive** — stop polling = Kafka thinks consumer is dead = triggers rebalance

---

## TP3 — Consumer Groups & Parallel Processing

### What is a Partition?
- **You** decide the number of partitions when creating the topic — Kafka does NOT create them automatically
- More partitions = more parallelism
- Each partition is an ordered, immutable log of messages

```java
new NewTopic("orders-topic", 4, (short) 1);  // 4 partitions, replication factor = 1
```

---

### Consumer Groups

| Scenario | Result |
|---|---|
| Same `group.id` | Consumers **share** messages — each message read by only one consumer |
| Different `group.id` | Each group reads **ALL** messages independently |

**The golden rule:**
> One partition → maximum **one consumer** per group reading it at a time

---

### Partition Assignment Examples

| Consumers | Partitions | Same Group? | Result |
|---|---|---|---|
| 2 | 4 | ✅ Yes | Each consumer reads 2 partitions |
| 3 | 4 | ✅ Yes | Rebalance → one consumer gets 2 partitions |
| 5 | 4 | ✅ Yes | 4 consumers work, **1 is idle** (no partition left) |
| 2 | 4 | ❌ No | Each reads **all 4 partitions** independently |

---

### Rebalancing

Rebalancing = Kafka **redistributes partitions** when the consumer group changes.

**When does it trigger?**
- A new consumer **joins** the group
- A consumer **crashes** or **leaves**

**Example — Adding a 3rd consumer to a group:**

```
BEFORE (2 consumers):         AFTER rebalance (3 consumers):
C1 → partition-0, partition-1    C1 → partition-0
C2 → partition-2, partition-3    C2 → partition-1
                                 C3 → partition-2, partition-3
```

**See rebalancing in logs with ConsumerRebalanceListener:**

```java
consumer.subscribe(Collections.singleton(TOPIC_NAME), new ConsumerRebalanceListener() {

    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
        System.out.println(consumerName + " >>> PARTITIONS REVOKED: " + partitions);
    }

    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
        System.out.println(consumerName + " >>> PARTITIONS ASSIGNED: " + partitions);
    }
});
```

---

### subscribe() vs assign()

| | `subscribe()` | `assign()` |
|---|---|---|
| Group coordination | ✅ Yes | ❌ No |
| Auto rebalancing | ✅ Yes | ❌ No |
| Partition assignment | Automatic by Kafka | Manual by you |
| Use case | ✅ Normal use (always prefer this) | Special cases / testing only |

```java
// subscribe() — Kafka decides which partitions you get
consumer.subscribe(Collections.singleton(TOPIC_NAME));

// assign() — You manually pick partitions (no group, no rebalancing)
consumer.assign(List.of(new TopicPartition(TOPIC_NAME, 0)));
```

> ⚠️ When using `assign()`, the consumer is **NOT part of any group**. No rebalancing happens.

---

### Multiple Groups — How to implement

Pass `groupId` as a parameter to your Consumer class:

```java
// Same group → share messages
Thread t1 = new Thread(new Consumer("C1", "group-A"));
Thread t2 = new Thread(new Consumer("C2", "group-A"));

// Different group → reads ALL messages independently
Thread t3 = new Thread(new Consumer("C3", "group-B"));
```

```java
static class Consumer implements Runnable {
    private final String consumerName;
    private final String groupId;

    public Consumer(String consumerName, String groupId) {
        this.consumerName = consumerName;
        this.groupId = groupId;
    }

    public void run() {
        props.put(ConsumerConfig.GROUP_ID_CONFIG, this.groupId); // use it here
        // ...
    }
}
```

---

### Key Config Properties

| Property | Value | Meaning |
|---|---|---|
| `AUTO_OFFSET_RESET_CONFIG` | `earliest` | Read from beginning if no committed offset exists |
| `ENABLE_AUTO_COMMIT_CONFIG` | `true` | Kafka auto-commits offsets periodically |
| `GROUP_ID_CONFIG` | your group name | Which consumer group this consumer belongs to |
| `BOOTSTRAP_SERVERS_CONFIG` | `localhost:9092` | Kafka broker address |

---

### TP3 — 5 Rules to Remember

1. **1 partition → max 1 consumer** per group reading it
2. **More consumers than partitions** = idle consumers (not an error)
3. **Different `group.id`** = full independent copy of all messages
4. **Rebalancing** = Kafka re-assigns partitions when group membership changes
5. **Always use `subscribe()`** for normal use — `assign()` only for special cases

---

## TP4 — ...

> 🚧 *Fill this in after completing TP4*

### Key Concepts
- ...

---

## TP5 — ...

> 🚧 *Fill this in after completing TP5*

### Key Concepts
- ...

---

*Updated progressively through the course* 🚀
