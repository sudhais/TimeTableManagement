package com.example.UniTimeTableManagemend.models;

import com.example.UniTimeTableManagemend.models.enums.Day;
import com.example.UniTimeTableManagemend.models.enums.Location;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Table {

    private Day day;
    private String startTime;
    private String endTime;
    private Location location;
    private String courseCode;
    private String courseName;

}
