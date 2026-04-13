package com.github.anacarlag.notification_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.anacarlag.notification_service.model.Notification;
import com.github.anacarlag.notification_service.repository.INotificationRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final INotificationRepository notificationRepository;

    @GetMapping
    public ResponseEntity<List<Notification>> findAll(){
        return ResponseEntity.ok(notificationRepository.findAll());
    }
    public ResponseEntity<List<Notification>> findByCustomerName(String customerName){
        return ResponseEntity.ok(notificationRepository.findByCustomerName(customerName));
    }
    @GetMapping("/order/{Id}")
    public ResponseEntity<List<Notification>> findByOrderId(@PathVariable long Id){
        return ResponseEntity.ok(notificationRepository.findByOrderId(Id));
    }
}
