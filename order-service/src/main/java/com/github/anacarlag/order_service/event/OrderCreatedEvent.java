package com.github.anacarlag.order_service.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.github.anacarlag.order_service.model.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {
    private Long id;
    private String customerName;
    private String productName;
    private BigDecimal amount;
    private OrderStatus status;
    private LocalDateTime createdAt;
}
