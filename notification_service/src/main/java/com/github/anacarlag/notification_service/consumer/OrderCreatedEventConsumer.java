package com.github.anacarlag.notification_service.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.github.anacarlag.notification_service.event.OrderCreatedEvent;
import com.github.anacarlag.notification_service.service.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreatedEventConsumer {
    private final NotificationService notificationService;
    @KafkaListener(topics = "orders-topic", groupId = "notification-group")
    public void consume(OrderCreatedEvent event){
        log.info("Received order created event for orderId: {}", event.getId());
        notificationService.processOrderCreatedEvent(event);
    }
}
