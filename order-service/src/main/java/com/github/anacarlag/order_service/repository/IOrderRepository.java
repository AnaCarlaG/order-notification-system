package com.github.anacarlag.order_service.repository;

import com.github.anacarlag.order_service.model.Order;
import com.github.anacarlag.order_service.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Long> {
    
    List<Order> findByCustomerName(String customerName);
    List<Order> findByStatus(OrderStatus status);
    
}
