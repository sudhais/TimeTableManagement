package com.example.UniTimeTableManagemend.controllers;

import com.example.UniTimeTableManagemend.models.Course;
import com.example.UniTimeTableManagemend.services.CourseService;
import com.example.UniTimeTableManagemend.exception.CourseException;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
public class CourseController {

    private CourseService courseService;

    @GetMapping("/adminAndFaculty/course")
    public ResponseEntity<?> getAllCourses(){
        List<Course> courses = courseService.getAllCourses();
        return new ResponseEntity<>(courses, courses.size() > 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @PostMapping("/adminAndFaculty/course")
    public ResponseEntity<?> insertCourse(@RequestBody Course course){
        try {
            courseService.insertCourse(course);
            return new ResponseEntity<>("successfully inserted " + course, HttpStatus.OK);
        }catch (ConstraintViolationException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.UNPROCESSABLE_ENTITY);
        }catch (CourseException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/adminAndFaculty/course/{courseId}")
    public ResponseEntity<?> deleteCourse(@PathVariable("courseId") String courseId){
        try {
            courseService.deleteCourse(courseId);
            return new ResponseEntity<>("Successfully deleted " + courseId, HttpStatus.OK);
        }catch (CourseException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/adminAndFaculty/course/{courseId}")
    public ResponseEntity<String> updateCourse(@PathVariable("courseId") String courseId, @RequestBody Course course){

       try {
          courseService.updateCourse(courseId, course);
          return new ResponseEntity<>("successfully updated" + course, HttpStatus.OK);
       }catch (ConstraintViolationException e){
           return new ResponseEntity<>(e.getMessage(),HttpStatus.UNPROCESSABLE_ENTITY);
       }catch(CourseException e) {
           return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
       }
    }

    @PutMapping("/adminAndFaculty/course/faculty/{courseId}")
    public ResponseEntity<String> updateCourseFaculty(@PathVariable("courseId") String courseId, @RequestBody Course course1){

        try {
             Course course = courseService.updateCourseFaculty(courseId, course1);
            return new ResponseEntity<>("successfully updated the faculty" + course, HttpStatus.OK);
        }catch (ConstraintViolationException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.UNPROCESSABLE_ENTITY);
        }catch(CourseException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }
}
