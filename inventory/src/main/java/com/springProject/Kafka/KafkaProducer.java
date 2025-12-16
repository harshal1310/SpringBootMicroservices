package com.springProject.Kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springProject.Pojo.ProductEntity;
import com.springProject.dto.Product;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {
   // private final KafkaTemplate<String, String> kafkaTemplateStringSerialiable;
    private KafkaTemplate<String, Product> kafkaTemplate;
    public KafkaProducer(KafkaTemplate<String, Product> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /*public void sendMessage(String topic,int partiiton, String message) throws JsonProcessingException {
        kafkaTemplate.send(topic,partiiton, null, message);
        System.out.println("Sent message: " + message);
    }*/

    public void sendMessage(String topic,int partiiton, Product message) throws JsonProcessingException {
        kafkaTemplate.send(topic,partiiton, null, message);
        System.out.println("Sent message: " + message);
    }

}
