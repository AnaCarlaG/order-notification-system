package com.github.anacarlag.notification_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.anacarlag.notification_service.model.Notification;

@Repository
public interface INotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByOrderId(Long Id);
    List<Notification> findByCustomerName(String customerName);

}
