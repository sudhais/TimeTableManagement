package com.example.UniTimeTableManagemend.controllers;

import com.example.UniTimeTableManagemend.models.Notification;
import com.example.UniTimeTableManagemend.services.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/notification")
@AllArgsConstructor
public class NotificationController {

    private NotificationService notificationService;

    @GetMapping
    public ResponseEntity<?> getNotification(){
        return new ResponseEntity<>(notificationService.getNotification(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addAnnouncement(@RequestBody String message){
        notificationService.addNotification(message);
        return new ResponseEntity<>("successfully added ", HttpStatus.CREATED);
    }
}
