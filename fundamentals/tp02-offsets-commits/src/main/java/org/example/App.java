package org.example;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class App {
    private static final String TOPIC_ONE = "TOPIC_ONE";
    private static final String TOPIC_TWO = "TOPIC_TWO";
    private static final String BOOTSTRAP_SERVERS_CONFIG = "localhost:9092";
    private static final String CONSUMER_GROUP_ID_LOGS_1 = "my-group-id-logs-1";
    private static final String CONSUMER_GROUP_ID_LOGS_2 = "my-group-id-logs-2";

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // First: Send messages
        Producer producer = new Producer();
        producer.sendMessage();
        System.out.println("Messages sent!");

        // 2. ⭐ WAIT to ensure messages are fully committed in Kafka
        System.out.println("Waiting 2 seconds for Kafka to settle...");
        Thread.sleep(10000);

        // Second: Run consumer ONCE
        System.out.println("\n=== FIRST RUN ===");
        Consumer consumer1 = new Consumer(1, List.of(TOPIC_ONE), CONSUMER_GROUP_ID_LOGS_1);
        Thread t1 = new Thread(consumer1::readMessage);
        t1.start();
        t1.join();

        System.out.println("\n=== Waiting 3 seconds... ===");
        Thread.sleep(3000);

        // Third: Run consumer AGAIN with SAME group.id
        System.out.println("\n=== SECOND RUN (same group.id) ===");
        Consumer consumer2 = new Consumer(2, List.of(TOPIC_ONE), CONSUMER_GROUP_ID_LOGS_1);
        Thread t2 = new Thread(consumer2::readMessage);
        t2.start();
        t2.join();

    }

    static class Producer {

        KafkaProducer<String, String> producer;

        public Producer() {
            Properties props = new Properties();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS_CONFIG); // Kafka broker
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            this.producer = new KafkaProducer<>(props);
        }

        public void sendMessage() throws ExecutionException, InterruptedException {
            for (int i = 1; i <= 10; i++) {
                ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_ONE, "key " + i, " message to topic one Hello Kafka " + i);
                RecordMetadata metadata = producer.send(record).get();
                System.out.println("Sent: " + record.value());
            }
            producer.close();
        }
    }


    static class Consumer {

        KafkaConsumer<String, String> consumer;
        private final List<String> topic;
        long numConsumer;

        public Consumer(long numConsumer, List<String> topic, String groupId) {
            this.numConsumer = numConsumer;
            this.topic = topic;
            this.consumer = new KafkaConsumer<>(new Properties() {{
                put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS_CONFIG);
                put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
                put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
                put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
                put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
            }});
        }

        public void readMessage() {
            this.consumer.subscribe(topic);
            int totalMessagesRead = 0;
            int emptyPollCount = 0;
            int maxEmptyPolls = 3;  // Stop after 3 empty polls
            System.out.println("Consumer " + numConsumer + " started polling...");
            while (emptyPollCount < maxEmptyPolls) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(2000));
                if (records.isEmpty()) {
                    emptyPollCount++;
                    System.out.println("Consumer " + numConsumer + " - empty poll " + emptyPollCount);
                } else {
                    emptyPollCount = 0;  // Reset counter when we get messages
                    for (ConsumerRecord<String, String> record : records) {
                        System.out.printf("numero consumer = %d, offset = %d, key = %s, value = %s%n",
                                numConsumer, record.offset(), record.key(), record.value());
                        totalMessagesRead++;
                    }
                }
            }
            System.out.println("Consumer " + numConsumer + " finished. Total messages read: " + totalMessagesRead);
            consumer.close();
        }
    }

}


