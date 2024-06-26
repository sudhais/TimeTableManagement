package com.example.UniTimeTableManagemend.models;

import com.example.UniTimeTableManagemend.models.enums.Day;
import com.example.UniTimeTableManagemend.models.enums.Location;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room {

    @Id
    private String id;
    @NotNull(message = "location cannot be null")
    private Location location;
    private String startTime;
    private String endTime;

    @NotNull(message = "day cannot be null")
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
