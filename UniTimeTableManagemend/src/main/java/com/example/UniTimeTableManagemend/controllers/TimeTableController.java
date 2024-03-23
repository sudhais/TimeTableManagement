package com.example.UniTimeTableManagemend.controllers;

import com.example.UniTimeTableManagemend.exception.CourseException;
import com.example.UniTimeTableManagemend.exception.TimeTableException;
import com.example.UniTimeTableManagemend.models.TimeTable;
import com.example.UniTimeTableManagemend.services.TimeTableService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/timetable")
@AllArgsConstructor
public class TimeTableController {

    private TimeTableService timeTableService;
    @PostMapping
    public ResponseEntity<?> addTimeTable(@RequestBody TimeTable timeTable) {

        try {
            timeTableService.addTimeTable(timeTable.getCourseCodes());
            return new ResponseEntity<>("Successfully inserted", HttpStatus.CREATED);
        }catch (CourseException | TimeTableException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }
}
