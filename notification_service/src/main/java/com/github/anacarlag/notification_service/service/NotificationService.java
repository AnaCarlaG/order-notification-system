package com.github.anacarlag.notification_service.service;

import org.springframework.stereotype.Service;

import com.github.anacarlag.notification_service.event.OrderCreatedEvent;
import com.github.anacarlag.notification_service.model.Notification;
import com.github.anacarlag.notification_service.repository.INotificationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final INotificationRepository notificationRepository;

    public void processOrderCreatedEvent(OrderCreatedEvent event){
        log.info("Processing order created event for orderId:{}", event.getId());
        // Create a notification based on the event data
        Notification notification = Notification.builder()
                                                .id(event.getId())
                                                .customerName(event.getCustomerName())
                                                .productName(event.getProductName())
                                                .amount(event.getAmount())
                                                .status(event.getStatus())
                                                .build();
        // Save the notification to the database
        notificationRepository.save(notification);
        log.info("Notification created for orderId:{}", event.getId());
    }
}
