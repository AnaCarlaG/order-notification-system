package com.github.anacarlag.notification_service.consumer;

import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.anacarlag.notification_service.event.OrderCreatedEvent;
import com.github.anacarlag.notification_service.model.OrderStatus;
import com.github.anacarlag.notification_service.service.NotificationService;

@ExtendWith(MockitoExtension.class)
public class OrderCreatedEventConsumerTest {
    @Mock
    private NotificationService notificationService;
    @InjectMocks
    private OrderCreatedEventConsumer consumer;

    @Test
    void shouldCallNotificationServiceWhenEventReceived(){
        OrderCreatedEvent event = OrderCreatedEvent.builder()
                                    .Id(1L)
                                    .customerName("Ana Carla")
                                    .productName("Notebook")
                                    .amount(new BigDecimal("100.0"))
                                    .status(OrderStatus.PENDING)
                                    .build();
        consumer.consume(event);
        verify(notificationService, times(1)).processOrderCreatedEvent(event);
    }
    @Test
    void shouldNotThrowExceptionWhenProcessingEvent(){
        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .Id(2L)
                .customerName("João Silva")
                .productName("Teclado")
                .amount(new BigDecimal("350.00"))
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

    doNothing().when(notificationService).processOrderCreatedEvent(any());    
    consumer.consume(event);
    
    verify(notificationService,times(1)).processOrderCreatedEvent(event);
    }
}
