package com.example.UniTimeTableManagemend.Course;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> insertCourse(@RequestBody Course course){
        Course insetedCoures = courseService.insertCourse(course);
        return insetedCoures == null ? new ResponseEntity<>("successfully inserted" + course, HttpStatus.OK)
                : new ResponseEntity<>("course code already exists " + insetedCoures, HttpStatusCode.valueOf(500));
    }

    @DeleteMapping(path = "{courseId}")
    public ResponseEntity<String> deleteCouerse(@PathVariable("courseId") String courseId){
        Boolean deleted =courseService.deleteCourse(courseId);
        return deleted ? new ResponseEntity<>("Successfully deleted", HttpStatus.OK)
                : new ResponseEntity<>("id does not found", HttpStatusCode.valueOf(500));
    }

    @PutMapping(path = "{courseId}")
    public ResponseEntity<String> updateCourse(@PathVariable("courseId") String courseId, @RequestBody Course course){
       Boolean updated = courseService.updateCourse(courseId, course);
       return updated ? new ResponseEntity<>("successfully inserted" + course, HttpStatus.OK)
               :new ResponseEntity<>("successfully failed to update" + course, HttpStatusCode.valueOf(500));
    }
}
