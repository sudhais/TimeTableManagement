package com.example.UniTimeTableManagemend.services.Impl;

import com.example.UniTimeTableManagemend.models.Notification;
import com.example.UniTimeTableManagemend.respositories.NotificationRepository;
import com.example.UniTimeTableManagemend.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImp implements NotificationService {

    private final NotificationRepository notificationRepository;

    public List<Notification> getNotification(){
       List<Notification> notifications = notificationRepository.findAll();
       if(notifications.size() > 0)
           return notifications;
       else
           return new ArrayList<>();
    }

    public void addNotification(String message){

        notificationRepository.insert(new Notification(message));

    }
}
