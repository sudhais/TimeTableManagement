package com.example.UniTimeTableManagemend.services;

import com.example.UniTimeTableManagemend.models.Notification;

import java.util.List;

public interface NotificationService {

    public List<Notification> getNotification();
    public void addNotification(String message);
}
