package com.springProject.Kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.springProject.Pojo.Product;
import com.springProject.dto.Product;
import jakarta.ws.rs.Consumes;
import org.hibernate.query.Order;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
    /*@KafkaListener(topics = "orders", groupId = "order-group")
    public void consume(String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Product order = objectMapper.readValue(message, Product.class);
            System.out.println("Received order: " + order);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }*/

    @KafkaListener(topics = "topic-1", groupId = "order-group")
    public void consume(Product product) {
        System.out.println("Received product: " + product);


    }

}
