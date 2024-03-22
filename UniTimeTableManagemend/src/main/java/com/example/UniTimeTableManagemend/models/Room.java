package com.example.UniTimeTableManagemend.models;

import com.example.UniTimeTableManagemend.models.enums.Day;
import com.example.UniTimeTableManagemend.models.enums.Location;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;

@Document
@Data
public class Room {

    @Id
    private String id;

//    @NotNull(message = "Location cannot be null")
//    @NotBlank(message = "Location connot bu blank")
    private Location location;
    private String startTime;
    private String endTime;
    private Day day;
    private String courseCode;

    public Room(Location location, String startTime, String endTime, Day day, String courseCode) {
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.day = day;
        this.courseCode = courseCode;
    }
}
