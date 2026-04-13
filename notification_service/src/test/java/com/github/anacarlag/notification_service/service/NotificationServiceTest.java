package com.github.anacarlag.notification_service.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.anacarlag.notification_service.event.OrderCreatedEvent;
import com.github.anacarlag.notification_service.model.Notification;
import com.github.anacarlag.notification_service.model.OrderStatus;
import com.github.anacarlag.notification_service.repository.INotificationRepository;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {
    @Mock
    private INotificationRepository notificationRepository;
    @InjectMocks
    private NotificationService notificationService;
    private OrderCreatedEvent event;

    @BeforeEach
    void setUp(){
        event = OrderCreatedEvent.builder()
                                    .Id(1L)
                                    .customerName("Ana Carla")
                                    .productName("Notebook")
                                    .amount(new BigDecimal("100.0"))
                                    .status(OrderStatus.PENDING)
                                    .build();
    }
    
    @Test
    void shouldProcessOrderCreatedEvent(){
        when(notificationRepository.save(any(Notification.class))).thenReturn(new Notification());
        
        notificationService.processOrderCreatedEvent(event);
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }
    @Test
    void shouldMapEventFieldsCorrectlyWhenSaving(){
        notificationService.processOrderCreatedEvent(event);

        verify(notificationRepository).save(argThat(notification ->
            notification.getId().equals(1L) &&
            notification.getCustomerName().equals("Ana Carla") &&
            notification.getProductName().equals("Notebook") &&
            notification.getAmount().equals(new BigDecimal("100.0")) &&
            notification.getStatus() == OrderStatus.PENDING
        ));
    }
}
