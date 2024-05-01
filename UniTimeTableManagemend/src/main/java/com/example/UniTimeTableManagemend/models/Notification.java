package com.example.UniTimeTableManagemend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    @Id
    private String id;
    private String message;

    public Notification(String message) {
        this.message = message;
    }
}
