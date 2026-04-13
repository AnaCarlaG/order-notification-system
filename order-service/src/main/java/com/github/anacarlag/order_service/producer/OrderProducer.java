package com.github.anacarlag.order_service.producer;

import javax.print.DocFlavor.STRING;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.github.anacarlag.order_service.event.OrderCreatedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderProducer {
    private static final String TOPIC = "orders-topic";
    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;
    
    public void sendOrderCreatedEvent(OrderCreatedEvent event){
        log.info("Publishing event to topic {}: {}", TOPIC, event);
        kafkaTemplate.send(TOPIC, String.valueOf(event.getId()), event);
    }
}
