package com.github.anacarlag.order_service.service;

import com.github.anacarlag.order_service.dto.OrderRequestDTO;
import com.github.anacarlag.order_service.dto.OrderResponseDTO;
import com.github.anacarlag.order_service.event.OrderCreatedEvent;
import com.github.anacarlag.order_service.exceptions.OrderNotFoundException;
import com.github.anacarlag.order_service.model.Order;
import com.github.anacarlag.order_service.producer.OrderProducer;
import com.github.anacarlag.order_service.repository.IOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final IOrderRepository orderRepository;
    private final OrderProducer orderProducer;

    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) {
        Order order = Order.builder()
                .customerName(orderRequestDTO.getCustomerName())
                .productName(orderRequestDTO.getProductName())
                .amount(orderRequestDTO.getAmount())
                .build();
        Order savedOrder = orderRepository.save(order);

        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .id(savedOrder.getId())
                .customerName(savedOrder.getCustomerName())
                .productName(savedOrder.getProductName())
                .amount(savedOrder.getAmount())
                .status(savedOrder.getStatus())
                .createdAt(savedOrder.getCreatedAt())
                .build();
        // Publish event to Kafka
        orderProducer.sendOrderCreatedEvent(event);
        return mapToResponseDTO(savedOrder);
    }
    
    public OrderResponseDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        return mapToResponseDTO(order);
    }
    public List<OrderResponseDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(this::mapToResponseDTO).toList();
    }

    private OrderResponseDTO mapToResponseDTO(Order order) {
        return OrderResponseDTO.builder()
                .id(order.getId())
                .customerName(order.getCustomerName())
                .productName(order.getProductName())
                .amount(order.getAmount())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
