package com.apex.trade.exchange.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @KafkaListener(topics = "my-topic", groupId = "my-consumer-group")
    public void consume(String message) {
        System.out.println("Received message " + message);
    }
}
