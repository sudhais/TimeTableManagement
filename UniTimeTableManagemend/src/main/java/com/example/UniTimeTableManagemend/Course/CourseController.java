package com.example.UniTimeTableManagemend.Course;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/course")
@AllArgsConstructor
public class CourseController {

    private CourseService courseService;

    @GetMapping
    public List<Course> getAllCourses(){
        return courseService.getAllCourses();
    }

    @PostMapping
    public void insertCourse(@RequestBody Course course){
        courseService.insertCourse(course);
    }

    @DeleteMapping(path = "{courseId}")
    public void deleteCouerse(@PathVariable("courseId") String courseId){
        courseService.deleteCourse(courseId);
    }
}
