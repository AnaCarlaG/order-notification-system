package com.github.anacarlag.order_service.service;

import com.github.anacarlag.order_service.dto.OrderRequestDTO;
import com.github.anacarlag.order_service.dto.OrderResponseDTO;
import com.github.anacarlag.order_service.exceptions.OrderNotFoundException;
import com.github.anacarlag.order_service.model.Order;
import com.github.anacarlag.order_service.model.OrderStatus;
import com.github.anacarlag.order_service.producer.OrderProducer;
import com.github.anacarlag.order_service.repository.IOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderProducer orderProducer;

    @Mock
    private IOrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private Order order;
    private OrderRequestDTO orderRequestDTO;

    @BeforeEach
    void SetUp() {
        order = Order.builder()
                .id(1L)
                .customerName("Ana Carla")
                .productName("Notebook")
                .amount(new BigDecimal("4500.00"))
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
        orderRequestDTO = OrderRequestDTO.builder()
                .customerName("Ana Carla")
                .productName("Notebook")
                .amount(new BigDecimal("4500.00"))
                .build();
    }

    @Test
    void testCreateOrderSuccesslly() {
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderResponseDTO responseDTO = orderService.createOrder(orderRequestDTO);
        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.getCustomerName()).isEqualTo("Ana Carla");
        assertThat(responseDTO.getProductName()).isEqualTo("Notebook");
        assertThat(responseDTO.getAmount()).isEqualTo(new BigDecimal("4500.00"));
        assertThat(responseDTO.getStatus()).isEqualTo(OrderStatus.PENDING);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testGetOrderByIdSuccess() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderResponseDTO responseDTO = orderService.getOrderById(1L);
        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.getId()).isEqualTo(1L);
        assertThat(responseDTO.getCustomerName()).isEqualTo("Ana Carla");
        assertThat(responseDTO.getProductName()).isEqualTo("Notebook");
        assertThat(responseDTO.getAmount()).isEqualTo(new BigDecimal("4500.00"));
        assertThat(responseDTO.getStatus()).isEqualTo(OrderStatus.PENDING);
    }

    @Test
    void testGetOrderByIdNotFound() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.getOrderById(99L))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessageContaining("Order not found with ID: " + 99L);
    }

    @Test
    void shouldReturnAllOrders() {
        when(orderRepository.findAll()).thenReturn(List.of(order));

        List<OrderResponseDTO> orders = orderService.getAllOrders();

        assertThat(orders).hasSize(1);
        assertThat(orders.get(0).getCustomerName()).isEqualTo("Ana Carla");
    }
}
