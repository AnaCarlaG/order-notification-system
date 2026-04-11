package com.github.anacarlag.order_service.dto;

import com.github.anacarlag.order_service.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {

    private Long id;
    private String customerName;
    private String productName;
    private BigDecimal amount;
    private OrderStatus status;
    private LocalDateTime createdAt;
}
