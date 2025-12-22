package com.example.kafka.service;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;

import java.util.Properties;
@Service
public class SimpleProducer {
    public void produce(String topicName,String message) {

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
       // ProducerRecord<String, String> record = new ProducerRecord<>(topicName, message);
        for(int i=0;i<100;i++){
            producer.send(new ProducerRecord<>(topicName, message+i));
        }
        SimpleConsumer simpleConsumer =new SimpleConsumer();
        simpleConsumer.consume(topicName);
        producer.close();
        System.out.println("Message envoyé !");
    }
}
