package com.github.anacarlag.order_service.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.anacarlag.order_service.dto.OrderRequestDTO;
import com.github.anacarlag.order_service.dto.OrderResponseDTO;
import com.github.anacarlag.order_service.exceptions.GlobalExceptionHandler;
import com.github.anacarlag.order_service.exceptions.OrderNotFoundException;
import com.github.anacarlag.order_service.model.OrderStatus;
import com.github.anacarlag.order_service.service.OrderService;

import net.bytebuddy.agent.builder.AgentBuilder.CircularityLock.Global;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
//@Import(GlobalExceptionHandler.class)
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderService orderService;
    private OrderResponseDTO orderResponseDTO;
    private OrderRequestDTO orderRequestDTO;


    @BeforeEach
    void setUp() {
        orderResponseDTO = OrderResponseDTO.builder()
                .id(1L)
                .customerName("Ana Carla")
                .productName("Notebook")
                .amount(new BigDecimal("4500.0"))
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        orderRequestDTO = OrderRequestDTO.builder()
                .customerName("Ana Carla")
                .productName("Notebook")
                .amount(new BigDecimal("4500.0"))
                .build();
    }

    @Test
    void shouldCreateOrderSuccessfully() throws Exception{
        when(orderService.createOrder(any())).thenReturn(orderResponseDTO);

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(orderResponseDTO.getId()))
                .andExpect(jsonPath("$.customerName").value(orderResponseDTO.getCustomerName()))
                .andExpect(jsonPath("$.productName").value(orderResponseDTO.getProductName()))
                .andExpect(jsonPath("$.amount").value(orderResponseDTO.getAmount()))
                .andExpect(jsonPath("$.status").value(orderResponseDTO.getStatus().toString()));
    }
    @Test
    void shouldReturnOrderById() throws Exception {
        when(orderService.getOrderById(1L)).thenReturn(orderResponseDTO);

        mockMvc.perform(get("/orders/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderResponseDTO.getId()))
                .andExpect(jsonPath("$.customerName").value(orderResponseDTO.getCustomerName()))
                .andExpect(jsonPath("$.productName").value(orderResponseDTO.getProductName()))
                .andExpect(jsonPath("$.amount").value(orderResponseDTO.getAmount()))
                .andExpect(jsonPath("$.status").value(orderResponseDTO.getStatus().toString()));
    }
    @Test
    void shouldReturnNotFoundWhenOrderDoesNotExist() throws Exception {
        when(orderService.getOrderById(99L)).thenThrow(new OrderNotFoundException(99L));

        mockMvc.perform(get("/orders/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Order not found with ID: 99"));
    }
    @Test
    void shouldReturnAllOrders() throws Exception{
        when(orderService.getAllOrders()).thenReturn(List.of(orderResponseDTO));

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
    @Test
    void shouldReturnBadRequestForInvalidOrderCreation() throws Exception{
        OrderRequestDTO invalidRequest = new OrderRequestDTO().builder().build();

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void shouldReturnInternalServerErrorOnUnexpectedException() throws Exception{
        when(orderService.getAllOrders()).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/orders"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500));
    }
}
