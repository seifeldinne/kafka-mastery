package org.example;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class App {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 1️⃣ Kafka configuration
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // Kafka broker
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // 2️⃣ Create producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        // 3️⃣ Send messages
        for (int i = 1; i <= 10; i++) {
            ProducerRecord<String, String> record = new ProducerRecord<>("my-topic1", "key "+i, "Hello Kafka "+i);
           RecordMetadata metadata= producer.send(record).get();
            System.out.println("Sent: " + record.value());
        }
        // 4️⃣ Close producer
        producer.close();
    }
}


