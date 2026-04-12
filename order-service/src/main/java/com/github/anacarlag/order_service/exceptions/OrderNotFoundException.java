package com.github.anacarlag.order_service.exceptions;


public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long id) {
        super("Order not found with ID: " + id);
    }

}
