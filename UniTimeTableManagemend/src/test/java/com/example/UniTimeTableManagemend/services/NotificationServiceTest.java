package com.example.UniTimeTableManagemend.services;

import com.example.UniTimeTableManagemend.models.Notification;
import com.example.UniTimeTableManagemend.respositories.NotificationRepository;
import com.example.UniTimeTableManagemend.services.Impl.NotificationServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.ArgumentMatchers.any;

class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationServiceImp notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getNotification_noNotifications_emptyListReturned() {
        when(notificationRepository.findAll()).thenReturn(new ArrayList<>());

        List<Notification> notifications = notificationService.getNotification();

        assertEquals(0, notifications.size());
    }

    @Test
    void getNotification_notificationsExist_listReturned() {
        List<Notification> dummyNotifications = new ArrayList<>();
        dummyNotifications.add(new Notification("Notification 1"));
        dummyNotifications.add(new Notification("Notification 2"));

        when(notificationRepository.findAll()).thenReturn(dummyNotifications);

        List<Notification> notifications = notificationService.getNotification();

        assertEquals(dummyNotifications.size(), notifications.size());
    }

    @Test
    void addNotification_notificationAddedSuccessfully() {
        String message = "New notification";

        notificationService.addNotification(message);

        verify(notificationRepository, times(1)).insert(any(Notification.class));
    }

}