package com.example.UniTimeTableManagemend.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
public class TimeTable {

    @Id
    private String id;
    private List<String> courseCodes;
    private List<Course> courses;
    private List<Room> rooms;


    public TimeTable(List<String> courseCodes, List<Course> courses, List<Room> rooms) {
        this.courseCodes = courseCodes;
        this.courses = courses;
        this.rooms = rooms;
    }
}
