package com.example.UniTimeTableManagemend.services;

import com.example.UniTimeTableManagemend.exception.CourseException;
import com.example.UniTimeTableManagemend.models.Course;
import com.example.UniTimeTableManagemend.models.Room;
import com.example.UniTimeTableManagemend.models.TimeTable;
import com.example.UniTimeTableManagemend.respositories.TimeTableRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class TimeTableService {

    private final TimeTableRepository timeTableRepository;
    private CourseService courseService;
    private RoomService roomService;

    public void addTimeTable(List<String> courseCodes) throws CourseException {
        List<Course> courses = new ArrayList<>();
        List<Room> rooms = new ArrayList<>();

        for (String code : courseCodes ){
            courses.add(courseService.findByCourseCode(code));
            rooms.addAll(roomService.findByCode(code));
        }

        timeTableRepository.insert(new TimeTable(courseCodes,courses,rooms));
    }
}
