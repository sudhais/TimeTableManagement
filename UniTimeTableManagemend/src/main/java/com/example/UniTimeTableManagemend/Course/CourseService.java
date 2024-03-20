package com.example.UniTimeTableManagemend.Course;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public void insertCourse(Course course) {

        courseRepository.findCourseByCode(course.getCode())
                .ifPresentOrElse(course1 -> {
                    System.out.println("Already course code " + course1.getCode() + " exist" );
                    throw new IllegalStateException("Already course code " + course1.getCode() + " exist");

                },() -> {
                    courseRepository.insert(course);
                    System.out.println("inserted " + course);
                });
    }

    public void deleteCourse(String courseId) {

        if(!courseRepository.existsById(courseId))
            throw new IllegalStateException("course id does not exists");

        courseRepository.deleteById(courseId);
        System.out.println("Deleted " + courseId);
    }
}
