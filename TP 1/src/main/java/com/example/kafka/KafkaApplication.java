package com.example.kafka;

import com.example.kafka.service.SimpleConsumer;
import com.example.kafka.service.SimpleProducer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaApplication.class, args);
		String topicName= "topic-1";
		String message ="Hello AKACHA";
		SimpleProducer simpleProducer = new SimpleProducer();
		simpleProducer.produce(topicName,message);
	}

}
