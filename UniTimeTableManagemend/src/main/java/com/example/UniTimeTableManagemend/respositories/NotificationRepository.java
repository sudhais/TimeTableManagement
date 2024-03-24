package com.example.UniTimeTableManagemend.respositories;

import com.example.UniTimeTableManagemend.models.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification,String> {

}
